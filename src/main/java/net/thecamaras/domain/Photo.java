package net.thecamaras.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by rcamara on 1/13/2016.
 */
@Entity
public class Photo {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String ownerId;

    @Column(nullable = false)
    private String flickrId;
    private Date datePosted;
    private Date dateDownloaded;
    private String title;
    private String description;
    private String fileLocation;

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
}
