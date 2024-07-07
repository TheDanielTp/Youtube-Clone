package com.wetube.models.playlist;

import com.wetube.models.ClassInfo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class Playlist extends ClassInfo
{
    //region [ - Attributes - ]

    UUID ID;
    UUID creatorID;
    UUID channelID;

    String title;
    String description;

    private ArrayList <UUID> videosID;

    boolean isPublic;
    boolean isOnlyComrade;

    LocalDateTime creationDate;

    //endregion

    //region [ - Constructor - ]

    public Playlist ()
    {
        super.className = "playlist";
    }

    public Playlist (UUID creatorID, UUID channelID, String title, String description, boolean isPublic, boolean isOnlyComrade)
    {
        this.creatorID     = creatorID;
        this.channelID     = channelID;
        this.title         = title;
        this.description   = description;
        this.isPublic      = isPublic;
        this.isOnlyComrade = isOnlyComrade;

        creationDate = LocalDateTime.now ();
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

    public UUID getCreatorID ()
    {
        return creatorID;
    }

    public void setCreatorID (UUID creatorID)
    {
        this.creatorID = creatorID;
    }

    public UUID getChannelID ()
    {
        return channelID;
    }

    public void setChannelID (UUID channelID)
    {
        this.channelID = channelID;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public ArrayList <UUID> getVideosID ()
    {
        return videosID;
    }

    public void setVideosID (ArrayList <UUID> videosID)
    {
        this.videosID = videosID;
    }

    public boolean isPublic ()
    {
        return isPublic;
    }

    public void setPublic (boolean aPublic)
    {
        isPublic = aPublic;
    }

    public boolean isOnlyComrade ()
    {
        return isOnlyComrade;
    }

    public void setOnlyComrade (boolean onlyComrade)
    {
        isOnlyComrade = onlyComrade;
    }

    public LocalDateTime getCreationDate ()
    {
        return creationDate;
    }

    public void setCreationDate (LocalDateTime creationDate)
    {
        this.creationDate = creationDate;
    }

    //endregion
}
