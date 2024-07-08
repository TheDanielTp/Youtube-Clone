package com.wetube.dao.impl;

import com.wetube.model.User;
import com.wetube.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDAOImpl
{
    public void create (User user)
    {
        String sql = "INSERT INTO Users (ID, channelID, firstName, lastName, username, email, password, birthdate, joinDate, isPremium, balance, profilePictureURL) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, user.getID ());
            pstmt.setObject (2, user.getChannelID ());
            pstmt.setString (3, user.getFirstName ());
            pstmt.setString (4, user.getLastName ());
            pstmt.setString (5, user.getUsername ());
            pstmt.setString (6, user.getEmail ());
            pstmt.setString (7, user.getPassword ());
            pstmt.setDate (8, Date.valueOf (user.getBirthdate ()));
            pstmt.setDate (9, Date.valueOf (user.getJoinDate ()));
            pstmt.setBoolean (10, user.isPremium ());
            pstmt.setDouble (11, user.getBalance ());
            pstmt.setString (12, user.getProfilePictureURL ());
            pstmt.executeUpdate ();
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public void update (User user)
    {
        String sql = "UPDATE Users SET channelID = ?, firstName = ?, lastName = ?, username = ?, email = ?, password = ?, birthdate = ?, joinDate = ?, isPremium = ?, balance = ?, profilePictureURL = ? WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, user.getChannelID ());
            pstmt.setString (2, user.getFirstName ());
            pstmt.setString (3, user.getLastName ());
            pstmt.setString (4, user.getUsername ());
            pstmt.setString (5, user.getEmail ());
            pstmt.setString (6, user.getPassword ());
            pstmt.setDate (7, Date.valueOf (user.getBirthdate ()));
            pstmt.setDate (8, Date.valueOf (user.getJoinDate ()));
            pstmt.setBoolean (9, user.isPremium ());
            pstmt.setDouble (10, user.getBalance ());
            pstmt.setString (11, user.getProfilePictureURL ());
            pstmt.setObject (12, user.getID ());
            pstmt.executeUpdate ();
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public void delete (UUID id)
    {
        try (Connection conn = DatabaseConnection.getConnection ();
             Statement stmt = conn.createStatement ())
        {
            String deleteComments      = "DELETE FROM Comments WHERE creatorID = '" + id + "'";
            String deleteVideos        = "DELETE FROM Videos WHERE creatorID = '" + id + "'";
            String deletePlaylists     = "DELETE FROM Playlists WHERE creatorID = '" + id + "'";
            String deleteNotifications = "DELETE FROM Notifications WHERE userID = '" + id + "'";
            String deleteChannels      = "DELETE FROM Channels WHERE userID = '" + id + "'";
            String deleteSubscribers   = "DELETE FROM Subscribers WHERE userID = '" + id + "' OR channelID IN (SELECT ID FROM Channels WHERE userID = '" + id + "')";

            stmt.executeUpdate (deleteSubscribers);
            stmt.executeUpdate (deleteNotifications);
            stmt.executeUpdate (deletePlaylists);
            stmt.executeUpdate (deleteVideos);
            stmt.executeUpdate (deleteComments);
            stmt.executeUpdate (deleteChannels);

            String deleteUser = "DELETE FROM Users WHERE ID = '" + id + "'";
            stmt.executeUpdate (deleteUser);
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public User findById (UUID id)
    {
        String sql = "SELECT * FROM Users WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, id);
            ResultSet rs = pstmt.executeQuery ();
            if (rs.next ())
            {
                return new User (
                        rs.getObject ("ID", UUID.class),
                        rs.getObject ("channelID", UUID.class),
                        rs.getString ("firstName"),
                        rs.getString ("lastName"),
                        rs.getString ("username"),
                        rs.getString ("email"),
                        rs.getString ("password"),
                        rs.getDate ("birthdate").toLocalDate (),
                        rs.getDate ("joinDate").toLocalDate (),
                        rs.getBoolean ("isPremium"),
                        rs.getDouble ("balance"),
                        rs.getString ("profilePictureURL")
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
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setString (1, username);
            ResultSet rs = pstmt.executeQuery ();
            if (rs.next ())
            {
                return new User (
                        rs.getObject ("ID", UUID.class),
                        rs.getObject ("channelID", UUID.class),
                        rs.getString ("firstName"),
                        rs.getString ("lastName"),
                        rs.getString ("username"),
                        rs.getString ("email"),
                        rs.getString ("password"),
                        rs.getDate ("birthdate").toLocalDate (),
                        rs.getDate ("joinDate").toLocalDate (),
                        rs.getBoolean ("isPremium"),
                        rs.getDouble ("balance"),
                        rs.getString ("profilePictureURL")
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
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setString (1, email);
            ResultSet rs = pstmt.executeQuery ();
            if (rs.next ())
            {
                return new User (
                        rs.getObject ("ID", UUID.class),
                        rs.getObject ("channelID", UUID.class),
                        rs.getString ("firstName"),
                        rs.getString ("lastName"),
                        rs.getString ("username"),
                        rs.getString ("email"),
                        rs.getString ("password"),
                        rs.getDate ("birthdate").toLocalDate (),
                        rs.getDate ("joinDate").toLocalDate (),
                        rs.getBoolean ("isPremium"),
                        rs.getDouble ("balance"),
                        rs.getString ("profilePictureURL")
                );
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return null;
    }

    public List <User> findAll ()
    {
        List <User> users = new ArrayList <> ();
        String      sql   = "SELECT * FROM Users";
        try (Connection conn = DatabaseConnection.getConnection ();
             Statement stmt = conn.createStatement ();
             ResultSet rs = stmt.executeQuery (sql))
        {
            while (rs.next ())
            {
                users.add (new User (
                        rs.getObject ("ID", UUID.class),
                        rs.getObject ("channelID", UUID.class),
                        rs.getString ("firstName"),
                        rs.getString ("lastName"),
                        rs.getString ("username"),
                        rs.getString ("email"),
                        rs.getString ("password"),
                        rs.getDate ("birthdate").toLocalDate (),
                        rs.getDate ("joinDate").toLocalDate (),
                        rs.getBoolean ("isPremium"),
                        rs.getDouble ("balance"),
                        rs.getString ("profilePictureURL")
                ));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return users;
    }
}
