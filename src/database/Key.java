package database;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by new_name on 11.11.2014.
 */
public class Key {
    private Long id;
    private String filename;
    private Integer type;
    private Timestamp timestamp;
    private Boolean status;

    public Key() {
        this.status = true;
        java.util.Date now = Calendar.getInstance().getTime();
        this.timestamp = new java.sql.Timestamp(now.getTime());
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setStatus(Boolean status) {
        this.status = status;
    }
    public void setTimestamp(Timestamp time) {
        this.timestamp = time;
    }
    public void setFilename(String filename) {this.filename = filename;}
    public void setType(Integer type) {this.type = type;}

    public Long getId() {
        return id;
    }
    public Timestamp getTimestamp() {
        return timestamp;
    }
    public Boolean getStatus() {
        return status;
    }
    public String getFilename() {return filename;}
    public Integer getType() {return type;}
}
