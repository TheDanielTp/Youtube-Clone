package com.wetube.dao.impl;

import com.wetube.model.*;
import com.wetube.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommentDAOImpl
{
    public UUID generateID ()
    {
        UUID uuid = UUID.randomUUID ();

        List <Content> all = VideoDAOImpl.findAllContents ();
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

    public void create (Comment comment)
    {
        String sql = "INSERT INTO Comments (ID, contentID, creatorID, parentCommentID, content, replyCount," +
                " creationDate, isReply, likesCount, dislikesCount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement preparedStatement = connection.prepareStatement (sql))
        {
            preparedStatement.setObject (1, comment.getID ());
            preparedStatement.setObject (2, comment.getContentID ());
            preparedStatement.setObject (3, comment.getCreatorID ());
            preparedStatement.setObject (4, comment.getParentCommentID ());
            preparedStatement.setString (5, comment.getContent ());
            preparedStatement.setInt (6, comment.getReplyCount ());
            preparedStatement.setTimestamp (7, Timestamp.valueOf (comment.getCreationDate ()));
            preparedStatement.setBoolean (8, comment.isReply ());
            preparedStatement.setInt (9, comment.getLikesCount ());
            preparedStatement.setInt (10, comment.getDislikesCount ());
            preparedStatement.executeUpdate ();
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public void update (Comment comment)
    {
        String sql = "UPDATE Comments SET contentID = ?, creatorID = ?, parentCommentID = ?, content = ?," +
                " replyCount = ?, creationDate = ?, isReply = ?, likesCount = ?, dislikesCount = ? WHERE ID = ?";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement preparedStatement = connection.prepareStatement (sql))
        {
            preparedStatement.setObject (1, comment.getContentID ());
            preparedStatement.setObject (2, comment.getCreatorID ());
            preparedStatement.setObject (3, comment.getParentCommentID ());
            preparedStatement.setString (4, comment.getContent ());
            preparedStatement.setInt (5, comment.getReplyCount ());
            preparedStatement.setTimestamp (6, Timestamp.valueOf (comment.getCreationDate ()));
            preparedStatement.setBoolean (7, comment.isReply ());
            preparedStatement.setInt (8, comment.getLikesCount ());
            preparedStatement.setInt (9, comment.getDislikesCount ());
            preparedStatement.setObject (10, comment.getID ());
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
             Statement stmt = connection.createStatement ())
        {
            String deleteComment = "DELETE FROM Comments WHERE ID = '" + id + "'";
            stmt.executeUpdate (deleteComment);
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public boolean checkActionExistence (User user, Comment comment)
    {
        String sql = "SELECT * FROM ContentsAction WHERE userID = ? AND contentID = ?";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, user.getID ());
            pstmt.setObject (2, comment.getID ());
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

    public void like (Comment comment, User user)
    {
        CommentDAOImpl commentDAO = new CommentDAOImpl ();
        if (commentDAO.findLikedUsers (comment).contains (user))
        {
            String sql = "UPDATE ContentsAction SET liked = ?, disliked = ? WHERE contentID = ? AND userID = ?";
            try (Connection conn = DatabaseConnection.getConnection ();
                 PreparedStatement pstmt = conn.prepareStatement (sql))
            {
                pstmt.setBoolean (1, false);
                pstmt.setBoolean (2, false);
                pstmt.setObject (3, comment.getID ());
                pstmt.setObject (4, user.getID ());
                pstmt.executeUpdate ();
                System.out.println ("> Database: comment like removed");
            }
            catch (SQLException e)
            {
                System.out.println (e.getMessage ());
                System.out.println ("> Database: failed to remove comment like");
            }
            comment.setLikesCount (comment.getLikesCount () - 1);
        }
        else if (checkActionExistence (user, comment))
        {
            String sql = "UPDATE ContentsAction SET liked = ?, disliked = ? WHERE contentID = ? AND userID = ?";
            try (Connection conn = DatabaseConnection.getConnection ();
                 PreparedStatement pstmt = conn.prepareStatement (sql))
            {
                pstmt.setBoolean (1, true);
                pstmt.setBoolean (2, false);
                pstmt.setObject (3, comment.getID ());
                pstmt.setObject (4, user.getID ());
                pstmt.executeUpdate ();
                System.out.println ("> Database: comment liked");
            }
            catch (SQLException e)
            {
                System.out.println (e.getMessage ());
                System.out.println ("> Database: failed to like comment");
            }
            comment.setLikesCount (comment.getLikesCount () + 1);
        }
        else
        {
            String sql = "INSERT INTO ContentsAction (contentID, userID, liked, disliked) VALUES (?, ?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection ();
                 PreparedStatement pstmt = conn.prepareStatement (sql))
            {
                pstmt.setObject (1, comment.getID ());
                pstmt.setObject (2, user.getID ());
                pstmt.setBoolean (3, true);
                pstmt.setBoolean (4, false);
                pstmt.executeUpdate ();
                System.out.println ("> Database: comment liked");
            }
            catch (SQLException e)
            {
                System.out.println (e.getMessage ());
                System.out.println ("> Database: failed to like comment");
            }
            comment.setLikesCount (comment.getLikesCount () + 1);
        }
        update (comment);
    }

    public void dislike (Comment comment, User user)
    {
        CommentDAOImpl commentDAO = new CommentDAOImpl ();
        if (commentDAO.findDislikedUsers (comment).contains (user))
        {
            String sql = "UPDATE ContentsAction SET liked = ?, disliked = ? WHERE contentID = ? AND userID = ?";
            try (Connection conn = DatabaseConnection.getConnection ();
                 PreparedStatement pstmt = conn.prepareStatement (sql))
            {
                pstmt.setBoolean (1, false);
                pstmt.setBoolean (2, false);
                pstmt.setObject (3, comment.getID ());
                pstmt.setObject (4, user.getID ());
                pstmt.executeUpdate ();
                System.out.println ("> Database: comment dislike removed");
            }
            catch (SQLException e)
            {
                System.out.println (e.getMessage ());
                System.out.println ("> Database: failed to remove comment dislike");
            }
            comment.setDislikesCount (comment.getDislikesCount () - 1);
        }
        else if (checkActionExistence (user, comment))
        {
            String sql = "UPDATE ContentsAction SET liked = ?, disliked = ? WHERE contentID = ? AND userID = ?";
            try (Connection conn = DatabaseConnection.getConnection ();
                 PreparedStatement pstmt = conn.prepareStatement (sql))
            {
                pstmt.setBoolean (1, false);
                pstmt.setBoolean (2, true);
                pstmt.setObject (3, comment.getID ());
                pstmt.setObject (4, user.getID ());
                pstmt.executeUpdate ();
                System.out.println ("> Database: comment disliked");
            }
            catch (SQLException e)
            {
                System.out.println (e.getMessage ());
                System.out.println ("> Database: failed to dislike comment");
            }
            comment.setDislikesCount (comment.getDislikesCount () + 1);
        }
        else
        {
            String sql = "INSERT INTO ContentsAction (contentID, userID, liked, disliked) VALUES (?, ?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection ();
                 PreparedStatement pstmt = conn.prepareStatement (sql))
            {
                pstmt.setObject (1, comment.getID ());
                pstmt.setObject (2, user.getID ());
                pstmt.setBoolean (3, false);
                pstmt.setBoolean (4, true);
                pstmt.executeUpdate ();
                System.out.println ("> Database: comment disliked");
            }
            catch (SQLException e)
            {
                System.out.println (e.getMessage ());
                System.out.println ("> Database: failed to dislike comment");
                comment.setDislikesCount (comment.getDislikesCount () + 1);
            }
        }
        update (comment);
    }

    public void removeLikeDislike (Comment comment, User user)
    {
        String sql = "INSERT INTO ContentsAction (contentID, userID, liked, disliked) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement preparedStatement = connection.prepareStatement (sql))
        {
            preparedStatement.setObject (1, comment.getID ());
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

    public Comment findById (UUID id)
    {
        String sql = "SELECT * FROM Comments WHERE ID = ?";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement preparedStatement = connection.prepareStatement (sql))
        {
            preparedStatement.setObject (1, id);
            ResultSet resultSet = preparedStatement.executeQuery ();
            if (resultSet.next ())
            {
                return new Comment (
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
                );
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return null;
    }

    public List <Comment> findVideoComments (Video video)
    {
        List <Comment> comments = new ArrayList <> ();
        String       sql    = "SELECT * FROM Comments";
        try (Connection connection = DatabaseConnection.getConnection ();
             Statement stmt = connection.createStatement ();
             ResultSet resultSet = stmt.executeQuery (sql))
        {
            while (resultSet.next ())
            {
                if (resultSet.getObject ("contentID", UUID.class).equals (video.getID ()))
                {
                    comments.add (new Comment (
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
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return comments;
    }

    public List <Comment> findAll ()
    {
        List <Comment> comments = new ArrayList <> ();
        String       sql    = "SELECT * FROM Comments";
        try (Connection connection = DatabaseConnection.getConnection ();
             Statement stmt = connection.createStatement ();
             ResultSet resultSet = stmt.executeQuery (sql))
        {
            while (resultSet.next ())
            {
                comments.add (new Comment (
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
        return comments;
    }

    public List <User> findLikedUsers (Comment comment)
    {
        List <User> users = new ArrayList <> ();
        String sql = "SELECT * FROM ContentsAction WHERE contentID = ?";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement preparedStatement = connection.prepareStatement (sql))
        {
            preparedStatement.setObject (1, comment.getID ());
            ResultSet resultSet = preparedStatement.executeQuery ();
            UserDAOImpl userDAO = new UserDAOImpl ();
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

    public List <User> findDislikedUsers (Comment comment)
    {
        List <User> users = new ArrayList <> ();
        String sql = "SELECT * FROM ContentsAction WHERE contentID = ?";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement preparedStatement = connection.prepareStatement (sql))
        {
            preparedStatement.setObject (1, comment.getID ());
            ResultSet resultSet = preparedStatement.executeQuery ();
            UserDAOImpl userDAO = new UserDAOImpl ();
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
