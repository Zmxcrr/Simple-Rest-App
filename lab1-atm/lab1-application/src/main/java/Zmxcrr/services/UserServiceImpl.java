package Zmxcrr.services;

import Zmxcrr.contracts.UserService;
import Zmxcrr.infrastructure.UserRepository;
import Zmxcrr.models.PinCode;
import Zmxcrr.models.User;
import Zmxcrr.models.accounts.Account;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(String name, PinCode pinCode) {

        User user = new User(
                UUID.randomUUID(),
                name,
                pinCode,
                new ArrayList<>()
        );

        userRepository.addUser(user);
        return user;
    }

    @Override
    public boolean authenticateUser(UUID userId, PinCode pinCode) {
        return userRepository.authenticateUser(userId, pinCode);
    }

    @Override
    public void changeUserPinCode(UUID userId, PinCode oldPinCode, PinCode newPinCode) {
        Optional<User> userOpt = userRepository.getUserById(userId);

        if (userOpt.isPresent() && userRepository.authenticateUser(userId, oldPinCode)) {
            User user = userOpt.get();
            user.setPinCode(newPinCode);
            userRepository.updateUserPinCode(userId, newPinCode);
        } else {
            throw new RuntimeException("Wrong PinCode or UserId");
        }
    }

    @Override
    public void linkAccountToUser(UUID userId, Account account) {
        Optional<User> userOpt = userRepository.getUserById(userId);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.getAccountIds().add(account.getId());
        } else {
            throw new RuntimeException("User not Found");
        }
    }

    @Override
    public Optional<User> getUserById(UUID id) {
        return userRepository.getUserById(id);
    }
}