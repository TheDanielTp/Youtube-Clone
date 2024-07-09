package com.wetube.dao.impl;

import com.wetube.model.Notification;
import com.wetube.model.User;
import com.wetube.util.DatabaseConnection;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class UserDAOImpl
{
    public String chooseRandomImage () throws IOException
    {
        Path        dir        = Paths.get ("D:\\Java\\Projects\\Project_WeTube\\src\\main\\resources\\images\\profilePictures");
        List <Path> imageFiles = new ArrayList <> ();

        try (DirectoryStream <Path> stream = Files.newDirectoryStream (dir, "*.{jpg,jpeg,png,gif,bmp}"))
        {
            for (Path entry : stream)
            {
                imageFiles.add (entry);
            }
        }
        catch (IOException e)
        {
            throw new IOException ("Failed to read directory stream", e);
        }

        if (imageFiles.isEmpty ())
        {
            return null;
        }

        Random random      = new Random ();
        int    randomIndex = random.nextInt (imageFiles.size ());
        return imageFiles.get (randomIndex).toString ();
    }

    public UUID generateID ()
    {
        UUID uuid = UUID.randomUUID ();

        List <User> all = findAll ();
        for (User object : all)
        {
            if (object.getID () == uuid)
            {
                uuid = generateID ();
                break;
            }
        }
        return uuid;
    }

    public void create (User user)
    {
        String sql = "INSERT INTO Users (ID, channelID, firstName, lastName, username, email, password, birthdate," +
                " joinDate, isPremium, balance, profilePictureURL) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement  preparedStatement = connection.prepareStatement (sql))
        {
             preparedStatement.setObject (1, user.getID ());
             preparedStatement.setObject (2, user.getChannelID ());
             preparedStatement.setString (3, user.getFirstName ());
             preparedStatement.setString (4, user.getLastName ());
             preparedStatement.setString (5, user.getUsername ());
             preparedStatement.setString (6, user.getEmail ());
             preparedStatement.setString (7, user.getPassword ());
             preparedStatement.setDate (8, Date.valueOf (user.getBirthdate ()));
             preparedStatement.setDate (9, Date.valueOf (user.getJoinDate ()));
             preparedStatement.setBoolean (10, user.isPremium ());
             preparedStatement.setDouble (11, user.getBalance ());
             preparedStatement.setString (12, user.getProfilePictureURL ());
             preparedStatement.executeUpdate ();
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public void update (User user)
    {
        String sql = "UPDATE Users SET channelID = ?, firstName = ?, lastName = ?, username = ?, email = ?," +
                " password = ?, birthdate = ?, joinDate = ?, isPremium = ?, balance = ?, profilePictureURL = ?" +
                " WHERE ID = ?";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement  preparedStatement = connection.prepareStatement (sql))
        {
             preparedStatement.setObject (1, user.getChannelID ());
             preparedStatement.setString (2, user.getFirstName ());
             preparedStatement.setString (3, user.getLastName ());
             preparedStatement.setString (4, user.getUsername ());
             preparedStatement.setString (5, user.getEmail ());
             preparedStatement.setString (6, user.getPassword ());
             preparedStatement.setDate (7, Date.valueOf (user.getBirthdate ()));
             preparedStatement.setDate (8, Date.valueOf (user.getJoinDate ()));
             preparedStatement.setBoolean (9, user.isPremium ());
             preparedStatement.setDouble (10, user.getBalance ());
             preparedStatement.setString (11, user.getProfilePictureURL ());
             preparedStatement.setObject (12, user.getID ());
             preparedStatement.executeUpdate ();
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public void delete (UUID id)
    {
        try (Connection connection = DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ())
        {

            String deletePlaylists             = "DELETE FROM Playlists WHERE creatorID = '" + id + "' AND isPublic" +
                    " = FALSE";
            String deleteNotifications         = "DELETE FROM Notifications WHERE userID = '" + id + "'";
            String deleteChannels              = "DELETE FROM Channels WHERE userID = '" + id + "'";
            String deleteContentActions        = "DELETE FROM ContentsAction WHERE userID = '" + id + "'";
            String deletePlaylistAdmins        = "DELETE FROM playlistAdmins WHERE adminID = '" + id + "'";
            String deleteSubscribers           = "DELETE FROM Subscribers WHERE userID = '" + id + "' OR channelID IN" +
                    " (SELECT ID FROM Channels WHERE userID = '" + id + "')";
            String deletePlaylistSubscribers   = "DELETE FROM PlaylistSubscribers WHERE userID = '" + id + "' OR" +
                    " playlistID IN (SELECT ID FROM playlists WHERE userID = '" + id + "')";

            statement.executeUpdate (deleteSubscribers);
            statement.executeUpdate (deleteNotifications);
            statement.executeUpdate (deletePlaylists);
            statement.executeUpdate (deletePlaylistSubscribers);
            statement.executeUpdate (deletePlaylistAdmins);
            statement.executeUpdate (deleteContentActions);
            statement.executeUpdate (deleteChannels);

            String deleteUser = "DELETE FROM Users WHERE ID = '" + id + "'";
            statement.executeUpdate (deleteUser);
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }

        UUID deleteAccountID = getDeleteAccountUserID();
        ArrayList <String> queries2 = new ArrayList <> ();

        queries2.add("UPDATE playlists SET creatorID = ? WHERE creatorID = ?");
        queries2.add("UPDATE comments SET commenterID = ? WHERE commenterID = ?");
        queries2.add("UPDATE videos SET creatorID = ? WHERE creatorID = ?");

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit (false);
            for (String query2 : queries2) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(query2)) {
                    preparedStatement.setObject(1, deleteAccountID);
                    preparedStatement.setObject(2, id);
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    connection.rollback();
                    e.printStackTrace();
                }
            }
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public User findById (UUID id)
    {
        String sql = "SELECT * FROM Users WHERE ID = ?";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement  preparedStatement = connection.prepareStatement (sql))
        {
             preparedStatement.setObject (1, id);
            ResultSet  resultSet =  preparedStatement.executeQuery ();
            if  (resultSet.next ())
            {
                return new User (
                         resultSet.getObject ("ID", UUID.class),
                         resultSet.getObject ("channelID", UUID.class),
                         resultSet.getString ("firstName"),
                         resultSet.getString ("lastName"),
                         resultSet.getString ("username"),
                         resultSet.getString ("email"),
                         resultSet.getString ("password"),
                         resultSet.getDate ("birthdate").toLocalDate (),
                         resultSet.getDate ("joinDate").toLocalDate (),
                         resultSet.getBoolean ("isPremium"),
                         resultSet.getDouble ("balance"),
                         resultSet.getString ("profilePictureURL")
                );
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return null;
    }

    public User findByUsername (String username)
    {
        String sql = "SELECT * FROM Users WHERE username = ?";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement  preparedStatement = connection.prepareStatement (sql))
        {
             preparedStatement.setString (1, username);
            ResultSet  resultSet =  preparedStatement.executeQuery ();
            if  (resultSet.next ())
            {
                return new User (
                         resultSet.getObject ("ID", UUID.class),
                         resultSet.getObject ("channelID", UUID.class),
                         resultSet.getString ("firstName"),
                         resultSet.getString ("lastName"),
                         resultSet.getString ("username"),
                         resultSet.getString ("email"),
                         resultSet.getString ("password"),
                         resultSet.getDate ("birthdate").toLocalDate (),
                         resultSet.getDate ("joinDate").toLocalDate (),
                         resultSet.getBoolean ("isPremium"),
                         resultSet.getDouble ("balance"),
                         resultSet.getString ("profilePictureURL")
                );
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return null;
    }

    public User findByEmail (String email)
    {
        String sql = "SELECT * FROM Users WHERE email = ?";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement  preparedStatement = connection.prepareStatement (sql))
        {
             preparedStatement.setString (1, email);
            ResultSet  resultSet =  preparedStatement.executeQuery ();
            if  (resultSet.next ())
            {
                return new User (
                         resultSet.getObject ("ID", UUID.class),
                         resultSet.getObject ("channelID", UUID.class),
                         resultSet.getString ("firstName"),
                         resultSet.getString ("lastName"),
                         resultSet.getString ("username"),
                         resultSet.getString ("email"),
                         resultSet.getString ("password"),
                         resultSet.getDate ("birthdate").toLocalDate (),
                         resultSet.getDate ("joinDate").toLocalDate (),
                         resultSet.getBoolean ("isPremium"),
                         resultSet.getDouble ("balance"),
                         resultSet.getString ("profilePictureURL")
                );
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return null;
    }

    public boolean checkEmail (String email) {
        String query = "SELECT EXISTS (SELECT 1 FROM users WHERE email = ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean(1);
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkUsername (String username) {
        String query = "SELECT EXISTS (SELECT 1 FROM users WHERE username = ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean(1);
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean checkPassword (String username, String password) {
        String query = "SELECT password FROM users WHERE username = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return password.equals(resultSet.getString("password"));
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public List <User> findAll ()
    {
        List <User> users = new ArrayList <> ();
        String      sql   = "SELECT * FROM Users";
        try (Connection connection = DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ();
             ResultSet  resultSet = statement.executeQuery (sql))
        {
            while  (resultSet.next ())
            {
                users.add (new User (
                         resultSet.getObject ("ID", UUID.class),
                         resultSet.getObject ("channelID", UUID.class),
                         resultSet.getString ("firstName"),
                         resultSet.getString ("lastName"),
                         resultSet.getString ("username"),
                         resultSet.getString ("email"),
                         resultSet.getString ("password"),
                         resultSet.getDate ("birthdate").toLocalDate (),
                         resultSet.getDate ("joinDate").toLocalDate (),
                         resultSet.getBoolean ("isPremium"),
                         resultSet.getDouble ("balance"),
                         resultSet.getString ("profilePictureURL")
                ));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return users;
    }

    public ArrayList <UUID> findAllNotifs (UUID id)
    {
        ArrayList <UUID> notifsID = new ArrayList <> ();
        String           sql      = "SELECT * FROM Notifications";
        try (Connection connection= DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ();
             ResultSet resultSet= statement.executeQuery (sql))
        {
            while (resultSet.next ())
            {
                if (id.equals (resultSet.getObject ("userID", UUID.class)))
                {
                    notifsID.add (resultSet.getObject ("ID", UUID.class));
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return notifsID;
    }

    public ArrayList <UUID> findSubscribedChannels (UUID id)
    {
        ArrayList <UUID> channelsID = new ArrayList <> ();
        String         sql          = "SELECT * FROM Subscribers";
        try (Connection connection= DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ();
             ResultSet resultSet= statement.executeQuery (sql))
        {
            while (resultSet.next ())
            {
                if (id.equals (resultSet.getObject ("userID", UUID.class)))
                {
                    channelsID.add (resultSet.getObject ("channelID", UUID.class));
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return channelsID;
    }

    public ArrayList <UUID> findSubscribedPlaylists (UUID id)
    {
        ArrayList <UUID> playlistsID = new ArrayList <> ();
        String         sql           = "SELECT * FROM playlistSubscriptions";
        try (Connection connection= DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ();
             ResultSet resultSet= statement.executeQuery (sql))
        {
            while (resultSet.next ())
            {
                if (id.equals (resultSet.getObject ("userID", UUID.class)))
                {
                    playlistsID.add (resultSet.getObject ("playlistID", UUID.class));
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return playlistsID;
    }


}
