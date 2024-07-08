// File: src/main/java/com/wetube/server/ClientHandler.java
package com.wetube.server;

import com.wetube.network.Request;
import com.wetube.network.Response;
import com.wetube.dao.impl.*;
import com.wetube.model.*;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.UUID;

public class ClientHandler extends Thread
{
    private Socket             socket;
    private ObjectInputStream  in;
    private ObjectOutputStream out;

    public ClientHandler (Socket socket)
    {
        this.socket = socket;
        try
        {
            out = new ObjectOutputStream (socket.getOutputStream ());
            in  = new ObjectInputStream (socket.getInputStream ());
        }
        catch (IOException e)
        {
            e.printStackTrace ();
        }
    }

    public void run ()
    {
        try
        {
            Request request;
            while ((request = (Request) in.readObject ()) != null)
            {
                Response response = handleRequest (request);
                out.writeObject (response);
            }
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace ();
        }
        finally
        {
            try
            {
                socket.close ();
            }
            catch (IOException e)
            {
                e.printStackTrace ();
            }
        }
    }

    private Response handleRequest (Request request)
    {
        String type = request.getType ();
        Object data = request.getData ();

        switch (type)
        {
            //region [ - User Functions - ]

            case "SIGN_UP":
            {
                return signUp ((User) data);
            }

            case "SIGN_IN":
            {
                return signIn ((Object[]) data);
            }

            case "DELETE_ACCOUNT":
            {
                return deleteAccount((User) data);
            }

            //endregion

            default:
            {
                return new Response ("ERROR", "Unknown request type");
            }
        }
    }

    //region [ - User Functions - ]

    private Response signUp (User user)
    {
        UserDAOImpl userDAO = new UserDAOImpl ();

        User findUserByUsername = userDAO.findByUsername (user.getUsername ());
        if (findUserByUsername != null)
        {
            return new Response ("ERROR", "Username already exists");
        }
        User findUserByEmail = userDAO.findByEmail (user.getEmail ());
        if (findUserByEmail != null)
        {
            return new Response ("ERROR", "Email already exists");
        }

        userDAO.create (user);
        return new Response ("SUCCESS", "User signed up successfully", user);
    }

    private Response signIn (Object[] data)
    {
        String username = (String) data[0];
        String password = (String) data[1];

        UserDAOImpl userDAO = new UserDAOImpl ();

        User user = userDAO.findByUsername (username);
        if (user == null)
        {
            return new Response ("ERROR", "User not found");
        }
        else if (! user.getPassword ().equals (password))
        {
            return new Response ("ERROR", "Wrong password");
        }

        return new Response ("SUCCESS", "User signed in successfully", user);
    }

    private Response deleteAccount (User user)
    {
        UserDAOImpl userDAO = new UserDAOImpl ();

        User findUserByUsername = userDAO.findByUsername (user.getUsername ());
        if (findUserByUsername == null)
        {
            return new Response ("ERROR", "User not found");
        }

        userDAO.delete (user.getID ());
        return new Response ("SUCCESS", "User deleted successfully", null);
    }

    //endregion
}
