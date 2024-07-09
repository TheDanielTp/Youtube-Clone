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

    public Client (Socket socket)
    {
        try
        {
            this.socket = socket;
            out    = new ObjectOutputStream (socket.getOutputStream ());
            in     = new ObjectInputStream (socket.getInputStream ());
        }
        catch (IOException e)
        {
            close (socket, out, in);
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

    private static void close (Socket socket, ObjectOutputStream out, ObjectInputStream in)
    {
        try
        {
            if (socket != null)
            {
                socket.close ();
            }
            if (out != null)
            {
                out.close ();
            }
            if (in != null)
            {
                in.close ();
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException (e);
        }
    }

    //region [ - User Functions - ]

    public Object[] create (User user)
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

    public Object[] login (String username, String password)
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

    public Object[] update (User user)
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

    public Object[] delete (User user)
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

    public Object[] create (Video video)
    {
        Request request = new Request ("CREATE_VIDEO", video);
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    public Object[] update (Video video)
    {
        Request request = new Request ("UPDATE_VIDEO", video);
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    public Object[] delete (Video video)
    {
        Request request = new Request ("DELETE_VIDEO", video);
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    public Object[] like (Video video, User user)
    {
        Request request = new Request ("LIKE_VIDEO", new Object[]{video, user});
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    public Object[] dislike (Video video, User user)
    {
        Request request = new Request ("DISLIKE_VIDEO", new Object[]{video, user});
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    public Object[] getLikedUsers (Video video)
    {
        Request request = new Request ("GET_VIDEO_LIKED_USERS", video);
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    public Object[] getDislikedUsers (Video video)
    {
        Request request = new Request ("GET_VIDEO_DISLIKED_USERS", video);
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    public Object[] getUserVideos (User user)
    {
        Request request = new Request ("GET_USER_VIDEOS", user);
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    public Object[] getAllVideos ()
    {
        Request request = new Request ("GET_ALL_VIDEOS", null);
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    //endregion

    //region [ - Post Functions - ]

    public Object[] create (Post post)
    {
        Request request = new Request ("CREATE_POST", post);
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    public Object[] update (Post post)
    {
        Request request = new Request ("UPDATE_POST", post);
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    public Object[] delete (Post post)
    {
        Request request = new Request ("DELETE_POST", post);
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    public Object[] like (Post post, User user)
    {
        Request request = new Request ("LIKE_POST", new Object[]{post, user});
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    public Object[] dislike (Post post, User user)
    {
        Request request = new Request ("DISLIKE_POST", new Object[]{post, user});
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    public Object[] getLikedUsers (Post post)
    {
        Request request = new Request ("GET_POST_LIKED_USERS", post);
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    public Object[] getDislikedUsers (Post post)
    {
        Request request = new Request ("GET_POST_DISLIKED_USERS", post);
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

    //region [ - Comment Functions - ]

    public Object[] create (Comment comment)
    {
        Request request = new Request ("CREATE_COMMENT", comment);
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    public Object[] update (Comment comment)
    {
        Request request = new Request ("UPDATE_COMMENT", comment);
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    public Object[] delete (Comment comment)
    {
        Request request = new Request ("DELETE_COMMENT", comment);
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    public Object[] like (Comment comment, User user)
    {
        Request request = new Request ("LIKE_COMMENT", new Object[]{comment, user});
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    public Object[] dislike (Comment comment, User user)
    {
        Request request = new Request ("DISLIKE_COMMENT", new Object[]{comment, user});
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    public Object[] getLikedUsers (Comment comment)
    {
        Request request = new Request ("GET_POST_LIKED_USERS", comment);
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    public Object[] getDislikedUsers (Comment comment)
    {
        Request request = new Request ("GET_POST_DISLIKED_USERS", comment);
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    public Object[] getAllComments ()
    {
        Request request = new Request ("GET_ALL_COMMENTS", null);
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    //endregion

    //region [ - Channel Functions - ]

    public Object[] create (Channel channel)
    {
        Request request = new Request ("CREATE_CHANNEL", channel);
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    public Object[] subscribe (User user, Channel channel)
    {
        Request request = new Request ("SUBSCRIBE", new Object[]{user, channel});
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    public Object[] findSubscribers (Channel channel)
    {
        Request request = new Request ("FIND_SUBSCRIBERS", channel);
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }

    //endregion

    //region [ - Category Functions - ]

    public Object[] create (Category category)
    {
        Request request = new Request ("CREATE_CATEGORY", category);
        Response response = sendRequest (request);

        return new Object[]{0, response.getData ()};
    }
}