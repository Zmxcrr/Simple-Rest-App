package Zmxcrr.services;

import Zmxcrr.clients.CatClient;
import Zmxcrr.dto.OwnerDto;
import Zmxcrr.entities.Owner;
import Zmxcrr.exceptions.UnknownOwnerException;
import Zmxcrr.mappers.OwnerDtoMapper;
import Zmxcrr.repositories.OwnerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OwnerServiceImpl implements OwnerService {
    private final OwnerRepository ownerRepository;
    private final CatClient catClient;
    @Override
    @Transactional
    public long createOwner(String firstName, String lastName, LocalDate birthdate) {
        var owner = new Owner();
        owner.setFirstName(firstName);
        owner.setLastName(lastName);
        owner.setBirthdate(birthdate);

        return ownerRepository.save(owner).getId();
    }

    @Override
    @Transactional
    public List<OwnerDto> getAllOwners() {
        List<OwnerDto> owners = new ArrayList<>();
        for (var owner : ownerRepository.findAll())
            owners.add(OwnerDtoMapper.map(owner));

        return owners;
    }

    @Override
    public void removeOwner(Long ownerID) throws RuntimeException {
        var owner = ownerRepository.findById(ownerID);
        if (owner.isEmpty())
            throw new UnknownOwnerException("Owner with ID=%s not found".formatted(ownerID));

        var request = catClient.filter(null, null, null, ownerID);
        if (request.hasBody() && !request.getBody().isEmpty())
            catClient.deleteAllByOwner(ownerID);

        ownerRepository.delete(owner.get());
    }

    @Override
    public Optional<OwnerDto> getById(Long id) {
        var ownerOptional = ownerRepository.findById(id);

        return ownerOptional.map(OwnerDtoMapper::map);
    }
}