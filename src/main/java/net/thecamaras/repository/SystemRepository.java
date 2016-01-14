package net.thecamaras.repository;

import net.thecamaras.domain.SystemConfig;
import net.thecamaras.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by qsz922 on 2016/01/13.
 */
public interface SystemRepository extends CrudRepository<SystemConfig, Integer> {
    SystemConfig findFirstByName(String name);
}
