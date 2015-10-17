package uk.ac.dundee.computing.aec.instagrim.lib;

/**
 * Created by Ryan on 17/10/2015.
 */
public class Validate {
    public boolean validCred(String username, String password) {
        if (username.equals("")) {
            return false;
        }

        if (password.equals("")) {
            return false;
        }

        return true;
    }


}
