package com.wetube.dao.impl;

import com.wetube.model.*;
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
             PreparedStatement preparedStatement = connection.prepareStatement (sql))
        {
            preparedStatement.setObject (1, playlist.getID ());
            preparedStatement.setObject (2, playlist.getCreatorID ());
            preparedStatement.setObject (3, playlist.getChannelID ());
            preparedStatement.setString (4, playlist.getTitle ());
            preparedStatement.setString (5, playlist.getDescription ());
            preparedStatement.setBoolean (6, playlist.isPublic ());
            preparedStatement.setBoolean (7, playlist.isOnlyComrade ());
            preparedStatement.setTimestamp (8, Timestamp.valueOf (playlist.getCreationDate ()));
            preparedStatement.executeUpdate ();
            System.out.println ("> Database: playlist created");
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to create playlist");
            System.out.println (e.getMessage ());
        }
    }

    public void update (Playlist playlist)
    {
        String sql = "UPDATE Playlists SET creatorID = ?, channelID = ?, title = ?, description = ?, isPublic = ?, isOnlyComrade = ?, creationDate = ? WHERE ID = ?";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement preparedStatement = connection.prepareStatement (sql))
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
            System.out.println ("> Database: playlist updated");
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to update playlist");
            System.out.println (e.getMessage ());
        }
    }

    public void delete (UUID id)
    {
        try (Connection connection = DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ())
        {
            String deletePlaylist = "DELETE FROM Playlists WHERE ID = '" + id + "'";
            statement.executeUpdate (deletePlaylist);
            System.out.println ("> Database: playlist deleted");
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to delete playlist");
            System.out.println (e.getMessage ());
        }
    }

    public void subscribe (User user, Playlist playlist)
    {
        String sql = "INSERT INTO PlaylistSubscribers (userID, playlistID) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement preparedStatement = connection.prepareStatement (sql))
        {
            preparedStatement.setObject (1, user.getID ());
            preparedStatement.setObject (2, playlist.getID ());
            preparedStatement.executeUpdate ();
            System.out.println ("> Database: playlist subscribed");
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to subscribe playlist");
            System.out.println (e.getMessage ());
        }
    }

    public void unsubscribe (User user, Playlist playlist)
    {
        try (Connection connection = DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ())
        {
            String unsubscribe = "DELETE FROM PlaylistSubscriber WHERE userID = '" + user.getID () +
                    "' && playlistID = '" + playlist.getID () + "'";
            statement.executeUpdate (unsubscribe);
            System.out.println ("> Database: playlist unsubscribed");
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to unsubscribe playlist");
            System.out.println (e.getMessage ());
        }
    }

    public Playlist findById (UUID id)
    {
        String sql = "SELECT * FROM Playlists WHERE ID = ?";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement preparedStatement = connection.prepareStatement (sql))
        {
            preparedStatement.setObject (1, id);
            ResultSet resultSet = preparedStatement.executeQuery ();
            if (resultSet.next ())
            {
                System.out.println ("> Database: playlist found");
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
            System.out.println ("> Database: failed to find playlist");
            System.out.println (e.getMessage ());
        }
        return null;
    }

    public Playlist findByNameUser (User user, String title)
    {
        String sql = "SELECT * FROM Playlists WHERE creatorID = ? AND title = ?";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement preparedStatement = connection.prepareStatement (sql))
        {
            preparedStatement.setObject (1, user.getID ());
            preparedStatement.setObject (2, title);
            ResultSet resultSet = preparedStatement.executeQuery ();
            if (resultSet.next ())
            {
                System.out.println ("> Database: playlist found");
                return new Playlist (
                        resultSet.getObject ("ID", UUID.class),
                        resultSet.getObject ("creatorID", UUID.class),
                        resultSet.getObject ("channelID", UUID.class),
                        resultSet.getString ("title"),
                        resultSet.getString ("description"),
                        findVideos (resultSet.getObject ("ID", UUID.class)),
                        resultSet.getBoolean ("isPublic"),
                        resultSet.getBoolean ("isOnlyComrade"),
                        resultSet.getObject ("creationDate", LocalDateTime.class)
                );
            }
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to find playlist");
            System.out.println (e.getMessage ());
        }
        return null;
    }

    public List <Playlist> findAllUserPlaylists (User user)
    {
        List <Playlist> playlists = new ArrayList <> ();
        String          sql       = "SELECT * FROM Playlists";
        try (Connection connection = DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ();
             ResultSet resultSet = statement.executeQuery (sql))
        {
            while (resultSet.next ())
            {
                playlists.add (new Playlist (
                        resultSet.getObject ("ID", UUID.class),
                        resultSet.getObject ("creatorID", UUID.class),
                        resultSet.getObject ("channelID", UUID.class),
                        resultSet.getString ("title"),
                        resultSet.getString ("description"),
                        findVideos (resultSet.getObject ("ID", UUID.class)),
                        resultSet.getBoolean ("isPublic"),
                        resultSet.getBoolean ("isOnlyComrade"),
                        resultSet.getObject ("creationDate", LocalDateTime.class)
                ));
            }
            System.out.println ("> Database: playlists found");
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to find playlists");
            System.out.println (e.getMessage ());
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

    public Boolean checkSubscription (UUID playlistID, UUID userID)
    {
        String sql = "SELECT * FROM playlistSubscribers";
        try (Connection connection = DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ();
             ResultSet resultSet = statement.executeQuery (sql))
        {
            while (resultSet.next ())
            {
                System.out.println ("> Database: playlist subscription checked");
                if (playlistID.equals (resultSet.getObject ("playlistID", UUID.class)) &&
                        userID.equals (resultSet.getObject ("userID", UUID.class)))
                {
                    return true;
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to check playlist subscription");
            System.out.println (e.getMessage ());
        }
        return false;
    }

    public List <Playlist> findAll ()
    {
        List <Playlist> playlists = new ArrayList <> ();
        String          sql       = "SELECT * FROM Playlists";
        try (Connection connection = DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ();
             ResultSet resultSet = statement.executeQuery (sql))
        {
            while (resultSet.next ())
            {
                playlists.add (new Playlist (
                        resultSet.getObject ("ID", UUID.class),
                        resultSet.getObject ("creatorID", UUID.class),
                        resultSet.getObject ("channelID", UUID.class),
                        resultSet.getString ("title"),
                        resultSet.getString ("description"),
                        findVideos (resultSet.getObject ("ID", UUID.class)),
                        resultSet.getBoolean ("isPublic"),
                        resultSet.getBoolean ("isOnlyComrade"),
                        resultSet.getObject ("creationDate", LocalDateTime.class)
                ));
            }
            System.out.println ("> Database: playlists found");
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to find playlists");
            System.out.println (e.getMessage ());
        }
        return playlists;
    }

    public ArrayList <UUID> findSubscribers (UUID id)
    {
        ArrayList <UUID> subscribersID = new ArrayList <> ();
        String           sql           = "SELECT * FROM PlaylistSubscribers";
        try (Connection connection = DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ();
             ResultSet resultSet = statement.executeQuery (sql))
        {
            while (resultSet.next ())
            {
                if (id.equals (resultSet.getObject ("playlistID", UUID.class)))
                {
                    subscribersID.add (resultSet.getObject ("userID", UUID.class));
                }
            }
            System.out.println ("> Database: playlist subscribers found");
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to find playlist subscribers");
            System.out.println (e.getMessage ());
        }
        return subscribersID;
    }

    public void addVideo (Playlist playlist, Video video)
    {
        String sql = "INSERT INTO VideoPlaylists (playlistID, videoID) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement preparedStatement = connection.prepareStatement (sql))
        {
            preparedStatement.setObject (1, playlist.getID ());
            preparedStatement.setObject (2, video.getID ());
            preparedStatement.executeUpdate ();
            System.out.println ("> Database: video added to playlist");
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to add video to playlist");
            System.out.println (e.getMessage ());
        }
    }

    public void deleteVideo (Playlist playlist, Video video)
    {
        try (Connection connection = DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ())
        {
            String deleteVideo = "DELETE FROM VideoPlaylists WHERE playlistID = '" + playlist.getID () +
                    "' && videoID = '" + video.getID () + "'";
            statement.executeUpdate (deleteVideo);
            System.out.println ("> Database: video deleted from playlist");
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to delete video from playlist");
            System.out.println (e.getMessage ());
        }
    }

    public ArrayList <UUID> findVideos (UUID id)
    {
        ArrayList <UUID> videosID = new ArrayList <> ();
        String           sql      = "SELECT * FROM VideoPlaylists";
        try (Connection connection = DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ();
             ResultSet resultSet = statement.executeQuery (sql))
        {
            while (resultSet.next ())
            {
                if (resultSet.getObject ("playlistID", UUID.class).equals (id))
                {
                    videosID.add (resultSet.getObject ("videoID", UUID.class));
                }
            }
            System.out.println ("> Database: playlist videos found");
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to find playlist videos ");
            System.out.println (e.getMessage ());
        }
        return videosID;
    }
}
