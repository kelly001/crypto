package database;

import com.zpayment.Login;

import java.sql.Timestamp;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

public class User {
    private Long id;
    private String email;
    private String password;
    private String salt;
    private String username;
    private Timestamp timestamp;
    private Boolean status;
    private ArrayList<Certificate> certificates;

    public User() {
        this.email = "";
        this.password = "";
        this.username = "";
        this.status = true;
        java.util.Date now = Calendar.getInstance().getTime();
        this.timestamp = new Timestamp(now.getTime());
        this.salt = Login.getRandomSalt();
    }

    public User(Long id) {
        this.id = id;
        this.salt = Login.getRandomSalt();
        java.util.Date now = Calendar.getInstance().getTime();

        this.timestamp = new Timestamp(now.getTime());
            this.certificates = Certificate.loadByUser(this.id);
            System.out.println("loaded certificates in constructor " + this.certificates.size());
    }

    public User(Long id, String email, String password, String salt, String username, Boolean status, Timestamp time) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.username = username;
        this.status = status;
        this.timestamp = time;
            this.certificates = Certificate.loadByUser(this.id);
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

    public void setSalt(String salt) { this.salt = salt;}

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

    public String getSalt() {return salt;}

    public Certificate getValidCertificate() {
        Certificate cert = null;
        try {
            Connection con = Database.getConnection();
            PreparedStatement preparedStatement = null;

            String query = "select * from certificate where user_id = ? and status = ?";
            try {
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setLong(1, this.id);
                preparedStatement.setBoolean(2,true);
                ResultSet rs = preparedStatement.executeQuery();

                while (rs.next()) {
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

            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String email = rs.getString("email");
                String password = rs.getString("password");
                String username = rs.getString("username");
                Boolean status = rs.getBoolean("status");
                Timestamp time = new Timestamp(rs.getLong("timestamp"));
                User user = new User(rs.getLong("id"), email, password, rs.getString("salt"), username, status, time);
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
                String password = result.getString("password");
                String username = result.getString("username");
                user = new User();
                user.setId(result.getLong("id"));
                user.setUsername(username);
                user.setPassword(password);
                user.setSalt(result.getString("salt"));
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
                String email = result.getString("email");
                String password = result.getString("password");
                String username = result.getString("username");
                Boolean status = result.getBoolean("status");
                Timestamp time = new Timestamp(result.getLong("timestamp"));
                user = new User(result.getLong("id"), email, password, result.getString("salt"), username, status, time);
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
                String password = result.getString("password");
                String username = result.getString("username");
                user = new User();
                user.setId(result.getLong("id"));
                user.setUsername(username);
                user.setPassword(password);
                user.setEmail(result.getString("email"));
                user.setSalt(result.getString("salt"));
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

    public static Long newUser (User user) {
        System.out.println("newUser User class, returns id");
        Long risultat = null;
        try {
            Connection con = Database.getConnection();
            PreparedStatement preparedStatement = null;
            String query = "insert into USER(id, type, email, password, salt, timestamp," +
                    "status, username) values(?,?,?,?,?,?,?,?)";
            try {
                preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setNull(1, 0);
                preparedStatement.setInt(2, 2);
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.setString(4, Login.getSecurePassword(user.getPassword(), user.getSalt()));
                preparedStatement.setString(5, user.getSalt());
                preparedStatement.setLong(6, Calendar.getInstance().getTime().getTime());
                preparedStatement.setInt(7, 1);
                preparedStatement.setString(8, user.getUsername());
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

    public static Boolean delete( Long user_id) {
        try {
            Connection con = Database.getConnection();
            PreparedStatement preparedStatement = null;

            String query = "UPDATE user SET status = ? where id = ?";
            try {
                System.out.println("");
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, 0);
                preparedStatement.setLong(2,user_id);
                preparedStatement.execute();
                return true;
            } catch (SQLException e) {
                System.out.println(e.getLocalizedMessage());
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            } finally {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            }
        } catch (SQLException sqlexc) {
            System.out.println("SQL error / set user status false : " + sqlexc.getLocalizedMessage());
        }
        return false;
    }
}
