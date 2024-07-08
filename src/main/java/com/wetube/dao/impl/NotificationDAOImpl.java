package com.wetube.dao.impl;

import com.wetube.model.Notification;
import com.wetube.model.Playlist;
import com.wetube.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.UUID;

public class NotificationDAOImpl
{
    public void create (Notification notification)
    {
        String sql = "INSERT INTO Notifications (ID, userID, contentID, title, receiveDate, isSeen) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, notification.getID ());
            pstmt.setObject (2, notification.getUserID ());
            pstmt.setObject (3, notification.getContentID ());
            pstmt.setString (4, notification.getTitle ());
            pstmt.setTimestamp (5, Timestamp.valueOf (notification.getReceiveDate ()));
            pstmt.setBoolean (6, notification.isSeen ());
            pstmt.executeUpdate ();
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public void update (Notification notification)
    {
        String sql = "UPDATE Notifications SET userID = ?, contentID = ?, title = ?, receiveDate = ?, isSeen = ? WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, notification.getUserID ());
            pstmt.setObject (2, notification.getContentID ());
            pstmt.setString (3, notification.getTitle ());
            pstmt.setTimestamp (4, Timestamp.valueOf (notification.getReceiveDate ()));
            pstmt.setBoolean (5, notification.isSeen ());
            pstmt.setObject (6, notification.getID ());
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
            String deleteNotification = "DELETE FROM Notifications WHERE ID = '" + id + "'";
            stmt.executeUpdate (deleteNotification);
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
    }

    public Notification findById (UUID id)
    {
        String sql = "SELECT * FROM Notifications WHERE ID = ?";
        try (Connection conn = DatabaseConnection.getConnection ();
             PreparedStatement pstmt = conn.prepareStatement (sql))
        {
            pstmt.setObject (1, id);
            ResultSet rs = pstmt.executeQuery ();
            if (rs.next ())
            {
                return new Notification (
                        rs.getObject ("ID", UUID.class),
                        rs.getObject ("userID", UUID.class),
                        rs.getObject ("contentID", UUID.class),
                        rs.getString ("title"),
                        rs.getObject ("receiveDate", LocalDateTime.class),
                        rs.getBoolean ("isSeen")
                );
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace ();
        }
        return null;
    }
}
