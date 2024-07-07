package com.wetube.dao.impl;

import com.wetube.model.Post;
import com.wetube.model.Video;
import com.wetube.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PostDAOImpl
{
    public void create (Post post)
    {
        String sql = "INSERT INTO Posts (ID, communityID, creatorID, channelID, title, description, image, commentsCount, likesCount, dislikesCount, creationDate, isOnlyComrade) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, post.getID ());
            pstmt.setObject (2, post.getCommunityID ());
            pstmt.setObject (3, post.getCreatorID ());
            pstmt.setObject (4, post.getChannelID ());
            pstmt.setString (5, post.getTitle ());
            pstmt.setString (6, post.getDescription ());
            pstmt.setString (7, post.getImageURL ());
            pstmt.setInt (8, post.getCommentsCount ());
            pstmt.setInt (9, post.getLikesCount ());
            pstmt.setInt (10, post.getDislikesCount ());
            pstmt.setTimestamp (11, Timestamp.valueOf (post.getCreationDate ()));
            pstmt.setBoolean (12, post.isOnlyComrade ());
            pstmt.executeUpdate ();
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public void update (Post post)
    {
        String sql = "UPDATE Posts SET communityID = ?, creatorID = ?, channelID = ?, title = ?, description = ?, imageURL = ?, commentsCount = ?, likesCount = ?, dislikesCount = ?, creationDate = ?, isOnlyComrade = ? WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, post.getCommunityID ());
            pstmt.setObject (2, post.getCreatorID ());
            pstmt.setObject (3, post.getChannelID ());
            pstmt.setString (4, post.getTitle ());
            pstmt.setString (5, post.getDescription ());
            pstmt.setString (6, post.getImageURL ());
            pstmt.setInt (7, post.getCommentsCount ());
            pstmt.setInt (8, post.getLikesCount ());
            pstmt.setInt (9, post.getDislikesCount ());
            pstmt.setTimestamp (10, Timestamp.valueOf (post.getCreationDate ()));
            pstmt.setBoolean (11, post.isOnlyComrade ());
            pstmt.setObject (12, post.getID ());
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
            String deleteComments = "DELETE FROM Comments WHERE contentID = '" + id + "'";
            stmt.executeUpdate (deleteComments);

            String deletePost = "DELETE FROM Posts WHERE ID = '" + id + "'";
            stmt.executeUpdate (deletePost);
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public Post findById (UUID id)
    {
        String sql = "SELECT * FROM Posts WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, id);
            ResultSet rs = pstmt.executeQuery ();
            if (rs.next ())
            {
                return new Post (
                        rs.getObject ("creatorID", UUID.class),
                        rs.getObject ("communityID", UUID.class),
                        rs.getObject ("channelID", UUID.class),
                        rs.getString ("title"),
                        rs.getString ("description"),
                        rs.getString ("imageURL"),
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

    public Post findByTitle (UUID id)
    {
        String sql = "SELECT * FROM Posts WHERE title = ?";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, id);
            ResultSet rs = pstmt.executeQuery ();
            if (rs.next ())
            {
                return new Post (
                        rs.getObject ("creatorID", UUID.class),
                        rs.getObject ("communityID", UUID.class),
                        rs.getObject ("channelID", UUID.class),
                        rs.getString ("title"),
                        rs.getString ("description"),
                        rs.getString ("imageURL"),
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

    public List <Post> findAll ()
    {
        List <Post> posts = new ArrayList <> ();
        String      sql   = "SELECT * FROM Posts";
        try (Connection conn = DatabaseConnection.getConnection ();
             Statement stmt = conn.createStatement ();
             ResultSet rs = stmt.executeQuery (sql))
        {
            while (rs.next ())
            {
                posts.add (new Post (
                        rs.getObject ("creatorID", UUID.class),
                        rs.getObject ("communityID", UUID.class),
                        rs.getObject ("channelID", UUID.class),
                        rs.getString ("title"),
                        rs.getString ("description"),
                        rs.getString ("imageURL"),
                        rs.getBoolean ("isOnlyComrade")
                ));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return posts;
    }
}
