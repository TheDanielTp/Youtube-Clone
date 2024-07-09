package com.wetube.dao.impl;

import com.wetube.model.Channel;
import com.wetube.model.Comment;
import com.wetube.model.User;
import com.wetube.util.DatabaseConnection;
import javafx.scene.image.Image;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChannelDAOImpl
{
    public UUID generateID ()
    {
        UUID uuid = UUID.randomUUID ();

        List <Channel> all = findAll ();
        for (Channel object : all)
        {
            if (object.getID () == uuid)
            {
                uuid = generateID ();
            }
        }
        System.out.println ("> Database: ID generated");
        return uuid;
    }

    public void create (Channel channel)
    {
        String sql = "INSERT INTO Channels (ID, userID, name, description, subscribersCount, totalVideos, totalViews, watchTime, creationDate, isVerified, outcome, channelPictureURL) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, channel.getID ());
            pstmt.setObject (2, channel.getUserID ());
            pstmt.setString (3, channel.getName ());
            pstmt.setString (4, channel.getDescription ());
            pstmt.setInt (5, channel.getSubscribersCount ());
            pstmt.setInt (6, channel.getTotalVideos ());
            pstmt.setInt (7, channel.getTotalViews ());
            pstmt.setInt (8, channel.getWatchTime ());
            pstmt.setDate (9, Date.valueOf (channel.getCreationDate ()));
            pstmt.setBoolean (10, channel.isVerified ());
            pstmt.setDouble (11, channel.getOutcome ());
            pstmt.setBytes (12, channel.getChannelPictureURL () != null ? channel.getChannelPictureURL ().toString ().getBytes () : null);
            pstmt.executeUpdate ();
            System.out.println ("> Database: channel created");
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to create channel");
        }
    }

    public void update (Channel channel)
    {
        String sql = "UPDATE Channels SET userID = ?, name = ?, description = ?, subscribersCount = ?, totalVideos = ?, totalViews = ?, watchTime = ?, creationDate = ?, isVerified = ?, outcome = ?, channelPictureURL = ? WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, channel.getUserID ());
            pstmt.setString (2, channel.getName ());
            pstmt.setString (3, channel.getDescription ());
            pstmt.setInt (4, channel.getSubscribersCount ());
            pstmt.setInt (5, channel.getTotalVideos ());
            pstmt.setInt (6, channel.getTotalViews ());
            pstmt.setInt (7, channel.getWatchTime ());
            pstmt.setDate (8, Date.valueOf (channel.getCreationDate ()));
            pstmt.setBoolean (9, channel.isVerified ());
            pstmt.setDouble (10, channel.getOutcome ());
            pstmt.setBytes (11, channel.getChannelPictureURL () != null ? channel.getChannelPictureURL ().toString ().getBytes () : null);
            pstmt.setObject (12, channel.getID ());
            pstmt.executeUpdate ();
            System.out.println ("> Database: channel updated");
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to update channel");
        }
    }

    public void delete (UUID id)
    {
        try (Connection conn = DatabaseConnection.getConnection ();
             Statement stmt = conn.createStatement ())
        {
            String deleteVideos      = "DELETE FROM Videos WHERE channelID = '" + id + "'";
            String deletePlaylists   = "DELETE FROM Playlists WHERE channelID = '" + id + "'";
            String deleteComments    = "DELETE FROM Comments WHERE channelID = '" + id + "'";
            String deleteSubscribers = "DELETE FROM Subscribers WHERE channelID = '" + id + "'";

            stmt.executeUpdate (deleteSubscribers);
            stmt.executeUpdate (deletePlaylists);
            stmt.executeUpdate (deleteVideos);
            stmt.executeUpdate (deleteComments);

            String deleteChannel = "DELETE FROM Channels WHERE ID = '" + id + "'";
            stmt.executeUpdate (deleteChannel);
            System.out.println ("> Database: channel deleted");
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to delete channel");
        }
    }

    public void subscribe (User user, Channel channel)
    {
        String sql = "INSERT INTO Subscribers (userID, channelID) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, user.getID ());
            pstmt.setObject (2, channel.getID ());
            pstmt.executeUpdate ();
            System.out.println ("> Database: channel subscribed");
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to subscribe channel");
        }
    }

    public void unsubscribe (User user, Channel channel)
    {
        try (Connection conn = DatabaseConnection.getConnection ();
             Statement stmt = conn.createStatement ())
        {
            String unsubscribe = "DELETE FROM Subscribers WHERE userID = '" + user.getID () + "' && channelID = '" + channel.getID () + "'";
            stmt.executeUpdate (unsubscribe);
            System.out.println ("> Database: channel unsubscribed");
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to unsubscribe channel");
        }
    }

    public Channel findById (UUID id)
    {
        String sql = "SELECT * FROM Channels WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, id);
            ResultSet rs = pstmt.executeQuery ();
            if (rs.next ())
            {
                System.out.println ("> Database: channel found by ID");
                return new Channel (
                        rs.getObject ("ID", UUID.class),
                        rs.getObject ("userID", UUID.class),
                        rs.getString ("name"),
                        rs.getString ("description"),
                        rs.getInt ("subscribersCount"),
                        rs.getInt ("totalVideos"),
                        rs.getInt ("totalViews"),
                        rs.getInt ("watchTime"),
                        findSubscribers (id),
                        rs.getObject ("creationDate", LocalDate.class),
                        rs.getBoolean ("isVerified"),
                        rs.getDouble ("outcome"),
                        rs.getString ("channelPictureURL")
                        );
            }
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to find channel");
        }
        return null;
    }

    public Channel findByName (String name)
    {
        String sql = "SELECT * FROM Channels WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, name);
            ResultSet rs = pstmt.executeQuery ();
            if (rs.next ())
            {
                System.out.println ("> Database: channel found by name");
                return new Channel (
                        rs.getObject ("ID", UUID.class),
                        rs.getObject ("userID", UUID.class),
                        rs.getString ("name"),
                        rs.getString ("description"),
                        rs.getInt ("subscribersCount"),
                        rs.getInt ("totalVideos"),
                        rs.getInt ("totalViews"),
                        rs.getInt ("watchTime"),
                        findSubscribers (rs.getObject ("ID", UUID.class)),
                        rs.getObject ("creationDate", LocalDate.class),
                        rs.getBoolean ("isVerified"),
                        rs.getDouble ("outcome"),
                        rs.getString ("channelPictureURL")
                );
            }
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to find channel");
        }
        return null;
    }

    public ArrayList <UUID> findSubscribers (UUID id)
    {
        ArrayList <UUID> subscribersID = new ArrayList <> ();
        String         sql      = "SELECT * FROM Subscribers";
        try (Connection conn = DatabaseConnection.getConnection ();
             Statement stmt = conn.createStatement ();
             ResultSet rs = stmt.executeQuery (sql))
        {
            while (rs.next ())
            {
                if (id.equals (rs.getObject ("channelID", UUID.class)))
                {
                    subscribersID.add (rs.getObject ("userID", UUID.class));
                }
            }
            System.out.println ("> Database: subscribers found");
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to find subscribers");
        }
        return subscribersID;
    }

    public List <Channel> findAll ()
    {
        List <Channel> channels = new ArrayList <> ();
        String         sql      = "SELECT * FROM Channels";
        try (Connection conn = DatabaseConnection.getConnection ();
             Statement stmt = conn.createStatement ();
             ResultSet rs = stmt.executeQuery (sql))
        {
            while (rs.next ())
            {
                channels.add (new Channel (
                        rs.getObject ("ID", UUID.class),
                        rs.getObject ("userID", UUID.class),
                        rs.getString ("name"),
                        rs.getString ("description"),
                        rs.getInt ("subscribersCount"),
                        rs.getInt ("totalVideos"),
                        rs.getInt ("totalViews"),
                        rs.getInt ("watchTime"),
                        findSubscribers (rs.getObject ("ID", UUID.class)),
                        rs.getObject ("creationDate", LocalDate.class),
                        rs.getBoolean ("isVerified"),
                        rs.getDouble ("outcome"),
                        rs.getString ("channelPictureURL")
                ));
            }
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to find channels");
        }
        return channels;
    }
}
