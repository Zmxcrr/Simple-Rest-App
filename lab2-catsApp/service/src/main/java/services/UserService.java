package services;

import dto.UserDto;
import entities.Owner;
import entities.User;
import exceptions.UnknownEntityIdException;
import exceptions.UserServiceOperationException;
import interfaces.OwnerDao;
import interfaces.UserDao;

import java.util.List;

public class UserService {
    private final UserDao userDao;
    private final OwnerDao ownerDao;

    public UserService(UserDao userDao, OwnerDao ownerDao) {
        this.userDao = userDao;
        this.ownerDao = ownerDao;
    }

    public User createUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setRole(userDto.getRole());

        if (userDto.getOwner() != null) {
            Owner owner = ownerDao.getById(userDto.getOwner());
            if (owner == null) {
                throw new UserServiceOperationException("Owner with ID=%s does not exist".formatted(userDto.getOwner()));
            }
            user.setOwner(owner);
        }

        return userDao.save(user);
    }

    public User updateUser(UserDto userDto) {
        User user = userDao.getByUsername(userDto.getUsername());
        if (user == null) {
            throw new UnknownEntityIdException("User with ID=%s does not exist".formatted(userDto.getUsername()));
        }

        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(userDto.getPassword());
        }

        user.setRole(userDto.getRole());

        if (userDto.getOwner() != null) {
            Owner owner = ownerDao.getById(userDto.getOwner());
            if (owner == null) {
                throw new UserServiceOperationException("Owner with ID=%s does not exist".formatted(userDto.getOwner()));
            }
            user.setOwner(owner);
        } else {
            user.setOwner(null);
        }

        return userDao.update(user);
    }

    public void deleteUser(String username) {
        userDao.deleteByUsername(username);
    }

    public User getUser(String username) {
        return userDao.getByUsername(username);
    }

    public List<User> getAllUsers() {
        return userDao.getAll();
    }
}