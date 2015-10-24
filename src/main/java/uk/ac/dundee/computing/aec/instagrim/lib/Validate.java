package uk.ac.dundee.computing.aec.instagrim.lib;

import uk.ac.dundee.computing.aec.instagrim.stores.UserDetails;

/**
 * Created by Ryan on 17/10/2015.
 */
public class Validate {


    public Validate() {
    }


    public boolean validCred(UserDetails ud) {
        return checkUsername(ud.getLogin()) &&
                checkPassword(ud.getPassword()) &&
                checkFirstname(ud.getFirstname()) &&
                checkLastname(ud.getLastname()) &&
                checkEmail(ud.getFirstEmail());
    }

    public boolean validLogin(String username, String password)
    {
        return checkUsername(username) && checkPassword(password);
    }


    private boolean checkUsername(String username) {
        /* TODO: finish username checks */
        if (username.isEmpty()) {
            return false;
        }
        return true;
    }

    private boolean checkPassword(String password) {
        /* TODO: finish password checks */

        if (password.isEmpty()) {
            return false;
        }
        return true;
    }

    private boolean checkFirstname(String firstname) {
        /* TODO: finish firstname checks */

        if (firstname.isEmpty()) {
            return false;
        }
        return true;
    }

    private boolean checkLastname(String lastname) {
        /* TODO: finish lastname checks */

        if (lastname.isEmpty()) {
            return false;
        }
        return true;
    }

    private boolean checkEmail(String email) {
        /* TODO: finish email checks */

        if (email.isEmpty()) {
            return false;
        }
        return true;
    }


}
