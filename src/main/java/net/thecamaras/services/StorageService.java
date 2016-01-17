package net.thecamaras.services;

import net.thecamaras.domain.ImportHistoryLocation;
import net.thecamaras.domain.Photo;
import net.thecamaras.domain.SystemConfig;
import net.thecamaras.domain.User;
import net.thecamaras.repository.ImportHistoryRepository;
import net.thecamaras.repository.PhotoRepository;
import net.thecamaras.repository.UserRepository;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rcamara on 1/14/2016.
 */
@Service
public class StorageService {
    private static  final int PAGE_SIZE = 100;

    private Logger logger = LoggerFactory.getLogger(StorageService.class);


    @Autowired
    private SystemService systemService;

    @Autowired
    private PhotoRepository photoRepository;

    private File downloadRoot;
    private Pattern photoLookup;

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

        photoLookup = Pattern.compile("(.*)\\((\\d*)\\).jpg");
    }

    public int findZeroFiles() {
        return findZeroFiles(downloadRoot);
    }

    @Transactional
    public int deleteFlaggedFiles(){
        int filesDeleted = 0;
        int pageNum = 0;
        PageRequest pageRequest = new PageRequest(pageNum, PAGE_SIZE);
        Page<Photo> page = photoRepository.findByFlagRemoval(true, pageRequest);
        do{
            for (Photo photo : page.getContent()){
                try{
                    File file = new File(downloadRoot, photo.getFileLocation());
                    if (!file.exists()) {
                        logger.warn("File not found " + file.getCanonicalPath());
                    }
                    if (file.length() > 10){
                        logger.error("File found to be too big: " + file.getCanonicalPath());
                        continue;
                    }
                    file.delete();
                    filesDeleted++;
                } catch (IOException e) {
                    logger.error("error removing the file " + photo.toString(), e);
                }
            }

            pageRequest = new PageRequest(++pageNum, PAGE_SIZE);
            page = photoRepository.findByFlagRemoval(true, pageRequest);
        }
        while(pageNum <= page.getTotalPages());

        photoRepository.updateFlaggedToDeleted();

        return filesDeleted;
    }


    private int findZeroFiles(File root) {
        int count = 0;
        ArrayList<Photo> photos = new ArrayList<>();
        for (File file : root.listFiles()) {
            if (file.isDirectory()) {
                count += findZeroFiles(file);
                continue;
            }
            if (file.length() > 10){
                continue;
            }
            String fileName = file.getName();
            Matcher matcher;
            matcher = photoLookup.matcher(fileName);
            String flickrId = null;
            if (matcher.matches()) {
                flickrId = matcher.group(2);
                Photo photo = photoRepository.getFirstByFlickrId(flickrId);
                if (photo != null && !photo.isDeleted() && !photo.isFlagRemoval()){
                    photo.setFlagRemoval(true);
                    photos.add(photo);
                }
            }
        }

        if (photos.size() > 0) {
            logger.info(String.format("Found new Zero files: %d in %s", photos.size(), root.getName()));
            photoRepository.save(photos);
        }

        return count + photos.size();
    }
}
