package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by new_name on 11.11.2014.
 */
public class Certificate {
    private Long id;
    private String username;
    private String email;
    private String filename;
    private String department;
    private String city;
    private String type;
    private String comment;
    private Timestamp timestamp;
    private Boolean status;
    private Long user_id;

    public Certificate () {
        /* root cert
        [v3_ca]
        basicConstraints        = CA:true
        nsComment       = "CA certificate of COMPANY_NAME"
        nsCertType      = sslCA, emailCA
        subjectKeyIdentifier=hash
        authorityKeyIdentifier=keyid:always,issuer:always
         */

        /* user cert
        [ v3_req ]
        basicConstraints = CA:FALSE
        keyUsage = "digitalSignature, keyEncipherment"
        nsCertType = "client, email, objsign"
         */
        this.type = "";
        this.comment = "";
        this.status = true;
        java.util.Date now = Calendar.getInstance().getTime();
        this.timestamp = new java.sql.Timestamp(now.getTime());
    }

    public Certificate (Long id, String email, String username, String filename, String comment,
                        String department, String city, String type, Boolean status, Long time) {

        this.id = id;
        this.email = email;
        this.username = username;
        this.filename = filename;
        this.comment = comment;
        this.department = department;
        this.city = city;
        this.type = type;
        this.status = status;
        this.timestamp = new java.sql.Timestamp(time);
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setStatus(Boolean status) {
        this.status = status;
    }
    public void setTimestamp(Timestamp time) {
        this.timestamp = time;
    }
    public void setDepartment(String department) {this.department = department; }
    public void setFilename(String filename) { this.filename = filename; }
    public void setCity(String city) {this.city = city;}
    public void setType(String type) {this.type = type;}
    public  void setComment(String comment) {this.comment = comment;}

    public Long getId() {return this.id; }
    public String getEmail() {
        return this.email;
    }
    public String getUsername() {
        return this.username;
    }
    public Timestamp getTimestamp() {
        return this.timestamp;
    }
    public Boolean getStatus() {
        return this.status;
    }
    public String getDepartment() { return this.department; }
    public String getFilename() { return this.filename; }
    public String getCity() {return this.city;}
    public String getType() {return  this.type; }
    public String getComment() {return this.comment; }

    public static ArrayList<Certificate> load() throws SQLException {
        System.out.println("load cert class");
        ArrayList<Certificate> certificates = new ArrayList<Certificate>();

        Connection con = Database.getConnection();
        Statement stmt = null;
        String query = "select * from certificates";
        try {
            System.out.println("query exec");
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                //Timestamp time = new Timestamp(rs.getLong("timestamp"));
                Certificate cert = new Certificate(
                        rs.getLong("id"), rs.getString("email"), rs.getString("username"), rs.getString("filename"),
                        rs.getString("comment"), rs.getString("department"),
                        rs.getString("city"), rs.getString("type"), rs.getBoolean("status"), rs.getLong("timestamp"));
                certificates.add(cert);
            }
        } catch (SQLException e ) {
            System.out.println(e.getLocalizedMessage());
        }catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        } finally {
            if (stmt != null) { stmt.close(); }
        }
        return certificates;
    }

    public static ArrayList<Certificate> load(Long user_id) throws SQLException {
        System.out.println("load cert class");
        ArrayList<Certificate> certificates = new ArrayList<Certificate>();

        Connection con = Database.getConnection();
        Statement stmt = null;
        String query = "select * from certificates";
        try {
            System.out.println("query exec");
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                //Timestamp time = new Timestamp(rs.getLong("timestamp"));
                Long id = rs.getLong("id");
                if (id.equals(user_id)) {
                    Certificate cert = new Certificate(
                            id, rs.getString("email"), rs.getString("username"), rs.getString("filename"),
                            rs.getString("comment"), rs.getString("department"),
                            rs.getString("city"), rs.getString("type"), rs.getBoolean("status"), rs.getLong("timestamp"));
                    certificates.add(cert);
                }

            }
        } catch (SQLException e ) {
            System.out.println(e.getLocalizedMessage());
        }catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        } finally {
            if (stmt != null) { stmt.close(); }
        }
        return certificates;
    }
}
