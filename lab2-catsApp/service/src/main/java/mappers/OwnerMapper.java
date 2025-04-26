package mappers;

import dto.OwnerDto;
import entities.Owner;

import java.util.List;
import java.util.stream.Collectors;

public class OwnerMapper {
    public static OwnerDto toDto(Owner owner) {
        return new OwnerDto(owner);
    }

    public static List<OwnerDto> toDtoList(List<Owner> owners) {
        return owners.stream()
                .map(OwnerMapper::toDto)
                .collect(Collectors.toList());
    }

    public static Owner toEntity(OwnerDto dto) {
        Owner owner = new Owner();
        if (dto.getId() != null) {
            owner.setId(dto.getId());
        }

        owner.setFirstName(dto.getFirstName());
        owner.setLastName(dto.getLastName());
        owner.setBirthdate(dto.getBirthdate());

        return owner;
    }
}