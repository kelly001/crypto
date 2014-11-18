package database;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Julia on 14.11.2014.
 */
public class Company extends User {

    protected String country;
    protected String region;
    protected String city;
    protected String department;
    protected ArrayList<Employer> employers;

    public Company() {
        super();
        this.employers = new ArrayList<Employer>();
    }

    public Company(Long id, String email, String password, String username, Boolean status, Timestamp time,
                   String Country, String Region, String City, String Department){
        super(id,email,password,username,status,time);

    }

    //setters
    public void setCountry(String country) { this.country = country;}
    public void setRegion(String region) {this.region = region; }
    public void setCity(String city) {this.city = city;}
    public void setDepartment(String department) {this.department = department;}
    public void setEmployers(ArrayList<Employer> employers) {this.employers = employers;}
    public void addEmployers (Employer employer) {this.employers.add(employer);}

    //getters
    public String getCountry() { return country;}
    public String getRegion() {return region;}
    public String getCity() {return city;}
    public String getDepartment() {return department;}
    public ArrayList<Employer> getEmployers() {return employers;}
    public Employer getEmployerById(Long id) {
        Employer employer = new Employer();
        for (Employer emp: employers) {
            if (emp.getId().equals(id))
                employer = emp;
        }
        return employer;}

    public static User loadByEmail (String name) throws SQLException {
        User user = new User();
        Connection con = Database.getConnection();
        String query = "select * from company where email = ?";
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

    public static User loadByName (String name) throws SQLException{
        User user = new User();

        Connection con = Database.getConnection();
        String query = "select * from company where username = ?";
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


}
