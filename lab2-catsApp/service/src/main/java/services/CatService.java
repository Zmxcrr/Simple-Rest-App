package services;

import entities.Owner;
import exceptions.UnknownEntityIdException;
import interfaces.CatDao;
import dto.CatDto;
import entities.Cat;
import interfaces.OwnerDao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CatService {
    private final CatDao catDao;
    private final OwnerDao ownerDao;

    public CatService(CatDao catDao, OwnerDao ownerDao) {
        this.catDao = catDao;
        this.ownerDao = ownerDao;
    }

    public Cat createCat(CatDto catDto) {
        Cat cat = new Cat();
        return updateCatFromDto(cat, catDto);
    }

    public Cat updateCat(CatDto catDto) {
        Cat cat = catDao.getById(catDto.getId());
        if (cat == null) {
            throw new RuntimeException("Cat not found with id: " + catDto.getId());
        }
        return updateCatFromDto(cat, catDto);
    }

    private Cat updateCatFromDto(Cat cat, CatDto catDto) {
        cat.setName(catDto.getName());
        cat.setBirthdate(catDto.getBirthdate());
        cat.setBreed(catDto.getBreed());
        cat.setColor(catDto.getColor());

        if (catDto.getOwner() != null) {
            Owner owner = ownerDao.getById(catDto.getOwner());
            if (owner == null) {
                throw new UnknownEntityIdException("Owner with ID=%s does not exist".formatted(catDto.getOwner()));
            }
            cat.setOwner(owner);
        }

        if (catDto.getFriends() != null && !catDto.getFriends().isEmpty()) {
            Set<Cat> friends = new HashSet<>();
            for (Long friendId : catDto.getFriends()) {
                Cat friend = catDao.getById(friendId);
                if (friend == null) {
                    throw new UnknownEntityIdException("Friend with ID=%s does not exist".formatted(friendId));
                }
                friends.add(friend);
            }
            cat.setFriends(friends);
        } else {
            cat.setFriends(new HashSet<>());
        }

        return catDao.save(cat);
    }

    public void deleteCat(long id) {
        catDao.deleteById(id);
    }

    public Cat getCat(long id) {
        return catDao.getById(id);
    }

    public List<Cat> getAllCats() {
        return catDao.getAll();
    }

    public List<Cat> getCatsByOwnerId(long ownerId) {
        return getAllCats().stream()
                .filter(cat -> cat.getOwner() != null && cat.getOwner().getId() == ownerId)
                .collect(Collectors.toList());
    }

}