package daos;

import business.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import security.UserSecurity;

/**
 * The UserDao class is used for communicating with the user table in the database
 * @author Phil
 */

public class UserDao extends Dao implements UserDaoInterface {

    private final String TABLE_NAME = "USERS";
    private final String USER_NAME  = "USERNAME";
    private final String PASSWORD   = "PASSWD";
    
    /**
     * Default Constructor for UsersDao
     */
    public UserDao() {
        super();
    }
    /**
     * Checks to see if the username is in the database
     * @param username The username we wish to check is in the database
     * @return 0 if it is in the database; -5 if it isn't in the database; -1 through -4 for errors
     */
    @Override
    public int checkUname(String username) {
        Connection con       = null;
        PreparedStatement ps = null;
        ResultSet rs         = null;

        try {
            con          = getConnection();
            String query = "SELECT " + USER_NAME + " FROM " + TABLE_NAME + " WHERE " + USER_NAME + " = ?";
            ps           = con.prepareStatement(query);
            ps.setString(1, username);
            rs = ps.executeQuery();

            if(rs.next())
                return SUCCESS; //success in this case means details exist and therefore user cannot use them
        }
        catch (SQLException ex2) {
                ex2.printStackTrace();
            return SQLEX;
        }
        catch(ClassNotFoundException cnf) {
            cnf.printStackTrace();
            return CLASSNOTFOUND;
        }
        finally {
            try {
                if(rs  != null)
                    rs.close();
                if(ps  != null)
                    ps.close();
                if(con != null)
                    freeConnection(con);
            }
            catch(SQLException e) {
                    e.printStackTrace();
               return CONNCLOSEFAIL;
            }
        }
        return OTHER;
    }

    /**
     * Registering a user by taking in a user object and attempting to insert into the database
     * @param u The user we wish to register
     * @return 0 if it inserted fine; -5 if it didn't insert; -1 through -4 for errors
     */
    @Override
    public int register(User u) {
        Connection con       = null;
        PreparedStatement ps = null;
        UserSecurity ms      = new UserSecurity();

        try {
            con          = getConnection();
            String query = "INSERT INTO " + TABLE_NAME + " (" + USER_NAME + ", " + PASSWORD  + 
                    ") VALUES (?, ?)";

            ps = con.prepareStatement(query);
            ps.setString(1, u.getUsername());
            ps.setString(2, ms.hash(u.getPassword().toCharArray()));

            if (ps.executeUpdate() > 0)
                return SUCCESS; //It successfully inserted into the database
        }
        catch (SQLException ex2) {
            ex2.printStackTrace();
            return SQLEX;
        }
        catch(ClassNotFoundException cnf) {
            cnf.printStackTrace();
            return CLASSNOTFOUND;
        }
        finally {
            try {
                if(ps  != null)
                    ps.close();
                if(con != null)
                    freeConnection(con);
            }
            catch(SQLException e) {
                    e.printStackTrace();
               return CONNCLOSEFAIL;
            }
        }
        return OTHER;
    }

    /**
     * Used for logging in a user
     * @param username String username to check against the database
     * @param password String password to check against the database
     * @return user object based on successful login, returns null Users object if not found
     */
    @Override
    public User logIn(String username, String password) {

        Connection con       = null;
        PreparedStatement ps = null;
        ResultSet rs         = null;
        User u               = null;
        UserSecurity ms      = new UserSecurity();

        try{
            con          = getConnection();
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + USER_NAME + " = ?";
            ps           = con.prepareStatement(query);
            ps.setString(1, username);
            rs = ps.executeQuery();

            if(rs.next()) {
                String dbPass = rs.getString(PASSWORD);
                if(ms.checkPassword(password.toCharArray(), dbPass)) {
                    u = new User();
                    u.setUsername(rs.getString(USER_NAME));
                    u.setPassword("open sesame!");

                    return u;
                }
            }
        }
        catch(Exception e) {
 
            e.printStackTrace();
        }
        finally {
            try {
                if(rs  != null)
                    rs.close();
                if(ps  != null)
                    ps.close();
                if(con != null)
                    freeConnection(con);
            }
            catch(SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    /**
     * Used for checking the validity of a username
     * for use with private messaging to confirm if a recipient is valid
     * @param username the username to check
     * @return boolean indicating if this user exists
     */
    @Override
    public boolean isValidUsername(String username) {
        
        Connection con       = null;
        PreparedStatement ps = null;
        ResultSet rs         = null;
        
        try {

            con = getConnection();
            ps = con.prepareStatement("SELECT " + USER_NAME + 
                    " FROM " + TABLE_NAME + 
                    " WHERE " + USER_NAME + " = ?");
            ps.setString(1, username);
            rs = ps.executeQuery();
            
            if(rs.next()) {
                return true;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        finally {
            try {
                if(ps != null) {
                    ps.close();
                }
                if(con != null) {
                    freeConnection(con);
                }
                if (rs != null) {
                    rs.close();
                }
            }
            catch(SQLException e) {
                e.printStackTrace();
                
                return false;
            }
        }
        return false;
    }
}    
