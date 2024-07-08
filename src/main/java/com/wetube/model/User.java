package com.wetube.model;

import com.wetube.dao.impl.UserDAOImpl;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public class User implements Serializable
{
    //region [ - Attributes - ]

    UUID ID;
    UUID channelID;

    String firstName;
    String lastName;
    String username;
    String email;
    String password;

    LocalDate birthdate;
    LocalDate joinDate;

    boolean isPremium;

    Double balance;

    String profilePictureURL;

    //endregion

    //region [ - Constructor - ]

    public User (String firstName, String lastName, String username, String email, String password, LocalDate birthdate)
    {
        UserDAOImpl userDAO = new UserDAOImpl();

        ID = userDAO.generateID();
        channelID = ID;

        this.firstName = firstName;
        this.lastName  = lastName;
        this.username  = username;
        this.email     = email;
        this.password  = password;
        this.birthdate = birthdate;

        joinDate = LocalDate.now ();

        isPremium = false;

        balance = 0.0;

        try
        {
            profilePictureURL = userDAO.chooseRandomImage ();
        }
        catch (IOException e)
        {
            throw new RuntimeException (e);
        }
    }

    public User (UUID ID, UUID channelID, String firstName, String lastName, String username, String email, String password, LocalDate birthdate, LocalDate joinDate, boolean isPremium, Double balance, String profilePictureURL)
    {
        this.ID                = ID;
        this.channelID         = channelID;
        this.firstName         = firstName;
        this.lastName          = lastName;
        this.username          = username;
        this.email             = email;
        this.password          = password;
        this.birthdate         = birthdate;
        this.joinDate          = joinDate;
        this.isPremium         = isPremium;
        this.balance           = balance;
        this.profilePictureURL = profilePictureURL;
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

    public String getFirstName ()
    {
        return firstName;
    }

    public void setFirstName (String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName ()
    {
        return lastName;
    }

    public void setLastName (String lastName)
    {
        this.lastName = lastName;
    }

    public String getUsername ()
    {
        return username;
    }

    public void setUsername (String username)
    {
        this.username = username;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getPassword ()
    {
        return password;
    }

    public void setPassword (String password)
    {
        this.password = password;
    }

    public LocalDate getBirthdate ()
    {
        return birthdate;
    }

    public void setBirthdate (LocalDate birthdate)
    {
        this.birthdate = birthdate;
    }

    public LocalDate getJoinDate ()
    {
        return joinDate;
    }

    public void setJoinDate (LocalDate joinDate)
    {
        this.joinDate = joinDate;
    }

    public boolean isPremium ()
    {
        return isPremium;
    }

    public void setPremium (boolean premium)
    {
        isPremium = premium;
    }

    public Double getBalance ()
    {
        return balance;
    }

    public void setBalance (Double balance)
    {
        this.balance = balance;
    }

    public String getProfilePictureURL ()
    {
        return profilePictureURL;
    }

    public void setProfilePictureURL (String profilePictureURL)
    {
        this.profilePictureURL = profilePictureURL;
    }

    //endregion
}
