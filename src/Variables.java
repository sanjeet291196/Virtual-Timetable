
import java.io.*;
import java.sql.*;

/**
 * Class containing definition of all the static variables used in application
 *
 * @author Sanjit
 */
public class Variables {
    //Database info
    /**
     * Stores Class Name to use for Database
     */
    static String ClassName = null;
    /**
     * Stores Database URL
     */
    static String DBurl = null;
    /**
     * Stores Database User Name
     */
    static String user = null;
    /**
     * Stores Database Password
     */
    static String psw = null;
    /**
     * For establishing Database Connection for reading faculty details
     */
    static Connection c = null;
    /**
     * For executing database queries
     */
    static Statement stmt = null;
    /**
     * For storing database query results
     */
    static ResultSet rs = null;
    
    
    //other variables
    /**
     * boolean to store whether the Affinity hours are to be assigned or not
     */
    static boolean Affinity;
    /**
     * FileReader for reading subject names and database info stored in files
     */
    
    //File handling
    static FileReader file=null;
    /**
     * BufferedReader associated with file for reading subjects
     */
    static BufferedReader Reader=null;
    /**
     * Stores most of the functions and variables used in program
     */
    
    //event handlers
    static HomePage homePageEvents=new HomePage();
}
