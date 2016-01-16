package net.thecamaras.services;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;

import net.thecamaras.domain.Photo;
import net.thecamaras.repository.PhotoRepository;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.thecamaras.domain.SystemConfig;
import net.thecamaras.domain.User;
import net.thecamaras.repository.UserRepository;

import java.util.Date;

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

    @Autowired
    private PhotoRepository photoRepository;

    private File downloadRoot;

    private Pattern idLookup;
    private Pattern photoLookup;
    private Pattern photoLookup_v1;
    private FastDateFormat dateParser;

    @PostConstruct
    public void init() {
        String root = systemService.getProperty(SystemConfig.DOWNLOAD_ROOT);
        if (root == null){
            root = ".";
        }
        downloadRoot = new File(root);
        if (!downloadRoot.exists()) {
            try {
                logger.error("Download root doesn't exists: " + downloadRoot.getCanonicalPath());
            } catch (IOException e) {
                logger.error("Problem getting download root", e);
            }
        }

        idLookup = Pattern.compile(".*@...");
        photoLookup = Pattern.compile("(\\d{6}-\\d{6})(.*)\\((\\d*)\\).jpg");
        photoLookup_v1 = Pattern.compile("(.*)\\((\\d*)\\).jpg");
        dateParser = FastDateFormat.getInstance("yyMMdd-HHmmss");
    }

    public int loadUsers() {
        return searchDirectory(downloadRoot);
    }

    private int searchDirectory(File root) {
        int count = 0;
        User user = null;
        ArrayList<Photo> photos = new ArrayList<>();
        for (File file : root.listFiles()) {
            if (file.isDirectory()) {
                count += searchDirectory(file);
                continue;
            }
            String userId = file.getName();
            Matcher matcher = idLookup.matcher(userId);
            if (matcher.find()) {
                logger.info(String.format("User found %s", userId));
                user = userRepository.getFirstByFlickrId(userId);
                if (user != null) {
                    logger.debug("User already found " + userId);
                } else {
                    user = flickrService.getUser(userId);
                    count++;
                }

                continue;
            }

            matcher = photoLookup.matcher(userId);
            if (matcher.matches()) {
                Photo photo = new Photo();
                photo.setTitle(matcher.group(2).trim());
                photo.setFlickrId(matcher.group(3));
                photo.setDatePosted(parseDate(matcher.group(1)));
                photo.setDateDownloaded(new Date(file.lastModified()));
                logger.info("Size: " + file.length());
                photo.setDeleted(file.length() < 10);
                photo.setFileLocation(getLocationToRoot(file));
                photos.add(photo);
                continue;
            }

            matcher = photoLookup_v1.matcher(userId);
            if (matcher.matches()) {
                Photo photo = new Photo();
                photo.setTitle(matcher.group(1).trim());
                photo.setFlickrId(matcher.group(2));
                photo.setDateDownloaded(new Date(file.lastModified()));
                photo.setDeleted(file.length() < 10);
                photo.setFileLocation(getLocationToRoot(file));
                photos.add(photo);
                continue;
            }
        }

        if (user != null) {
            userRepository.save(user);
            for(Photo photo : photos){
                photo.setOwnerId(user.getFlickrId());
            }
        }
        if (photos.size() > 0) {
            photoRepository.save(photos);
        }
        return count;
    }

    private Date parseDate(String input) {
        try {
            return dateParser.parse(input);
        } catch (ParseException e) {
            logger.warn("Error reading date " + input);
        }
        return null;
    }

    private String getLocationToRoot(File file) {
        return downloadRoot.toURI().relativize(file.toURI()).getPath();
    }


}
