package net.thecamaras.controllers;


import net.thecamaras.domain.User;
import net.thecamaras.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by rcamara on 1/14/2016.
 */
@RestController
@RequestMapping(path = "/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/{flickrId}", method = RequestMethod.GET)
    public User getUser(@PathVariable String flickrId) {
        User user = userService.getUser(flickrId);
        if (user == null) {
            throw new IllegalArgumentException("Unknown flickrId: " + flickrId);
        }
        return user;
    }

    @RequestMapping(value = "/{flickrId}/add", method = RequestMethod.GET)
    public User addUser(@PathVariable String flickrId) {
        return userService.addUser(flickrId);
    }

    @RequestMapping(value = "/{flickrId}/ignoreSize/{value}", method = RequestMethod.GET)
    public User setIgnoreSize(@PathVariable String flickrId, @PathVariable Boolean ignoreSize) {
        User user = getUser(flickrId);
        user.setIgnoreSizeCheck(ignoreSize);
        return userService.save(user);
    }

    @RequestMapping(value = "/{flickrId}/autoDownload/{value}", method = RequestMethod.GET)
    public User setAutoDownload(@PathVariable String flickrId, @PathVariable boolean autoDownload) {
        User user = getUser(flickrId);
        user.setAutoDownload(autoDownload);
        return userService.save(user);
    }

    @RequestMapping(value = "/{flickrId}/autoGroup/{value}", method = RequestMethod.GET)
    public User setAutoDownloadGroup(@PathVariable String flickrId, @PathVariable boolean autoDownloadGroup) {
        User user = getUser(flickrId);
        user.setAutoDownloadGroup(autoDownloadGroup);
        return userService.save(user);
    }
}
