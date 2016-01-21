package net.thecamaras.controllers;

import net.thecamaras.domain.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by rcamara on 1/14/2016.
 */
@RestController
@RequestMapping(path = "/state")
public class StateController {

    @Autowired
    private State state;

    @RequestMapping(value = "/active", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public boolean isActive(){
        return state.active;
    }

    @RequestMapping(value = "/active/{canceled}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public boolean setActive(@PathVariable Boolean canceled){
        state.active = canceled;
        return state.active;
    }
}
