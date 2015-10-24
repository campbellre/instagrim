/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.instagrim.models;

import com.datastax.driver.core.*;
import com.datastax.driver.core.Row;
import com.sun.rowset.internal.*;
import uk.ac.dundee.computing.aec.instagrim.lib.AeSimpleSHA1;
import uk.ac.dundee.computing.aec.instagrim.stores.UserDetails;

import javax.management.modelmbean.RequiredModelMBean;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @author Administrator
 */
public class User {

    Cluster cluster;

    public User() {

    }

    public boolean RegisterUser(String username, String Password) {
        AeSimpleSHA1 sha1handler = new AeSimpleSHA1();
        String EncodedPassword = null;
        try {
            EncodedPassword = sha1handler.SHA1(Password);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException et) {
            System.out.println("Can't check your password");
            return false;
        }
        Session session = cluster.connect("instagrim");

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
        AeSimpleSHA1 sha1handler = new AeSimpleSHA1();
        String EncodedPassword = null;
        try {
            EncodedPassword = sha1handler.SHA1(Password);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException et) {
            System.out.println("Can't check your password");
            return false;
        }
        Session session = cluster.connect("instagrim");
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
        Session session = cluster.connect("instagrim");

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
                if(row.isNull("email")) {
                    returnUser.setEmail("");
                }
                else {
                    returnUser.setEmail(row.getString("email"));
                }
            }
        }

        return returnUser;

    }


    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }


    public void setUserDetails(UserDetails ud) {
        Session session = cluster.connect("instagrim");

        PreparedStatement ps = session.prepare("UPDATE userprofiles SET first_name = ?, last_name = ?, email = ? WHERE login = ?");

        BoundStatement b = new BoundStatement(ps);

        session.execute(b.bind(ud.getFirstname(),ud.getLastname(),ud.getEmail(),ud.getLogin()));

        return;
    }
}
