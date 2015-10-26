package uk.ac.dundee.computing.aec.instagrim.lib;

import java.sql.Timestamp;

/**
 * Created by Ryan on 24/10/2015.
 */
public class CommentWrapper {

    private Timestamp time;
    private String user;
    private String comment;


    public CommentWrapper(Timestamp t, String u, String c)
    {
        this.time = t;
        this.user = u;
        this.comment = c;
    }


    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
