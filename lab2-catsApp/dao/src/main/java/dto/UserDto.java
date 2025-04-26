package dto;

import entities.User;
import enums.UserRole;
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
        if (user.getOwner() != null)
            owner = user.getOwner().getId();
    }
}