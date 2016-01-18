package net.thecamaras.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by rcamara on 1/13/2016.
 */
@Entity
@Table(indexes = {@Index(name = "idx_owner", columnList = "ownerId")})
public class Photo {

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
    private boolean deleted;
    private String secret;

    private boolean flagRemoval;

    public Photo() {
        super();
    }

    public Photo(com.flickr4java.flickr.photos.Photo source) {
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
        this.description = description;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public boolean isFlagRemoval() {
        return flagRemoval;
    }

    public void setFlagRemoval(boolean flagRemoval) {
        this.flagRemoval = flagRemoval;
    }
}
