package com.wetube.dao.impl;

import com.wetube.model.Content;
import com.wetube.model.Post;
import com.wetube.model.User;
import com.wetube.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PostDAOImpl
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

    public void create (Post post)
    {
        String sql = "INSERT INTO Posts (ID, communityID, creatorID, channelID, title, description, image," +
                " commentsCount, likesCount, dislikesCount, creationDate, isOnlyComrade) VALUES" +
                " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement  preparedStatement = connection.prepareStatement (sql))
        {
             preparedStatement.setObject (1, post.getID ());
             preparedStatement.setObject (2, post.getCommunityID ());
             preparedStatement.setObject (3, post.getCreatorID ());
             preparedStatement.setObject (4, post.getChannelID ());
             preparedStatement.setString (5, post.getTitle ());
             preparedStatement.setString (6, post.getDescription ());
             preparedStatement.setString (7, post.getImageURL ());
             preparedStatement.setInt (8, post.getCommentsCount ());
             preparedStatement.setInt (9, post.getLikesCount ());
             preparedStatement.setInt (10, post.getDislikesCount ());
             preparedStatement.setTimestamp (11, Timestamp.valueOf (post.getCreationDate ()));
             preparedStatement.setBoolean (12, post.isOnlyComrade ());
             preparedStatement.executeUpdate ();
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public void update (Post post)
    {
        String sql = "UPDATE Posts SET communityID = ?, creatorID = ?, channelID = ?, title = ?, description = ?," +
                " imageURL = ?, commentsCount = ?, likesCount = ?, dislikesCount = ?, creationDate = ?," +
                " isOnlyComrade = ? WHERE ID = ?";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement  preparedStatement = connection.prepareStatement (sql))
        {
             preparedStatement.setObject (1, post.getCommunityID ());
             preparedStatement.setObject (2, post.getCreatorID ());
             preparedStatement.setObject (3, post.getChannelID ());
             preparedStatement.setString (4, post.getTitle ());
             preparedStatement.setString (5, post.getDescription ());
             preparedStatement.setString (6, post.getImageURL ());
             preparedStatement.setInt (7, post.getCommentsCount ());
             preparedStatement.setInt (8, post.getLikesCount ());
             preparedStatement.setInt (9, post.getDislikesCount ());
             preparedStatement.setTimestamp (10, Timestamp.valueOf (post.getCreationDate ()));
             preparedStatement.setBoolean (11, post.isOnlyComrade ());
             preparedStatement.setObject (12, post.getID ());
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
            String deleteComments = "DELETE FROM Comments WHERE contentID = '" + id + "'";
            statement.executeUpdate (deleteComments);

            String deletePost = "DELETE FROM Posts WHERE ID = '" + id + "'";
            statement.executeUpdate (deletePost);
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public void like (Post post, User user)
    {
        String sql = "INSERT INTO ContentsAction (contentID, userID, liked, disliked) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement  preparedStatement = connection.prepareStatement (sql))
        {
             preparedStatement.setObject (1, post.getID ());
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

    public void dislike (Post post, User user)
    {
        String sql = "INSERT INTO ContentsAction (contentID, userID, liked, disliked) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement  preparedStatement = connection.prepareStatement (sql))
        {
             preparedStatement.setObject (1, post.getID ());
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

    public void removeLikeDislike (Post post, User user)
    {
        String sql = "INSERT INTO ContentsAction (contentID, userID, liked, disliked) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement  preparedStatement = connection.prepareStatement (sql))
        {
             preparedStatement.setObject (1, post.getID ());
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

    public Post findById (UUID id)
    {
        String sql = "SELECT * FROM Posts WHERE ID = ?";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement  preparedStatement = connection.prepareStatement (sql))
        {
             preparedStatement.setObject (1, id);
            ResultSet  resultSet =  preparedStatement.executeQuery ();
            if  (resultSet.next ())
            {
                return new Post (
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
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement  preparedStatement = connection.prepareStatement (sql))
        {
             preparedStatement.setObject (1, id);
            ResultSet  resultSet =  preparedStatement.executeQuery ();
            if  (resultSet.next ())
            {
                return new Post (
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
        try (Connection connection = DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ();
             ResultSet  resultSet = statement.executeQuery (sql))
        {
            while  (resultSet.next ())
            {
                posts.add (new Post (
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
        return posts;
    }

    public List <Post> findUserPosts (UUID id)
    {
        List <Post> posts = new ArrayList <> ();
        String      sql   = "SELECT * FROM Posts";
        try (Connection connection = DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ();
             ResultSet  resultSet = statement.executeQuery (sql))
        {
            while  (resultSet.next ())
            {
                posts.add (new Post (
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
        catch(SQLException e)
        {
            e.printStackTrace ();
        }
        List <Post> userPosts = new ArrayList <> ();
        for (Post post : posts)
        {
            if (post.getCreatorID () == id)
            {
                userPosts.add (post);
            }
        }
        return userPosts;
    }

    public List <User> findLikedUsers (Post post)
    {
        List <User> users = new ArrayList <> ();
        String      sql   = "SELECT * FROM ContentsAction WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement  preparedStatement = connection.prepareStatement (sql))
        {
             preparedStatement.setObject (1, post.getID ());
            ResultSet    resultSet      =  preparedStatement.executeQuery ();
            UserDAOImpl userDAO = new UserDAOImpl ();
            while  (resultSet.next ())
            {
                if  (resultSet.getBoolean ("liked"))
                {
                    users.add (userDAO.findById  (resultSet.getObject ("userID", UUID.class)));
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return users;
    }

    public List <User> findDislikedUsers (Post post)
    {
        List <User> users = new ArrayList <> ();
        String      sql   = "SELECT * FROM ContentsAction WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement  preparedStatement = connection.prepareStatement (sql))
        {
             preparedStatement.setObject (1, post.getID ());
            ResultSet    resultSet      =  preparedStatement.executeQuery ();
            UserDAOImpl userDAO = new UserDAOImpl ();
            while  (resultSet.next ())
            {
                if  (resultSet.getBoolean ("disliked"))
                {
                    users.add (userDAO.findById  (resultSet.getObject ("userID", UUID.class)));
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
