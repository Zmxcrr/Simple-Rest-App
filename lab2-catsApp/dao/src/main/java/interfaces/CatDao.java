package interfaces;

import entities.Cat;

import java.util.List;

public interface CatDao {
    Cat save(Cat cat);
    void deleteById(long id);
    void delete(Cat cat);
    void deleteAll();
    Cat update(Cat cat);
    Cat getById(long id);
    List<Cat> getAll();
}
