package interfaces;

import entities.User;

import java.util.List;

public interface UserDao {
    User save(User user);
    void deleteByUsername(String username);
    void delete(User user);
    void deleteAll();
    User update(User user);
    User getByUsername(String username);
    List<User> getAll();
}