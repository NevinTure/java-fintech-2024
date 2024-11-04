package edu.java.kudagoapi.repositories;

import edu.java.kudagoapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface JpaUserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String username);
}
