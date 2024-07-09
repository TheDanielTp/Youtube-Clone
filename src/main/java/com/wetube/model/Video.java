package com.wetube.model;

import com.wetube.dao.impl.VideoDAOImpl;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Video extends Content implements Serializable
{
    //region [ - Attributes - ]

    UUID categoryID;

    String title;
    String description;
    String dataType;

    String videoURL;
    String thumbnailURL;

    int commentsCount;

    //endregion

    //region [ - Constructor - ]

    public Video (UUID creatorID, UUID categoryID, UUID channelID, String title, String description, String videoURL, String thumbnailURL, boolean isOnlyComrade)
    {
        super (creatorID, channelID, isOnlyComrade);

        VideoDAOImpl videoDAO = new VideoDAOImpl();
        ID = videoDAO.generateID ();

        this.categoryID  = categoryID;
        this.title        = title;
        this.description  = description;
        this.videoURL     = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    public Video (UUID ID, UUID creatorID, UUID categoryID, UUID channelID, int likesCount, int dislikesCount, LocalDateTime creationDate, boolean isOnlyComrade, String title, String description, String dataType, String videoURL, String thumbnailURL, int commentsCount)
    {
        super (ID, creatorID, channelID, likesCount, dislikesCount, creationDate, isOnlyComrade);
        this.categoryID   = categoryID;
        this.title         = title;
        this.description   = description;
        this.dataType      = dataType;
        this.videoURL      = videoURL;
        this.thumbnailURL  = thumbnailURL;
        this.commentsCount = commentsCount;
    }

    //endregion

    //region [ - Getters & Setters - ]

    public UUID getCategoryID ()
    {
        return categoryID;
    }

    public void setCategoryID (UUID categoryID)
    {
        this.categoryID = categoryID;
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

    public String getDataType ()
    {
        return dataType;
    }

    public void setDataType (String dataType)
    {
        this.dataType = dataType;
    }

    public String getVideoURL ()
    {
        return videoURL;
    }

    public void setVideoURL (String videoURL)
    {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL ()
    {
        return thumbnailURL;
    }

    public void setThumbnailURL (String thumbnailURL)
    {
        this.thumbnailURL = thumbnailURL;
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
