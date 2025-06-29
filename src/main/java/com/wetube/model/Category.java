package com.wetube.model;

import com.wetube.dao.impl.CategoryDAOImpl;

import java.io.Serializable;
import java.util.UUID;

public class Category implements Serializable
{
    //region [ - Attributes - ]

    UUID   ID;
    String title;

    //endregion

    //region [ - Constructor - ]

    public Category (String title)
    {
        CategoryDAOImpl categoryDAO = new CategoryDAOImpl();
        ID = categoryDAO.generateID ();

        this.title = title;
    }

    public Category (UUID ID, String title)
    {
        this.ID = ID;
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
