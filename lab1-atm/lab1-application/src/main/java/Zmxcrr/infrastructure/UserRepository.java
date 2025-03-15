package Zmxcrr.infrastructure;

import Zmxcrr.models.PinCode;
import Zmxcrr.models.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    void addUser(User user);
    Optional<User> getUserById(UUID id);
    boolean authenticateUser(UUID userId, PinCode pinCode);
    void updateUserPinCode(UUID userId, PinCode pinCode);
}