package uk.ac.dundee.computing.aec.instagrim.stores;

import javax.servlet.annotation.WebServlet;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Ryan on 23/10/2015.
 *
 * Data class to hold the details for a user.
 */
public class UserDetails {

    private String login;
    private String firstname;
    private String lastname;
    private String email;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserDetails() { }


}
