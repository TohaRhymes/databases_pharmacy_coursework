package se.ifmo.pepe.cwdb.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import se.ifmo.pepe.cwdb.backend.auth.User;

public interface UserRepo extends JpaRepository<User, Long>, CrudRepository<User, Long> {
    User findByUsername(String username);
}
