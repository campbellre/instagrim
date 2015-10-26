package uk.ac.dundee.computing.aec.instagrim.lib;

import com.datastax.driver.core.*;

public final class Keyspaces {

    public Keyspaces() {

    }

    public static void SetUpKeySpaces(Cluster c) {
        try {
            //Add some keyspaces here
            String createkeyspace = "create keyspace if not exists " + Default.KEYSPACE_NAME
                    +"  WITH replication = {'class':'SimpleStrategy', 'replication_factor':1}";

            String CreatePicTable = "CREATE TABLE if not exists " + Default.KEYSPACE_NAME + ".Pics ("
                    + " user varchar,"
                    + " picid uuid, "
                    + " interaction_time timestamp,"
                    + " title varchar,"
                    + " image blob,"
                    + " thumb blob,"
                    + " processed blob,"
                    + " imagelength int,"
                    + " thumblength int,"
                    + " processedlength int,"
                    + " type  varchar,"
                    + " name  varchar,"
                    + " PRIMARY KEY (picid)"
                    + ")";
            String Createuserpiclist = "CREATE TABLE if not exists " + Default.KEYSPACE_NAME + ".userpiclist (\n"
                    + "picid uuid,\n"
                    + "user varchar,\n"
                    + "pic_added timestamp,\n"
                    + "PRIMARY KEY (user,pic_added)\n"
                    + ") WITH CLUSTERING ORDER BY (pic_added desc);";
            String CreateAddressType = "CREATE TYPE if not exists " + Default.KEYSPACE_NAME + ".address (\n"
                    + "      street text,\n"
                    + "      city text,\n"
                    + "      zip int\n"
                    + "  );";
            String CreateUserProfile = "CREATE TABLE if not exists " + Default.KEYSPACE_NAME + ".userprofiles (\n"
                    + "      login text PRIMARY KEY,\n"
                    + "       password text,\n"
                    + "      first_name text,\n"
                    + "      last_name text,\n"
                    + "      email set<text>,\n"
                    + "      addresses  map<text, frozen <address>>,\n"
                    + "      profile_pic UUID \n"
                    + "  );";
            String CreatePicComments = "CREATE TABLE if not exists " + Default.KEYSPACE_NAME + ".piccomments (\n" +
                    "    user text,\n" +
                    "    picid uuid,\n" +
                    "    time timestamp,\n" +
                    "    comment text,\n" +
                    "    PRIMARY KEY (user, picid, time)\n" +
                    ")  WITH CLUSTERING ORDER BY (picid DESC, time DESC);";
            String CreatePicCommentsIndex = "CREATE INDEX ON " + Default.KEYSPACE_NAME + ".piccomments (picid) ;";
            Session session = c.connect();
            try {
                PreparedStatement statement = session
                        .prepare(createkeyspace);
                BoundStatement boundStatement = new BoundStatement(
                        statement);
                ResultSet rs = session
                        .execute(boundStatement);
                System.out.println("created " + Default.KEYSPACE_NAME + " ");
            } catch (Exception et) {
                System.out.println("Can't create " + Default.KEYSPACE_NAME + " " + et);
            }

            //now add some column families 
            System.out.println("" + CreatePicTable);

            try {
                SimpleStatement cqlQuery = new SimpleStatement(CreatePicTable);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create tweet table " + et);
            }
            System.out.println("" + Createuserpiclist);

            try {
                SimpleStatement cqlQuery = new SimpleStatement(Createuserpiclist);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create user pic list table " + et);
            }
            System.out.println("" + CreateAddressType);
            try {
                SimpleStatement cqlQuery = new SimpleStatement(CreateAddressType);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create Address type " + et);
            }
            System.out.println("" + CreateUserProfile);
            try {
                SimpleStatement cqlQuery = new SimpleStatement(CreateUserProfile);
                session.execute(cqlQuery);
            } catch (Exception et) {
                System.out.println("Can't create Address Profile " + et);
            }
            System.out.println("" + CreatePicComments);
            try {
                SimpleStatement cqlQeury = new SimpleStatement(CreatePicComments);
                session.execute(cqlQeury);
            }
            catch(Exception et)
            {
                System.out.print("Can't create comments table "+ et);
            }
            try{
                SimpleStatement cqlQuery = new SimpleStatement(CreatePicCommentsIndex);
                session.execute(cqlQuery);
            }
            catch (Exception et)
            {
                System.out.print("Can't create index on comments table " + et);
            }
            session.close();
        } catch (Exception et) {
            System.out.println("Other keyspace or coulm definition error" + et);
        }

    }
}
