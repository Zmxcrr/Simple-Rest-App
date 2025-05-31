package Zmxcrr.users.dto;

import Zmxcrr.users.entities.User;
import Zmxcrr.users.enums.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
    private String username;
    private String password;
    private UserRole role;
    private Long owner;

    public UserDto(User user) {
        username = user.getUsername();
        role = user.getRole();
        password = user.getPassword();
        owner = user.getOwner();
    }
}