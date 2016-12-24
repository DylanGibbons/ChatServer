package daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Phillix
 * DAO super class, used for handling connection to the database
 * as well as containing constant values to be used in method returns
 */
public class Dao {
    
    // Integer returns for DAO's
    protected static final int SUCCESS       = 0;
    protected static final int CLASSNOTFOUND = -1;
    protected static final int SQLEX         = -2;
    protected static final int CONNCLOSEFAIL = -3;
    protected static final int SQLINTEG      = -4;
    protected static final int OTHER         = -5;

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        String driver   = "com.mysql.jdbc.Driver";
        String url      = "jdbc:mysql://localhost:3306/chatroom";
        String username = "root";
        String password = "";
        Connection con  = null;
           
        Class.forName(driver);
        con = DriverManager.getConnection(url, username, password);          
        
        return con;
    }

    public void freeConnection(Connection con) {
        try {
            if (con != null) {
                con.close();
                con = null;
            }
        } catch (SQLException e) {
                e.printStackTrace();
        }
    }
}