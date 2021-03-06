/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.instagrim.models;

import com.datastax.driver.core.*;
import uk.ac.dundee.computing.aec.instagrim.lib.AeSimpleSHA1;
import uk.ac.dundee.computing.aec.instagrim.lib.Default;
import uk.ac.dundee.computing.aec.instagrim.stores.UserDetails;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Administrator
 */
public class User {

    private Cluster cluster;

    public User() {

    }

    private boolean RegisterUser(String username, String Password) {
        AeSimpleSHA1 sha1handler = new AeSimpleSHA1();
        String EncodedPassword = null;
        try {
            EncodedPassword = AeSimpleSHA1.SHA1(Password);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException et) {
            System.out.println("Can't check your password");
            return false;
        }

        Session session = cluster.connect(Default.KEYSPACE_NAME);

        PreparedStatement checkExist = session.prepare("select * from userprofiles where login=?");

        BoundStatement b = new BoundStatement(checkExist);
        ResultSet rs = session.execute(checkExist.bind(username));

        if (rs.isExhausted()) {
            PreparedStatement ps = session.prepare("insert into userprofiles (login,password) Values(?,?)");

            BoundStatement boundStatement = new BoundStatement(ps);
            session.execute( // this is where the query is executed
                    boundStatement.bind( // here you are binding the 'boundStatement'
                            username, EncodedPassword));
            //We are assuming this always works.  Also a transaction would be good here !
        }
        return true;
    }

    public boolean IsValidUser(String username, String Password) {
        String EncodedPassword;
        try {
            EncodedPassword = AeSimpleSHA1.SHA1(Password);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException et) {
            System.out.println("Can't check your password");
            return false;
        }
        Session session = cluster.connect(Default.KEYSPACE_NAME);
        PreparedStatement ps = session.prepare("select password from userprofiles where login =?");
        ResultSet rs;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        username));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return false;
        } else {
            for (Row row : rs) {

                String StoredPass = row.getString("password");
                if (StoredPass.compareTo(EncodedPassword) == 0) {
                    return true;
                }
            }
        }

        return false;
    }

    public UserDetails getUserInfo(String username) {
        Session session = cluster.connect(Default.KEYSPACE_NAME);

        PreparedStatement validUser = session.prepare("select * from userprofiles where login=?");

        BoundStatement b = new BoundStatement(validUser);

        ResultSet rs = session.execute(b.bind(username));

        UserDetails returnUser = new UserDetails();

        if (rs.isExhausted()) {
            System.out.print("Invalid Username");
        } else {
            for (Row row : rs) {
                if(row.isNull("login")){
                    returnUser.setLogin("");
                }
                else {
                    returnUser.setLogin(row.getString("login"));
                }
                if (row.isNull("first_name")) {
                    returnUser.setFirstname("");
                }
                else {
                    returnUser.setFirstname(row.getString("first_name"));
                }
                if(row.isNull("last_name")) {
                    returnUser.setLastname("");
                }
                else {
                    returnUser.setLastname(row.getString("last_name"));
                }
                if(row.isNull("profile_pic"))
                {
                    // This should work by showing default.
                    // Should be set in the store (UserDetails).
                }
                else
                {
                    returnUser.setProfilepicUUID(row.getUUID("profile_pic"));
                }
                if(row.isNull("email")) {
                    returnUser.addEmail("");
                }
                else {
                    for (String str : row.getSet("email",String.class)) {
                        returnUser.emailClear();
                        returnUser.addEmail(str);
                    }

                }
            }
        }

        return returnUser;

    }


    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }


    public void setUserDetails(UserDetails ud) {

        Session session = cluster.connect(Default.KEYSPACE_NAME);

        PreparedStatement ps = session.prepare("UPDATE userprofiles SET first_name = ?, last_name = ?, email = ? WHERE login = ?");

        BoundStatement b = new BoundStatement(ps);

        session.execute(b.bind(ud.getFirstname(),ud.getLastname(),ud.getEmail(),ud.getLogin()));

    }

    public void registerAddUser(UserDetails ud)
    {
        /*
        First call the register user function
        with the login and password from the
        UserDetails object.
        */
        // Use the returned bool from register to ensure
        // The user has been created before messing around
        // with their details.
        if(RegisterUser(ud.getLogin(),ud.getPassword())) {
            /*
            Then call the setUserDetails for simplicity
            At the moment. :(
            */
            setUserDetails(ud);
        }

    }

    public void deleteUser(String user) {
        Session session = cluster.connect(Default.KEYSPACE_NAME);

        PreparedStatement psDeleteUser = session.prepare("delete from userprofiles where login = ?");
        BoundStatement bsDeleteUser = new BoundStatement(psDeleteUser);

        session.execute(bsDeleteUser.bind(user));

    }
}
