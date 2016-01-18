package net.thecamaras.services;

import com.flickr4java.flickr.groups.Group;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.Size;
import net.thecamaras.domain.Photo;
import net.thecamaras.domain.SystemConfig;
import net.thecamaras.domain.User;
import net.thecamaras.repository.PhotoRepository;
import net.thecamaras.repository.UserRepository;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;

/**
 * Created by rcamara on 1/14/2016.
 */
@Service
public class DownloadService {
    private static final int USER_PAGE_SIZE = 10;

    private Logger logger = LoggerFactory.getLogger(DownloadService.class);

    @Autowired
    private SystemService systemService;

    @Autowired
    private FlickrService flickrService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhotoRepository photoRepository;

    private File downloadRoot;
    private int maxDownload;
    private FastDateFormat dateParser;

    @PostConstruct
    public void init() {
        String root = systemService.getProperty(SystemConfig.DOWNLOAD_ROOT);
        if (root == null) {
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
        maxDownload = systemService.getIntegerProperty(SystemConfig.MAX_DOWNLOAD);
        dateParser = FastDateFormat.getInstance("yyMMdd-HHmmss");
    }

    public String getValidFileName(String fileName) {
        String newFileName = fileName.replace("^\\.+", "").replaceAll("[\\\\/:*?\"<>|]", "").trim();
        if (newFileName.length() == 0)
            throw new IllegalStateException("File Name " + fileName + " results in a empty fileName!");

        newFileName = newFileName.replaceAll("[^\\p{Print}]", "").trim();
        if (newFileName.length() == 0)
            throw new IllegalStateException("File Name " + fileName + " results in a empty fileName!");
        return newFileName;
    }

    public String downloadPhoto(String pictureId) {

        return "";
    }

    public int downloadUser(String userId) {
        logger.info("Downloading photos for: " + userId);
        flickrService.doAuthenticate();

        User user = getUser(userId);
        if (user == null) {
            return 0;
        }
        File destination = getDestination(user);

        PhotoList<com.flickr4java.flickr.photos.Photo> photoList = new PhotoList<>();
        int downloadCount = 0;
        do {
            photoList = flickrService.getUserPhotos(userId, photoList.getPage() + 1, USER_PAGE_SIZE);

            for (int i = 0; i < photoList.size(); i++) {
                com.flickr4java.flickr.photos.Photo photo = photoList.get(i);
                writeImage(photo, user, destination);
            }
            downloadCount += photoList.getPerPage();

        } while (photoList.getPage() < photoList.getPages() && downloadCount < maxDownload);

        logger.info("Downloaded photos for: " + userId);
        return downloadCount;
    }

    public int downloadUserByGroup(String userId, String groupId){
        logger.info("Downloading photos for: " + userId + " in " + groupId);
        flickrService.doAuthenticate();

        User user = getUser(userId);
        if (user == null) {
            return 0;
        }
        File destination = getDestination(user);
        int downloadCount = downloadUserByGroup(user, destination, groupId);

        logger.info("Downloaded photos for: " + userId);
        return downloadCount;
    }

    public int downloadUserAllGroups(String userId){
        logger.info("Downloading photos for all groups: " + userId);
        flickrService.doAuthenticate();

        User user = getUser(userId);
        if (user == null) {
            return 0;
        }
        File destination = getDestination(user);
        Collection<Group> groupList = flickrService.getMyGroups();
        int downloadCount = 0;
        for (Group group : groupList) {
            logger.info("Downloading from " + group.getName());
            downloadCount = downloadUserByGroup(user, destination, group.getId());
        }

        logger.info("Downloaded photos for: " + userId);
        return downloadCount;
    }

    private int downloadUserByGroup(User user, File destination, String groupId){
        int downloadCount = 0;
        PhotoList<com.flickr4java.flickr.photos.Photo> photoList = new PhotoList<>();
        do {
            photoList = flickrService.getUserPhotosInGroup(groupId, user.getFlickrId(), photoList.getPage() + 1, USER_PAGE_SIZE);
            if (photoList.getPage() == 1) {
                logger.info(String.format("Downloading %d images from %s", photoList.getTotal(), groupId));
            }
            downloadCount++;

            for (int i = 0; i < photoList.size(); i++) {
                com.flickr4java.flickr.photos.Photo photo = photoList.get(i);
                writeImage(photo, user, destination);
            }
            downloadCount += photoList.getPerPage();
        } while (photoList.getPage() < photoList.getPages() && downloadCount < maxDownload);
        return  downloadCount;
    }

    private boolean writeImage(com.flickr4java.flickr.photos.Photo photo, User user, File destination) {
        try {
            String filename = dateParser.format(photo.getDatePosted()) + " " + photo.getTitle() + " (" + photo.getId() + ")";
            if (photoRepository.getFirstByFlickrId(photo.getId()) != null) {
                logger.debug("Already exists: " + filename);
                return false;
            }
            filename = getValidFileName(filename);
            File newFile = new File(destination, filename + ".jpg");
            if (newFile.exists()) {
                logger.warn("Already exists on disk: " + filename);
                return false;
            }

            Size size = flickrService.getBestPhoto(photo.getId());
            if (!user.isIgnoreSizeCheck() && size.getWidth() < 900 && size.getHeight() < 900) {
                logger.debug("Too small: " + filename);
                return false;
            }

            URL u = new URL(size.getSource());
            logger.info("Now writing " + filename + " to " + destination.getCanonicalPath());
            URLConnection con = u.openConnection();
            con.setConnectTimeout(30000);
            con.setReadTimeout(30000);
            BufferedInputStream inStream = new BufferedInputStream(con.getInputStream());
            FileOutputStream fos = new FileOutputStream(newFile);

            int read;

            while ((read = inStream.read()) != -1) {
                fos.write(read);
            }
            fos.flush();
            fos.close();
            inStream.close();

            Photo photoRecord = new Photo(photo);
            photoRecord.setFileLocation(downloadRoot.toURI().relativize(newFile.toURI()).getPath());
            photoRepository.save(photoRecord);
        } catch (MalformedURLException e) {
            logger.error("Error creating downloading photo" + photo.getId(), e);
        } catch (IOException e) {
            logger.error("Error creating downloading photo" + photo.getId(), e);
        }
        return true;
    }

    /**
     * Get the user and update local use model if the name is gone.
     *
     * @param userId
     * @return
     */
    private User getUser(String userId) {
        User fUser = flickrService.getUser(userId);
        User user = userRepository.getFirstByFlickrId(userId);

        if (fUser == null) {
            if (user != null && !user.isInactive()) {
                logger.warn("User being marked inactive");
                user.setInactive(true);
                userRepository.save(user);
            }
            return null;
        }

        if (user == null) {
            logger.debug("New user found");
            user = fUser;
            userRepository.save(user);
        }

        if (!user.getUsername().equals(fUser.getUsername())) {
            // TODO update the directory location
            user.setUsername(fUser.getUsername());
            user.addUsername(fUser.getUsername());
            userRepository.save(user);
        }
        return user;
    }

    protected File getDestination(User user) {
        File destination = new File(downloadRoot, getValidFileName(user.getUsername()));
        if (!destination.exists()) {
            destination.mkdirs();
            try {
                File userFile = new File(destination, user.getFlickrId());
                if (!userFile.exists()) {
                    userFile.createNewFile();
                }
            } catch (IOException e) {
                logger.error("Error creating user folder" + user.getFlickrId(), e);
                e.printStackTrace();
            }
        }
        return destination;
    }
}
