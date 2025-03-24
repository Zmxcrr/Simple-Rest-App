package Zmxcrr;

import Zmxcrr.infrastructure.UserRepository;
import Zmxcrr.models.PinCode;
import Zmxcrr.models.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository implements UserRepository {
    private final Map<UUID, User> users;

    public InMemoryUserRepository() {
        this.users = new TreeMap<>();
    }

    @Override
    public void addUser(User user) {
        if (users.containsKey(user.getId())) {
            throw new RuntimeException("Account with this id %s already exists".formatted(user.getId()));
        }
        users.put(user.getId(), user);
    }

    @Override
    public Optional<User> getUserById(UUID id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public boolean authenticateUser(UUID userId, PinCode pinCode) {
        User user = users.get(userId);
        if (user == null) {
            return false;
        }

        return Objects.equals(user.getPinCode(), pinCode);
    }

    @Override
    public void updateUserPinCode(UUID userId, PinCode pinCode) {
        User user = users.get(userId);
        if (user == null) {
            throw new RuntimeException("User with id " + userId + " not found");
        }
    }
}