package net.thecamaras.services;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.people.PeopleInterface;
import com.flickr4java.flickr.photos.PhotosInterface;
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

    public Photo getPhoto(String photoId) {
        PhotosInterface photosInterface = flickr.getPhotosInterface();
        try {
            com.flickr4java.flickr.photos.Photo photo = photosInterface.getPhoto(photoId);
            Photo result = new Photo(photo);
            return result;
        } catch (FlickrException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUserFromPhotoId(String photoId){
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
}
