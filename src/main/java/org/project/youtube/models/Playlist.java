package org.project.youtube.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Playlist
{
    //region [ - Attributes - ]

    UUID ID;
    UUID creatorID;
    UUID channelID;

    String title;
    String description;

    boolean isPublic;
    boolean isOnlyComrade;

    LocalDateTime creationDate;

    //endregion

    //region [ - Constructor - ]

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
