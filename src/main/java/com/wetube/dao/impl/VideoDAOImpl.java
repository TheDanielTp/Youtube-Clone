package com.wetube.dao.impl;

import com.wetube.model.Video;
import com.wetube.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VideoDAOImpl
{
    public void create (Video video)
    {
        String sql = "INSERT INTO Videos (ID, creatorID, channelID, communityID, title, description, dataType, videoURL, thumbnailURL, commentsCount, likesCount, dislikesCount, creationDate, isOnlyComrade) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, video.getID ());
            pstmt.setObject (2, video.getCreatorID ());
            pstmt.setObject (3, video.getChannelID ());
            pstmt.setObject (4, video.getCommunityID ());
            pstmt.setString (5, video.getTitle ());
            pstmt.setString (6, video.getDescription ());
            pstmt.setString (7, video.getDataType ());
            pstmt.setString (8, video.getVideoURL ());
            pstmt.setBytes (9, video.getThumbnailURL () != null ? video.getThumbnailURL ().toString ().getBytes () : null);
            pstmt.setInt (10, video.getCommentsCount ());
            pstmt.setInt (11, video.getLikesCount ());
            pstmt.setInt (12, video.getDislikesCount ());
            pstmt.setTimestamp (13, Timestamp.valueOf (video.getCreationDate ()));
            pstmt.setBoolean (14, video.isOnlyComrade ());
            pstmt.executeUpdate ();
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public Video findById (UUID id)
    {
        String sql = "SELECT * FROM Videos WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, id);
            ResultSet rs = pstmt.executeQuery ();
            if (rs.next ())
            {
                return new Video (
                        rs.getObject ("creatorID", UUID.class),
                        rs.getObject ("communityID", UUID.class),
                        rs.getObject ("channelID", UUID.class),
                        rs.getString ("title"),
                        rs.getString ("description"),
                        rs.getString ("videoURL"),
                        rs.getString ("thumbnailURL"),
                        rs.getBoolean ("isOnlyComrade")
                );
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return null;
    }

    public Video findByTitle (UUID id)
    {
        String sql = "SELECT * FROM Videos WHERE title = ?";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, id);
            ResultSet rs = pstmt.executeQuery ();
            if (rs.next ())
            {
                return new Video (
                        rs.getObject ("creatorID", UUID.class),
                        rs.getObject ("communityID", UUID.class),
                        rs.getObject ("channelID", UUID.class),
                        rs.getString ("title"),
                        rs.getString ("description"),
                        rs.getString ("videoURL"),
                        rs.getString ("thumbnailURL"),
                        rs.getBoolean ("isOnlyComrade")
                );
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return null;
    }

    public List <Video> findAll ()
    {
        List <Video> videos = new ArrayList <> ();
        String       sql    = "SELECT * FROM Videos";
        try (Connection conn = DatabaseConnection.getConnection ();
             Statement stmt = conn.createStatement ();
             ResultSet rs = stmt.executeQuery (sql))
        {
            while (rs.next ())
            {
                videos.add (new Video (
                        rs.getObject ("creatorID", UUID.class),
                        rs.getObject ("communityID", UUID.class),
                        rs.getObject ("channelID", UUID.class),
                        rs.getString ("title"),
                        rs.getString ("description"),
                        rs.getString ("videoURL"),
                        rs.getString ("thumbnailURL"),
                        rs.getBoolean ("isOnlyComrade")
                ));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return videos;
    }

    public void update (Video video)
    {
        String sql = "UPDATE Videos SET creatorID = ?, channelID = ?, communityID = ?, title = ?, description = ?, dataType = ?, videoURL = ?, thumbnail = ?, commentsCount = ?, likesCount = ?, dislikesCount = ?, creationDate = ?, isOnlyComrade = ? WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, video.getCreatorID ());
            pstmt.setObject (2, video.getChannelID ());
            pstmt.setObject (3, video.getCommunityID ());
            pstmt.setString (4, video.getTitle ());
            pstmt.setString (5, video.getDescription ());
            pstmt.setString (6, video.getDataType ());
            pstmt.setString (7, video.getVideoURL ());
            pstmt.setBytes (8, video.getThumbnailURL () != null ? video.getThumbnailURL ().toString ().getBytes () : null);
            pstmt.setInt (9, video.getCommentsCount ());
            pstmt.setInt (10, video.getLikesCount ());
            pstmt.setInt (11, video.getDislikesCount ());
            pstmt.setTimestamp (12, Timestamp.valueOf (video.getCreationDate ()));
            pstmt.setBoolean (13, video.isOnlyComrade ());
            pstmt.setObject (14, video.getID ());
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
            String deleteComments       = "DELETE FROM Comments WHERE contentID = '" + id + "'";
            String deleteVideoPlaylists = "DELETE FROM VideoPlaylists WHERE videoID = '" + id + "'";

            stmt.executeUpdate (deleteComments);
            stmt.executeUpdate (deleteVideoPlaylists);

            String deleteVideo = "DELETE FROM Videos WHERE ID = '" + id + "'";
            stmt.executeUpdate (deleteVideo);
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }
}
