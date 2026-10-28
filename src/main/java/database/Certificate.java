package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Certificate {
    private Long id;
    private String email;
    private String username;
    private String filename;
    private String organization;
    private String department;
    private String locality;
    private String state;

    private String type;

    private String comment;
    private Timestamp timestamp;
    private Boolean status;
    private User owner;
    private String country;

    public Certificate () {
        this.type = "";
        this.comment = "";
        this.status = true;
        java.util.Date now = Calendar.getInstance().getTime();
        this.timestamp = new java.sql.Timestamp(now.getTime());
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
    public void setOrganization(String organization_name) { this.organization = organization_name; }
    public void setState(String state) {this.state = state;}
    public void setLocality(String locality) {this.locality = locality;}
    public void setType(String type) {this.type = type;}
    public  void setComment(String comment) {this.comment = comment;}
    public void setCountry(String country) { this.country = country;}
    public void setOwner(User user) {this.owner = user;}
    public void setOwner(Long user_id) {
        User user = new User();
        try {
            user = User.loadById(user_id);
        }catch (Exception exception) {
            exception.printStackTrace();
        }

        this.owner = user;}

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
    public String getOrganization() {return  this.organization; }
    public String getState() {return this.state;}
    public String getLocality() {return this.locality;}
    public String getType() {return  this.type; }
    public String getComment() {return this.comment; }
    public User getOwner() {return this.owner;}
    public String getCountry() {return this.country;}

    public String getInfo() {
        String label = "Сертификат " + this.getUsername() + " от " + this.getTimestamp() + " числа";
        return label;
    }

    public static ArrayList<Certificate> loadByUser(Long user_id){
        System.out.println("loadByUser Certificate");
        ArrayList<Certificate> certificates = new ArrayList<Certificate>();
        try {
            Connection con = Database.getConnection();
            PreparedStatement preparedStatement = null;

            String query = "select * from certificate where user_id = ? and status = ?";
            try {
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setLong(1, user_id);
                preparedStatement.setBoolean(2, true);
                ResultSet rs = preparedStatement.executeQuery();

                while (rs.next()) {
                        Timestamp time = new Timestamp(rs.getLong("timestamp"));
                        Certificate cert = new Certificate();
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

                        certificates.add(cert);
                }
            } catch (SQLException e ) {
                System.out.println("loadByUser Certificate SQLException: " + e.getLocalizedMessage());
            }catch (Exception e) {
                System.out.println("loadByUser Certificate Exception: " + e.getLocalizedMessage());
            } finally {
                if (preparedStatement != null) { preparedStatement.close(); }
            }
        }catch (SQLException sqlexc) {
            System.out.println("SQLExCeption load certificate class " + sqlexc.getLocalizedMessage());
        }
        return certificates;
    }

    public static Boolean newUser (HashMap<String,String> values) throws SQLException {
        System.out.println("new Certificate Certificate class");
        Connection con = Database.getConnection();
        PreparedStatement preparedStatement = null;
        String query = "insert into CERTIFICATE(id, email, username, filename, organization, department," +
                " locality, state, type, comment, timestamp, status, user_id, country) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setNull(1,0);
            preparedStatement.setString(2,values.get("email"));
            preparedStatement.setString(3,values.get("username"));
            preparedStatement.setString(4,values.get("filename"));
            preparedStatement.setString(5,values.get("organization"));
            preparedStatement.setString(6,values.get("department"));
            preparedStatement.setString(7,values.get("locality"));
            preparedStatement.setString(8,values.get("state"));
            preparedStatement.setString(9,values.get("type"));
            preparedStatement.setString(10,values.get("comment"));
            preparedStatement.setLong(11, Calendar.getInstance().getTime().getTime());
            preparedStatement.setInt(12,1);
            preparedStatement.setInt(13,Integer.valueOf(values.get("user_id")));
            preparedStatement.setString(14,values.get("country"));

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

    public static boolean cancel(Long cert_id) throws SQLException {
        Connection con = Database.getConnection();
        PreparedStatement preparedStatement = null;

        String query = "UPDATE certificate SET status = ? where id = ?";
        try {
            System.out.println("query exec");
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, 0);
            preparedStatement.setLong(2, cert_id);
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }
}
