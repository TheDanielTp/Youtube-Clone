package com.wetube.models.video;

import com.wetube.models.ServerResponse;

public class Server_video extends Video
{
    @Override
    public ServerResponse handle_request ()
    {
        if (request.equals ("send_video_info"))
        {
            return insert_video_info ();
        }
        else if (request.equals ("get_video_info"))
        {
            return send_video_info ();
        }
        else if (request.equals ("get_videos_id"))
        {
            return send_videos_id ();
        }
        else if (request.equals ("change_channel_photo"))
        {
            return change_channel_photo ();
        }
        else if (request.equals ("change_profile_photo"))
        {
            return change_profile_photo ();
        }
        else if (request.equals ("change_thumbnail"))
        {
            return change_thumbnail ();
        }
        else if (request.equals ("change_playlist_photo"))
        {
            return change_playlist_photo ();
        }
        else if (request.equals ("search"))
        {
            return search ();
        }
        else if (request.equals ("like"))
        {
            return like ();
        }
        else if (request.equals ("dislike"))
        {
            return dislike ();
        }
        return null;
    }

    private ServerResponse dislike ()
    {
        return null;
    }

    private ServerResponse like ()
    {
        return null;
    }

    private ServerResponse search ()
    {
        return null;
    }

    private ServerResponse change_playlist_photo ()
    {
        return null;
    }

    private ServerResponse change_thumbnail ()
    {
        return null;
    }

    private ServerResponse change_profile_photo ()
    {
        return null;
    }

    private ServerResponse change_channel_photo ()
    {
        return null;
    }

    private ServerResponse insert_video_info ()
    {
        System.out.println (super.getTitle ());

        ServerResponse serverResponse = new ServerResponse ();
        serverResponse.setRequest_id (super.getRequest_id ());

//        PostDAO.add_post (UUID.fromString (super.getVideoURL ()), super.getChannelID (), super.getTitle (), super.getDescription (), UUID.randomUUID (), true);
        serverResponse.add_part ("status", "received");
//        notification.upload_post();
        return serverResponse;
    }

    private ServerResponse send_video_info ()
    {
//        return PostDAO.get_post (UUID.fromString (super.getVideoURL ()), super.getRequest_id ());
        return null;
    }

    private ServerResponse send_videos_id ()
    {
//        return PostDAO.get_all_posts (super.getRequest_id ());
        return null;
    }
}
