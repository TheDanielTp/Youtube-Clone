package org.project.youtube.models;

import javafx.scene.image.Image;

import java.util.UUID;

public class Video extends Content
{
    //region [ - Attributes - ]

    UUID communityID;

    String title;
    String description;

    String videoURL;
    Image thumbnail;

    int commentsCount;

    //endregion

    //region [ - Constructor - ]

    public Video (UUID creatorID, UUID communityID, UUID channelID, String title, String description, String videoURL, boolean isOnlyComrade)
    {
        super (creatorID, channelID, isOnlyComrade);

        this.communityID = communityID;
        this.title = title;
        this.description = description;
        this.videoURL = videoURL;
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

    public String getVideoURL ()
    {
        return videoURL;
    }

    public void setVideoURL (String videoURL)
    {
        this.videoURL = videoURL;
    }

    public Image getThumbnail ()
    {
        return thumbnail;
    }

    public void setThumbnail (Image thumbnail)
    {
        this.thumbnail = thumbnail;
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
