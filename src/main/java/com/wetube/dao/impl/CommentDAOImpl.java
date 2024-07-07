package com.wetube.dao.impl;

import com.wetube.model.Comment;
import com.wetube.model.Post;
import com.wetube.model.Video;
import com.wetube.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommentDAOImpl
{
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
        String sql = "UPDATE Comments SET communityID = ?, creatorID = ?, channelID = ?, title = ?, description = ?, imageURL = ?, commentsCount = ?, likesCount = ?, dislikesCount = ?, creationDate = ?, isOnlyComrade = ? WHERE ID = ?";
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
                        rs.getObject ("contentID", UUID.class),
                        rs.getObject ("creatorID", UUID.class),
                        rs.getObject ("parentCommentID", UUID.class),
                        rs.getString ("content"),
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
                        rs.getObject ("contentID", UUID.class),
                        rs.getObject ("creatorID", UUID.class),
                        rs.getObject ("parentCommentID", UUID.class),
                        rs.getString ("content"),
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
}
