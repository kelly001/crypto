/**
 * Created by new_name on 15.10.2014.
 */

import database.Company;
import database.Employer;
import database.User;

public class Login {
    protected static User user;

    public static boolean authenticate(String username, String password) {
        // hardcoded username and password
        try {
            user = User.loadByEmail(username);
            System.out.println(User.getSecurePassword(password));
            System.out.println(user.getPassword());
            if (user != null &&  user.getPassword().equals(User.getSecurePassword(password))) {
                return true;
            }/* else {
                user = Employer.loadByEmail(username);
                if (user != null && user.getPassword().equals(password)) {
                    return true;
                } else {
                    user = Company.loadByEmail(username);
                    if (user != null &&  user.getPassword().equals(password)) return true;
                }
            }*/
        } catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public Long getUser() {
        return user.getId();
    }



}
