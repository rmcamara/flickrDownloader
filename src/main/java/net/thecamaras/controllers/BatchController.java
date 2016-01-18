package net.thecamaras.controllers;


import net.thecamaras.services.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by rcamara on 1/14/2016.
 */
@RestController
@RequestMapping(path = "/batch")
public class BatchController {

    @Autowired
    private DownloadService downloadService;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public int getPhotoDetails() {
        return downloadService.downloadAllUsersRecent();
    }

    @RequestMapping(value = "/groups", method = RequestMethod.GET)
    public int downloadUser() {
        return downloadService.downloadAllUsersRecent();
    }
}
