package net.thecamaras.controllers;


import net.thecamaras.domain.Photo;
import net.thecamaras.services.FlickrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by rcamara on 1/14/2016.
 */
@RestController
@RequestMapping(path = "/download")
public class DownloadController {

    @Autowired
    private FlickrService flickrService;

    @RequestMapping(value = "/photo/{id}", method = RequestMethod.GET)
    public Photo getPhotoDetails(@PathVariable String id){
        return flickrService.getPhoto(id);
    }
}
