package Zmxcrr.services;


import Zmxcrr.dto.CatDto;
import Zmxcrr.enums.CatColor;
import Zmxcrr.exceptions.UnknownEntityIdException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CatService {
    long addCat(String name, LocalDate bithdate, String breed, CatColor color, long ownerID) throws UnknownEntityIdException;
    boolean removeCat(long id);

    List<CatDto> getAllCats();

    Optional<CatDto> getCatByID(long id);

    boolean areFriends(long firstCatID, long secondCatID) throws UnknownEntityIdException;

    void makeFriendship(long firstCatID, long secondCatID) throws UnknownEntityIdException;

    void destroyFriendship(long firstCatID, long secondCatID) throws UnknownEntityIdException;

    List<CatDto> getFriends(long id) throws UnknownEntityIdException;

    List<CatDto> findFiltered(CatColor color, String breed, Integer year);
}
