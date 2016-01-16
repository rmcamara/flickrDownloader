package net.thecamaras.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.thecamaras.domain.Photo;
import net.thecamaras.services.FlickrService;
import net.thecamaras.services.ImportService;

/**
 * Created by rcamara on 1/14/2016.
 */
@RestController
@RequestMapping(path = "/storage")
public class StorageController {

    @Autowired
    private ImportService importService;

    @RequestMapping(value = "/loadUsers", method = RequestMethod.GET)
    public Integer loadUsers(){
        return importService.loadUsers();
    }
}
