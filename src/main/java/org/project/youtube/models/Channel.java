package org.project.youtube.models;

import javafx.scene.image.Image;

import java.time.LocalDate;
import java.util.UUID;

public class Channel
{
    //region [ - Attributes - ]

    private UUID ID;
    private UUID userID;

    private String name;
    private String description;

    private int subscribersCount;
    private int videosCount;
    private int totalViews;
    private int watchTime;

    private LocalDate creationDate;

    private boolean isVerified;

    private Double outcome;

    private Image channelPic;

    //endregion

    //region [ - Constructor - ]

    public Channel (String name, String description)
    {
        this.name        = name;
        this.description = description;

        subscribersCount = 0;
        videosCount = 0;
        totalViews = 0;
        watchTime = 0;

        creationDate = LocalDate.now ();

        outcome = 0.0;
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

    public int getVideosCount ()
    {
        return videosCount;
    }

    public void setVideosCount (int videosCount)
    {
        this.videosCount = videosCount;
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

    public Image getChannelPic ()
    {
        return channelPic;
    }

    public void setChannelPic (Image channelPic)
    {
        this.channelPic = channelPic;
    }

    //endregion
}
