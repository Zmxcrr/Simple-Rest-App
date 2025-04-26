package services;

import dto.OwnerDto;
import entities.Owner;
import exceptions.UnknownEntityIdException;
import interfaces.OwnerDao;

import java.util.List;

public class OwnerService {
    private final OwnerDao ownerDao;

    public OwnerService(OwnerDao ownerDao) {
        this.ownerDao = ownerDao;
    }

    public Owner createOwner(OwnerDto ownerDto) {
        Owner owner = new Owner();
        owner.setFirstName(ownerDto.getFirstName());
        owner.setLastName(ownerDto.getLastName());
        owner.setBirthdate(ownerDto.getBirthdate());

        return ownerDao.save(owner);
    }

    public Owner updateOwner(OwnerDto ownerDto) {
        Owner owner = ownerDao.getById(ownerDto.getId());
        if (owner == null) {
            throw new UnknownEntityIdException("Owner with ID=%s does not exist".formatted(ownerDto.getId()));
        }

        owner.setFirstName(ownerDto.getFirstName());
        owner.setLastName(ownerDto.getLastName());
        owner.setBirthdate(ownerDto.getBirthdate());

        return ownerDao.update(owner);
    }

    public void deleteOwner(long id) {
        ownerDao.deleteById(id);
    }

    public Owner getOwner(long id) {
        return ownerDao.getById(id);
    }

    public List<Owner> getAllOwners() {
        return ownerDao.getAll();
    }
}