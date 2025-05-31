package Zmxcrr.clients;

import Zmxcrr.dto.CatDto;
import Zmxcrr.dto.OwnerDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
public interface OwnerClient {
    ResponseEntity<List<OwnerDto>> getAll();

    void save(OwnerDto owner);

    ResponseEntity<?> getById(long id);

    void delete(long id);

    ResponseEntity<List<CatDto>> getCats(long id);
}