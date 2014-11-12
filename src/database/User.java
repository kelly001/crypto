package database;

import javax.jws.soap.SOAPBinding;
import java.sql.Timestamp;
import java.security.cert.CertificateEncodingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

public class User {
    private Long id;
    private String email;
    private String password;
    private String username;
    private Timestamp timestamp;
    private Boolean status;
    private ArrayList<Certificate> certificates = new ArrayList<Certificate>();
    private ArrayList<Key> keys = new ArrayList<Key>();
    private String dbName = "test";

    public User() {
        this.status = true;
        java.util.Date now = Calendar.getInstance().getTime();
        //java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
        this.timestamp = new java.sql.Timestamp(now.getTime());
        this.certificates = new ArrayList<Certificate>();
        this.keys = new ArrayList<Key>();
    }
    public User(Long id, String email, String password, String username, Boolean status, Timestamp time) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.status = status;
        this.timestamp = time;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
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
    public void setCertificates(ArrayList<Certificate> certificates){
        this.certificates = certificates;
    }
    public void addCertificates(Certificate certificate){
        this.certificates.add(certificate);
    }
    public void setKeys(ArrayList<Key> keys){
        this.keys = keys;
    }
    public void setKeys(Key keys){
        this.keys.add(keys);
    }

    public Long getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getUsername() {
        return username;
    }
    public Timestamp getTimestamp() {
        return timestamp;
    }
    public Boolean getStatus() {
        return status;
    }
    public ArrayList<Certificate> getCertificates() {
        return certificates;
    }
    public ArrayList<Key> keys() {
        return keys;
    }

    public static ArrayList<User> loadUsers()
            throws SQLException {
        System.out.println("load users class");
        ArrayList<User> users = new ArrayList<User>();
        Connection con = Database.getConnection();
        Statement stmt = null;
        String query = "select * from users";
        try {
            System.out.println("query exec");
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String email = rs.getString("email").isEmpty()?rs.getString("email"):"";
                String password = rs.getString("password").isEmpty()?rs.getString("password"):"";
                String username = rs.getString("username").isEmpty()?rs.getString("username"):"";
                Boolean status = rs.getBoolean("status");
                Timestamp time = rs.getTimestamp("timestamp");
                User user = new User(rs.getLong("id"), email, password, username, status, time);
                users.add(user);
                System.out.println(user.getUsername() + "\t" + user.getId());
            }
        } catch (SQLException e ) {
            System.out.println(e.getLocalizedMessage());
        }catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        } finally {
            if (stmt != null) { stmt.close(); }
        }
        return users;
    }

    public static Boolean saveUser (User user) throws SQLException {
        Connection con = Database.getConnection();
        Statement stmt = null;
        String query = "insert into users values ("+
                +user.getId()+","+user.getUsername()+")";
        try {
            System.out.println("query exec");
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
        } catch (SQLException e ) {
            System.out.println(e.getLocalizedMessage());
        }catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        } finally {
            if (stmt != null) { stmt.close(); }
        }
        return true;
    }


}
