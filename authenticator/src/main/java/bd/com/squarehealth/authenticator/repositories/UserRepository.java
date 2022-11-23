package bd.com.squarehealth.authenticator.repositories;

import bd.com.squarehealth.authenticator.entities.User;
import bd.com.squarehealth.authenticator.enumerations.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUserType(UserType userType);
}
