package net.thecamaras.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by rcamara on 1/16/2016.
 */
@Entity
public class UsernameHisotry {
    @Id
    @GeneratedValue
    private Integer id;

    private String username;
    private Date dateFound;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "USER_ID")
    private User user;

    @PrePersist
    public void dateFoundOn() {
        dateFound = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (!user.getPreviousNames().contains(this)) {
            user.getPreviousNames().add(this);
        }
    }
}
