package Zmxcrr.mappers;

import Zmxcrr.dto.CatDto;
import Zmxcrr.entities.Cat;

public class CatDtoMapper {
    public static CatDto map(Cat cat) {
        var dto = new CatDto();
        dto.setName(cat.getName());
        dto.setBreed(cat.getBreed());
        dto.setColor(cat.getColor().name());
        dto.setBirthdate(cat.getBirthdate());
        dto.setId(cat.getId());
        dto.setOwner(cat.getOwner());
        dto.setFriends(cat.getFriends().stream().map(Cat::getId).toList());

        return dto;
    }
}
