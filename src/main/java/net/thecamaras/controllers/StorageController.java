package net.thecamaras.controllers;


import net.thecamaras.services.StorageService;
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

    @Autowired
    private StorageService storageService;

    @RequestMapping(value = "/loadUsers", method = RequestMethod.GET)
    public Integer loadUsers(){
        return importService.loadUsers();
    }

    @RequestMapping(value = "/deleteFlagged", method = RequestMethod.GET)
    public Integer deleteFlagged(){
        return storageService.deleteFlaggedFiles();
    }

    @RequestMapping(value = "/findEmpty", method = RequestMethod.GET)
    public Integer findEmpty(){
        return storageService.findZeroFiles();
    }
}
