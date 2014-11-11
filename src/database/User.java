package database;

import java.security.Timestamp;
import java.security.cert.CertificateEncodingException;
import java.sql.*;
import java.util.ArrayList;

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

    public void loadUsers(Connection con, String dbName)
            throws SQLException {

        //Connection conn = Database.getConnection(); TODO
        Statement stmt = null;
        String query = "select * " +
                "from " + dbName;
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String coffeeName = rs.getString("COF_NAME");
                int supplierID = rs.getInt("SUP_ID");
                float price = rs.getFloat("PRICE");
                int sales = rs.getInt("SALES");
                int total = rs.getInt("TOTAL");
                System.out.println(coffeeName + "\t" + supplierID +
                        "\t" + price + "\t" + sales +
                        "\t" + total);
            }
        } catch (SQLException e ) {
            e.getLocalizedMessage();
        } finally {
            if (stmt != null) { stmt.close(); }
        }
    }

}
