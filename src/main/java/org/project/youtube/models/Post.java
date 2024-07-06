package org.project.youtube.models;

import javafx.scene.image.Image;

import java.util.UUID;

public class Post extends Content
{
    //region [ - Attributes - ]

    UUID communityID;

    String title;
    String description;

    Image image;

    int commentsCount;

    //endregion

    //region [ - Constructor - ]

    public Post (UUID creatorID, UUID communityID, UUID channelID, String title, String description, Image image, boolean isOnlyComrade)
    {
        super (creatorID, channelID, isOnlyComrade);

        this.communityID = communityID;
        this.title = title;
        this.description = description;
        this.image = image;

        commentsCount = 0;
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

    public Image getImage ()
    {
        return image;
    }

    public void setImage (Image image)
    {
        this.image = image;
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
