package net.thecamaras.repository;

import net.thecamaras.domain.Photo;
import net.thecamaras.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by qsz922 on 2016/01/13.
 */
public interface PhotoRepository extends PagingAndSortingRepository<Photo, Integer> {
    Photo getFirstByFlickrId(String id);

    Page<Photo> findByFlagRemoval(boolean flagged, Pageable pageable);

    @Modifying
    @Query("UPDATE Photo p SET p.deleted=1, p.flagRemoval=0 WHERE p.flagRemoval=1")
    void updateFlaggedToDeleted();

}
