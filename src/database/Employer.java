package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

public class Employer extends User {
    private User company;

    public Employer() {
        super();
        /*this.company = new Company();
        try {
            this.company = Company.loadByUser(this.id);
        } catch (SQLException e)
        { System.out.println(e.getLocalizedMessage());}*/
    }

    public Employer(Long id) {
        super(id);
    }

    public void setCompany(User company) {this.company=company;}
    public User getCompany() {return company;}

    public static ArrayList<Employer> loadByCompany (Long company_id) throws SQLException{
        ArrayList<Employer> users = new ArrayList<Employer>();
        Connection con = Database.getConnection();
        String query = "select * from user where company_id = ?";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setLong(1,company_id);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                Employer employer = new Employer(result.getLong("id"));
                Timestamp time = new Timestamp(result.getLong("timestamp"));
                employer.setId(result.getLong("id"));
                employer.setUsername(result.getString("username"));
                employer.setPassword(result.getString("password"));
                employer.setStatus(result.getBoolean("status"));
                employer.setTimestamp(time);
                employer.setCertificates(Certificate.loadByUser(result.getLong("id")));
                //employer.setCompany(Company.loadById(company_id));
                users.add(employer);
            }
        } catch (SQLException e ) {
            System.out.println(e.getLocalizedMessage());
        }catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        } finally {
            if (preparedStatement != null) { preparedStatement.close(); }
        }
        return users;
    }

    public static User loadByEmail (String name) throws SQLException{
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

    public static User loadById(Long id) throws SQLException{
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

    public static Boolean newUser (Employer user) throws SQLException {
        System.out.println("newUser User class");
        Connection con = Database.getConnection();
        PreparedStatement preparedStatement = null;
        String query = "insert into USER(id, type, email, password, timestamp," +
                "status, username, company_id) values(?,?,?,?,?,?,?,?)";
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setNull(1,0);
            preparedStatement.setInt(2,2);
            preparedStatement.setString(3,user.getEmail());
            preparedStatement.setString(4, Database.getSecurePassword(user.getPassword()));
            preparedStatement.setLong(5, Calendar.getInstance().getTime().getTime());
            preparedStatement.setInt(6,1);
            preparedStatement.setString(7, user.getUsername());
            if (user.getCompany()!= null) preparedStatement.setLong(8, user.getCompany().getId());
                else preparedStatement.setNull(8,0);
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
