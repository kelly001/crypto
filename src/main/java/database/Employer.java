package database;

import com.zpayment.Login;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

public class Employer extends User {
    private User company;

    public Employer() {
        super();
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
                employer.setSalt(result.getString("salt"));
                employer.setStatus(result.getBoolean("status"));
                employer.setTimestamp(time);
                employer.setCertificates(Certificate.loadByUser(result.getLong("id")));

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

    public static Boolean newUser (Employer user) throws SQLException {
        System.out.println("newUser User class");
        Connection con = Database.getConnection();
        PreparedStatement preparedStatement = null;
        String query = "insert into USER(id, type, email, password, salt, timestamp," +
                "status, username, company_id) values(?,?,?,?,?,?,?,?,?)";
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setNull(1,0);
            preparedStatement.setInt(2,2);
            preparedStatement.setString(3,user.getEmail());
            preparedStatement.setString(4, Login.getSecurePassword(user.getPassword(), user.getSalt()));
            preparedStatement.setString(5, user.getSalt());
            preparedStatement.setLong(6, Calendar.getInstance().getTime().getTime());
            preparedStatement.setInt(7,1);
            preparedStatement.setString(8, user.getUsername());
            if (user.getCompany()!= null) preparedStatement.setLong(9, user.getCompany().getId());
                else preparedStatement.setNull(9,0);
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
