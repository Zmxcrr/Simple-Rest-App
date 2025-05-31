package Zmxcrr.repositories;

import Zmxcrr.entities.Cat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatRepository extends JpaRepository<Cat, Long>, CatRepositoryCustom {
    void deleteAllByOwner(Long ownerId);
}