/**
 * Created by new_name on 15.10.2014.
 */
import database.User;

public class Login {
    protected static User user = new User();

    public static boolean authenticate(String username, String password) {
        // hardcoded username and password
        try {
            user = User.loadByName(username);
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
