package Zmxcrr.services;

import Zmxcrr.dto.UserDto;
import Zmxcrr.enums.UserRole;
import java.util.List;

public interface UserService {
    void create(String username, String password, UserRole role, Long ownerId);
    void delete(String username);
    List<UserDto> getAll();
}