package com.wetube.model;

import com.wetube.dao.impl.CommentDAOImpl;
import com.wetube.dao.impl.CommunityDAOImpl;

import java.io.Serializable;
import java.util.UUID;

public class Community implements Serializable
{
    //region [ - Attributes - ]

    UUID ID;
    UUID channelID;

    //endregion

    //region [ - Constructor - ]

    public Community (UUID channelID)
    {
        CommunityDAOImpl communityDAO = new CommunityDAOImpl ();
        ID = communityDAO.generateID ();

        this.channelID = channelID;
    }

    public Community (UUID ID, UUID channelID)
    {
        this.ID = ID;
        this.channelID = channelID;
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

    public UUID getChannelID ()
    {
        return channelID;
    }

    public void setChannelID (UUID channelID)
    {
        this.channelID = channelID;
    }

    //endregion
}
