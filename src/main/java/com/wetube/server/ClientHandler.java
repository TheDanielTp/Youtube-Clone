// File: src/main/java/com/wetube/server/ClientHandler.java
package com.wetube.server;

import com.wetube.network.Request;
import com.wetube.network.Response;
import com.wetube.dao.impl.*;
import com.wetube.model.*;

import java.io.*;
import java.net.Socket;
import java.util.List;

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

            case "CREATE_USER":
            {
                return signUp ((User) data);
            }

            case "CHECK_USER":
            {
                return signIn ((Object[]) data);
            }

            case "UPDATE_USER":
            {
                return updateUser ((User) data);
            }

            case "DELETE_USER":
            {
                return deleteAccount ((User) data);
            }

            case "GET_ALL_USERS":
            {
                return getAllUsers ();
            }

            //endregion

            //region [ - Video Functions - ]

            case "CREATE_VIDEO":
            {
                return create ((Video) data);
            }

            case "UPDATE_VIDEO":
            {
                return update ((Video) data);
            }

            case "DELETE_VIDEO":
            {
                return delete ((Video) data);
            }

            case "GET_LIKED_VIDEOS":
            {
                return getLikedUsers ((Video) data);
            }

            //endregion

            //region [ - Post Functions - ]

            case "CREATE_POST":
            {
                return create ((Post) data);
            }

            case "UPDATE_POST":
            {
                return update ((Post) data);
            }

            case "DELETE_POST":
            {
                return delete ((Post) data);
            }

            case "GET_LIKED_USERS":
            {
                return getLikedUsers ((Post) data);
            }

            case "GET_DISLIKED_USERS":
            {
                return getDislikedUsers ((Post) data);
            }

            case "GET_USER_POSTS":
            {
                return getUserPosts ((User) data);
            }

            case "GET_ALL_POSTS":
            {
                return getAllPosts ();
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
        return new Response ("SUCCESS", "User created successfully", user);
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

        return new Response ("SUCCESS", "User checked successfully", user);
    }

    private Response updateUser (User user)
    {
        UserDAOImpl userDAO = new UserDAOImpl ();

        User findUserByUsername = userDAO.findByUsername (user.getUsername ());
        if (findUserByUsername == null)
        {
            return new Response ("ERROR", "User not found");
        }

        userDAO.update (user);
        return new Response ("SUCCESS", "User updated successfully", user);
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

    private Response getAllUsers ()
    {
        UserDAOImpl userDAO = new UserDAOImpl ();

        List<User> users = userDAO.findAll ();
        return new Response ("SUCCESS", "Users found successfully", users);
    }

    //endregion

    //region [ - Video Functions - ]

    private Response create (Video video)
    {
        VideoDAOImpl videoDAO = new VideoDAOImpl ();
        videoDAO.create (video);

        return new Response ("SUCCESS", "Video created successfully", video);
    }

    private Response update (Video video)
    {
        VideoDAOImpl videoDAO = new VideoDAOImpl ();
        videoDAO.update (video);

        return new Response ("SUCCESS", "Video updated successfully", null);
    }

    private Response delete (Video video)
    {
        VideoDAOImpl videoDAO = new VideoDAOImpl ();
        videoDAO.delete (video.getID ());

        return new Response ("SUCCESS", "Video deleted successfully", null);
    }

    private Response getLikedUsers (Video video)
    {
        VideoDAOImpl videoDAO = new VideoDAOImpl ();

        List<User> likedUsers = videoDAO.findLikedUsers (video);
        return new Response ("SUCCESS", "Liked users found successfully", likedUsers);
    }

    private Response getDislikedUsers (Video video)
    {
        VideoDAOImpl videoDAO = new VideoDAOImpl ();

        List<User> dislikedUsers = videoDAO.findDislikedUsers (video);
        return new Response ("SUCCESS", "Disliked users found successfully", dislikedUsers);
    }

    private Response getUserVideos (User user)
    {
        VideoDAOImpl videoDAO = new VideoDAOImpl ();

        List<Video> videos = videoDAO.findByUser (user.getID ());
        return new Response ("SUCCESS", "Videos found successfully", videos);
    }

    private Response getAllVideos ()
    {
        VideoDAOImpl videoDAO = new VideoDAOImpl ();

        List<Video> videos = videoDAO.findAll ();
        return new Response ("SUCCESS", "Videos found successfully", videos);
    }

    //endregion

    //region [ - Post Functions - ]

    private Response create (Post post)
    {
        PostDAOImpl postDAO = new PostDAOImpl ();

        postDAO.create (post);
        return new Response ("SUCCESS", "Post created successfully", post);
    }

    private Response update (Post post)
    {
        PostDAOImpl postDAO = new PostDAOImpl ();

        postDAO.update (post);
        return new Response ("SUCCESS", "Post updated successfully", post);
    }

    private Response delete (Post post)
    {
        PostDAOImpl postDAO = new PostDAOImpl ();

        postDAO.delete (post.getID ());
        return new Response ("SUCCESS", "Post deleted successfully", null);
    }

    private Response getLikedUsers (Post post)
    {
        PostDAOImpl postDAO = new PostDAOImpl ();

        List<User> likedUsers = postDAO.findLikedUsers (post);
        return new Response ("SUCCESS", "Liked users found successfully", likedUsers);
    }

    private Response getDislikedUsers (Post post)
    {
        PostDAOImpl postDAO = new PostDAOImpl ();

        List<User> dislikedUsers = postDAO.findDislikedUsers (post);
        return new Response ("SUCCESS", "Disliked users found successfully", dislikedUsers);
    }

    private Response getUserPosts (User user)
    {
        PostDAOImpl postDAO = new PostDAOImpl ();

        List<Post> posts = postDAO.findUserPosts (user.getID ());
        return new Response ("SUCCESS", "Posts found successfully", posts);
    }

    private Response getAllPosts ()
    {
        PostDAOImpl postDAO = new PostDAOImpl ();

        List<Post> posts = postDAO.findAll ();
        return new Response ("SUCCESS", "Posts found successfully", posts);
    }

    //endregion
}
