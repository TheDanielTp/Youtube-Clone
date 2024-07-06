package org.project.youtube.models;

import java.util.UUID;

public class Community
{
    //region [ - Attributes - ]

    UUID ID;
    UUID channelID;

    //endregion

    //region [ - Constructor - ]

    public Community (UUID channelID)
    {
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
