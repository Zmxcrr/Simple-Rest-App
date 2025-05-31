package Zmxcrr.clients;

import org.springframework.http.ResponseEntity;
import Zmxcrr.dto.CatDto;
import Zmxcrr.dto.FriendshipDto;

import java.util.List;

public interface CatClient {
    ResponseEntity<List<CatDto>> filter(String color, String breed, Integer year, Long ownerId);
    void save(CatDto cat);
    ResponseEntity<CatDto> getById(long id);
    void delete(long id);
    void deleteAllByOwner(long ownerId);
    ResponseEntity<?> findFriends(long id);
    void makeFriend(FriendshipDto friendshipDto);
    void deleteFriend(FriendshipDto friendshipDto);
}