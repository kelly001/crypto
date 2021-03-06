package database;

import com.zpayment.Login;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

public class Company extends User {

    protected String country;
    protected String region;
    protected String city;
    protected String department;
    protected ArrayList<User> employers;

    public Company() {
        super();
        this.employers = new ArrayList<User>();
    }

    public Company(Long id, String email, String password, String salt, String username, Boolean status, Timestamp time,
                   String Country, String Region, String City, String Department){
        super(id,email,password, salt,username,status,time);

    }

    //setters
    public void setCountry(String country) { this.country = country;}
    public void setRegion(String region) {this.region = region; }
    public void setCity(String city) {this.city = city;}
    public void setDepartment(String department) {this.department = department;}
    public void setEmployers(ArrayList<User> employers) {this.employers = employers;}
    public void addEmployers (Employer employer) {this.employers.add(employer);}

    //getters
    public String getCountry() { return country;}
    public String getRegion() {return region;}
    public String getCity() {return city;}
    public String getDepartment() {return department;}
    public ArrayList<User> getEmployers() {return employers;}
    public User getEmployerById(Long id) {
        User employer = new User();
        for (User emp: employers) {
            if (emp.getId().equals(id))
                employer = emp;
        }
        return employer;}

    public static Company loadByEmail (String name) throws SQLException {
        System.out.println("loadByEmail Company");
        Company user = null;
        Connection con = Database.getConnection();
        String query = "select * from user where email = ?";
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(query);
            stmt.setString(1, name);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                if (result.getInt("type") == 1) {
                    String password = result.getString("password");//.isEmpty()?rs.getString("password"):"";
                    String username = result.getString("username");//.isEmpty()?rs.getString("username"):"";
                    user = new Company();
                    user.setId(result.getLong("id"));
                    user.setUsername(username);
                    user.setPassword(password);
                    user.setEmail(result.getString("email"));
                    user.setStatus(result.getBoolean("status"));
                    user.setDepartment(result.getString("department"));
                    user.setCity(result.getString("city"));
                    user.setRegion(result.getString("region"));
                    user.setCountry(result.getString("country"));
                    user.setSalt(result.getString("salt"));
                }
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
        System.out.println("loadByName Company");
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

    public static Boolean newUser (Company user) throws SQLException {
        System.out.println("newUser User class");
        Connection con = Database.getConnection();
        PreparedStatement preparedStatement = null;
        String query = "insert into USER(id, type, email, password, salt, timestamp," +
                "status, username, country, region, city) values(?,?,?,?,?,?,?,?,?,?,?)";
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setNull(1,0);
            preparedStatement.setInt(2,1);
            preparedStatement.setString(3,user.getEmail());
            preparedStatement.setString(4, Login.getSecurePassword(user.getPassword(), user.getSalt()));
            preparedStatement.setString(5,user.getSalt());
            preparedStatement.setLong(6, Calendar.getInstance().getTime().getTime());
            preparedStatement.setInt(7,1);
            preparedStatement.setString(8, user.getUsername());
            preparedStatement.setString(9, user.getCountry());
            preparedStatement.setString(10, user.getRegion());
            preparedStatement.setString(11, user.getCity());
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

    public static Boolean updateUser (Company user) throws SQLException {
        System.out.println("updateUser User class");
        Connection con = Database.getConnection();
        PreparedStatement preparedStatement = null;
        String query = "UPDATE user SET email=?, timestamp=?," +
                "status=?, username=?, country=?, region=?, city=?, department=? where id = ?";
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1,user.getEmail());

            preparedStatement.setLong(2, Calendar.getInstance().getTime().getTime());
            preparedStatement.setInt(3,1);
            preparedStatement.setString(4, user.getUsername());
            preparedStatement.setString(5, user.getCountry());
            preparedStatement.setString(6, user.getRegion());
            preparedStatement.setString(7, user.getCity());
            preparedStatement.setString(8, user.getDepartment());
            preparedStatement.setLong(9,user.getId());
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
