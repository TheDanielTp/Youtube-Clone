package com.wetube.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Post extends Content
{
    //region [ - Attributes - ]

    UUID communityID;

    String title;
    String description;

    String imageURL;

    int commentsCount;

    //endregion

    //region [ - Constructor - ]

    public Post (UUID creatorID, UUID communityID, UUID channelID, String title, String description, String imageURL, boolean isOnlyComrade)
    {
        super (creatorID, channelID, isOnlyComrade);

        this.communityID = communityID;
        this.title       = title;
        this.description = description;
        this.imageURL    = imageURL;

        commentsCount = 0;
    }

    public Post (UUID ID, UUID creatorID, UUID communityID, UUID channelID, int likesCount, int dislikesCount, LocalDateTime creationDate, boolean isOnlyComrade, String title, String description, String imageURL, int commentsCount)
    {
        super (ID, creatorID, channelID, likesCount, dislikesCount, creationDate, isOnlyComrade);
        this.communityID   = communityID;
        this.title         = title;
        this.description   = description;
        this.imageURL      = imageURL;
        this.commentsCount = commentsCount;
    }

    //endregion

    //region [ - Getters & Setters - ]

    public UUID getCommunityID ()
    {
        return communityID;
    }

    public void setCommunityID (UUID communityID)
    {
        this.communityID = communityID;
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

    public String getImageURL ()
    {
        return imageURL;
    }

    public void setImageURL (String imageURL)
    {
        this.imageURL = imageURL;
    }

    public int getCommentsCount ()
    {
        return commentsCount;
    }

    public void setCommentsCount (int commentsCount)
    {
        this.commentsCount = commentsCount;
    }

    //endregion
}
