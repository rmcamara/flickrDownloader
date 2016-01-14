package net.thecamaras.controllers;

import net.thecamaras.domain.SystemConfig;
import net.thecamaras.repository.SystemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by rcamara on 1/14/2016.
 */
@RestController
@RequestMapping(path = "/system")
public class SystemController {

    @Autowired
    private SystemRepository systemRepository;

    @RequestMapping(value = "/{name}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public SystemConfig getSystemParameter(@PathVariable String name){
        return systemRepository.findFirstByName(name);
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public SystemConfig setSystemParameter(@PathVariable String name, @RequestBody String value){

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
