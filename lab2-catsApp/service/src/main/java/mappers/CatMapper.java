package mappers;

import dto.CatDto;
import entities.Cat;
import interfaces.CatDao;
import interfaces.OwnerDao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CatMapper {
    public static CatDto toDto(Cat cat) {
        return new CatDto(cat);
    }

    public static List<CatDto> toDtoList(List<Cat> cats) {
        return cats.stream()
                .map(CatMapper::toDto)
                .collect(Collectors.toList());
    }

    public static Cat toEntity(CatDto dto, OwnerDao ownerDao, CatDao catDao) {
        Cat cat = new Cat();
        if (dto.getId() != null) {
            cat.setId(dto.getId());
        }

        cat.setName(dto.getName());
        cat.setBirthdate(dto.getBirthdate());
        cat.setBreed(dto.getBreed());
        cat.setColor(dto.getColor());

        if (dto.getOwner() != null) {
            cat.setOwner(ownerDao.getById(dto.getOwner()));
        }

        if (dto.getFriends() != null && !dto.getFriends().isEmpty()) {
            Set<Cat> friends = new HashSet<>();
            for (Long friendId : dto.getFriends()) {
                Cat friend = catDao.getById(friendId);
                if (friend != null) {
                    friends.add(friend);
                }
            }
            cat.setFriends(friends);
        } else {
            cat.setFriends(new HashSet<>());
        }

        return cat;
    }
}