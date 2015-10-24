package uk.ac.dundee.computing.aec.instagrim.models;

import com.datastax.driver.core.*;

import java.util.HashMap;


/**
 * Created by Ryan on 24/10/2015.
 */
public class Comment {

    Cluster cluster;

    public Comment() {}

    public void EnterComment(String User, String Body, String PUUID){
        Session session = cluster.connect("instagrim");

        PreparedStatement AddComment = session.prepare("INSERT INTO piccomments (user, picid, comment) values (?,?,?);");

        BoundStatement b = new BoundStatement(AddComment);

        session.execute(b.bind(User,PUUID,Body));

    }

    public HashMap<String,String> GetComments(String PUUID) {
        HashMap<String,String> returnMap = new HashMap<>();
        Session session = cluster.connect("instagrim");

        PreparedStatement GetComments = session.prepare("SELECT * FROM piccomments where picid=?");

        BoundStatement b = new BoundStatement(GetComments);

        ResultSet rs = session.execute(b.bind(PUUID));

        for(Row row : rs)
        {
            returnMap.put(row.getString("user"), row.getString("comment"));
        }

        return returnMap;
    }


}
