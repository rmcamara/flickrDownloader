package net.thecamaras;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

@RestController
@SpringBootApplication
public class DownloadServer {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(DownloadServer.class, args);
    }

}