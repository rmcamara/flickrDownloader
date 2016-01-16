package net.thecamaras.repository;

import net.thecamaras.domain.ImportHistoryLocation;
import net.thecamaras.domain.Photo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by qsz922 on 2016/01/13.
 */
public interface ImportHistoryRepository extends CrudRepository<ImportHistoryLocation, Integer> {
    ImportHistoryLocation getFirstByFileLocation(String location);

}
