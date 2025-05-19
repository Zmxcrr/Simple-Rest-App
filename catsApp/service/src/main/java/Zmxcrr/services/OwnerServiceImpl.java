package Zmxcrr.services;

import Zmxcrr.dto.CatDto;
import Zmxcrr.dto.OwnerDto;
import Zmxcrr.entities.Owner;
import Zmxcrr.exceptions.UnknownEntityIdException;
import Zmxcrr.repositories.CatRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import Zmxcrr.repositories.OwnerRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OwnerServiceImpl implements OwnerService {
    private final OwnerRepository ownerRepository;
    private final CatRepository catRepository;

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    public long createOwner(String firstName, String lastName, LocalDate birthdate) {
        var owner = new Owner();
        owner.setFirstName(firstName);
        owner.setLastName(lastName);
        owner.setBirthdate(birthdate);
        owner.setCats(new ArrayList<>());

        return ownerRepository.save(owner).getId();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    @Transactional
    public Page<OwnerDto> getAllOwners(Pageable pageable) {
        return ownerRepository.findAll(pageable)
                .map(OwnerDto::new);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    @Transactional
    public boolean removeOwner(long ownerID) throws RuntimeException {
        var owner = ownerRepository.findById(ownerID);
        if (owner.isEmpty())
            return false;

        if (!owner.get().getCats().isEmpty())
            throw new RuntimeException("Cant remove owner while he has cats");

        ownerRepository.delete(owner.get());
        return true;
    }

    @PreAuthorize("hasAuthority('ADMIN') or @authorizeService.isCurrentOwner(authentication, #id.longValue())")
    @Override
    public Optional<OwnerDto> getById(long id) {
        var ownerOptional = ownerRepository.findById(id);
        if (ownerOptional.isEmpty())
            return Optional.empty();
        var ownerDto = new OwnerDto(ownerOptional.get());

        return Optional.of(ownerDto);
    }

    @PreAuthorize("hasAuthority('ADMIN') or @authorizeService.isCurrentOwner(authentication, #id.longValue())")
    @Override
    public List<CatDto> getAllCats(long id) throws UnknownEntityIdException {
        var owner = ownerRepository.findById(id);
        if (owner.isEmpty())
            throw new UnknownEntityIdException("Owner with ID=%s not found");

        return catRepository.findFiltered(null, null, null, id).stream().map(CatDto::new).toList();
    }
}