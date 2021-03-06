package uk.ac.dundee.computing.aec.instagrim.models;

import com.datastax.driver.core.*;
import uk.ac.dundee.computing.aec.instagrim.lib.CommentWrapper;
import uk.ac.dundee.computing.aec.instagrim.lib.Default;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.UUID;


/**
 * Created by Ryan on 24/10/2015.
 */
public class Comment {

    private Cluster cluster;

    public Comment() {}

    public void EnterComment(String User, String Body, String PUUID){
        Session session = cluster.connect(Default.KEYSPACE_NAME);

        PreparedStatement AddComment = session.prepare("INSERT INTO piccomments (user, picid, time, comment) values (?,?, ?, ?);");

        BoundStatement b = new BoundStatement(AddComment);
        Timestamp t = new Timestamp(Calendar.getInstance().getTimeInMillis());
        session.execute(b.bind(User, UUID.fromString(PUUID),t,Body));

    }

    public TreeSet<CommentWrapper> GetComments(String PUUID) {
        Session session = cluster.connect(Default.KEYSPACE_NAME);

        PreparedStatement GetComments = session.prepare("SELECT * FROM piccomments where picid=?");

        BoundStatement b = new BoundStatement(GetComments);

        ResultSet rs = session.execute(b.bind(UUID.fromString(PUUID)));

        // From : http://stackoverflow.com/a/15636244
        Comparator<CommentWrapper> cmp = new Comparator<CommentWrapper>() {
            @Override
            public int compare(CommentWrapper o1, CommentWrapper o2) {
                if(o1.getTime().after(o2.getTime())) return 1;
                else if(o1.getTime().before(o2.getTime())) return -1;
                else return (o1.getTime().equals(o2.getTime()) ? 0 : 1);
            }
        };

        TreeSet<CommentWrapper> returnSet = new TreeSet<>(cmp);

        for(Row row : rs)
        {
            returnSet.add(new CommentWrapper(new Timestamp((row.getDate("time")).getTime()),
                    row.getString("user"),
                    row.getString("comment")) );
        }

        return returnSet;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }


}
