package com.wetube.dao.impl;

import com.wetube.model.*;
import com.wetube.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VideoDAOImpl
{
    public UUID generateID ()
    {
        UUID uuid = UUID.randomUUID ();

        List <Content> all = findAllContents ();
        for (Content object : all)
        {
            if (object.getID () == uuid)
            {
                uuid = generateID ();
                break;
            }
        }
        return uuid;
    }

    public void create (Video video)
    {
        String sql = "INSERT INTO Videos (ID, creatorID, channelID, categoryID, title, description, dataType," +
                " videoURL, thumbnailURL, commentsCount, likesCount, dislikesCount, creationDate, isOnlyComrade)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement preparedStatement = connection.prepareStatement (sql))
        {
            preparedStatement.setObject (1, video.getID ());
            preparedStatement.setObject (2, video.getCreatorID ());
            preparedStatement.setObject (3, video.getChannelID ());
            preparedStatement.setObject (4, video.getCategoryID ());
            preparedStatement.setString (5, video.getTitle ());
            preparedStatement.setString (6, video.getDescription ());
            preparedStatement.setString (7, video.getDataType ());
            preparedStatement.setString (8, video.getVideoURL ());
            preparedStatement.setBytes (9, video.getThumbnailURL () != null ?
                    video.getThumbnailURL ().toString ().getBytes () : null);
            preparedStatement.setInt (10, video.getCommentsCount ());
            preparedStatement.setInt (11, video.getLikesCount ());
            preparedStatement.setInt (12, video.getDislikesCount ());
            preparedStatement.setTimestamp (13, Timestamp.valueOf (video.getCreationDate ()));
            preparedStatement.setBoolean (14, video.isOnlyComrade ());
            preparedStatement.executeUpdate ();
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public void update (Video video)
    {
        String sql = "UPDATE Videos SET creatorID = ?, channelID = ?, communityID = ?, title = ?, description = ?," +
                " dataType = ?, videoURL = ?, thumbnail = ?, commentsCount = ?, likesCount = ?, dislikesCount = ?," +
                " creationDate = ?, isOnlyComrade = ? WHERE ID = ?";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement preparedStatement = connection.prepareStatement (sql))
        {
            preparedStatement.setObject (1, video.getCreatorID ());
            preparedStatement.setObject (2, video.getChannelID ());
            preparedStatement.setObject (3, video.getCategoryID ());
            preparedStatement.setString (4, video.getTitle ());
            preparedStatement.setString (5, video.getDescription ());
            preparedStatement.setString (6, video.getDataType ());
            preparedStatement.setString (7, video.getVideoURL ());
            preparedStatement.setBytes (8, video.getThumbnailURL () != null ?
                    video.getThumbnailURL ().toString ().getBytes () : null);
            preparedStatement.setInt (9, video.getCommentsCount ());
            preparedStatement.setInt (10, video.getLikesCount ());
            preparedStatement.setInt (11, video.getDislikesCount ());
            preparedStatement.setTimestamp (12, Timestamp.valueOf (video.getCreationDate ()));
            preparedStatement.setBoolean (13, video.isOnlyComrade ());
            preparedStatement.setObject (14, video.getID ());
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
            String deleteComments       = "DELETE FROM Comments WHERE contentID = '" + id + "'";
            String deleteVideoPlaylists = "DELETE FROM VideoPlaylists WHERE videoID = '" + id + "'";
            statement.executeUpdate (deleteComments);
            statement.executeUpdate (deleteVideoPlaylists);

            String deleteVideo = "DELETE FROM Videos WHERE ID = '" + id + "'";
            statement.executeUpdate (deleteVideo);
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public void like (Video video, User user)
    {
        String sql = "INSERT INTO ContentsAction (contentID, userID, liked, disliked) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement preparedStatement = connection.prepareStatement (sql))
        {
            preparedStatement.setObject (1, video.getID ());
            preparedStatement.setObject (2, user.getID ());
            preparedStatement.setBoolean (3, true);
            preparedStatement.setBoolean (4, false);
            preparedStatement.executeUpdate ();
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public void dislike (Video video, User user)
    {
        String sql = "INSERT INTO ContentsAction (contentID, userID, liked, disliked) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement preparedStatement = connection.prepareStatement (sql))
        {
            preparedStatement.setObject (1, video.getID ());
            preparedStatement.setObject (2, user.getID ());
            preparedStatement.setBoolean (3, false);
            preparedStatement.setBoolean (4, true);
            preparedStatement.executeUpdate ();
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public void removeLikeDislike (Video video, User user)
    {
        String sql = "INSERT INTO ContentsAction (contentID, userID, liked, disliked) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement preparedStatement = connection.prepareStatement (sql))
        {
            preparedStatement.setObject (1, video.getID ());
            preparedStatement.setObject (2, user.getID ());
            preparedStatement.setBoolean (3, false);
            preparedStatement.setBoolean (4, false);
            preparedStatement.executeUpdate ();
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public Video findById (UUID id)
    {
        String sql = "SELECT * FROM Videos WHERE ID = ?";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement preparedStatement = connection.prepareStatement (sql))
        {
            preparedStatement.setObject (1, id);
            ResultSet resultSet = preparedStatement.executeQuery ();
            if (resultSet.next ())
            {
                return new Video (
                        resultSet.getObject ("ID", UUID.class),
                        resultSet.getObject ("creatorID", UUID.class),
                        resultSet.getObject ("categoryID", UUID.class),
                        resultSet.getObject ("channelID", UUID.class),
                        resultSet.getInt ("likesCount"),
                        resultSet.getInt ("dislikesCount"),
                        resultSet.getObject ("creationDate", LocalDateTime.class),
                        resultSet.getBoolean ("isOnlyComrade"),
                        resultSet.getString ("title"),
                        resultSet.getString ("description"),
                        resultSet.getString ("dataType"),
                        resultSet.getString ("videoURL"),
                        resultSet.getString ("thumbnailURL"),
                        resultSet.getInt ("commentsCount"),
                        resultSet.getInt ("viewsCount")
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
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement preparedStatement = connection.prepareStatement (sql))
        {
            preparedStatement.setObject (1, id);
            ResultSet resultSet = preparedStatement.executeQuery ();
            if (resultSet.next ())
            {
                return new Video (
                        resultSet.getObject ("ID", UUID.class),
                        resultSet.getObject ("creatorID", UUID.class),
                        resultSet.getObject ("categoryID", UUID.class),
                        resultSet.getObject ("channelID", UUID.class),
                        resultSet.getInt ("likesCount"),
                        resultSet.getInt ("dislikesCount"),
                        resultSet.getObject ("creationDate", LocalDateTime.class),
                        resultSet.getBoolean ("isOnlyComrade"),
                        resultSet.getString ("title"),
                        resultSet.getString ("description"),
                        resultSet.getString ("dataType"),
                        resultSet.getString ("videoURL"),
                        resultSet.getString ("thumbnailURL"),
                        resultSet.getInt ("commentsCount"),
                        resultSet.getInt ("viewsCount")
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
        try (Connection connection = DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ();
             ResultSet resultSet = statement.executeQuery (sql))
        {
            while (resultSet.next ())
            {
                videos.add (new Video (
                        resultSet.getObject ("ID", UUID.class),
                        resultSet.getObject ("creatorID", UUID.class),
                        resultSet.getObject ("categoryID", UUID.class),
                        resultSet.getObject ("channelID", UUID.class),
                        resultSet.getInt ("likesCount"),
                        resultSet.getInt ("dislikesCount"),
                        resultSet.getObject ("creationDate", LocalDateTime.class),
                        resultSet.getBoolean ("isOnlyComrade"),
                        resultSet.getString ("title"),
                        resultSet.getString ("description"),
                        resultSet.getString ("dataType"),
                        resultSet.getString ("videoURL"),
                        resultSet.getString ("thumbnailURL"),
                        resultSet.getInt ("commentsCount"),
                        resultSet.getInt ("viewsCount")
                ));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return videos;
    }

    public static List <Content> findAllContents ()
    {
        List <Content> contents = new ArrayList <> ();

        String sql = "SELECT * FROM Videos";
        try (Connection connection = DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ();
             ResultSet resultSet = statement.executeQuery (sql))
        {
            while (resultSet.next ())
            {
                contents.add (new Video (
                        resultSet.getObject ("ID", UUID.class),
                        resultSet.getObject ("creatorID", UUID.class),
                        resultSet.getObject ("categoryID", UUID.class),
                        resultSet.getObject ("channelID", UUID.class),
                        resultSet.getInt ("likesCount"),
                        resultSet.getInt ("dislikesCount"),
                        resultSet.getObject ("creationDate", LocalDateTime.class),
                        resultSet.getBoolean ("isOnlyComrade"),
                        resultSet.getString ("title"),
                        resultSet.getString ("description"),
                        resultSet.getString ("dataType"),
                        resultSet.getString ("videoURL"),
                        resultSet.getString ("thumbnailURL"),
                        resultSet.getInt ("commentsCount"),
                        resultSet.getInt ("viewsCount")
                ));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }

        String sql2 = "SELECT * FROM Posts";
        try (Connection connection = DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ();
             ResultSet resultSet = statement.executeQuery (sql2))
        {
            while (resultSet.next ())
            {
                contents.add (new Post (
                        resultSet.getObject ("ID", UUID.class),
                        resultSet.getObject ("creatorID", UUID.class),
                        resultSet.getObject ("communityID", UUID.class),
                        resultSet.getObject ("channelID", UUID.class),
                        resultSet.getInt ("likesCount"),
                        resultSet.getInt ("dislikesCount"),
                        resultSet.getObject ("creationDate", LocalDateTime.class),
                        resultSet.getBoolean ("isOnlyComrade"),
                        resultSet.getString ("title"),
                        resultSet.getString ("description"),
                        resultSet.getString ("imageURL"),
                        resultSet.getInt ("commentsCount")
                ));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }

        String sql3 = "SELECT * FROM Comment";
        try (Connection connection = DatabaseConnection.getConnection ();
             Statement stmt = connection.createStatement ();
             ResultSet resultSet = stmt.executeQuery (sql3))
        {
            while (resultSet.next ())
            {
                contents.add (new Comment (
                        resultSet.getObject ("ID", UUID.class),
                        resultSet.getObject ("contentID", UUID.class),
                        resultSet.getObject ("creatorID", UUID.class),
                        resultSet.getObject ("channelID", UUID.class),
                        resultSet.getInt ("likesCount"),
                        resultSet.getInt ("dislikesCount"),
                        resultSet.getObject ("creationDate", LocalDateTime.class),
                        resultSet.getBoolean ("isOnlyComrade"),
                        resultSet.getObject ("parentCommentID", UUID.class),
                        resultSet.getString ("content"),
                        resultSet.getInt ("replyCount"),
                        resultSet.getBoolean ("isReply")
                ));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return contents;
    }

    public List <Video> findByUser (UUID id)
    {
        List <Video> videos = new ArrayList <> ();
        String       sql    = "SELECT * FROM Posts";
        try (Connection connection = DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ();
             ResultSet resultSet = statement.executeQuery (sql))
        {
            while (resultSet.next ())
            {
                videos.add (new Video (
                        resultSet.getObject ("ID", UUID.class),
                        resultSet.getObject ("creatorID", UUID.class),
                        resultSet.getObject ("categoryID", UUID.class),
                        resultSet.getObject ("channelID", UUID.class),
                        resultSet.getInt ("likesCount"),
                        resultSet.getInt ("dislikesCount"),
                        resultSet.getObject ("creationDate", LocalDateTime.class),
                        resultSet.getBoolean ("isOnlyComrade"),
                        resultSet.getString ("title"),
                        resultSet.getString ("description"),
                        resultSet.getString ("dataType"),
                        resultSet.getString ("videoURL"),
                        resultSet.getString ("thumbnailURL"),
                        resultSet.getInt ("commentsCount"),
                        resultSet.getInt ("viewsCount")
                ));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        List <Video> userVideos = new ArrayList <> ();
        for (Video video : videos)
        {
            if (video.getCreatorID () == id)
            {
                userVideos.add (video);
            }
        }
        return userVideos;
    }

    public List <User> findLikedUsers (Video video)
    {
        List <User> users = new ArrayList <> ();
        String      sql   = "SELECT * FROM ContentsAction WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement preparedStatement = connection.prepareStatement (sql))
        {
            preparedStatement.setObject (1, video.getID ());
            ResultSet   resultSet = preparedStatement.executeQuery ();
            UserDAOImpl userDAO   = new UserDAOImpl ();
            while (resultSet.next ())
            {
                if (resultSet.getBoolean ("liked"))
                {
                    users.add (userDAO.findById (resultSet.getObject ("userID", UUID.class)));
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return users;
    }

    public List <User> findDislikedUsers (Video video)
    {
        List <User> users = new ArrayList <> ();
        String      sql   = "SELECT * FROM ContentsAction WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement preparedStatement = connection.prepareStatement (sql))
        {
            preparedStatement.setObject (1, video.getID ());
            ResultSet   resultSet = preparedStatement.executeQuery ();
            UserDAOImpl userDAO   = new UserDAOImpl ();
            while (resultSet.next ())
            {
                if (resultSet.getBoolean ("disliked"))
                {
                    users.add (userDAO.findById (resultSet.getObject ("userID", UUID.class)));
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return users;
    }
}
