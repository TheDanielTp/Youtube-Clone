package org.project.youtube.models;

import java.util.UUID;

public class Category
{
    //region [ - Attributes - ]

    UUID   ID;
    String title;

    //endregion

    //region [ - Constructor - ]

    public Category (String title)
    {
        this.title = title;
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

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    //endregion
}
