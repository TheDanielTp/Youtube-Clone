package com.wetube.dao.impl;

import com.wetube.model.Channel;
import com.wetube.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChannelDAOImpl
{

    public void create (Channel channel)
    {
        String sql = "INSERT INTO Channels (ID, userID, name, description, subscribersCount, totalVideos, totalViews, watchTime, creationDate, isVerified, outcome, channelPicture) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
            pstmt.setBytes (12, channel.getChannelPicture () != null ? channel.getChannelPicture ().toString ().getBytes () : null);
            pstmt.executeUpdate ();
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
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
                return new Channel (
                        rs.getString ("name"),
                        rs.getString ("description")
                );
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return null;
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
                        rs.getString ("name"),
                        rs.getString ("description")
                ));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return channels;
    }

    public void update (Channel channel)
    {
        String sql = "UPDATE Channels SET userID = ?, name = ?, description = ?, subscribersCount = ?, totalVideos = ?, totalViews = ?, watchTime = ?, creationDate = ?, isVerified = ?, outcome = ?, channelPicture = ? WHERE ID = ?";
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
            pstmt.setBytes (11, channel.getChannelPicture () != null ? channel.getChannelPicture ().toString ().getBytes () : null);
            pstmt.setObject (12, channel.getID ());
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
            // Delete related records from other tables
            String deleteVideos      = "DELETE FROM Videos WHERE channelID = '" + id + "'";
            String deletePlaylists   = "DELETE FROM Playlists WHERE channelID = '" + id + "'";
            String deleteComments    = "DELETE FROM Comments WHERE channelID = '" + id + "'";
            String deleteSubscribers = "DELETE FROM Subscribers WHERE channelID = '" + id + "'";

            // Execute deletion in reverse order of dependencies
            stmt.executeUpdate (deleteSubscribers);
            stmt.executeUpdate (deletePlaylists);
            stmt.executeUpdate (deleteVideos);
            stmt.executeUpdate (deleteComments);

            // Delete the channel
            String deleteChannel = "DELETE FROM Channels WHERE ID = '" + id + "'";
            stmt.executeUpdate (deleteChannel);

        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }
}
