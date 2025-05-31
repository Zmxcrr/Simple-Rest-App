package Zmxcrr.services;

import Zmxcrr.clients.OwnerClient;
import Zmxcrr.dto.CatDto;
import Zmxcrr.entities.Cat;
import Zmxcrr.enums.CatColor;
import Zmxcrr.exceptions.FriendshipException;
import Zmxcrr.exceptions.UnknownCatException;
import Zmxcrr.mappers.CatDtoMapper;
import Zmxcrr.repositories.CatRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.*;

@Service
@AllArgsConstructor
public class CatServiceImpl implements CatService {
    private final CatRepository catRepository;
    private final OwnerClient ownerClient;

    @Override
    public long addCat(String name, LocalDate birthdate, String breed, CatColor color, Long ownerID) throws UnknownCatException {
        var response = ownerClient.getById(ownerID);
        if (!response.getStatusCode().isSameCodeAs(HttpStatus.OK))
            throw new UnknownCatException("Owner with ID=%s not found".formatted(ownerID));

        var cat = new Cat();
        cat.setName(name);
        cat.setBirthdate(birthdate);
        cat.setBreed(breed);
        cat.setColor(color);
        cat.setOwner(ownerID);
        cat.setFriends(new HashSet<>());

        return catRepository.save(cat).getId();
    }

    @Override
    @Transactional
    public void removeCat(Long id) {
        var optional = catRepository.findById(id);
        if (optional.isEmpty())
            return;

        var friends = optional.get().getFriends();
        for (var friend : friends)
            friend.getFriends().remove(optional.get());

        catRepository.delete(optional.get());
    }

    @Override
    @Transactional
    public void removeAllCatsByOwnerId(Long ownerId) {
        var cats = catRepository.findFiltered(null, null, null, ownerId);
        for (var cat : cats)
            for (var friend : cat.getFriends())
                friend.getFriends().removeAll(cats);
        catRepository.deleteAllByOwner(ownerId);
    }

    @Override
    @Transactional
    public Optional<CatDto> getCatByID(Long id) {
        var cat = catRepository.findById(id);

        return cat.map(CatDtoMapper::map);
    }


    @Override
    @Transactional
    public boolean areFriends(long firstCatID, long secondCatID) throws UnknownCatException {
        var firstCat = checkCatPersistence(catRepository, firstCatID);
        var secondCat = checkCatPersistence(catRepository, secondCatID);

        return firstCat.getFriends().contains(secondCat);
    }

    @Override
    @Transactional
    public void makeFriendship(long firstCatID, long secondCatID) throws UnknownCatException, FriendshipException {
        var firstCat = checkCatPersistence(catRepository, firstCatID);
        var secondCat = checkCatPersistence(catRepository, secondCatID);

        if (firstCat.getFriends().contains(secondCat))
            throw new FriendshipException("Cats %s and %s are already friends".formatted(firstCatID, secondCatID));

        firstCat.getFriends().add(secondCat);
        secondCat.getFriends().add(firstCat);

        catRepository.saveAll(Arrays.asList(firstCat, secondCat));
    }

    @Override
    @Transactional
    public void destroyFriendship(long firstCatID, long secondCatID) throws UnknownCatException, FriendshipException {
        var firstCat = checkCatPersistence(catRepository, firstCatID);
        var secondCat = checkCatPersistence(catRepository, secondCatID);

        if (!firstCat.getFriends().contains(secondCat))
            throw new FriendshipException("Cats %s and %s are not friends".formatted(firstCatID, secondCatID));

        firstCat.getFriends().remove(secondCat);
        secondCat.getFriends().remove(firstCat);

        catRepository.saveAll(Arrays.asList(firstCat, secondCat));
    }

    @Override
    @Transactional
    public List<CatDto> getFriends(long id) throws UnknownCatException {
        var cat = checkCatPersistence(catRepository, id);

        List<CatDto> friends = new ArrayList<>();
        for (var friend : cat.getFriends())
            friends.add(CatDtoMapper.map(friend));

        return friends;
    }

    @Override
    @Transactional
    public List<CatDto> findFiltered(String color, String breed, Integer year, Long ownerId) {
        return catRepository.findFiltered(color, breed, year, ownerId).stream().map(CatDtoMapper::map).toList();
    }

    private Cat checkCatPersistence(CatRepository repository, long id) {
        var cat = repository.findById(id);
        if (cat.isEmpty())
            throw new UnknownCatException("Cat with ID=%s does not exist".formatted(id));

        return cat.get();
    }
}
