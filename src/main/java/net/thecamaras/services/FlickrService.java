package net.thecamaras.services;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.groups.Group;
import com.flickr4java.flickr.groups.pools.PoolsInterface;
import com.flickr4java.flickr.people.PeopleInterface;
import com.flickr4java.flickr.photos.*;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.PhotosetsInterface;
import net.thecamaras.domain.Photo;
import net.thecamaras.domain.SystemConfig;
import net.thecamaras.domain.User;
import org.scribe.model.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by rcamara on 1/14/2016.
 */
@Service
public class FlickrService {

    private Logger logger = LoggerFactory.getLogger(FlickrService.class);

    @Value("${flickr.downloader.apiKey}")
    private String apiKey;

    @Value("${flickr.downloader.secret}")
    private String apiSecret;

    @Autowired
    private SystemService systemService;

    private Flickr flickr;

    @PostConstruct
    public void init() {
        try {
            flickr = new Flickr(apiKey, apiSecret, new REST());
            AuthInterface authInterface = flickr.getAuthInterface();
            Auth token = authInterface.checkToken(new Token(systemService.getProperty(SystemConfig.TOKEN), systemService.getProperty(SystemConfig.TOKEN_SECRET)));
            RequestContext.getRequestContext().setAuth(token);
            logger.info("startup authentication success");
        } catch (Exception e) {
            logger.error("startup authentication failed");
        }
    }

    public void doAuthenticate() {
        try {
            AuthInterface authInterface = flickr.getAuthInterface();
            Auth token = authInterface.checkToken(new Token(systemService.getProperty(SystemConfig.TOKEN), systemService.getProperty(SystemConfig.TOKEN_SECRET)));
            RequestContext.getRequestContext().setAuth(token);
            logger.info("authentication success");
        } catch (Exception e) {
            logger.error("authentication failed");
        }
    }

    public com.flickr4java.flickr.photos.Photo getPhoto(String photoId) {
        PhotosInterface photosInterface = flickr.getPhotosInterface();
        try {
            com.flickr4java.flickr.photos.Photo photo = photosInterface.getPhoto(photoId);
            return photo;
        } catch (FlickrException e) {
            String msg = String.format("Error getting photo (%s). Caused by: %s-%s", photoId, e.getErrorCode(), e.getErrorMessage());
            logger.error(String.format(msg));
            logger.debug(msg, e);
        }
        return null;
    }


    public User getUserFromPhotoId(String photoId) {
        PhotosInterface photosInterface = flickr.getPhotosInterface();
        try {
            com.flickr4java.flickr.photos.Photo photo = photosInterface.getPhoto(photoId);
            User result = new User(photo.getOwner());
            return result;
        } catch (FlickrException e) {
            if ("1".equals(e.getErrorCode())) {
                return null;
            }

            logger.error("Error loading photo info " + photoId + " Message: " + e.getErrorMessage());
        }
        return null;
    }

    public User getUser(String userId) {
        PeopleInterface peopleInterface = flickr.getPeopleInterface();

        try {
            com.flickr4java.flickr.people.User user = peopleInterface.getInfo(userId);
            User result = new User(user);
            return result;
        } catch (FlickrException e) {
            if ("1".equals(e.getErrorCode())) {
                return null;
            }

            logger.error("Error loading user info " + userId + " Message: " + e.getErrorMessage());
        }

        return null;
    }

    public PhotoList<com.flickr4java.flickr.photos.Photo> getUserPhotos(String userId, int page, int pageSize) {
        return getUserPhotos(userId, null, page, pageSize);
    }

    public PhotoList<com.flickr4java.flickr.photos.Photo> getUserPhotos(String userId, Date minDate, int page, int pageSize) {
        PhotosInterface photosInterface = flickr.getPhotosInterface();
        SearchParameters params = new SearchParameters();
        params.setUserId(userId);
        params.setSafeSearch(Flickr.SAFETYLEVEL_RESTRICTED);
        params.setExtras(getExtras());
        if (minDate != null) {
            params.setMinUploadDate(minDate);
        }

        try {
            return photosInterface.search(params, pageSize, page);
        } catch (FlickrException e) {
            String msg = String.format("Error getting user photo (%s, %d). Caused by: %s-%s", userId, page, e.getErrorCode(), e.getErrorMessage());
            logger.error(String.format(msg));
            logger.debug(msg, e);
        }
        return new PhotoList<>();
    }

    public PhotoList<com.flickr4java.flickr.photos.Photo> getUserPhotosInGroup(String groupId, String userId, int page, int pageSize) {
        PoolsInterface poolsInterface = flickr.getPoolsInterface();
        try {
            return poolsInterface.getPhotos(groupId, userId, null, getExtras(), pageSize, page);
        } catch (FlickrException e) {
            String msg = String.format("Error getting user(%s, %d) in group(%s). Caused by: %s-%s", userId, page, groupId, e.getErrorCode(), e.getErrorMessage());
            logger.error(String.format(msg));
            logger.debug(msg, e);
        }
        return null;
    }

    public Collection<Group> getMyGroups() {
        PoolsInterface poolsInterface = flickr.getPoolsInterface();
        try {
            return poolsInterface.getGroups();
        } catch (FlickrException e) {
            String msg = String.format("Error getting my groups. Caused by: %s-%s", e.getErrorCode(), e.getErrorMessage());
            logger.error(String.format(msg));
            logger.debug(msg, e);
        }
        return null;
    }

    public Photoset getPhotoSet(String photoSetId){
        PhotosetsInterface photosetsInterface = flickr.getPhotosetsInterface();

        try {
            return photosetsInterface.getInfo(photoSetId);
        } catch (FlickrException e) {
            String msg = String.format("Error getting photoset info. Caused by: %s-%s", e.getErrorCode(), e.getErrorMessage());
            logger.error(String.format(msg));
            logger.debug(msg, e);
        }
        return null;
    }

    public PhotoList<com.flickr4java.flickr.photos.Photo> getPhotoSetPhotos(String photoSetId, int page, int pageSize) {
        PhotosetsInterface photosetsInterface = flickr.getPhotosetsInterface();
        try {
            return photosetsInterface.getPhotos(photoSetId, getExtras(), Flickr.PRIVACY_LEVEL_FRIENDS_FAMILY, pageSize, page);
        } catch (FlickrException e) {
            String msg = String.format("Error getting photoset (%s, %d). Caused by: %s-%s", photoSetId, page, e.getErrorCode(), e.getErrorMessage());
            logger.error(String.format(msg));
            logger.debug(msg, e);
        }
        return null;
    }

    public Size getBestPhoto(String photoId) {
        PhotosInterface photosInterface = flickr.getPhotosInterface();

        try {
            Collection<Size> sizes = photosInterface.getSizes(photoId);
            Size size = new Size();
            long sizeVal = 0;
            for (Size csize : sizes) {
                long tempVal = csize.getWidth() * csize.getHeight();
                if (tempVal > sizeVal) {
                    size = csize;
                    sizeVal = tempVal;
                }
            }

            return size;
        } catch (FlickrException e) {
            String msg = String.format("Error getting photo (%s. Caused by: %s-%s", photoId, e.getErrorCode(), e.getErrorMessage());
            logger.error(String.format(msg));
            logger.debug(msg, e);
            return null;
        }
    }

    protected Set<String> getExtras() {
        Set<String> results = new HashSet<>();
        results.add("date_upload");
        results.add("description");
        return results;
    }
}
