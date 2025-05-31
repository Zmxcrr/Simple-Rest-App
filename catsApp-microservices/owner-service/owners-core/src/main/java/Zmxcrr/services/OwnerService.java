package Zmxcrr.services;

import Zmxcrr.dto.OwnerDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OwnerService {
    long createOwner(String firstName, String lastName, LocalDate birthdate);

    List<OwnerDto> getAllOwners();

    void removeOwner(Long ownerID);

    Optional<OwnerDto> getById(Long id);
}