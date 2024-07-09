package com.wetube.dao.impl;

import com.wetube.model.Channel;
import com.wetube.model.Comment;
import com.wetube.model.User;
import com.wetube.util.DatabaseConnection;
import javafx.scene.image.Image;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChannelDAOImpl
{
    public UUID generateID ()
    {
        UUID uuid = UUID.randomUUID ();

        List <Channel> all = findAll ();
        for (Channel object : all)
        {
            if (object.getID () == uuid)
            {
                uuid = generateID ();
                break;
            }
        }
        return uuid;
    }

    public void create (Channel channel)
    {
        String sql = "INSERT INTO Channels (ID, userID, name, description, subscribersCount, totalVideos, totalViews," +
                " watchTime, creationDate, isVerified, outcome, channelPicture) VALUES" +
                " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection= DatabaseConnection.getConnection ();
             PreparedStatement preparedStatement = connection.prepareStatement (sql))
        {
            preparedStatement.setObject (1, channel.getID ());
            preparedStatement.setObject (2, channel.getUserID ());
            preparedStatement.setString (3, channel.getName ());
            preparedStatement.setString (4, channel.getDescription ());
            preparedStatement.setInt (5, channel.getSubscribersCount ());
            preparedStatement.setInt (6, channel.getTotalVideos ());
            preparedStatement.setInt (7, channel.getTotalViews ());
            preparedStatement.setInt (8, channel.getWatchTime ());
            preparedStatement.setDate (9, Date.valueOf (channel.getCreationDate ()));
            preparedStatement.setBoolean (10, channel.isVerified ());
            preparedStatement.setDouble (11, channel.getOutcome ());
            preparedStatement.setBytes (12, channel.getChannelPictureURL () != null ?
                    channel.getChannelPictureURL ().toString ().getBytes () : null);
            preparedStatement.executeUpdate ();
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public void update (Channel channel)
    {
        String sql = "UPDATE Channels SET userID = ?, name = ?, description = ?, subscribersCount = ?," +
                " totalVideos = ?, totalViews = ?, watchTime = ?, creationDate = ?, isVerified = ?, outcome = ?," +
                " channelPicture = ? WHERE ID = ?";
        try (Connection connection= DatabaseConnection.getConnection ();
             PreparedStatement preparedStatement = connection.prepareStatement (sql))
        {
            preparedStatement.setObject (1, channel.getUserID ());
            preparedStatement.setString (2, channel.getName ());
            preparedStatement.setString (3, channel.getDescription ());
            preparedStatement.setInt (4, channel.getSubscribersCount ());
            preparedStatement.setInt (5, channel.getTotalVideos ());
            preparedStatement.setInt (6, channel.getTotalViews ());
            preparedStatement.setInt (7, channel.getWatchTime ());
            preparedStatement.setDate (8, Date.valueOf (channel.getCreationDate ()));
            preparedStatement.setBoolean (9, channel.isVerified ());
            preparedStatement.setDouble (10, channel.getOutcome ());
            preparedStatement.setBytes (11, channel.getChannelPictureURL () != null ?
                    channel.getChannelPictureURL ().toString ().getBytes () : null);
            preparedStatement.setObject (12, channel.getID ());
            preparedStatement.executeUpdate ();
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public void delete (UUID id)
    {
        try (Connection connection= DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ())
        {
            String deletePosts       = "DELETE FROM Posts WHERE channelID = '" + id + "'";
            String deleteCommunity   = "DELETE FROM Communities WHERE channelID = '" + id + "'";

            statement.executeUpdate (deleteCommunity);
            statement.executeUpdate (deletePosts);

            String deleteChannel = "DELETE FROM Channels WHERE ID = '" + id + "'";
            statement.executeUpdate (deleteChannel);

        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public void subscribe (User user, Channel channel)
    {
        String sql = "INSERT INTO Subscribers (userID, channelID) VALUES (?, ?)";
        try (Connection connection= DatabaseConnection.getConnection ();
             PreparedStatement preparedStatement = connection.prepareStatement (sql))
        {
            preparedStatement.setObject (1, user.getID ());
            preparedStatement.setObject (2, channel.getID ());
            preparedStatement.executeUpdate ();
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public void unsubscribe (User user, Channel channel)
    {
        try (Connection connection= DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ())
        {
            String unsubscribe = "DELETE FROM Subscriber WHERE userID = '" + user.getID () +
                    "' && channelID = '" + channel.getID () + "'";
            statement.executeUpdate (unsubscribe);
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public Channel findById (UUID id)
    {
        String sql = "SELECT * FROM Channels WHERE ID = ?";
        try (Connection connection= DatabaseConnection.getConnection ();
             PreparedStatement preparedStatement = connection.prepareStatement (sql))
        {
            preparedStatement.setObject (1, id);
            ResultSet resultSet= preparedStatement.executeQuery ();
            if (resultSet.next ())
            {
                return new Channel (
                        resultSet.getObject ("ID", UUID.class),
                        resultSet.getObject ("userID", UUID.class),
                        resultSet.getString ("name"),
                        resultSet.getString ("description"),
                        resultSet.getInt ("subscribersCount"),
                        resultSet.getInt ("totalVideos"),
                        resultSet.getInt ("totalViews"),
                        resultSet.getInt ("watchTime"),
                        findSubscribers(id),
                        resultSet.getObject ("creationDate", LocalDate.class),
                        resultSet.getBoolean ("isVerified"),
                        resultSet.getDouble ("outcome"),
                        resultSet.getString ("channelPicture")
                        );
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return null;
    }

    public Channel findByName (String name)
    {
        String sql = "SELECT * FROM Channels WHERE name = ?";
        try (Connection connection= DatabaseConnection.getConnection ();
             PreparedStatement preparedStatement = connection.prepareStatement (sql))
        {
            preparedStatement.setObject (1, name);
            ResultSet resultSet= preparedStatement.executeQuery ();
            if (resultSet.next ())
            {
                return new Channel (
                        resultSet.getObject ("ID", UUID.class),
                        resultSet.getObject ("userID", UUID.class),
                        resultSet.getString ("name"),
                        resultSet.getString ("description"),
                        resultSet.getInt ("subscribersCount"),
                        resultSet.getInt ("totalVideos"),
                        resultSet.getInt ("totalViews"),
                        resultSet.getInt ("watchTime"),
                        findSubscribers(resultSet.getObject ("ID", UUID.class)),
                        resultSet.getObject ("creationDate", LocalDate.class),
                        resultSet.getBoolean ("isVerified"),
                        resultSet.getDouble ("outcome"),
                        resultSet.getString ("channelPicture")
                );
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return null;
    }

    public ArrayList <UUID> findSubscribers (UUID id)
    {
        ArrayList <UUID> subscribersID = new ArrayList <> ();
        String         sql             = "SELECT * FROM Subscribers";
        try (Connection connection= DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ();
             ResultSet resultSet= statement.executeQuery (sql))
        {
            while (resultSet.next ())
            {
                if (id.equals (resultSet.getObject ("channelID", UUID.class)))
                {
                    subscribersID.add (resultSet.getObject ("userID", UUID.class));
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return subscribersID;
    }

    public ArrayList <UUID> findOnlyComradeSubscribers (UUID id)
    {
        Boolean isOnlyComrade = true;
        ArrayList <UUID> subscribersID = new ArrayList <> ();
        String         sql             = "SELECT * FROM Subscribers";
        try (Connection connection= DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ();
             ResultSet resultSet= statement.executeQuery (sql))
        {
            while (resultSet.next ())
            {
                if (id.equals (resultSet.getObject ("channelID", UUID.class)) &&
                        isOnlyComrade.equals (resultSet.getBoolean ("isOnlyComrade")))
                {
                    subscribersID.add (resultSet.getObject ("userID", UUID.class));
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return subscribersID;
    }

    public Boolean checkSubscription (UUID channelID, UUID userID)
    {
        String sql = "SELECT * FROM subscribers";
        try (Connection connection= DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ();
             ResultSet resultSet= statement.executeQuery (sql))
        {
            while (resultSet.next ())
            {
                if (channelID.equals (resultSet.getObject ("channelID", UUID.class)) &&
                        userID.equals (resultSet.getObject ("userID", UUID.class)))
                {
                    return true;
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return false;
    }

    public Boolean checkOnlyComrade (UUID channelID, UUID userID)
    {
        Boolean isOnlyComrade = true;
        String sql = "SELECT * FROM subscribers";
        try (Connection connection= DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ();
             ResultSet resultSet= statement.executeQuery (sql))
        {
            while (resultSet.next ())
            {
                if (channelID.equals (resultSet.getObject ("channelID", UUID.class)) &&
                        userID.equals (resultSet.getObject ("userID", UUID.class)) &&
                        isOnlyComrade.equals (resultSet.getBoolean ("isOnlyComrade")))
                {
                    return true;
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return false;
    }

    public List <Channel> findAll ()
    {
        List <Channel> channels = new ArrayList <> ();
        String         sql      = "SELECT * FROM Channels";
        try (Connection connection = DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ();
             ResultSet resultSet = statement.executeQuery (sql))
        {
            while (resultSet.next ())
            {
                channels.add (new Channel (
                        resultSet.getObject ("ID", UUID.class),
                        resultSet.getObject ("userID", UUID.class),
                        resultSet.getString ("name"),
                        resultSet.getString ("description"),
                        resultSet.getInt ("subscribersCount"),
                        resultSet.getInt ("totalVideos"),
                        resultSet.getInt ("totalViews"),
                        resultSet.getInt ("watchTime"),
                        findSubscribers (resultSet.getObject ("ID", UUID.class)),
                        resultSet.getObject ("creationDate", LocalDate.class),
                        resultSet.getBoolean ("isVerified"),
                        resultSet.getDouble ("outcome"),
                        resultSet.getString ("channelPicture")
                ));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return channels;
    }

    public ArrayList <UUID> findAllPosts (UUID id)
    {
        ArrayList <UUID> postsID = new ArrayList <> ();
        String         sql       = "SELECT * FROM posts";
        try (Connection connection= DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ();
             ResultSet resultSet= statement.executeQuery (sql))
        {
            while (resultSet.next ())
            {
                if (id.equals (resultSet.getObject ("channelID", UUID.class)))
                {
                    postsID.add (resultSet.getObject ("ID", UUID.class));
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return postsID;
    }

    public ArrayList <UUID> findAllVideos (UUID id)
    {
        ArrayList <UUID> videosID = new ArrayList <> ();
        String         sql        = "SELECT * FROM videos";
        try (Connection connection= DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ();
             ResultSet resultSet= statement.executeQuery (sql))
        {
            while (resultSet.next ())
            {
                if (id.equals (resultSet.getObject ("channelID", UUID.class)))
                {
                    videosID.add (resultSet.getObject ("ID", UUID.class));
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
