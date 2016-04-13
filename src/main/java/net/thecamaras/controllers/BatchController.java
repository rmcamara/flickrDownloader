package net.thecamaras.controllers;


import io.swagger.annotations.ApiOperation;
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

    @ApiOperation("Display list of users who will be auto downloaded from")
    @RequestMapping(value = "/users/list", method = RequestMethod.GET)
    public List<User> listUsers() {
        return userService.getUserAutoDownload();
    }

    @ApiOperation("Download new photos from users")
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public int getPhotoDetails() {
        return downloadService.downloadAllUsersRecent();
    }

    @ApiOperation("Download all photos from listed users added to each group in the last X days")
    @RequestMapping(value = "/groups", method = RequestMethod.GET)
    public int downloadUser() {
        return downloadService.downloadAllUsersInGroups();
    }

    @ApiOperation("List of users that are displayed by group")
    @RequestMapping(value = "/groups/list", method = RequestMethod.GET)
    public List<User> listUsersByGroup() {
        return userService.getUserAutoDownloadByGroup();
    }
}
