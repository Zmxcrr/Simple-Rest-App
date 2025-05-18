package Zmxcrr.repositories;


import Zmxcrr.entities.Cat;

import java.util.List;

public interface CatRepositoryCustom {
    List<Cat> findFiltered(String color, String breed, Integer year, Long ownerId);
}
