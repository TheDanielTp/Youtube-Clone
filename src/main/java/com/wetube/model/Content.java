package com.wetube.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Content
{
    //region [ - Attributes - ]

    UUID ID;
    UUID creatorID;
    UUID channelID;

    int likesCount;
    int dislikesCount;

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

        likesCount    = 0;
        dislikesCount = 0;
    }

    public Content (UUID ID, UUID creatorID, UUID channelID, int likesCount, int dislikesCount, LocalDateTime creationDate, boolean isOnlyComrade)
    {
        this.ID            = ID;
        this.creatorID     = creatorID;
        this.channelID     = channelID;
        this.likesCount    = likesCount;
        this.dislikesCount = dislikesCount;
        this.creationDate  = creationDate;
        this.isOnlyComrade = isOnlyComrade;
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

    public int getDislikesCount ()
    {
        return dislikesCount;
    }

    public void setDislikesCount (int dislikesCount)
    {
        this.dislikesCount = dislikesCount;
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
