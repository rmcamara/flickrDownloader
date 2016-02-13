package net.thecamaras.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by rcamara on 1/13/2016.
 */
@Entity
@Table(indexes = {@Index(name = "idx_owner", columnList = "ownerId")})
public class Video {

    @Id
    @GeneratedValue
    private Long id;

    private String ownerId;

    @Column(nullable = false, unique = true)
    private String flickrId;
    private Date datePosted;
    private Date dateDownloaded;

    @Column(length = 1024)
    private String title;

    @Column(length = 2024)
    private String description;

    @Column(length = 1024)
    private String fileLocation;

    private String secret;

    public Video() {
        super();
    }

    public Video(com.flickr4java.flickr.photos.Photo source) {
        setFlickrId(source.getId());
        if (source.getOwner() != null) {
            setOwnerId(source.getOwner().getId());
        }
        setTitle(source.getTitle());
        setDescription(source.getDescription());
        setDatePosted(source.getDatePosted());
        setSecret(source.getSecret());
    }

    @PrePersist
    public void downloadedOn() {
        dateDownloaded = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getFlickrId() {
        return flickrId;
    }

    public void setFlickrId(String flickrId) {
        this.flickrId = flickrId;
    }

    public Date getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    public Date getDateDownloaded() {
        return dateDownloaded;
    }

    public void setDateDownloaded(Date dateDownloaded) {
        this.dateDownloaded = dateDownloaded;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description != null && description.length() > 2024){
            description = description.substring(0, 2023);
        }
        this.description = description;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
