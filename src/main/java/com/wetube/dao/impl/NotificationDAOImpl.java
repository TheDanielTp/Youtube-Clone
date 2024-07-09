package com.wetube.dao.impl;

import com.wetube.model.Notification;
import com.wetube.model.Playlist;
import com.wetube.model.Video;
import com.wetube.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NotificationDAOImpl
{
    public UUID generateID ()
    {
        UUID uuid = UUID.randomUUID ();

        List <Notification> all = findAll ();
        for (Notification object : all)
        {
            if (object.getID () == uuid)
            {
                uuid = generateID ();
                break;
            }
        }
        return uuid;
    }

    public void create (Notification notification)
    {
        String sql = "INSERT INTO Notifications (ID, userID, contentID, title, receiveDate, isSeen) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement  preparedStatement = connection.prepareStatement (sql))
        {
             preparedStatement.setObject (1, notification.getID ());
             preparedStatement.setObject (2, notification.getUserID ());
             preparedStatement.setObject (3, notification.getContentID ());
             preparedStatement.setString (4, notification.getTitle ());
             preparedStatement.setTimestamp (5, Timestamp.valueOf (notification.getReceiveDate ()));
             preparedStatement.setBoolean (6, notification.isSeen ());
             preparedStatement.executeUpdate ();
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public void update (Notification notification)
    {
        String sql = "UPDATE Notifications SET userID = ?, contentID = ?, title = ?, receiveDate = ?, isSeen = ? WHERE ID = ?";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement  preparedStatement = connection.prepareStatement (sql))
        {
             preparedStatement.setObject (1, notification.getUserID ());
             preparedStatement.setObject (2, notification.getContentID ());
             preparedStatement.setString (3, notification.getTitle ());
             preparedStatement.setTimestamp (4, Timestamp.valueOf (notification.getReceiveDate ()));
             preparedStatement.setBoolean (5, notification.isSeen ());
             preparedStatement.setObject (6, notification.getID ());
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
            String deleteNotification = "DELETE FROM Notifications WHERE ID = '" + id + "'";
            statement.executeUpdate (deleteNotification);
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public Notification findById (UUID id)
    {
        String sql = "SELECT * FROM Notifications WHERE ID = ?";
        try (Connection connection = DatabaseConnection.getConnection ();
             PreparedStatement  preparedStatement = connection.prepareStatement (sql))
        {
             preparedStatement.setObject (1, id);
            ResultSet  resultSet =  preparedStatement.executeQuery ();
            if  (resultSet.next ())
            {
                return new Notification (
                         resultSet.getObject ("ID", UUID.class),
                         resultSet.getObject ("userID", UUID.class),
                         resultSet.getObject ("contentID", UUID.class),
                         resultSet.getString ("title"),
                         resultSet.getObject ("receiveDate", LocalDateTime.class),
                         resultSet.getBoolean ("isSeen")
                );
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return null;
    }

    public List <Notification> findAll ()
    {
        List <Notification> notifications = new ArrayList <> ();
        String       sql    = "SELECT * FROM Notifications";
        try (Connection connection = DatabaseConnection.getConnection ();
             Statement statement = connection.createStatement ();
             ResultSet  resultSet = statement.executeQuery (sql))
        {
            while  (resultSet.next ())
            {
                notifications.add (new Notification (
                         resultSet.getObject ("ID", UUID.class),
                         resultSet.getObject ("userID", UUID.class),
                         resultSet.getObject ("contentID", UUID.class),
                         resultSet.getString ("title"),
                         resultSet.getObject ("receiveDate", LocalDateTime.class),
                         resultSet.getBoolean ("isSeen")
                ));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return notifications;
    }
}
