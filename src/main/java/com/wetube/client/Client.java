package com.wetube.client;

import com.wetube.network.Request;
import com.wetube.network.Response;
import com.wetube.model.*;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class Client
{
    private Socket             socket;
    private ObjectInputStream  in;
    private ObjectOutputStream out;

    public Client (String serverAddress, int port)
    {
        try
        {
            socket = new Socket (serverAddress, port);
            out    = new ObjectOutputStream (socket.getOutputStream ());
            in     = new ObjectInputStream (socket.getInputStream ());
        }
        catch (IOException e)
        {
            e.printStackTrace ();
        }
    }

    public Response sendRequest (Request request)
    {
        try
        {
            out.writeObject (request);
            return (Response) in.readObject ();
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace ();
            return null;
        }
    }

    public Object[] signUp (User user)
    {
        Request  request  = new Request ("SIGN_UP", user);
        Response response = sendRequest (request);

        if (response.getStatus ().equals ("ERROR"))
        {
            if (response.getMessage ().equals ("Username already exists"))
            {
                return new Object[]{1, null};
            }
            if (response.getMessage ().equals ("Email already exists"))
            {
                return new Object[]{2, null};
            }
        }
        else if (response.getStatus ().equals ("SUCCESS"))
        {
            return new Object[]{0, response.getData ()};
        }
        return null;
    }

    public Object[] signIn (String username, String password)
    {
        Request  request  = new Request ("SIGN_IN", new Object[]{username, password});
        Response response = sendRequest (request);

        if (response.getStatus ().equals ("ERROR"))
        {
            if (response.getMessage ().equals ("Username not found"))
            {
                return new Object[]{1, null};
            }
            if (response.getMessage ().equals ("Wrong password"))
            {
                return new Object[]{2, null};
            }
        }
        else if (response.getStatus ().equals ("SUCCESS"))
        {
            return new Object[]{0, response.getData ()};
        }

        return null;
    }

    public Object[] deleteAccount (UUID user)
    {
        Request request = new Request ("DELETE_ACCOUNT", user);
        Response response = sendRequest (request);

        if (response.getStatus ().equals ("ERROR"))
        {

        }
    }
}