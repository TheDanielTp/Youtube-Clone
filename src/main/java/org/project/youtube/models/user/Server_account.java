package org.project.youtube.models.user;

import org.project.youtube.database.objects.UserDAO;
import org.project.youtube.models.ServerResponse;

public class Server_account extends Account
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
            return sign_up ();
        }
        else if (request.equals ("change_email"))
        {
            return sign_up ();
        }
        else if (request.equals ("change_password"))
        {
            return sign_up ();
        }
        else if (request.equals ("change_username"))
        {
            return sign_up ();
        }
        return null;
    }

    private ServerResponse change_name ()
    {
        return null;
    }

    private ServerResponse change_email ()
    {
        return null;
    }

    private ServerResponse change_password ()
    {
        return null;
    }

    private ServerResponse change_username ()
    {
        return null;
    }

    private ServerResponse login ()
    {
        System.out.println ("[SERVER] : user wants to login");
        System.out.println ("username : " + super.getUsername ());
        System.out.println ("password : " + super.getPassword ());
        return UserDAO.login (super.getUsername (), super.getPassword (), super.getRequest_id ());
    }

    private ServerResponse sign_up ()
    {
        return UserDAO.sign_up (super.getUsername (), super.getPassword (), super.getEmail (), super.getRequest_id ());
    }
}