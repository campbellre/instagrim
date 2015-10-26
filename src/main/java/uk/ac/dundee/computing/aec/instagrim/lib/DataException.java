package uk.ac.dundee.computing.aec.instagrim.lib;

/**
 * Created by Ryan on 26/10/2015.
 *
 * From: http://stackoverflow.com/a/1754473
 */
public class DataException extends Exception {
    public DataException() { super(); }
    public DataException(String message) { super(message); }
    public DataException(String message, Throwable cause) { super(message, cause); }
    public DataException(Throwable cause) { super(cause); }
}
