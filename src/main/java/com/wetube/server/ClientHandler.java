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

            case "LIKE_VIDEO":
            {
                return likeVideo ((Object[]) data);
            }

            case "DISLIKE_VIDEO":
            {
                return dislikeVideo ((Object[]) data);
            }

            case "GET_VIDEO_LIKED_USERS":
            {
                return getLikedUsers ((Video) data);
            }

            case "GET_VIDEO_DISLIKED_USERS":
            {
                return getDislikedUsers ((Video) data);
            }

            case "GET_USER_VIDEOS":
            {
                return getUserVideos ((User) data);
            }

            case "GET_ALL_VIDEOS":
            {
                return getAllVideos ();
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

            case "LIKE_POST":
            {
                return likePost ((Object[]) data);
            }

            case "DISLIKE_POST":
            {
                return dislikePost ((Object[]) data);
            }

            case "GET_POST_LIKED_USERS":
            {
                return getLikedUsers ((Post) data);
            }

            case "GET_POST_DISLIKED_USERS":
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

            //region [ - Comment Functions - ]

            case "CREATE_COMMENT":
            {
                return create ((Comment) data);
            }

            case "UPDATE_COMMENT":
            {
                return update ((Comment) data);
            }

            case "DELETE_COMMENT":
            {
                return delete ((Comment) data);
            }

            case "LIKE_COMMENT":
            {
                return likeComment ((Object[]) data);
            }

            case "DISLIKE_COMMENT":
            {
                return dislikeComment ((Object[]) data);
            }

            case "GET_COMMENT_LIKED_USERS":
            {
                return getLikedUsers ((Comment) data);
            }

            case "GET_COMMENT_DISLIKED_USERS":
            {
                return getDislikedUsers ((Comment) data);
            }

            case "GET_ALL_COMMENTS":
            {
                return getAllComments ();
            }

            //endregion

            //region [ - Channel Functions - ]



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

        return new Response ("SUCCESS", "Video updated successfully", video);
    }

    private Response delete (Video video)
    {
        VideoDAOImpl videoDAO = new VideoDAOImpl ();
        videoDAO.delete (video.getID ());

        return new Response ("SUCCESS", "Video deleted successfully", null);
    }

    private Response likeVideo (Object[] data)
    {
        Video video = (Video) data[0];
        User user = (User) data[1];

        VideoDAOImpl videoDAO = new VideoDAOImpl ();
        if (videoDAO.findLikedUsers (video).contains (user))
        {
            videoDAO.removeLikeDislike (video, user);
            return new Response ("SUCCESS", "Like removed successfully", null);
        }
        else
        {
            videoDAO.like (video, user);
            return new Response ("SUCCESS", "Like added successfully", null);
        }
    }

    private Response dislikeVideo (Object[] data)
    {
        Video video = (Video) data[0];
        User user = (User) data[1];

        VideoDAOImpl videoDAO = new VideoDAOImpl ();
        if (videoDAO.findDislikedUsers (video).contains (user))
        {
            videoDAO.removeLikeDislike (video, user);
            return new Response ("SUCCESS", "Dislike removed successfully", null);
        }
        else
        {
            videoDAO.dislike (video, user);
            return new Response ("SUCCESS", "Dislike added successfully", null);
        }
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

    private Response likePost (Object[] data)
    {
        Post post = (Post) data[0];
        User user = (User) data[1];

        PostDAOImpl postDAO = new PostDAOImpl ();
        if (postDAO.findLikedUsers (post).contains (user))
        {
            postDAO.removeLikeDislike (post, user);
            return new Response ("SUCCESS", "Like removed successfully", null);
        }
        else
        {
            postDAO.like (post, user);
            return new Response ("SUCCESS", "Like added successfully", null);
        }
    }

    private Response dislikePost (Object[] data)
    {
        Post post = (Post) data[0];
        User user = (User) data[1];

        PostDAOImpl postDAO = new PostDAOImpl ();
        if (postDAO.findDislikedUsers (post).contains (user))
        {
            postDAO.removeLikeDislike (post, user);
            return new Response ("SUCCESS", "Dislike removed successfully", null);
        }
        else
        {
            postDAO.dislike (post, user);
            return new Response ("SUCCESS", "Dislike added successfully", null);
        }
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

    //region [ - Comment Functions - ]

    private Response create (Comment comment)
    {
        CommentDAOImpl commentDAO = new CommentDAOImpl ();
        commentDAO.create (comment);

        return new Response ("SUCCESS", "Comment created successfully", comment);
    }

    private Response update (Comment comment)
    {
        CommentDAOImpl commentDAO = new CommentDAOImpl ();
        commentDAO.update (comment);

        return new Response ("SUCCESS", "Comment updated successfully", comment);
    }

    private Response delete (Comment comment)
    {
        CommentDAOImpl commentDAO = new CommentDAOImpl ();
        commentDAO.delete (comment.getID ());

        return new Response ("SUCCESS", "Comment deleted successfully", null);
    }

    private Response likeComment (Object[] data)
    {
        Comment comment = (Comment) data[0];
        User user = (User) data[1];

        CommentDAOImpl commentDAO = new CommentDAOImpl ();
        if (commentDAO.findLikedUsers (comment).contains (user))
        {
            commentDAO.removeLikeDislike (comment, user);
            return new Response ("SUCCESS", "Like removed successfully", null);
        }
        else
        {
            commentDAO.like (comment, user);
            return new Response ("SUCCESS", "Like added successfully", null);
        }
    }

    private Response dislikeComment (Object[] data)
    {
        Comment comment = (Comment) data[0];
        User user = (User) data[1];

        CommentDAOImpl commentDAO = new CommentDAOImpl ();
        if (commentDAO.findDislikedUsers (comment).contains (user))
        {
            commentDAO.removeLikeDislike (comment, user);
            return new Response ("SUCCESS", "Dislike removed successfully", null);
        }
        else
        {
            commentDAO.dislike (comment, user);
            return new Response ("SUCCESS", "Dislike added successfully", null);
        }
    }

    private Response getLikedUsers (Comment comment)
    {
        CommentDAOImpl commentDAO = new CommentDAOImpl ();
        List<User> likedUsers = commentDAO.findLikedUsers (comment);

        return new Response ("SUCCESS", "Liked users found successfully", likedUsers);
    }

    private Response getDislikedUsers (Comment comment)
    {
        CommentDAOImpl commentDAO    = new CommentDAOImpl ();
        List <User>    dislikedUsers = commentDAO.findDislikedUsers (comment);

        return new Response ("SUCCESS", "Disliked users found successfully", dislikedUsers);
    }

    private Response getAllComments ()
    {
        CommentDAOImpl commentDAO = new CommentDAOImpl ();
        List<Comment> comments = commentDAO.findAll ();

        return new Response ("SUCCESS", "Comments found successfully", comments);
    }

    //endregion

    //region [ - Channel Functions - ]

    private Response create (Channel channel)
    {
        ChannelDAOImpl channelDAO = new ChannelDAOImpl ();

        Channel findChannelByName = channelDAO.findById (channel.getID ());
        if (findChannelByName != null)
        {
            return new Response ("ERROR", "Channel already exists");
        }

        channelDAO.create (channel);
        return new Response ("SUCCESS", "Channel created successfully", channel);
    }

    private Response update (Channel channel)
    {
        ChannelDAOImpl channelDAO = new ChannelDAOImpl ();
        channelDAO.update (channel);

        return new Response ("SUCCESS", "Channel updated successfully", channel);
    }

    private Response delete (Channel channel)
    {
        ChannelDAOImpl channelDAO = new ChannelDAOImpl ();
        channelDAO.delete (channel.getID ());

        return new Response ("SUCCESS", "Channel deleted successfully", null);
    }

//    private Response subscribe (Object[] data)
//    {
//        ChannelDAOImpl channelDAO = new ChannelDAOImpl ();
//
//
//    }

    //endregion
}
