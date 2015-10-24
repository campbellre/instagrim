package uk.ac.dundee.computing.aec.instagrim.stores;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Ryan on 23/10/2015.
 *
 * Data class to hold the details for a user.
 */
public class UserDetails {

    private String login;
    private String firstname;
    private String lastname;
    private TreeSet<String> email;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;


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

    public TreeSet<String> getEmail() {
        return email;
    }

    public void setEmail(TreeSet<String> email) {
        this.email.clear();
        this.email = email;
    }

    public void addEmail(String email)
    {
        this.email.add(email);
    }

    public String getFirstEmail(){
        return this.email.first();
    }


    public UserDetails() {
        this.email = new TreeSet<>();
    }


    public void emailClear() {
        this.email.clear();
    }
}
