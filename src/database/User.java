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
    private ArrayList<Certificate> certificates;
    private ArrayList<Key> keys;

    public User() {
        this.status = true;
        java.util.Date now = Calendar.getInstance().getTime();
        //java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
        this.timestamp = new Timestamp(now.getTime());
    }

    public User(Long id) {
        this.id = id;
        java.util.Date now = Calendar.getInstance().getTime();
        //java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
        this.timestamp = new Timestamp(now.getTime());
        try {
            this.certificates = Certificate.loadByUser(this.id);
            System.out.println("loaded certificates in constructor " + this.certificates.size());
        } catch (SQLException e)
        { System.out.println("Load certficates SQLException in User constructor " + e.getLocalizedMessage());}
        //this.keys = new ArrayList<Key>();
    }

    public User(Long id, String email, String password, String username, Boolean status, Timestamp time) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.status = status;
        this.timestamp = time;
        try {
            this.certificates = Certificate.loadByUser(this.id);
        } catch (SQLException e)
        { System.out.println("Load certficates SQLException in User constructor " + e.getLocalizedMessage());}
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
        System.out.println("loadUsers");
        ArrayList<User> users = new ArrayList<User>();
        Connection con = Database.getConnection();
        String query = "select * from user";
        Statement stmt = null;
        try {
            System.out.println("query exec");

            PreparedStatement preparedStatement = null;
            preparedStatement = con.prepareStatement(query);
            ResultSet result2 = preparedStatement.executeQuery();


            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String email = rs.getString("email");//.isEmpty()?rs.getString("email"):"";
                String password = rs.getString("password");//.isEmpty()?rs.getString("password"):"";
                String username = rs.getString("username");//.isEmpty()?rs.getString("username"):"";
                Boolean status = rs.getBoolean("status");
                Timestamp time = new Timestamp(rs.getLong("timestamp"));
                User user = new User(rs.getLong("id"), email, password, username, status, time);
                users.add(user);
                System.out.println(user.getUsername() + " " + user.getId() + " " + user.getTimestamp());
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

    public static User loadByName (String name) throws SQLException{
        System.out.println("loadByName");
        User user = null;
        Connection con = Database.getConnection();
        String query = "select * from user where username = ?";
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(query);
            stmt.setString(1, name);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                String password = result.getString("password");//.isEmpty()?rs.getString("password"):"";
                String username = result.getString("username");//.isEmpty()?rs.getString("username"):"";
                user = new User();
                user.setId(result.getLong("id"));
                user.setUsername(username);
                user.setPassword(password);
            }
        } catch (SQLException e ) {
            System.out.println(e.getLocalizedMessage());
        }catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        } finally {
            if (stmt != null) { stmt.close(); }
        }
        return user;
    }


    public static User loadById(Long id) throws SQLException{
        System.out.println("loadById User");
        User user = null;
        Connection con = Database.getConnection();
        String query = "select * from user where id = ?";
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(query);
            stmt.setLong(1, id);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                String email = result.getString("email");//.isEmpty()?rs.getString("email"):"";
                String password = result.getString("password");//.isEmpty()?rs.getString("password"):"";
                String username = result.getString("username");//.isEmpty()?rs.getString("username"):"";
                Boolean status = result.getBoolean("status");
                Timestamp time = new Timestamp(result.getLong("timestamp"));
                user = new User(result.getLong("id"), email, password, username, status, time);
                System.out.println(user.getUsername() + " " + user.getId() + " " + user.getTimestamp());
            }
        } catch (SQLException e ) {
            System.out.println(e.getLocalizedMessage());
        }catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        } finally {
            if (stmt != null) { stmt.close(); }
        }

        return user;
    }

    public static User loadByEmail (String name) throws SQLException{
        System.out.println("loadByEmail User");
        User user = null;
        Connection con = Database.getConnection();
        String query = "select * from user where email = ?";
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(query);
            stmt.setString(1, name);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                String password = result.getString("password");//.isEmpty()?rs.getString("password"):"";
                String username = result.getString("username");//.isEmpty()?rs.getString("username"):"";
                user = new User();
                user.setId(result.getLong("id"));
                user.setUsername(username);
                user.setPassword(password);
                user.setEmail(result.getString("email"));
            }
        } catch (SQLException e ) {
            System.out.println(e.getLocalizedMessage());
        }catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        } finally {
            if (stmt != null) { stmt.close(); }
        }
        return user;
    }



    public static Boolean saveUser (User user) throws SQLException {
        Connection con = Database.getConnection();
        Statement stmt = null;
        String query = "insert into users values ("+
                +user.getId()+","+user.getUsername()+")";
        try {
            System.out.println("query exec");
            stmt = con.createStatement();
            //ResultSet rs = stmt.executeQuery(query);
            stmt.executeUpdate(query);
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
