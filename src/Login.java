import database.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Login {
    protected static User user;

    public static boolean authenticate(String username, String password) {
        // hardcoded username and password
        try {
            user = User.loadByEmail(username);
            if (user != null &&  user.getPassword().equals(Login.getSecurePassword(password))) {
                return true;
            }
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

    //Secure password
    public static String getSecurePassword(String passwordToHash)
    {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(getSalt().getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest(passwordToHash.getBytes());
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    //Add salt
    public static String getSalt()
    {
        return "securesalt123!!!!!!";
    }

}
