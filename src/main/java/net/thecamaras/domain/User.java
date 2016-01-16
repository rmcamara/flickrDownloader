package net.thecamaras.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by qsz922 on 2016/01/13.
 */
@Entity
public class User {

    @Id
    @GeneratedValue
    private Integer id;

    private String flickrId;

    private String username;
    private Boolean autoDownload;
    private Boolean autoDownloadGroup;
    private Boolean ignoreSizeCheck;

    public User(){
        super();
    }

    public User(com.flickr4java.flickr.people.User source){
        setUsername(source.getUsername());
        setFlickrId(source.getId());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFlickrId() {
        return flickrId;
    }

    public void setFlickrId(String flickrId) {
        this.flickrId = flickrId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getAutoDownload() {
        return autoDownload;
    }

    public void setAutoDownload(Boolean autoDownload) {
        this.autoDownload = autoDownload;
    }

    public Boolean getAutoDownloadGroup() {
        return autoDownloadGroup;
    }

    public void setAutoDownloadGroup(Boolean autoDownloadGroup) {
        this.autoDownloadGroup = autoDownloadGroup;
    }

    public Boolean getIgnoreSizeCheck() {
        return ignoreSizeCheck;
    }

    public void setIgnoreSizeCheck(Boolean ignoreSizeCheck) {
        this.ignoreSizeCheck = ignoreSizeCheck;
    }
}
