package bd.com.squarehealth.corelibrary.repositories;

import bd.com.squarehealth.corelibrary.entities.House;
import bd.com.squarehealth.corelibrary.enumerations.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HouseRepository extends JpaRepository<House, Long> {

    Optional<House> findByIdAndPostedBy(Long id, Long postedBy);
    List<House> findByPostedBy(Long postedBy);
    List<House> findByPostStatus(PostStatus postStatus);
}
