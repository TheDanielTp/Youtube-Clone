package com.wetube.models.user;

import com.wetube.database.objects.UserDAO;
import com.wetube.models.ServerResponse;

public class UserServer extends User
{
    @Override
    public ServerResponse handle_request ()
    {
        if (request.equals ("login"))
        {
            return login ();
        }
        else if (request.equals ("sign_up"))
        {
            return sign_up ();
        }
        else if (request.equals ("change_name"))
        {
            return change_name ();
        }
        else if (request.equals ("change_email"))
        {
            return change_email ();
        }
        else if (request.equals ("change_password"))
        {
            return change_password ();
        }
        else if (request.equals ("change_username"))
        {
            return change_username ();
        }
        return null;
    }

    private ServerResponse change_name ()
    {
        System.out.println ("> Server: user wants to change name");

        return UserDAO.changeName (super.getRequest_id (), super.getID (), super.getNewFirstName (), super.getNewLastName ());
    }

    private ServerResponse change_email ()
    {
        System.out.println ("> Server: user wants to change email");

        return UserDAO.changeEmail (super.getRequest_id (), super.getID (), super.getNewEmail ());
    }

    private ServerResponse change_password ()
    {
        System.out.println ("> Server: user wants to change password");

        return UserDAO.changePassword (super.getRequest_id (), super.getID (), super.getNewPassword ());
    }

    private ServerResponse change_username ()
    {
        System.out.println ("> Server: user wants to change username");

        return UserDAO.changeUsername (super.getRequest_id (), super.getID (), super.getNewUsername ());
    }

    private ServerResponse login ()
    {
        System.out.println ("> Server: user wants to login");
        System.out.println ("> username: " + super.getUsername ());
        System.out.println ("> password: " + super.getPassword ());

        return UserDAO.login (super.getRequest_id (), super.getUsername (), super.getPassword ());
    }

    private ServerResponse sign_up ()
    {
        System.out.println ("> Server: user wants to signup");
        System.out.println ("> username: " + super.getUsername ());
        System.out.println ("> email: " + super.getEmail ());
        System.out.println ("> password: " + super.getPassword ());

        return UserDAO.signup (super.getRequest_id (), super.getUsername (), super.getEmail (), super.getPassword (),
                super.getFirstName (), super.getLastName (), super.getBirthdate (), super.getProfilePictureURL ());
    }
}