package net.thecamaras.controllers;


import net.thecamaras.domain.User;
import net.thecamaras.services.DownloadService;
import net.thecamaras.services.SystemService;
import net.thecamaras.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by rcamara on 1/14/2016.
 */
@RestController
@RequestMapping(path = "/batch")
public class BatchController {

    @Autowired
    private DownloadService downloadService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/users/list", method = RequestMethod.GET)
    public List<User> listUsers() {
        return userService.getUserAutoDownload();
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public int getPhotoDetails() {
        return downloadService.downloadAllUsersRecent();
    }

    @RequestMapping(value = "/groups", method = RequestMethod.GET)
    public int downloadUser() {
        return downloadService.downloadAllUsersInGroups();
    }

    @RequestMapping(value = "/groups/list", method = RequestMethod.GET)
    public List<User> listUsersByGroup() {
        return userService.getUserAutoDownloadByGroup();
    }
}
