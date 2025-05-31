package Zmxcrr.users.repositories;

import Zmxcrr.users.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}

