package com.wetube.model;

import com.wetube.dao.impl.NotificationDAOImpl;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Notification implements Serializable
{
    //region [ - Attributes - ]

    UUID ID;
    UUID userID;
    UUID contentID;

    String title;

    LocalDateTime receiveDate;

    boolean isSeen;

    //endregion

    //region [ - Constructor - ]

    public Notification (UUID userID, UUID contentID, String title)
    {
        NotificationDAOImpl notificationDAO = new NotificationDAOImpl();
        ID = notificationDAO.generateID ();

        this.userID    = userID;
        this.contentID = contentID;
        this.title     = title;

        receiveDate = LocalDateTime.now ();

        isSeen = false;
    }

    public Notification (UUID ID, UUID userID, UUID contentID, String title, LocalDateTime receiveDate, boolean isSeen)
    {
        this.ID = ID;
        this.userID    = userID;
        this.contentID = contentID;
        this.title     = title;
        this.receiveDate = receiveDate;
        this.isSeen = isSeen;
    }

    //endregion

    //region [ - Getters & Setters - ]

    public UUID getID ()
    {
        return ID;
    }

    public void setID (UUID ID)
    {
        this.ID = ID;
    }

    public UUID getUserID ()
    {
        return userID;
    }

    public void setUserID (UUID userID)
    {
        this.userID = userID;
    }

    public UUID getContentID ()
    {
        return contentID;
    }

    public void setContentID (UUID contentID)
    {
        this.contentID = contentID;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public LocalDateTime getReceiveDate ()
    {
        return receiveDate;
    }

    public void setReceiveDate (LocalDateTime receiveDate)
    {
        this.receiveDate = receiveDate;
    }

    public boolean isSeen ()
    {
        return isSeen;
    }

    public void setSeen (boolean seen)
    {
        isSeen = seen;
    }

    //endregion
}
