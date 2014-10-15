/**
 * Created by new_name on 15.10.2014.
 */
public class Login {

    public static boolean authenticate(String username, String password) {
        // hardcoded username and password
        if (username.equals("bob") && password.equals("secret")) {
            return true;
        }
        return false;
    }

}
