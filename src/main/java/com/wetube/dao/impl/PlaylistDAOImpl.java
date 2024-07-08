package com.wetube.dao.impl;

import com.wetube.model.Playlist;
import com.wetube.model.User;
import com.wetube.model.Video;
import com.wetube.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlaylistDAOImpl
{
    public void create (Playlist playlist)
    {
        String sql = "INSERT INTO Playlists (ID, creatorID, channelID, title, description, isPublic, isOnlyComrade, creationDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, playlist.getID ());
            pstmt.setObject (2, playlist.getCreatorID ());
            pstmt.setObject (3, playlist.getChannelID ());
            pstmt.setString (5, playlist.getTitle ());
            pstmt.setString (6, playlist.getDescription ());
            pstmt.setBoolean (7, playlist.isPublic ());
            pstmt.setBoolean (8, playlist.isOnlyComrade ());
            pstmt.setTimestamp (9, Timestamp.valueOf (playlist.getCreationDate ()));
            pstmt.executeUpdate ();
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public void update (Playlist playlist)
    {
        String sql = "UPDATE Playlists SET creatorID = ?, channelID = ?, title = ?, description = ?, isPublic = ?, isOnlyComrade = ?, creationDate = ? WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, playlist.getCreatorID ());
            pstmt.setObject (2, playlist.getChannelID ());
            pstmt.setString (3, playlist.getTitle ());
            pstmt.setString (4, playlist.getDescription ());
            pstmt.setBoolean (5, playlist.isPublic ());
            pstmt.setBoolean (6, playlist.isOnlyComrade ());
            pstmt.setTimestamp (7, Timestamp.valueOf (playlist.getCreationDate ()));
            pstmt.setObject (8, playlist.getID ());
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
            String deletePlaylist = "DELETE FROM Playlists WHERE ID = '" + id + "'";
            stmt.executeUpdate (deletePlaylist);
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public Playlist findById (UUID id)
    {
        String sql = "SELECT * FROM Playlists WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, id);
            ResultSet rs = pstmt.executeQuery ();
            if (rs.next ())
            {
                return new Playlist (
                        rs.getObject ("ID", UUID.class),
                        rs.getObject ("creatorID", UUID.class),
                        rs.getObject ("channelID", UUID.class),
                        rs.getString ("title"),
                        rs.getString ("description"),
                        findVideos (id),
                        rs.getBoolean ("isPublic"),
                        rs.getBoolean ("isOnlyComrade"),
                        rs.getObject ("creationDate", LocalDateTime.class)
                );
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return null;
    }

    public List <Playlist> findAllUserPlaylists (User user)
    {
        List <Playlist> playlists = new ArrayList <> ();
        String      sql   = "SELECT * FROM Playlists";
        try (Connection conn = DatabaseConnection.getConnection ();
             Statement stmt = conn.createStatement ();
             ResultSet rs = stmt.executeQuery (sql))
        {
            while (rs.next ())
            {
                playlists.add (new Playlist (
                        rs.getObject ("ID", UUID.class),
                        rs.getObject ("creatorID", UUID.class),
                        rs.getObject ("channelID", UUID.class),
                        rs.getString ("title"),
                        rs.getString ("description"),
                        findVideos (rs.getObject ("ID", UUID.class)),
                        rs.getBoolean ("isPublic"),
                        rs.getBoolean ("isOnlyComrade"),
                        rs.getObject ("creationDate", LocalDateTime.class)
                ));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        List <Playlist> userPlaylists = new ArrayList <> ();
        for (Playlist playlist : playlists)
        {
            if (playlist.getCreatorID ().equals (user.getID ()))
            {
                userPlaylists.add (playlist);
            }
        }
        return userPlaylists;
    }

    public List <Playlist> findAll ()
    {
        List <Playlist> playlists = new ArrayList <> ();
        String      sql   = "SELECT * FROM Playlists";
        try (Connection conn = DatabaseConnection.getConnection ();
             Statement stmt = conn.createStatement ();
             ResultSet rs = stmt.executeQuery (sql))
        {
            while (rs.next ())
            {
                playlists.add (new Playlist (
                        rs.getObject ("ID", UUID.class),
                        rs.getObject ("creatorID", UUID.class),
                        rs.getObject ("channelID", UUID.class),
                        rs.getString ("title"),
                        rs.getString ("description"),
                        findVideos (rs.getObject ("ID", UUID.class)),
                        rs.getBoolean ("isPublic"),
                        rs.getBoolean ("isOnlyComrade"),
                        rs.getObject ("creationDate", LocalDateTime.class)
                ));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return playlists;
    }

    public void addVideo (Playlist playlist, Video video)
    {
        String sql = "INSERT INTO VideoPlaylists (playlistID, videoID) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, playlist.getID ());
            pstmt.setObject (2, video.getID ());
            pstmt.executeUpdate ();
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public void deleteVideo (Playlist playlist, Video video)
    {
        try (Connection conn = DatabaseConnection.getConnection ();
             Statement stmt = conn.createStatement ())
        {
            String deleteVideo = "DELETE FROM VideoPlaylists WHERE playlistID = '" + playlist.getID () + "' && videoID = '" + video.getID () + "'";
            stmt.executeUpdate (deleteVideo);
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public ArrayList <UUID> findVideos (UUID id)
    {
        ArrayList <UUID> videosID = new ArrayList <> ();
        String      sql   = "SELECT * FROM VideoPlaylists";
        try (Connection conn = DatabaseConnection.getConnection ();
             Statement stmt = conn.createStatement ();
             ResultSet rs = stmt.executeQuery (sql))
        {
            while (rs.next ())
            {
                if (rs.getObject ("playlistID", UUID.class).equals (id))
                {
                    videosID.add (rs.getObject ("videoID", UUID.class));
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return videosID;
    }
}
