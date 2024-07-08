package com.wetube.model;

import com.wetube.dao.impl.ChannelDAOImpl;
import javafx.scene.image.Image;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

public class Channel implements Serializable
{
    //region [ - Attributes - ]

    private UUID ID;
    private UUID userID;

    private String name;
    private String description;

    private int subscribersCount;
    private int totalVideos;
    private int totalViews;
    private int watchTime;

    private ArrayList<UUID> subscribersID;

    private LocalDate creationDate;

    private boolean isVerified;

    private Double outcome;

    private String channelPictureURL;

    //endregion

    //region [ - Constructor - ]

    public Channel (String name, String description)
    {
        ChannelDAOImpl channelDAO = new ChannelDAOImpl();
        ID = channelDAO.generateID ();
        userID = ID;

        this.name        = name;
        this.description = description;

        subscribersCount = 0;
        totalVideos      = 0;
        totalViews       = 0;
        watchTime        = 0;

        creationDate = LocalDate.now ();

        outcome = 0.0;
    }

    public Channel (UUID ID, UUID userID, String name, String description, int subscribersCount, int totalVideos, int totalViews, int watchTime, ArrayList <UUID> subscribersID, LocalDate creationDate, boolean isVerified, Double outcome, String channelPictureURL)
    {
        this.ID                = ID;
        this.userID            = userID;
        this.name              = name;
        this.description       = description;
        this.subscribersCount  = subscribersCount;
        this.totalVideos       = totalVideos;
        this.totalViews        = totalViews;
        this.watchTime         = watchTime;
        this.subscribersID     = subscribersID;
        this.creationDate     = creationDate;
        this.isVerified       = isVerified;
        this.outcome           = outcome;
        this.channelPictureURL = channelPictureURL;
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

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public int getSubscribersCount ()
    {
        return subscribersCount;
    }

    public void setSubscribersCount (int subscribersCount)
    {
        this.subscribersCount = subscribersCount;
    }

    public int getTotalVideos ()
    {
        return totalVideos;
    }

    public void setTotalVideos (int totalVideos)
    {
        this.totalVideos = totalVideos;
    }

    public int getTotalViews ()
    {
        return totalViews;
    }

    public void setTotalViews (int totalViews)
    {
        this.totalViews = totalViews;
    }

    public int getWatchTime ()
    {
        return watchTime;
    }

    public void setWatchTime (int watchTime)
    {
        this.watchTime = watchTime;
    }

    public ArrayList <UUID> getSubscribersID ()
    {
        return subscribersID;
    }

    public void setSubscribersID (ArrayList <UUID> subscribersID)
    {
        this.subscribersID = subscribersID;
    }

    public LocalDate getCreationDate ()
    {
        return creationDate;
    }

    public void setCreationDate (LocalDate creationDate)
    {
        this.creationDate = creationDate;
    }

    public boolean isVerified ()
    {
        return isVerified;
    }

    public void setVerified (boolean verified)
    {
        isVerified = verified;
    }

    public Double getOutcome ()
    {
        return outcome;
    }

    public void setOutcome (Double outcome)
    {
        this.outcome = outcome;
    }

    public String getChannelPictureURL ()
    {
        return channelPictureURL;
    }

    public void setChannelPictureURL (String channelPictureURL)
    {
        this.channelPictureURL = channelPictureURL;
    }

    //endregion
}
