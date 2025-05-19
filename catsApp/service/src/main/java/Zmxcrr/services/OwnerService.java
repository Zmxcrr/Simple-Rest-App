package Zmxcrr.services;

import Zmxcrr.dto.CatDto;
import Zmxcrr.dto.OwnerDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OwnerService {

    long createOwner(String firstName, String lastName, LocalDate birthdate);

    Page<OwnerDto> getAllOwners(Pageable pageable);

    boolean removeOwner(long ownerID);

    Optional<OwnerDto> getById(long id);

    List<CatDto> getAllCats(long id);
}