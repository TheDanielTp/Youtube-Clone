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
                " videoURL, thumbnailURL, commentsCount, likesCount, dislikesCount, creationDate, isOnlyComrade, viewsCount)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
            preparedStatement.setString (9, video.getThumbnailURL ());
            preparedStatement.setInt (10, video.getCommentsCount ());
            preparedStatement.setInt (11, video.getLikesCount ());
            preparedStatement.setInt (12, video.getDislikesCount ());
            preparedStatement.setTimestamp (13, Timestamp.valueOf (video.getCreationDate ()));
            preparedStatement.setBoolean (14, video.isOnlyComrade ());
            preparedStatement.setInt (15, video.getViewsCount ());
            preparedStatement.executeUpdate ();
            System.out.println ("> Database: video created");
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to create video");
            System.out.println (e.getMessage ());
        }
    }

    public void update (Video video)
    {
        String sql = "UPDATE Videos SET creatorID = ?, channelID = ?, categoryID = ?, title = ?, description = ?," +
                " dataType = ?, videoURL = ?, thumbnailURL = ?, commentsCount = ?, likesCount = ?, dislikesCount = ?," +
                " creationDate = ?, isOnlyComrade = ?, viewsCount = ? WHERE ID = ?";
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
            preparedStatement.setString (8, video.getThumbnailURL ());
            preparedStatement.setInt (9, video.getCommentsCount ());
            preparedStatement.setInt (10, video.getLikesCount ());
            preparedStatement.setInt (11, video.getDislikesCount ());
            preparedStatement.setTimestamp (12, Timestamp.valueOf (video.getCreationDate ()));
            preparedStatement.setBoolean (13, video.isOnlyComrade ());
            preparedStatement.setInt (14, video.getViewsCount ());
            preparedStatement.setObject (15, video.getID ());
            preparedStatement.executeUpdate ();
            System.out.println ("> Database: video updated");
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to update video");
            System.out.println (e.getMessage ());
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
            System.out.println ("> Database: video deleted");
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to delete video");
            System.out.println (e.getMessage ());
        }
    }

    public boolean checkActionExistence (User user, Video video)
    {
        String sql = "SELECT * FROM ContentsAction WHERE userID = ? AND contentID = ?";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, user.getID ());
            pstmt.setObject (2, video.getID ());
            ResultSet rs = pstmt.executeQuery ();
            if (rs.next ())
            {
                System.out.println ("> Database: action existence checked");
                return true;
            }
        }
        catch (SQLException e)
        {
            System.out.println ("> Database: failed to check action existence");
            System.out.println (e.getMessage ());
        }
        System.out.println ("> Database: action existence checked");
        return false;
    }

    public void like (Video video, User user)
    {
        VideoDAOImpl videoDAO = new VideoDAOImpl ();
        if (videoDAO.findLikedUsers (video).contains (user))
        {
            String sql = "UPDATE ContentsAction SET liked = ?, disliked = ? WHERE contentID = ? AND userID = ?";
            try (Connection conn = DatabaseConnection.getConnection ();
                 PreparedStatement pstmt = conn.prepareStatement (sql))
            {
                pstmt.setBoolean (1, false);
                pstmt.setBoolean (2, false);
                pstmt.setObject (3, video.getID ());
                pstmt.setObject (4, user.getID ());
                pstmt.executeUpdate ();
                System.out.println ("> Database: video like removed");
            }
            catch (SQLException e)
            {
                System.out.println (e.getMessage ());
                System.out.println ("> Database: failed to remove video like");
            }
            video.setLikesCount (video.getLikesCount () - 1);
        }
        else if (checkActionExistence (user, video))
        {
            String sql = "UPDATE ContentsAction SET liked = ?, disliked = ? WHERE contentID = ? AND userID = ?";
            try (Connection conn = DatabaseConnection.getConnection ();
                 PreparedStatement pstmt = conn.prepareStatement (sql))
            {
                pstmt.setBoolean (1, true);
                pstmt.setBoolean (2, false);
                pstmt.setObject (3, video.getID ());
                pstmt.setObject (4, user.getID ());
                pstmt.executeUpdate ();
                System.out.println ("> Database: video liked");
            }
            catch (SQLException e)
            {
                System.out.println (e.getMessage ());
                System.out.println ("> Database: failed to like video");
            }
            video.setLikesCount (video.getLikesCount () + 1);
        }
        else
        {
            String sql = "INSERT INTO ContentsAction (contentID, userID, liked, disliked) VALUES (?, ?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection ();
                 PreparedStatement pstmt = conn.prepareStatement (sql))
            {
                pstmt.setObject (1, video.getID ());
                pstmt.setObject (2, user.getID ());
                pstmt.setBoolean (3, true);
                pstmt.setBoolean (4, false);
                pstmt.executeUpdate ();
                System.out.println ("> Database: video liked");
            }
            catch (SQLException e)
            {
                System.out.println (e.getMessage ());
                System.out.println ("> Database: failed to like video");
            }
            video.setLikesCount (video.getLikesCount () + 1);
        }
        update (video);
    }

    public void dislike (Video video, User user)
    {
        VideoDAOImpl videoDAO = new VideoDAOImpl ();
        if (videoDAO.findDislikedUsers (video).contains (user))
        {
            String sql = "UPDATE ContentsAction SET liked = ?, disliked = ? WHERE contentID = ? AND userID = ?";
            try (Connection conn = DatabaseConnection.getConnection ();
                 PreparedStatement pstmt = conn.prepareStatement (sql))
            {
                pstmt.setBoolean (1, false);
                pstmt.setBoolean (2, false);
                pstmt.setObject (3, video.getID ());
                pstmt.setObject (4, user.getID ());
                pstmt.executeUpdate ();
                System.out.println ("> Database: video dislike removed");
            }
            catch (SQLException e)
            {
                System.out.println (e.getMessage ());
                System.out.println ("> Database: failed to remove video dislike");
            }
            video.setDislikesCount (video.getDislikesCount () - 1);
        }
        else if (checkActionExistence (user, video))
        {
            String sql = "UPDATE ContentsAction SET liked = ?, disliked = ? WHERE contentID = ? AND userID = ?";
            try (Connection conn = DatabaseConnection.getConnection ();
                 PreparedStatement pstmt = conn.prepareStatement (sql))
            {
                pstmt.setBoolean (1, false);
                pstmt.setBoolean (2, true);
                pstmt.setObject (3, video.getID ());
                pstmt.setObject (4, user.getID ());
                pstmt.executeUpdate ();
                System.out.println ("> Database: video disliked");
            }
            catch (SQLException e)
            {
                System.out.println (e.getMessage ());
                System.out.println ("> Database: failed to dislike video");
            }
            video.setDislikesCount (video.getDislikesCount () + 1);
        }
        else
        {
            String sql = "INSERT INTO ContentsAction (contentID, userID, liked, disliked) VALUES (?, ?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection ();
                 PreparedStatement pstmt = conn.prepareStatement (sql))
            {
                pstmt.setObject (1, video.getID ());
                pstmt.setObject (2, user.getID ());
                pstmt.setBoolean (3, false);
                pstmt.setBoolean (4, true);
                pstmt.executeUpdate ();
                System.out.println ("> Database: video disliked");
            }
            catch (SQLException e)
            {
                System.out.println (e.getMessage ());
                System.out.println ("> Database: failed to dislike video");
                video.setDislikesCount (video.getDislikesCount () + 1);
            }
        }
        update (video);
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
                        resultSet.getObject ("categoryID", UUID.class),
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

        String sql3 = "SELECT * FROM Comments";
        try (Connection connection = DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ();
             ResultSet resultSet = statement.executeQuery (sql3))
        {
            while (resultSet.next ())
            {
                contents.add (new Comment (
                        resultSet.getObject ("ID", UUID.class),
                        resultSet.getObject ("contentID", UUID.class),
                        resultSet.getObject ("creatorID", UUID.class),
                        resultSet.getObject ("creatorID", UUID.class),
                        resultSet.getInt ("likesCount"),
                        resultSet.getInt ("dislikesCount"),
                        resultSet.getObject ("creationDate", LocalDateTime.class),
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
        String      sql   = "SELECT * FROM ContentsAction WHERE contentID = ?";
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
        String      sql   = "SELECT * FROM ContentsAction WHERE contentID = ?";
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
