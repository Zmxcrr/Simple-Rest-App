package Zmxcrr.clients;

import Zmxcrr.config.RabbitMQConfig;
import Zmxcrr.dto.CatDto;
import Zmxcrr.dto.OwnerDto;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


import java.util.List;

@AllArgsConstructor
@Component
public class OwnerClientImpl implements OwnerClient {
    private static final String BASE_URL = "http://localhost:8082";
    private RestTemplate restTemplate;
    @Override
    public ResponseEntity<List<OwnerDto>> getAll() {
        return restTemplate.exchange(
                BASE_URL + "/owners",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
    }

    @Override
    @RabbitListener(queues = RabbitMQConfig.CREATE_OWNER_QUEUE)
    public void save(OwnerDto owner) {
        var request = RequestEntity
                .post(BASE_URL + "/owners")
                .contentType(MediaType.APPLICATION_JSON)
                .body(owner);

        restTemplate.exchange(request, Void.class);
    }

    @Override
    public ResponseEntity<?> getById(long id) {
        return restTemplate.exchange(
                BASE_URL + "/owners/" + id,
                HttpMethod.GET,
                null,
                OwnerDto.class
        );
    }

    @Override
    @RabbitListener(queues = RabbitMQConfig.DELETE_OWNER_QUEUE)
    public void delete(long id) {
        restTemplate.delete(BASE_URL + "/owners/" + id);
    }

    @Override
    public ResponseEntity<List<CatDto>> getCats(long id) {
        return restTemplate.exchange(
                BASE_URL + "/owners/" + id + "/cats",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() { }
        );
    }
}