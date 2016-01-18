package net.thecamaras.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import net.thecamaras.domain.User;

import java.util.List;

/**
 * Created by qsz922 on 2016/01/13.
 */
public interface UserRepository extends CrudRepository<User, Integer> {
    @Query("SELECT COUNT(u) FROM User u")
    Long getUsercount();

    User getFirstByFlickrId(String flickrId);

    List<User> findByFlickrIdIsNull();

}
