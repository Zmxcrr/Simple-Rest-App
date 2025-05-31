package Zmxcrr.services;

import Zmxcrr.dto.CatDto;
import Zmxcrr.enums.CatColor;
import Zmxcrr.exceptions.UnknownCatException;
import org.hibernate.query.sqm.UnknownEntityException;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CatService {
    long addCat(String name, LocalDate birthdate, String breed, CatColor color, Long ownerID) throws UnknownCatException;

    void removeCat(Long id);

    void removeAllCatsByOwnerId(Long ownerId);

    Optional<CatDto> getCatByID(Long id);

    boolean areFriends(long firstCatID, long secondCatID) throws UnknownCatException;

    void makeFriendship(long firstCatID, long secondCatID) throws UnknownCatException;

    void destroyFriendship(long firstCatID, long secondCatID) throws UnknownCatException;

    List<CatDto> getFriends(long id) throws UnknownCatException;

    List<CatDto> findFiltered(String color, String breed, Integer year, Long ownerId);
}
