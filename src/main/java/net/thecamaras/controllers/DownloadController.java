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
@RequestMapping(path = "/download")
public class DownloadController {

    @Autowired
    private DownloadService downloadService;

    @RequestMapping(value = "/photo/{id}", method = RequestMethod.GET)
    public String getPhotoDetails(@PathVariable String id) {
        return downloadService.downloadPhoto(id);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public int downloadUser(@PathVariable String id) {
        return downloadService.downloadUser(id);
    }

    @RequestMapping(value = "/group/{grouId}/user/{userId}", method = RequestMethod.GET)
    public int downloadUser(@PathVariable String grouId, @PathVariable String userId) {
        return downloadService.downloadUserByGroup(userId, grouId);
    }

    @RequestMapping(value = "/user/{userId}/allGroups", method = RequestMethod.GET)
    public int downloadUserInGroups(@PathVariable String userId) {
        return downloadService.downloadUserAllGroups(userId);
    }
}
