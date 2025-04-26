package mappers;

import dto.UserDto;
import entities.User;
import interfaces.OwnerDao;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserDto toDto(User user) {
        return new UserDto(user);
    }

    public static List<UserDto> toDtoList(List<User> users) {
        return users.stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    public static User toEntity(UserDto dto, OwnerDao ownerDao) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());

        if (dto.getOwner() != null) {
            user.setOwner(ownerDao.getById(dto.getOwner()));
        }

        return user;
    }
}