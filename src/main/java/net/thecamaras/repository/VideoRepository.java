package net.thecamaras.repository;

import net.thecamaras.domain.User;
import net.thecamaras.domain.Video;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by qsz922 on 2016/01/13.
 */
public interface VideoRepository extends CrudRepository<Video, Integer> {
    Video getFirstByFlickrId(String id);
}
