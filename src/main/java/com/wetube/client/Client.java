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

    //region [ - User Functions - ]

    public Object[] signUp (User user)
    {
        Request  request  = new Request ("CREATE_USER", user);
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
        Request  request  = new Request ("CHECK_USER", new Object[]{username, password});
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

    public Object[] updateUser (User user)
    {
        Request request  = new Request ("UPDATE_USER", user);
        Response response = sendRequest (request);

        if (response.getStatus ().equals ("ERROR"))
        {
            if (response.getMessage ().equals ("User not found"))
            {
                return new Object[]{1, null};
            }
        }
        else if (response.getStatus ().equals ("SUCCESS"))
        {
            return new Object[]{0, response.getData ()};
        }
        return null;
    }

    public Object[] deleteAccount (User user)
    {
        Request request = new Request ("DELETE_USER", user);
        Response response = sendRequest (request);

        if (response.getStatus ().equals ("ERROR"))
        {
            if (response.getMessage ().equals ("User not found"))
            {
                return new Object[]{1, null};
            }
        }
        else if (response.getStatus ().equals ("SUCCESS"))
        {
            return new Object[]{0, response.getData ()};
        }
        return null;
    }

    public Object[] getAllUsers ()
    {
        Request request = new Request ("GET_ALL_USERS", null);
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    //endregion

    //region [ - Video Functions - ]

    public Object[] createVideo (Video video)
    {
        Request request = new Request ("CREATE_VIDEO", video);
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    public Object[] updateVideo (Video video)
    {
        Request request = new Request ("UPDATE_VIDEO", video);
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    public Object[] deleteVideo (Video video)
    {
        Request request = new Request ("DELETE_VIDEO", video);
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    //endregion

    //region [ - Post Functions - ]

    public Object[] createPost (Post post)
    {
        Request request = new Request ("CREATE_POST", post);
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    public Object[] updatePost (Post post)
    {
        Request request = new Request ("UPDATE_POST", post);
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    public Object[] deletePost (Post post)
    {
        Request request = new Request ("DELETE_POST", post);
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    public Object[] getLikedUsers (Post post)
    {
        Request request = new Request ("GET_LIKED_USERS", post);
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    public Object[] getDislikedUsers (Post post)
    {
        Request request = new Request ("GET_DISLIKED_USERS", post);
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    public Object[] getUserPosts (User user)
    {
        Request request = new Request ("GET_USER_POSTS", user);
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    public Object[] getAllPosts ()
    {
        Request request = new Request ("GET_ALL_POSTS", null);
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    //endregion
}