package net.thecamaras;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import net.thecamaras.domain.User;
import net.thecamaras.repository.UserRepository;

@RestController
@SpringBootApplication
public class FlickrDownloader {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/")
    String home() {
        User me = new User();
        me.setName("Ross");
        userRepository.save(me);
        return "Hello World!" + userRepository.getUsercount();
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(FlickrDownloader.class, args);
    }

}