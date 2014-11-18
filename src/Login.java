/**
 * Created by new_name on 15.10.2014.
 */
import database.Company;
import database.Employer;
import database.User;

public class Login {
    protected static User user = new User();

    public static boolean authenticate(String username, String password) {
        // hardcoded username and password
        user = new User();
        try {
            user = User.loadByEmail(username);
            if (user == null) user = Employer.loadByEmail(username);
            if (user == null) Company.loadByEmail(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (user!=null &&  user.getPassword().equals(password)) {

            return true;
        }
        return false;
    }

    public Long getUser() {
        return user.getId();
    }

}
