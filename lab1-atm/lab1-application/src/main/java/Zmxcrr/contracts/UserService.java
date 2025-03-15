package Zmxcrr.contracts;

import Zmxcrr.models.PinCode;
import Zmxcrr.models.User;
import Zmxcrr.models.accounts.Account;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User createUser(String name, PinCode pinCode);
    boolean authenticateUser(UUID userId, PinCode pinCode);
    void changeUserPinCode(UUID userId, PinCode oldPinCode, PinCode newPinCode);
    void linkAccountToUser(UUID userId, Account account);
    Optional<User> getUserById(UUID id);
}