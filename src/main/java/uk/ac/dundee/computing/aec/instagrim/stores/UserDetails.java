package uk.ac.dundee.computing.aec.instagrim.stores;

import com.sun.javafx.font.PrismFontFile;
import uk.ac.dundee.computing.aec.instagrim.lib.Default;

import java.util.TreeSet;
import java.util.UUID;

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
    private String password;
    private Pic profilepicuuid = null;

    public String getProfilepicUUID()
    {
        return profilepicuuid.getSUUID();
    }

    public void setProfilepicUUID(UUID uuid) {
        this.profilepicuuid.setUUID(uuid);
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

    public void emailClear() {
        this.email.clear();
    }


    public UserDetails() {
        this.email = new TreeSet<>();
        this.profilepicuuid = new Pic();
    }
}
