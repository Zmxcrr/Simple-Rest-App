package Zmxcrr.users.services;

import Zmxcrr.clients.OwnerClient;
import Zmxcrr.dto.OwnerDto;
import Zmxcrr.users.dto.UserDto;
import Zmxcrr.users.entities.User;
import Zmxcrr.users.enums.UserRole;
import Zmxcrr.users.exceptions.UserServiceException;
import Zmxcrr.users.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private OwnerClient ownerClient;
    @Override
    @Transactional
    public void create(String username, String password, UserRole role, Long ownerId) {
        if (userRepository.findById(username).isPresent())
            throw new UserServiceException("Failed to create user: user with username %s already exists".formatted(username));

        var response = ownerClient.getById(ownerId);
        if (!response.getStatusCode().isSameCodeAs(HttpStatus.OK) || response.getBody() == null)
            throw new UserServiceException("Failed to create user: owner %s does not exist");

        var user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        user.setOwner(((OwnerDto)response.getBody()).getId());

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(String username) {
        var optional = userRepository.findById(username);
        if (optional.isEmpty())
            throw new UserServiceException("Failed to delete: user with username %s not found".formatted(username));

        userRepository.delete(optional.get());
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(UserDto::new).toList();
    }

    @Override
    public Optional<UserDto> getByUsername(String username) throws UserServiceException {
        var optional = userRepository.findById(username);
        return optional.map(UserDto::new);
    }
}
