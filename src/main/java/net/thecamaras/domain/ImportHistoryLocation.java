package net.thecamaras.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by rcamara on 1/13/2016.
 */
@Entity
public class ImportHistoryLocation {

    @Id
    @GeneratedValue
    private Long id;

    private Date importDate;
    private String fileLocation;

    public ImportHistoryLocation() {
        super();
    }

    @PrePersist
    public void downloadedOn() {
        importDate = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }
}
