package Zmxcrr.users.services;

import Zmxcrr.users.dto.UserDto;
import Zmxcrr.users.enums.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void create(String username, String password, UserRole role, Long ownerId);
    void delete(String username);
    List<UserDto> getAll();
    Optional<UserDto> getByUsername(String username);
}
