package Zmxcrr.repositories;

import Zmxcrr.entities.Cat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatRepository extends JpaRepository<Cat, Long>, CatRepositoryCustom {
}
