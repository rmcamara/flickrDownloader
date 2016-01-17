package net.thecamaras.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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
    private boolean autoDownload;
    private boolean autoDownloadGroup;
    private boolean ignoreSizeCheck;
    private boolean inactive;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UsernameHisotry> previousNames;

    public User() {
        super();
        previousNames = new HashSet<>();
    }

    public User(com.flickr4java.flickr.people.User source) {
        this();
        setUsername(source.getUsername());
        setFlickrId(source.getId());
        addUsername(source.getUsername());
    }

    public void addUsername(String username) {
        UsernameHisotry record = new UsernameHisotry();
        record.setUsername(username);
        previousNames.add(record);
        if (record.getUser() != this) {
            record.setUser(this);
        }
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

    public boolean isAutoDownload() {
        return autoDownload;
    }

    public void setAutoDownload(boolean autoDownload) {
        this.autoDownload = autoDownload;
    }

    public boolean isAutoDownloadGroup() {
        return autoDownloadGroup;
    }

    public void setAutoDownloadGroup(boolean autoDownloadGroup) {
        this.autoDownloadGroup = autoDownloadGroup;
    }

    public boolean isIgnoreSizeCheck() {
        return ignoreSizeCheck;
    }

    public void setIgnoreSizeCheck(boolean ignoreSizeCheck) {
        this.ignoreSizeCheck = ignoreSizeCheck;
    }

    public boolean isInactive() {
        return inactive;
    }

    public void setInactive(boolean inactive) {
        this.inactive = inactive;
    }

    public Set<UsernameHisotry> getPreviousNames() {
        return previousNames;
    }

    public void setPreviousNames(Set<UsernameHisotry> previousNames) {
        this.previousNames = previousNames;
    }
}
