package org.project.youtube.models.user;

import javafx.scene.image.Image;
import org.project.youtube.models.ClassInfo;

import java.time.LocalDate;
import java.util.UUID;

public class Account extends ClassInfo
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

    Image profilePicture;

    //endregion

    //region [ - Constructor - ]

    public Account ()
    {
        super.className = "account";
    }

    public Account (String firstName, String lastName, String username, String email, String password, LocalDate birthdate)
    {
        this.firstName      = firstName;
        this.lastName       = lastName;
        this.username       = username;
        this.email          = email;
        this.password       = password;
        this.birthdate      = birthdate;

        joinDate = LocalDate.now ();

        isPremium = false;

        balance = 0.0;
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

    public Image getProfilePicture ()
    {
        return profilePicture;
    }

    public void setProfilePicture (Image profilePicture)
    {
        this.profilePicture = profilePicture;
    }

    //endregion
}
