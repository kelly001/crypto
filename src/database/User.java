package database;

import javax.jws.soap.SOAPBinding;
import java.sql.Timestamp;
import java.security.cert.CertificateEncodingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

public class User {
    private Long id;
    private Integer type;
    private String email;
    private String password;
    private String username;
    private Timestamp timestamp;
    private Boolean status;
    private ArrayList<Certificate> certificates;
    private ArrayList<Key> keys;

    public User() {
        this.type=1;
        this.status = true;
        java.util.Date now = Calendar.getInstance().getTime();
        this.timestamp = new Timestamp(now.getTime());
    }

    public User(Long id) {
        this.id = id;
        java.util.Date now = Calendar.getInstance().getTime();
        //java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
        this.timestamp = new Timestamp(now.getTime());
            this.certificates = Certificate.loadByUser(this.id);
            System.out.println("loaded certificates in constructor " + this.certificates.size());
        //this.keys = new ArrayList<Key>();
    }

    public User(Long id, String email, String password, String username, Boolean status, Timestamp time) {
        this.id = id;
        this.type = 1;
        this.email = email;
        this.password = password;
        this.username = username;
        this.status = status;
        this.timestamp = time;
            this.certificates = Certificate.loadByUser(this.id);
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setType(Integer type) {
        this.type = type;
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
    public Integer getType() {return type; }
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

    public Certificate getValidCertificate() {
        Certificate cert = null;
        try {
            Connection con = Database.getConnection();
            PreparedStatement preparedStatement = null;
            // select by user_id
            String query = "select * from certificate where user_id = ? and status = ?";
            try {
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setLong(1, this.id);
                preparedStatement.setBoolean(2,true);
                ResultSet rs = preparedStatement.executeQuery();

                while (rs.next()) {
                    //Timestamp time = new Timestamp(rs.getLong("timestamp"));
                    Timestamp time = new Timestamp(rs.getLong("timestamp"));
                    cert = new Certificate();
                    cert.setId(rs.getLong("id"));
                    cert.setEmail(rs.getString("email"));
                    cert.setUsername(rs.getString("username"));
                    cert.setFilename(rs.getString("filename"));
                    cert.setDepartment(rs.getString("department"));
                    cert.setComment(rs.getString("comment"));
                    cert.setLocality(rs.getString("locality"));
                    cert.setState(rs.getString("state"));
                    cert.setOrganization(rs.getString("organization"));
                    cert.setStatus(rs.getBoolean("status"));
                    cert.setTimestamp(time);
                    cert.setOwner(rs.getLong("user_id"));
                }
            } catch (SQLException e ) {
                System.out.println("getValidCertificate SQLException: " + e.getLocalizedMessage());
            }catch (Exception e) {
                System.out.println("getValidCertificate Exception: " + e.getLocalizedMessage());
            } finally {
                if (preparedStatement != null) { preparedStatement.close(); }
            }
        }catch (SQLException sqlexc) {
            System.out.println("SQLExCeption load certificate class " + sqlexc.getLocalizedMessage());
        }
        return cert;
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
        System.out.println("saveUser User class");
        Connection con = Database.getConnection();
        PreparedStatement preparedStatement = null;
        String query = "UPDATE user SET email =?, timestamp=?, username=?, password=? WHERE id=?";
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setLong(5,user.getId());
            preparedStatement.setString(1,user.getEmail());
            //preparedStatement.setString(4, user.getPassword());
            preparedStatement.setLong(2, Calendar.getInstance().getTime().getTime());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.execute();
        } catch (SQLException e ) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }
        return true;
    }

    public static Long newUserId (User user) {
        System.out.println("newUser User class return long");
        Long risultat = null;
        try {
            Connection con = Database.getConnection();
            PreparedStatement preparedStatement = null;
            String query = "insert into USER(id, type, email, password, timestamp," +
                    "status, username) values(?,?,?,?,?,?,?)";
            try {
                preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setNull(1, 0);
                preparedStatement.setInt(2, 2);
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.setString(4, user.getPassword());
                preparedStatement.setLong(5, Calendar.getInstance().getTime().getTime());
                preparedStatement.setInt(6, 1);
                preparedStatement.setString(7, user.getUsername());
                preparedStatement.execute();

                ResultSet rs = preparedStatement.getGeneratedKeys();
                if (rs.next()){
                    risultat=rs.getLong(1);
                }
            } catch (SQLException e) {
                System.out.println(e.getLocalizedMessage());
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            } finally {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            }
        }catch (SQLException sqlexc) {
            System.out.println("SQLExCeption newUser User class " + sqlexc.getLocalizedMessage());
        }
        return risultat;
    }

    public static Boolean newUser (User user) throws SQLException {
        System.out.println("newUser User class");
        Connection con = Database.getConnection();
        PreparedStatement preparedStatement = null;
        String query = "insert into USER(id, type, email, password, timestamp," +
                "status, username) values(?,?,?,?,?,?,?)";
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setNull(1,0);
            preparedStatement.setInt(2,2);
            preparedStatement.setString(3,user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setLong(5, Calendar.getInstance().getTime().getTime());
            preparedStatement.setInt(6,1);
            preparedStatement.setString(7, user.getUsername());
            preparedStatement.execute();
        } catch (SQLException e ) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }
        return true;
    }


}
