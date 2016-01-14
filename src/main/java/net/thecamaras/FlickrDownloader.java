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
    public static void main(String[] args) throws Exception {
        SpringApplication.run(FlickrDownloader.class, args);
    }

}