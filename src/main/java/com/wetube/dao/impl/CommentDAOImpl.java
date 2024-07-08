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

        List <Comment> all = findAll ();
        for (Comment object : all)
        {
            if (object.getID () == uuid)
            {
                uuid = generateID ();
            }
        }
        return uuid;
    }

    public void create (Comment comment)
    {
        String sql = "INSERT INTO Comments (ID, contentID, creatorID, parentCommentID, content, replyCount, creationDate, isReply, likesCount, dislikesCount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, comment.getID ());
            pstmt.setObject (2, comment.getContentID ());
            pstmt.setObject (3, comment.getCreatorID ());
            pstmt.setObject (4, comment.getParentCommentID ());
            pstmt.setString (5, comment.getContent ());
            pstmt.setInt (6, comment.getReplyCount ());
            pstmt.setTimestamp (7, Timestamp.valueOf (comment.getCreationDate ()));
            pstmt.setBoolean (8, comment.isReply ());
            pstmt.setInt (9, comment.getLikesCount ());
            pstmt.setInt (10, comment.getDislikesCount ());
            pstmt.executeUpdate ();
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public void update (Comment comment)
    {
        String sql = "UPDATE Comments SET contentID = ?, creatorID = ?, parentCommentID = ?, content = ?, replyCount = ?, creationDate = ?, isReply = ?, likesCount = ?, dislikesCount = ? WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, comment.getContentID ());
            pstmt.setObject (2, comment.getCreatorID ());
            pstmt.setObject (3, comment.getParentCommentID ());
            pstmt.setString (4, comment.getContent ());
            pstmt.setInt (5, comment.getReplyCount ());
            pstmt.setTimestamp (6, Timestamp.valueOf (comment.getCreationDate ()));
            pstmt.setBoolean (7, comment.isReply ());
            pstmt.setInt (8, comment.getLikesCount ());
            pstmt.setInt (9, comment.getDislikesCount ());
            pstmt.setObject (10, comment.getID ());
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
            String deleteComment = "DELETE FROM Comments WHERE ID = '" + id + "'";
            stmt.executeUpdate (deleteComment);
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public void like (Comment comment, User user)
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
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public void dislike (Comment comment, User user)
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
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public void removeLikeDislike (Comment comment, User user)
    {
        String sql = "INSERT INTO ContentsAction (contentID, userID, liked, disliked) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, comment.getID ());
            pstmt.setObject (2, user.getID ());
            pstmt.setBoolean (3, false);
            pstmt.setBoolean (4, false);
            pstmt.executeUpdate ();
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public Comment findById (UUID id)
    {
        String sql = "SELECT * FROM Comments WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, id);
            ResultSet rs = pstmt.executeQuery ();
            if (rs.next ())
            {
                return new Comment (
                        rs.getObject ("ID", UUID.class),
                        rs.getObject ("contentID", UUID.class),
                        rs.getObject ("creatorID", UUID.class),
                        rs.getObject ("channelID", UUID.class),
                        rs.getInt ("likesCount"),
                        rs.getInt ("dislikesCount"),
                        rs.getObject ("creationDate", LocalDateTime.class),
                        rs.getBoolean ("isOnlyComrade"),
                        rs.getObject ("parentCommentID", UUID.class),
                        rs.getString ("content"),
                        rs.getInt ("replyCount"),
                        rs.getBoolean ("isReply")
                );
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return null;
    }

    public List <Comment> findAll ()
    {
        List <Comment> comments = new ArrayList <> ();
        String       sql    = "SELECT * FROM Comment";
        try (Connection conn = DatabaseConnection.getConnection ();
             Statement stmt = conn.createStatement ();
             ResultSet rs = stmt.executeQuery (sql))
        {
            while (rs.next ())
            {
                comments.add (new Comment (
                        rs.getObject ("ID", UUID.class),
                        rs.getObject ("contentID", UUID.class),
                        rs.getObject ("creatorID", UUID.class),
                        rs.getObject ("channelID", UUID.class),
                        rs.getInt ("likesCount"),
                        rs.getInt ("dislikesCount"),
                        rs.getObject ("creationDate", LocalDateTime.class),
                        rs.getBoolean ("isOnlyComrade"),
                        rs.getObject ("parentCommentID", UUID.class),
                        rs.getString ("content"),
                        rs.getInt ("replyCount"),
                        rs.getBoolean ("isReply")
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
        String sql = "SELECT * FROM ContentsAction WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, comment.getID ());
            ResultSet rs = pstmt.executeQuery ();
            UserDAOImpl userDAO = new UserDAOImpl ();
            while (rs.next ())
            {
                if (rs.getBoolean ("liked"))
                {
                    users.add (userDAO.findById (rs.getObject ("userID", UUID.class)));
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
        String sql = "SELECT * FROM ContentsAction WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, comment.getID ());
            ResultSet rs = pstmt.executeQuery ();
            UserDAOImpl userDAO = new UserDAOImpl ();
            while (rs.next ())
            {
                if (rs.getBoolean ("disliked"))
                {
                    users.add (userDAO.findById (rs.getObject ("userID", UUID.class)));
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
