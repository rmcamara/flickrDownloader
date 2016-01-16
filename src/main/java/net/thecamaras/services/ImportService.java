package net.thecamaras.services;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.thecamaras.domain.SystemConfig;
import net.thecamaras.domain.User;
import net.thecamaras.repository.UserRepository;

/**
 * Created by rcamara on 1/14/2016.
 */
@Service
public class ImportService {
    private Logger logger = LoggerFactory.getLogger(ImportService.class);

    @Autowired
    private SystemService systemService;

    @Autowired
    private FlickrService flickrService;

    @Autowired
    private UserRepository userRepository;

    private File downloadRoot;

    @PostConstruct
    public void init() {
        downloadRoot = new File(systemService.getProperty(SystemConfig.DOWNLOAD_ROOT));
        if (!downloadRoot.exists()) {
            try {
                logger.error("Download root doesn't exists: " + downloadRoot.getCanonicalPath());
            } catch (IOException e) {
                logger.error("Problem getting download root", e);
            }
        }
    }

    public int loadUsers() {
        return searchDirectory(downloadRoot);
    }

    private int searchDirectory(File root) {
        int count = 0;
        Pattern idlookup = Pattern.compile(".*@...");
        for (File file : root.listFiles()) {
            if (file.isDirectory()) {
                count += searchDirectory(file);
                continue;
            }
            String userId = file.getName();
            Matcher matcher = idlookup.matcher(userId);
            if (matcher.find()) {
                logger.info(String.format("User found %s", userId));
                if (userRepository.getFirstByFlickrId(userId) != null) {
                    logger.debug("User already found " + userId);
                    continue;
                }
                User user = flickrService.getUser(userId);
                if (user != null) {
                    userRepository.save(user);
                }
                //else if () { // try to figure it out from an image file
                //}
                else {
                    user = new User();
                    user.setUsername(root.getName());
                }

                count++;
            }
        }
        return count;
    }


}
