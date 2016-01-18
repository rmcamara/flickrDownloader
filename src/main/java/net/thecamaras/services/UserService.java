package net.thecamaras.services;

import net.thecamaras.domain.SystemConfig;
import net.thecamaras.domain.User;
import net.thecamaras.repository.SystemRepository;
import net.thecamaras.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by rcamara on 1/14/2016.
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FlickrService flickrService;

    public List<User> getUserAutoDownload(){
        return userRepository.findByAutoDownloadTrueAndInactiveFalse();
    }

    public List<User> getUserAutoDownloadByGroup(){
        return userRepository.findByAutoDownloadGroupTrueAndInactiveFalse();
    }

    public User getUser(String flickrId){
        return userRepository.getFirstByFlickrId(flickrId);
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public User addUser(String flickrId){
        User user = flickrService.getUser(flickrId);
        return userRepository.save(user);
    }
}
