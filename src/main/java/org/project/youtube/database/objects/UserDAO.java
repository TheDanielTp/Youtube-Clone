package org.project.youtube.database.objects;

import org.project.youtube.models.Content;
import org.project.youtube.models.ServerResponse;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class UserDAO
{
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/WeTube-DataBase";
    private static final String USER     = "postgres";
    private static final String PASSWORD = "0000";

    private static Connection connection () throws SQLException
    {
        return DriverManager.getConnection (JDBC_URL, USER, PASSWORD);
    }

    //region [ - Existence Check Methods - ]

    public static boolean checkID (UUID ID)
    {
        System.out.println ("> Database: Checking if " + ID + " exists...");
        String query = "SELECT EXISTS (SELECT 1 FROM users WHERE email = ?)";
        try (Connection connection = connection (); PreparedStatement preparedStatement = connection.prepareStatement (query))
        {
            preparedStatement.setObject (1, ID);
            try (ResultSet resultSet = preparedStatement.executeQuery ())
            {
                if (resultSet.next ())
                {
                    System.out.println ("> Database: " + ID + " exists.");
                    return resultSet.getBoolean (1);
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println (e.getMessage ());
        }
        return false;
    }

    public static boolean checkEmail (String email)
    {
        System.out.println ("> Database: Checking if " + email + " exists...");
        String query = "SELECT EXISTS (SELECT 1 FROM users WHERE email = ?)";
        try (Connection connection = connection (); PreparedStatement preparedStatement = connection.prepareStatement (query))
        {
            preparedStatement.setString (1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery ())
            {
                if (resultSet.next ())
                {
                    System.out.println ("> Database: " + email + " exists.");
                    return resultSet.getBoolean (1);
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println (e.getMessage ());
        }
        return false;
    }

    public static boolean checkUsername (String username)
    {
        System.out.println ("> Database: Checking if " + username + " exists...");
        String query = "SELECT EXISTS (SELECT 1 FROM users WHERE username = ?)";
        try (Connection connection = connection (); PreparedStatement preparedStatement = connection.prepareStatement (query))
        {
            preparedStatement.setString (1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery ())
            {
                if (resultSet.next ())
                {
                    System.out.println ("> Database: " + username + " exists.");
                    return resultSet.getBoolean (1);
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println (e.getMessage ());
        }
        return false;
    }

    public static boolean checkPassword (String username, String password)
    {
        System.out.println ("> Database: Checking password correction... ");
        String query = "SELECT password FROM users WHERE username = ?";
        try (Connection connection = connection (); PreparedStatement preparedStatement = connection.prepareStatement (query))
        {
            preparedStatement.setString (1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery ())
            {
                if (resultSet.next ())
                {
                    System.out.println ("> Database: password is correct.");
                    return password.equals (resultSet.getString ("password"));
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println (e.getMessage ());
        }
        return false;
    }
    //endregion

    //region [ - User Creation & Update Methods - ]

    public static void createAccount (String username, String email, String password, String firstName, String lastName,
                                      LocalDate birthdate, String profilePicture)
    {
        System.out.println ("> Database: Creating new account for " + username + " ...");
        UUID    ID        = UUID.randomUUID ();
        boolean isPremium = false;
        double  balance   = 0.00;
        ChannelDAO.createChannel (ID);
        Playlist_DB.create_watch_later (get_id_by_username (username));


        String query = "INSERT INTO users (ID, channelID, firstname, lastname, username, email, password, " +
                "birthdate, joindate, ispremium, balance, profilePicture) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = connection ();
             PreparedStatement preparedStatement = connection.prepareStatement (query))
        {
            connection.setAutoCommit (false);
            preparedStatement.setObject (1, ID);
            preparedStatement.setObject (2, ID);
            preparedStatement.setString (3, firstName);
            preparedStatement.setString (4, lastName);
            preparedStatement.setString (5, username);
            preparedStatement.setString (6, email);
            preparedStatement.setString (7, password);
            preparedStatement.setObject (8, birthdate);
            preparedStatement.setObject (9, LocalDate.now ());
            preparedStatement.setBoolean (10, isPremium);
            preparedStatement.setDouble (11, balance);
            preparedStatement.setString (12, profilePicture);
            preparedStatement.executeUpdate ();
            connection.commit ();
            System.out.println ("> Database: New account for " + username + " created.");
        }
        catch (SQLException e)
        {
            System.out.println (e.getMessage ());
        }
    }

    public static void setEmail (UUID ID, String email)
    {
        System.out.println ("> Database: Setting new information of " + getUsernameViaID (ID) + " ...");

        String query = "UPDATE users SET email VALUES ? WHERE ID = ?";
        try (Connection connection = connection ();
             PreparedStatement preparedStatement = connection.prepareStatement (query))
        {
            connection.setAutoCommit (false);
            preparedStatement.setString (1, email);
            preparedStatement.setObject (2, ID);
            preparedStatement.executeUpdate ();
            connection.commit ();
            System.out.println ("> Database: New information of " + getUsernameViaID (ID) + " set.");
        }
        catch (SQLException e)
        {
            System.out.println (e.getMessage ());
        }
    }

    public static void setUsername (UUID ID, String username)
    {
        System.out.println ("> Database: Setting new information of " + getUsernameViaID (ID) + " ...");

        String query = "UPDATE users SET userName VALUES ? WHERE ID = ?";
        try (Connection connection = connection ();
             PreparedStatement preparedStatement = connection.prepareStatement (query))
        {
            connection.setAutoCommit (false);
            preparedStatement.setString (1, username);
            preparedStatement.setObject (2, ID);
            preparedStatement.executeUpdate ();
            connection.commit ();
            System.out.println ("> Database: New information of " + getUsernameViaID (ID) + " set.");
        }
        catch (SQLException e)
        {
            System.out.println (e.getMessage ());
        }
    }

    public static void setName (UUID ID, String firstName, String lastName)
    {
        System.out.println ("> Database: Setting new information of " + getUsernameViaID (ID) + " ...");

        String query = "UPDATE users SET (firstname, lastname) VALUES (?, ?) WHERE ID = ?";
        try (Connection connection = connection ();
             PreparedStatement preparedStatement = connection.prepareStatement (query))
        {
            connection.setAutoCommit (false);
            preparedStatement.setString (1, firstName);
            preparedStatement.setString (2, lastName);
            preparedStatement.setObject (3, ID);
            preparedStatement.executeUpdate ();
            connection.commit ();
            System.out.println ("> Database: New information of " + getUsernameViaID (ID) + " set.");
        }
        catch (SQLException e)
        {
            System.out.println (e.getMessage ());
        }
    }

    public static void setProfilePicture (UUID ID, String profilePicture)
    {
        System.out.println ("> Database: Setting new profile picture for " + getUsernameViaID (ID) + " ...");

        String query = "UPDATE users SET profilePicture VALUES ? WHERE ID = ?";
        try (Connection connection = connection ();
             PreparedStatement preparedStatement = connection.prepareStatement (query))
        {
            connection.setAutoCommit (false);
            preparedStatement.setString (1, profilePicture);
            preparedStatement.setObject (3, ID);
            preparedStatement.executeUpdate ();
            connection.commit ();
            System.out.println ("> Database: New profile picture for " + getUsernameViaID (ID) + " set.");
        }
        catch (SQLException e)
        {
            System.out.println (e.getMessage ());
        }
    }

    public static void setBalance (UUID userID, double balance)
    {
        System.out.println ("> Database: Setting " + balance + " rubles for " + getUsernameViaID (userID) + " ...");
        String query = "UPDATE users SET balance = ? WHERE ID = ?";
        try (Connection connection = connection (); PreparedStatement preparedStatement = connection.prepareStatement (query);)
        {
            connection.setAutoCommit (false);
            preparedStatement.setDouble (1, balance);
            preparedStatement.setObject (2, userID);
            preparedStatement.executeUpdate ();
            connection.commit ();
            System.out.println ("> Database: " + balance + " rubles set for " + getUsernameViaID (userID) + ".");
        }
        catch (SQLException e)
        {
            System.out.println (e.getMessage ());
        }
    }

    public static void setPremium (UUID userID, boolean isPremium)
    {
        if (isPremium)
        {
            System.out.println ("> Database: Setting " + getUsernameViaID (userID) + " on premium...");
        }
        else
        {
            System.out.println ("> Database: Setting " + getUsernameViaID (userID) + " off premium...");
        }
        String query = "UPDATE users SET isPremium = ? WHERE ID = ?";
        try (Connection connection = connection (); PreparedStatement preparedStatement = connection.prepareStatement (query);)
        {
            connection.setAutoCommit (false);
            preparedStatement.setBoolean (1, isPremium);
            preparedStatement.setObject (2, userID);
            preparedStatement.executeUpdate ();
            connection.commit ();
            if (isPremium)
            {
                System.out.println ("> Database: " + getUsernameViaID (userID) + "  is premium.");
            }
            else
            {
                System.out.println ("> Database: " + getUsernameViaID (userID) + " is not premium.");
            }
        }
        catch (SQLException e)
        {
            System.out.println (e.getMessage ());
        }
    }

    public static void setPassword (UUID userID, String password)
    {
        System.out.println ("> Database: Changing password of " + userID + " ...");
        String query = "UPDATE users SET password = ? WHERE ID = ?";
        try (Connection connection = connection (); PreparedStatement preparedStatement = connection.prepareStatement (query);)
        {
            connection.setAutoCommit (false);
            preparedStatement.setString (1, password);
            preparedStatement.setObject (2, userID);
            preparedStatement.executeUpdate ();
            connection.commit ();
            System.out.println ("> Database: Password changed.");
        }
        catch (SQLException e)
        {
            System.out.println (e.getMessage ());
        }
    }

    public static void addNotification (UUID userID, UUID contentID, String title)
    {
        System.out.println ("> Database: Sending notification to " + getUsernameViaID (userID) + " ...");
        LocalDate receivedDate = LocalDate.now ();
        boolean   isSeen       = false;
        String    query        = "INSERT INTO notifications (userID, contentID, receivedDate, title) VALUES (?, ?, ?, ?)";
        try (Connection connection = connection (); PreparedStatement preparedStatement = connection.prepareStatement (query))
        {
            connection.setAutoCommit (false);
            preparedStatement.setObject (1, userID);
            preparedStatement.setObject (2, contentID);
            preparedStatement.setObject (3, receivedDate);
            preparedStatement.setString (4, title);
            preparedStatement.setBoolean (5, isSeen);
            preparedStatement.executeUpdate ();
            connection.commit ();
            System.out.println ("> Database: notification sent.");
        }
        catch (SQLException e)
        {
            System.out.println (e.getMessage ());
        }
    }

    public static void deleteAccount (UUID userID)
    {
        System.out.println ("> Database: Deleting account of " + userID + " ...");

        // Deleting from the tables that are only for the user
        ArrayList <String> queries = new ArrayList <> ();

        queries.add ("DELETE FROM users WHERE ID = ?");
        queries.add ("DELETE FROM channels WHERE userID = ?");
        queries.add ("DELETE FROM subscriptions WHERE subscriberID = ?");
        queries.add ("DELETE FROM playlistSubscriptions WHERE subscriberID = ?");
        queries.add ("DELETE FROM communities WHERE channelID = ?");
        queries.add ("DELETE FROM contentAction WHERE userID = ?");
        queries.add ("DELETE FROM commentAction WHERE userID = ?");
        queries.add ("DELETE FROM posts WHERE creatorID = ?");
        queries.add ("DELETE FROM commentAction WHERE userID = ?");

        try (Connection connection = connection ())
        {
            connection.setAutoCommit (false);
            for (String query : queries)
            {
                try (PreparedStatement preparedStatement = connection.prepareStatement (query))
                {
                    preparedStatement.setObject (1, userID);
                    preparedStatement.executeUpdate ();
                }
                catch (SQLException e)
                {
                    connection.rollback ();
                    System.out.println (e.getMessage ());
                }
            }
            connection.commit ();
        }
        catch (SQLException e)
        {
            System.out.println (e.getMessage ());
        }

        String contentType = "post";
        String query       = "DELETE FROM contents WHERE (creatorID, contentType) = (?, ?)";
        try (Connection connection = connection (); PreparedStatement preparedStatement = connection.prepareStatement (query))
        {
            connection.setAutoCommit (false);
            preparedStatement.setObject (1, userID);
            preparedStatement.setString (2, contentType);
            preparedStatement.executeUpdate ();
            connection.commit ();
        }
        catch (SQLException e)
        {
            System.out.println (e.getMessage ());
        }

        // Setting the other tables' users to delete account
        UUID               deleteAccountID = getDeleteAccountUserID ();
        ArrayList <String> queries2        = new ArrayList <> ();

        queries2.add ("UPDATE playlists SET creatorID = ? WHERE creatorID = ?");
        queries2.add ("UPDATE comments SET commenterID = ? WHERE commenterID = ?");
        queries2.add ("UPDATE videos SET creatorID = ? WHERE creatorID = ?");
        queries2.add ("UPDATE contents SET creatorID = ? WHERE creatorID = ?");
        queries2.add ("UPDATE communities SET channelID = ? WHERE channelID = ?");
        queries2.add ("UPDATE contentAction SET userID = ? WHERE userID = ?");
        queries2.add ("UPDATE commentAction SET userID = ? WHERE userID = ?");

        try (Connection connection = connection ())
        {
            connection.setAutoCommit (false);
            for (String query2 : queries2)
            {
                try (PreparedStatement preparedStatement = connection.prepareStatement (query2))
                {
                    preparedStatement.setObject (1, deleteAccountID);
                    preparedStatement.setObject (2, userID);
                    preparedStatement.executeUpdate ();
                }
                catch (SQLException e)
                {
                    connection.rollback ();
                    System.out.println (e.getMessage ());
                }
            }
            connection.commit ();
            System.out.println ("> Database: User deleted.");
        }
        catch (SQLException e)
        {
            System.out.println (e.getMessage ());
        }

    }

    //endregion

    //region [ - Date Receiver Methods - ]

    public static UUID getIDViaUsername (String username)
    {
        String query = "SELECT ID FROM users WHERE username = ?";
        try (Connection connection = connection (); PreparedStatement preparedStatement = connection.prepareStatement (query))
        {
            preparedStatement.setString (1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery ())
            {
                if (resultSet.next ())
                {
                    return (UUID) resultSet.getObject ("ID");
                }
                else
                {
                    return null;
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println (e.getMessage ());
        }
        return null;
    }

    public static String getUsernameViaID (UUID ID)
    {
        String query = "SELECT username FROM users WHERE ID = ?";
        try (Connection connection = connection (); PreparedStatement preparedStatement = connection.prepareStatement (query))
        {
            preparedStatement.setObject (1, ID);
            try (ResultSet resultSet = preparedStatement.executeQuery ())
            {
                if (resultSet.next ())
                {
                    return resultSet.getString ("username");
                }
                else
                {
                    return null;
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println (e.getMessage ());
        }
        return null;
    }

    public static ServerResponse signup (int requestID, String username, String email, String password,
                                         String firstName, String lastName, LocalDate birthdate, String profilePicture)
    {
        System.out.println ("> Database: Signing up user " + username + " ...");
        ServerResponse serverResponse = new ServerResponse ();
        serverResponse.setRequest_id (requestID);
        serverResponse.add_part ("isValidUsername", ! checkUsername (username));
        serverResponse.add_part ("isValidEmail", ! checkEmail (email));
        if ((boolean) serverResponse.get_part ("isValidUsername") && (boolean) serverResponse.get_part ("isValidEmail"))
        {
            createAccount (username, email, password, firstName, lastName, birthdate, profilePicture);
            serverResponse.add_part ("isSuccessful", true);
            serverResponse.add_part ("userID", Objects.requireNonNull (getIDViaUsername (username)).toString ());
        }
        else
        {
            serverResponse.add_part ("isSuccessful", false);
        }
        System.out.println ("> Database: user signed up.");
        return serverResponse;
    }

    public static ServerResponse login (int requestID, String username, String password)
    {
        System.out.println ("> Database: User " + username + " is logging in ...");
        ServerResponse serverResponse = new ServerResponse ();
        serverResponse.setRequest_id (requestID);
        serverResponse.add_part ("isSuccessful", checkUsername (username) && checkPassword (username, password));
        if ((boolean) serverResponse.get_part ("isSuccessful"))
        {
            serverResponse.add_part ("userID", Objects.requireNonNull (getIDViaUsername (username)).toString ());
        }
        System.out.println ("> Database: user logged in.");
        return serverResponse;
    }

    public static ServerResponse changeName (int requestID, UUID userID, String firstName, String lastName)
    {
        System.out.println ("> Database: Changing name to " + firstName + " " + lastName + " ...");
        ServerResponse serverResponse = new ServerResponse ();
        serverResponse.setRequest_id (requestID);
        setName (userID, firstName, lastName);
        serverResponse.add_part ("isSuccessful", true);
        System.out.println ("> Database: name changed.");
        return serverResponse;
    }

    public static ServerResponse changeEmail (int requestID, UUID userID, String email)
    {
        System.out.println ("> Database: Changing email to " + email + " ...");
        ServerResponse serverResponse = new ServerResponse ();
        serverResponse.setRequest_id (requestID);
        serverResponse.add_part ("isValidEmail", ! checkEmail (email));
        if ((boolean) serverResponse.get_part ("isValidEmail"))
        {
            setEmail (userID, email);
            serverResponse.add_part ("isSuccessful", true);
        }
        else
        {
            serverResponse.add_part ("isSuccessful", false);
        }
        System.out.println ("> Database: email changed.");
        return serverResponse;
    }

    public static ServerResponse changeUsername (int requestID, UUID userID, String username)
    {
        System.out.println ("> Database: Changing username to " + username + " ...");
        ServerResponse serverResponse = new ServerResponse ();
        serverResponse.setRequest_id (requestID);
        serverResponse.add_part ("isValidUsername", ! checkUsername (username));
        if ((boolean) serverResponse.get_part ("isValidUsername"))
        {
            setUsername (userID, username);
            serverResponse.add_part ("isSuccessful", true);
        }
        else
        {
            serverResponse.add_part ("isSuccessful", false);
        }
        System.out.println ("> Database: username changed.");
        return serverResponse;
    }

    public static ServerResponse changePassword (int requestID, UUID userID, String password)
    {
        System.out.println ("> Database: Changing password to " + password + " ...");
        ServerResponse serverResponse = new ServerResponse ();
        serverResponse.setRequest_id (requestID);
        setPassword (userID, password);
        serverResponse.add_part ("isSuccessful", true);

        System.out.println ("> Database: password changed.");
        return serverResponse;
    }

    public static ServerResponse getAllUserData (int requestID, UUID userID)
    {
        System.out.println ("> Database: Receiving all data of user " + userID + " ...");
        ServerResponse serverResponse = new ServerResponse ();
        serverResponse.setRequest_id (requestID);
        String query = "SELECT * FROM users WHERE ID = ?";
        try (Connection connection = connection (); PreparedStatement preparedStatement = connection.prepareStatement (query))
        {
            preparedStatement.setObject (1, userID);
            try (ResultSet resultSet = preparedStatement.executeQuery ())
            {
                if (resultSet.next ())
                {
                    serverResponse.add_part ("ID", resultSet.getString ("ID"));
                    serverResponse.add_part ("channelID", resultSet.getString ("ID"));
                    serverResponse.add_part ("firstName", resultSet.getString ("firstname"));
                    serverResponse.add_part ("lastName", resultSet.getString ("lastname"));
                    serverResponse.add_part ("username", resultSet.getString ("username"));
                    serverResponse.add_part ("email", resultSet.getString ("email"));
                    serverResponse.add_part ("password", resultSet.getString ("password"));
                    serverResponse.add_part ("birthdate", resultSet.getString ("birthdate"));
                    serverResponse.add_part ("creationDate", resultSet.getString ("joinDate"));
                    serverResponse.add_part ("isPremium", resultSet.getString ("isPremium"));
                    serverResponse.add_part ("balance", resultSet.getString ("balance"));
                    serverResponse.add_part ("profilePicture", resultSet.getString ("profilePicture"));
                }
            }
            System.out.println ("> Database: all data received.");
            return serverResponse;
        }
        catch (SQLException e)
        {
            System.out.println (e.getMessage ());
        }
        return null;
    }

    public static List <UUID> getSubscribedChannels (UUID userID)
    {
        System.out.println ("> Database: Receiving subscribed channels of user " + userID + " ...");
        List <UUID> IDs = new ArrayList <> ();
        String      sql = "SELECT channelID FROM subscriptions WHERE subscriberID = ?";
        try (Connection connection = DriverManager.getConnection (JDBC_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement (sql))
        {
            preparedStatement.setObject (1, userID);
            try (ResultSet resultSet = preparedStatement.executeQuery ())
            {
                while (resultSet.next ())
                {
                    UUID channelId = (UUID) resultSet.getObject ("channelID");
                    IDs.add (channelId);
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println (e.getMessage ());
        }
        System.out.println ("> Database: Subscribed channels received.");
        return IDs;
    }

    public static List <UUID> getUsersPlaylists (UUID userID)
    {
        System.out.println ("> Database: Receiving playlists of user " + userID + " ...");
        List <UUID> IDs = new ArrayList <> ();
        String      sql = "SELECT playlistID FROM playlistSubscriptions WHERE subscriberID = ?";
        try (Connection connection = DriverManager.getConnection (JDBC_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement (sql))
        {
            preparedStatement.setObject (1, userID);
            try (ResultSet resultSet = preparedStatement.executeQuery ())
            {
                while (resultSet.next ())
                {
                    UUID playlistId = (UUID) resultSet.getObject ("playlistID");
                    IDs.add (playlistId);
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println (e.getMessage ());
        }
        System.out.println ("> Database: Playlists received.");
        return IDs;
    }

    //endregion
}
