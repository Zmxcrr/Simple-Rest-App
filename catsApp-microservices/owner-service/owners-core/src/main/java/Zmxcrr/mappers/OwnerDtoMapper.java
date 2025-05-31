package Zmxcrr.mappers;


import Zmxcrr.dto.OwnerDto;
import Zmxcrr.entities.Owner;

public class OwnerDtoMapper {
    public static OwnerDto map (Owner owner) {
        var dto = new OwnerDto();
        dto.setId(owner.getId());
        dto.setFirstName(owner.getFirstName());
        dto.setLastName(owner.getLastName());
        dto.setBirthdate(owner.getBirthdate());

        return dto;
    }
}