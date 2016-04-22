package net.thecamaras.controllers;

import net.thecamaras.domain.SystemConfig;
import net.thecamaras.repository.SystemRepository;
import net.thecamaras.services.FlickrService;
import net.thecamaras.services.SystemService;
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
    private SystemService systemService;

    @Autowired
    private FlickrService flickrService;

    @RequestMapping(value = "/prop/{name}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public SystemConfig getSystemParameter(@PathVariable String name){
        return systemService.getParameter(name);
    }

    @RequestMapping(value = "/prop/{name}", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    public SystemConfig setSystemParameter(@PathVariable String name, @RequestBody String value){
        return systemService.setSystemParameter(name, value);
    }

    @RequestMapping(value = "/user/", method = RequestMethod.POST)
    public String setUserPrefix(@RequestParam(name = "name", required = false, defaultValue = "") String name){
        return flickrService.setUserPrefix(name);
    }

    @RequestMapping(value = "/authorize", method = RequestMethod.GET)
    public String startAuthorization(){
        return flickrService.doPreAuthorize();
    }

    @RequestMapping(value = "/authorize/{code}", method = RequestMethod.GET)
    public boolean finishAuthorization(@PathVariable String code){
        flickrService.doAuthorize(code);
        return true;
    }
}
