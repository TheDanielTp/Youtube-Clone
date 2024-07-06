package org.project.youtube.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Content
{
    //region [ - Attributes - ]

    UUID ID;
    UUID creatorID;
    UUID channelID;

    int likesCount;
    int dislikesCounts;

    LocalDateTime creationDate;

    boolean isOnlyComrade;

    //endregion

    //region [ - Constructor - ]

    public Content (UUID creatorID, UUID channelID, boolean isOnlyComrade)
    {
        this.creatorID     = creatorID;
        this.channelID     = channelID;
        this.isOnlyComrade = isOnlyComrade;

        creationDate  = LocalDateTime.now ();

        likesCount     = 0;
        dislikesCounts = 0;
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

    public int getLikesCount ()
    {
        return likesCount;
    }

    public void setLikesCount (int likesCount)
    {
        this.likesCount = likesCount;
    }

    public int getDislikesCounts ()
    {
        return dislikesCounts;
    }

    public void setDislikesCounts (int dislikesCounts)
    {
        this.dislikesCounts = dislikesCounts;
    }

    public LocalDateTime getCreationDate ()
    {
        return creationDate;
    }

    public void setCreationDate (LocalDateTime creationDate)
    {
        this.creationDate = creationDate;
    }

    public boolean isOnlyComrade ()
    {
        return isOnlyComrade;
    }

    public void setOnlyComrade (boolean onlyComrade)
    {
        isOnlyComrade = onlyComrade;
    }

    //endregion
}
