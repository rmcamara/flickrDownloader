package net.thecamaras.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by rcamara on 1/14/2016.
 */
@Entity
public class SystemConfig {

    public static final String TOKEN = "TOKEN";
    public static final String TOKEN_SECRET = "TOKEN_SECRET";
    public static final String DOWNLOAD_ROOT = "DOWNLOAD_ROOT";
    public static final String MAX_DOWNLOAD = "MAX_DOWNLOAD";
    public static final String AUTO_MAX_DAY = "AUTO_MAX_DAY";

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
