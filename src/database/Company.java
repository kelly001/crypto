package database;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by Julia on 14.11.2014.
 */
public class Company extends User {

    protected String Country;
    protected String Region;
    protected String City;
    protected String Department;
    protected ArrayList<Employer> employers;

    public Company() {
        super();
        this.employers = new ArrayList<Employer>();
    }

    public Company(Long id, String email, String password, String username, Boolean status, Timestamp time,
                   String Country, String Region, String City, String Department){
        super(id,email,password,username,status,time);

    }
}
