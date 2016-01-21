package net.thecamaras.controllers;


import net.thecamaras.domain.SystemConfig;
import net.thecamaras.services.DownloadService;
import net.thecamaras.services.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

/**
 * Created by rcamara on 1/14/2016.
 */
@RestController
@RequestMapping(path = "/download")
public class DownloadController {

    @Autowired
    private DownloadService downloadService;

    @Autowired
    private SystemService systemService;

    private int maxDownload;

    @RequestMapping(value = "/photo/{id}", method = RequestMethod.GET)
    public boolean getPhotoDetails(@PathVariable String id,
                                   @RequestParam(name = "useCommon", required = false, defaultValue = "true") boolean useCommon) {
        return downloadService.downloadPhoto(id, useCommon);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public int downloadUser(@PathVariable String id,
                            @RequestParam(name = "max", required = false) Integer maxDownload) {
        return downloadService.downloadUser(id, getMaxDownload(maxDownload));
    }

    @RequestMapping(value = "/photoset/{id}", method = RequestMethod.GET)
    public int downloadPhotoset(@PathVariable String id,
                                @RequestParam(name = "max", required = false) Integer maxDownload) {
        return downloadService.downloadPhotoset(id, getMaxDownload(maxDownload));
    }

    @RequestMapping(value = "/group/{grouId}/user/{userId}", method = RequestMethod.GET)
    public int downloadUser(@PathVariable String grouId, @PathVariable String userId, @RequestParam(name = "max", required = false) Integer maxDownload) {
        return downloadService.downloadUserByGroup(userId, grouId, getMaxDownload(maxDownload));
    }

    @RequestMapping(value = "/user/{userId}/allGroups", method = RequestMethod.GET)
    public int downloadUserInGroups(@PathVariable String userId, @RequestParam(name = "max", required = false) Integer maxDownload) {
        return downloadService.downloadUserAllGroups(userId, getMaxDownload(maxDownload));
    }

    @PostConstruct
    public void init() {
        maxDownload = systemService.getIntegerProperty(SystemConfig.MAX_DOWNLOAD);
    }

    private int getMaxDownload(Integer max){
        if (max == null){
            return maxDownload;
        }
        return max;
    }
}
