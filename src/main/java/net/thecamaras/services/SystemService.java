package net.thecamaras.services;

import net.thecamaras.domain.SystemConfig;
import net.thecamaras.repository.SystemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by rcamara on 1/14/2016.
 */
@Service
public class SystemService {

    @Autowired
    private SystemRepository systemRepository;

    public SystemConfig getParameter(String name) {
        return systemRepository.findFirstByName(name);
    }

    public String getProperty(String name) {
        SystemConfig cfg = systemRepository.findFirstByName(name);
        if (cfg == null) {
            return null;
        }
        return cfg.getValue();
    }

    public Integer getIntegerProperty(String name){
        SystemConfig cfg = systemRepository.findFirstByName(name);
        if (cfg == null) {
            return null;
        }
        return Integer.parseInt(cfg.getValue());
    }

    public SystemConfig setSystemParameter(String name, String value){

        SystemConfig cfg;
        cfg = systemRepository.findFirstByName(name);
        if (cfg == null){
            cfg = new SystemConfig();
            cfg.setName(name);
        }
        cfg.setValue(value);
        systemRepository.save(cfg);
        return cfg;
    }
}
