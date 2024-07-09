package com.wetube.dao.impl;

import com.wetube.model.Playlist;
import com.wetube.model.Post;
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
    public UUID generateID ()
    {
        UUID uuid = UUID.randomUUID ();

        List <Playlist> all = findAll ();
        for (Playlist object : all)
        {
            if (object.getID () == uuid)
            {
                uuid = generateID ();
                break;
            }
        }
        return uuid;
    }

    public void create (Playlist playlist)
    {
        String sql = "INSERT INTO Playlists (ID, creatorID, channelID, title, description, isPublic, isOnlyComrade, creationDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement  preparedStatement = connection.prepareStatement (sql))
        {
             preparedStatement.setObject (1, playlist.getID ());
             preparedStatement.setObject (2, playlist.getCreatorID ());
             preparedStatement.setObject (3, playlist.getChannelID ());
             preparedStatement.setString (5, playlist.getTitle ());
             preparedStatement.setString (6, playlist.getDescription ());
             preparedStatement.setBoolean (7, playlist.isPublic ());
             preparedStatement.setBoolean (8, playlist.isOnlyComrade ());
             preparedStatement.setTimestamp (9, Timestamp.valueOf (playlist.getCreationDate ()));
             preparedStatement.executeUpdate ();
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public void update (Playlist playlist)
    {
        String sql = "UPDATE Playlists SET creatorID = ?, channelID = ?, title = ?, description = ?, isPublic = ?, isOnlyComrade = ?, creationDate = ? WHERE ID = ?";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement  preparedStatement = connection.prepareStatement (sql))
        {
             preparedStatement.setObject (1, playlist.getCreatorID ());
             preparedStatement.setObject (2, playlist.getChannelID ());
             preparedStatement.setString (3, playlist.getTitle ());
             preparedStatement.setString (4, playlist.getDescription ());
             preparedStatement.setBoolean (5, playlist.isPublic ());
             preparedStatement.setBoolean (6, playlist.isOnlyComrade ());
             preparedStatement.setTimestamp (7, Timestamp.valueOf (playlist.getCreationDate ()));
             preparedStatement.setObject (8, playlist.getID ());
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
            String deletePlaylist = "DELETE FROM Playlists WHERE ID = '" + id + "'";
            statement.executeUpdate (deletePlaylist);
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public Playlist findById (UUID id)
    {
        String sql = "SELECT * FROM Playlists WHERE ID = ?";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement  preparedStatement = connection.prepareStatement (sql))
        {
             preparedStatement.setObject (1, id);
            ResultSet  resultSet =  preparedStatement.executeQuery ();
            if  (resultSet.next ())
            {
                return new Playlist (
                         resultSet.getObject ("ID", UUID.class),
                         resultSet.getObject ("creatorID", UUID.class),
                         resultSet.getObject ("channelID", UUID.class),
                         resultSet.getString ("title"),
                         resultSet.getString ("description"),
                        findVideos (id),
                         resultSet.getBoolean ("isPublic"),
                         resultSet.getBoolean ("isOnlyComrade"),
                         resultSet.getObject ("creationDate", LocalDateTime.class)
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
        try (Connection connection = DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ();
             ResultSet  resultSet = statement.executeQuery (sql))
        {
            while  (resultSet.next ())
            {
                playlists.add (new Playlist (
                         resultSet.getObject ("ID", UUID.class),
                         resultSet.getObject ("creatorID", UUID.class),
                         resultSet.getObject ("channelID", UUID.class),
                         resultSet.getString ("title"),
                         resultSet.getString ("description"),
                        findVideos  (resultSet.getObject ("ID", UUID.class)),
                         resultSet.getBoolean ("isPublic"),
                         resultSet.getBoolean ("isOnlyComrade"),
                         resultSet.getObject ("creationDate", LocalDateTime.class)
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
        try (Connection connection = DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ();
             ResultSet  resultSet = statement.executeQuery (sql))
        {
            while  (resultSet.next ())
            {
                playlists.add (new Playlist (
                         resultSet.getObject ("ID", UUID.class),
                         resultSet.getObject ("creatorID", UUID.class),
                         resultSet.getObject ("channelID", UUID.class),
                         resultSet.getString ("title"),
                         resultSet.getString ("description"),
                        findVideos  (resultSet.getObject ("ID", UUID.class)),
                         resultSet.getBoolean ("isPublic"),
                         resultSet.getBoolean ("isOnlyComrade"),
                         resultSet.getObject ("creationDate", LocalDateTime.class)
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
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement  preparedStatement = connection.prepareStatement (sql))
        {
             preparedStatement.setObject (1, playlist.getID ());
             preparedStatement.setObject (2, video.getID ());
             preparedStatement.executeUpdate ();
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public void deleteVideo (Playlist playlist, Video video)
    {
        try (Connection connection = DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ())
        {
            String deleteVideo = "DELETE FROM VideoPlaylists WHERE playlistID = '" + playlist.getID () + "' && videoID = '" + video.getID () + "'";
            statement.executeUpdate (deleteVideo);
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
        try (Connection connection = DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ();
             ResultSet  resultSet = statement.executeQuery (sql))
        {
            while  (resultSet.next ())
            {
                if  (resultSet.getObject ("playlistID", UUID.class).equals (id))
                {
                    videosID.add  (resultSet.getObject ("videoID", UUID.class));
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
