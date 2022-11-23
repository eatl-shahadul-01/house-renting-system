package bd.com.squarehealth.corelibrary.repositories;

import bd.com.squarehealth.corelibrary.entities.User;
import bd.com.squarehealth.corelibrary.enumerations.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUserRole(UserRole userRole);
}
