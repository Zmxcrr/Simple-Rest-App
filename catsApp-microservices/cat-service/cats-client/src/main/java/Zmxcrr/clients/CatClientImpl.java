package Zmxcrr.clients;

import Zmxcrr.dto.CatDto;
import Zmxcrr.dto.FriendshipDto;
import Zmxcrr.config.RabbitMQConfig;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Component("CatClient")
public class CatClientImpl implements CatClient {
    private static final String BASE_URL = "http://localhost:8081";
    private RestTemplate restTemplate;

    @Override
    public ResponseEntity<List<CatDto>> filter(String color, String breed, Integer year, Long ownerId) {
        var uri = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/cats")
                .queryParamIfPresent("color", Optional.ofNullable(color))
                .queryParamIfPresent("breed", Optional.ofNullable(breed))
                .queryParamIfPresent("year", Optional.ofNullable(year))
                .queryParamIfPresent("owner", Optional.ofNullable(ownerId))
                .build();

        return restTemplate.exchange(
                uri.toUri(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() { }
        );
    }

    @Override
    @RabbitListener(queues = RabbitMQConfig.CREATE_CAT_QUEUE)
    public void save(CatDto cat) {
        var request = RequestEntity
                .post(BASE_URL + "/cats")
                .contentType(MediaType.APPLICATION_JSON)
                .body(cat);

        restTemplate.exchange(request, Void.class);
    }

    @Override
    public ResponseEntity<CatDto> getById(long id) {
        return restTemplate.getForEntity(
                BASE_URL + "/cats/" + id,
                CatDto.class
        );
    }

    @Override
    @RabbitListener(queues = RabbitMQConfig.DELETE_CAT_QUEUE)
    public void delete(long id) {
        restTemplate.delete(BASE_URL + "/cats/" + id);
    }

    @Override
    public void deleteAllByOwner(long ownerId) {
        var uri = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/cats")
                .queryParam("owner", ownerId)
                .build();

        restTemplate.delete(uri.toUri());
    }

    @Override
    public ResponseEntity<?> findFriends(long id) {
        return restTemplate.exchange(
                BASE_URL + "/cats/" + id + "/friends",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
    }

    @Override
    @RabbitListener(queues = RabbitMQConfig.CREATE_FRIEND_QUEUE)
    public void makeFriend(FriendshipDto friendshipDto) {
        var uri = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/cats/" + friendshipDto.getFirstCatId() + "/friends")
                .queryParam("friendId", friendshipDto.getSecondCatId())
                .build();

        restTemplate.exchange(
                uri.toUri(),
                HttpMethod.POST,
                null,
                Void.class
        );
    }

    @Override
    @RabbitListener(queues = RabbitMQConfig.DELETE_FRIEND_QUEUE)
    public void deleteFriend(FriendshipDto friendshipDto) {
        var uri = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/cats/" + friendshipDto.getFirstCatId() + "/friends")
                .queryParam("friendId", friendshipDto.getSecondCatId())
                .build();

        restTemplate.exchange(
                uri.toUri(),
                HttpMethod.DELETE,
                null,
                Void.class
        );
    }
}