package uk.ac.dundee.computing.aec.instagrim.models;

/*
 * Expects a cassandra columnfamily defined as
 * use keyspace2;
 CREATE TABLE Tweets (
 user varchar,
 interaction_time timeuuid,
 tweet varchar,
 PRIMARY KEY (user,interaction_time)
 ) WITH CLUSTERING ORDER BY (interaction_time DESC);
 * To manually generate a UUID use:
 * http://www.famkruithof.net/uuid/uuidgen
 */

import com.datastax.driver.core.*;
import org.imgscalr.Scalr.*;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.lib.DataException;
import uk.ac.dundee.computing.aec.instagrim.lib.Default;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.UUID;

import static org.imgscalr.Scalr.*;
//import uk.ac.dundee.computing.aec.stores.TweetStore;

public class PicModel {

    private Cluster cluster;

    private static BufferedImage createThumbnail(BufferedImage img) {
        img = resize(img, Method.SPEED, 250, OP_ANTIALIAS, OP_GRAYSCALE);
        // Let's add a little border before we return result.
        return pad(img, 2);
    }

    private static BufferedImage createProcessed(BufferedImage img) {
        int Width = img.getWidth() - 1;
        img = resize(img, Method.SPEED, Width, OP_ANTIALIAS, OP_GRAYSCALE);
        return pad(img, 4);
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public void insertPic(byte[] b, String type, String name, String user) {
        try {
            String types[] = Convertors.SplitFiletype(type);
            ByteBuffer buffer = ByteBuffer.wrap(b);
            int length = b.length;
            java.util.UUID picid = Convertors.getTimeUUID();

            //The following is a quick and dirty way of doing this, will fill the disk quickly !
            Boolean success = (new File("/var/tmp/" + Default.KEYSPACE_NAME + "/")).mkdirs();
            FileOutputStream output = new FileOutputStream(new File("/var/tmp/"+Default.KEYSPACE_NAME+"/" + picid));

            output.write(b);
            byte[] thumbb = picresize(picid.toString(), types[1]);
            int thumblength = thumbb.length;
            ByteBuffer thumbbuf = ByteBuffer.wrap(thumbb);
            byte[] processedb = picdecolour(picid.toString(), types[1]);
            ByteBuffer processedbuf = ByteBuffer.wrap(processedb);
            int processedlength = processedb.length;
            Session session = cluster.connect(Default.KEYSPACE_NAME);

            PreparedStatement psInsertPic = session.prepare("insert into pics ( picid, image,thumb,processed, user, interaction_time,imagelength,thumblength,processedlength,type,name) values(?,?,?,?,?,?,?,?,?,?,?)");
            PreparedStatement psInsertPicToUser = session.prepare("insert into userpiclist ( picid, user, pic_added) values(?,?,?)");
            BoundStatement bsInsertPic = new BoundStatement(psInsertPic);
            BoundStatement bsInsertPicToUser = new BoundStatement(psInsertPicToUser);

            Date DateAdded = new Date();
            session.execute(bsInsertPic.bind(picid, buffer, thumbbuf, processedbuf, user, DateAdded, length, thumblength, processedlength, type, name));
            session.execute(bsInsertPicToUser.bind(picid, user, DateAdded));
            session.close();

        } catch (IOException ex) {
            System.out.println("Error --> " + ex);
        }
    }


    // NOTE: Ensure that you have write permissions to the ******* tomcat temp dir
    // This can be found in your apache tom cat folder with the name "temp"
    // and can be set though temp > properties > security > users > write.
    // If you have changed the folder be sure that you have write permissions
    // there as well.
    private byte[] picresize(String picid, String type) {
        try {

            BufferedImage BI = ImageIO.read(new File("/var/tmp/" + Default.KEYSPACE_NAME+"/" + picid));
                    BufferedImage thumbnail = createThumbnail(BI);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ImageIO.write(thumbnail, type, baos);
            
            baos.flush();

            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException et) {

        }
        return null;
    }

    private byte[] picdecolour(String picid, String type) {
        try {
            BufferedImage BI = ImageIO.read(new File("/var/tmp/"+ Default.KEYSPACE_NAME + "/" + picid));
            BufferedImage processed = createProcessed(BI);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(processed, type, baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException et) {

        }
        return null;
    }

    public java.util.LinkedList<Pic> getPicsForUser(String User) throws DataException {
        java.util.LinkedList<Pic> Pics = new java.util.LinkedList<>();
        Session session = cluster.connect(Default.KEYSPACE_NAME);
                PreparedStatement ps = session.prepare("select picid from userpiclist where user =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        try {
            rs = session.execute( // this is where the query is executed
                    boundStatement.bind( // here you are binding the 'boundStatement'
                            User));
            if (rs.isExhausted()) {
                System.out.println("No Images returned");
                return null;
            } else {
                for (Row row : rs) {
                    Pic pic = new Pic();
                    java.util.UUID UUID = row.getUUID("picid");
                    System.out.println("UUID" + UUID.toString());
                    pic.setUUID(UUID);
                    Pics.add(pic);

                }
            }
        }
        catch(Exception ex) {
            throw new DataException("No Such User");
        }
        return Pics;
    }

    public Pic getPic(int image_type, java.util.UUID picid) throws DataException {
        Session session = cluster.connect(Default.KEYSPACE_NAME);
        ByteBuffer bImage = null;
        String type = null;
        int length = 0;
        try {
            Convertors convertor = new Convertors();
            ResultSet rs = null;
            PreparedStatement ps = null;

            if (image_type == Convertors.DISPLAY_IMAGE) {

                ps = session.prepare("select image,imagelength,type from pics where picid =?");
            } else if (image_type == Convertors.DISPLAY_THUMB) {
                ps = session.prepare("select thumb,imagelength,thumblength,type from pics where picid =?");
            } else if (image_type == Convertors.DISPLAY_PROCESSED) {
                ps = session.prepare("select processed,processedlength,type from pics where picid =?");
            }
            BoundStatement boundStatement = new BoundStatement(ps);
            rs = session.execute( // this is where the query is executed
                    boundStatement.bind( // here you are binding the 'boundStatement'
                            picid));

            if (rs.isExhausted()) {
                System.out.println("No Images returned");
                return null;
            } else {
                for (Row row : rs) {
                    if (image_type == Convertors.DISPLAY_IMAGE) {
                        bImage = row.getBytes("image");
                        length = row.getInt("imagelength");
                    } else if (image_type == Convertors.DISPLAY_THUMB) {
                        bImage = row.getBytes("thumb");
                        length = row.getInt("thumblength");

                    } else if (image_type == Convertors.DISPLAY_PROCESSED) {
                        bImage = row.getBytes("processed");
                        length = row.getInt("processedlength");
                    }

                    type = row.getString("type");

                }
            }
        } catch (Exception et) {
            throw new DataException("No Such Pic");
        }
        session.close();
        Pic p = new Pic();
        p.setPic(bImage, length, type);

        return p;

    }


    public void InsertProfilePic(byte[] b, String type, String name, String user)
    {
        try {
            String types[] = Convertors.SplitFiletype(type);
            ByteBuffer buffer = ByteBuffer.wrap(b);
            int length = b.length;
            java.util.UUID picid = Convertors.getTimeUUID();

            //The following is a quick and dirty way of doing this, will fill the disk quickly !
            Boolean success = (new File("/var/tmp/" + Default.KEYSPACE_NAME + "/")).mkdirs();
            FileOutputStream output = new FileOutputStream(new File("/var/tmp/" + Default.KEYSPACE_NAME + "/" + picid));

            output.write(b);
            byte[] thumbb = picresize(picid.toString(), types[1]);
            // Go to 'picresize' function on a "NULL POINTER EXCEPTION".
            int thumblength = thumbb.length;
            ByteBuffer thumbbuf = ByteBuffer.wrap(thumbb);
            byte[] processedb = picdecolour(picid.toString(), types[1]);
            ByteBuffer processedbuf = ByteBuffer.wrap(processedb);
            int processedlength = processedb.length;
            Session session = cluster.connect(Default.KEYSPACE_NAME);

            PreparedStatement psInsertPic = session.prepare(
                    "insert into pics " +
                            "( picid, image,thumb,processed, user, interaction_time,imagelength,thumblength,processedlength,type,name)" +
                            " values(?,?,?,?,?,?,?,?,?,?,?)");
            PreparedStatement psInsertPicToUser = session.prepare(
                    "update userprofiles set profile_pic = ? where login = ?");
            BoundStatement bsInsertPic = new BoundStatement(psInsertPic);
            BoundStatement bsInsertPicToUser = new BoundStatement(psInsertPicToUser);

            Date DateAdded = new Date();
            session.execute(bsInsertPic.bind(picid, buffer, thumbbuf, processedbuf, user, DateAdded, length, thumblength, processedlength, type, name));
            session.execute(bsInsertPicToUser.bind(picid, user));
            session.close();

        } catch (IOException ex) {
            System.out.println("Error --> " + ex);
        }
    }

    public String getOwner(String PUUID) throws DataException {
        Session session = cluster.connect(Default.KEYSPACE_NAME);
        PreparedStatement psOwner = session.prepare("Select user from pic where picid = ?");

        BoundStatement bsGetOwner = new BoundStatement(psOwner);

        ResultSet rs = session.execute(bsGetOwner.bind(UUID.fromString(PUUID)));

        if(rs.isExhausted())
        {
            throw new DataException("No Such User");
        }

        String ret = null;

        for(Row row : rs)
        {
            ret = row.getString("user");
        }

        return ret;
    }

    public void deletePic(String PUUID)
    {
        Session session = cluster.connect(Default.KEYSPACE_NAME);

        PreparedStatement psDelete = session.prepare("Delete from pic where picid = ?");
        BoundStatement bsDelete = new BoundStatement(psDelete);

        session.execute(bsDelete.bind(UUID.fromString(PUUID)));


    }



}
