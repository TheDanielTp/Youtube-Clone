package org.project.youtube.models.notification;

import org.project.youtube.server.Client_Handler;
import org.project.youtube.models.ServerResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Server_notification
{
    private ServerResponse                   serverResponse = new ServerResponse ();
    private Notification                     notification   = new Notification ();
    private HashMap <String, Client_Handler> online_clients = new HashMap <> ();

    public Server_notification (HashMap <String, Client_Handler> online_clients)
    {
        serverResponse.setResponse_type ("notification");
        this.online_clients = online_clients;
    }

    public void upload_post (ArrayList <String> user_ids, UUID post_id)
    {
        notification.setTitle ("upload_post");
        notification.setContentID (post_id);
        serverResponse.getNotifications_list ().add (notification);

        for (String user_id : user_ids)
        {
            if (online_clients.get (user_id) != null)
            {
                online_clients.get (user_id).send_response (serverResponse);
            }
        }
    }

    public void like_post (UUID user_id, UUID post_id, String post_owner_id)
    {
        notification.setTitle ("like_post");
        notification.setContentID (post_id);
        notification.setUserID (user_id);
        serverResponse.getNotifications_list ().add (notification);


        if (online_clients.get (post_owner_id) != null)
        {
            online_clients.get (post_owner_id).send_response (serverResponse);
        }
    }

    public void like_comment (UUID user_id, UUID comment_id, String comment_owner_id)
    {
        notification.setTitle ("like_comment");
        notification.setContentID (comment_id);
        notification.setUserID (user_id);
        serverResponse.getNotifications_list ().add (notification);

        if (online_clients.get (comment_owner_id) != null)
        {
            online_clients.get (comment_owner_id).send_response (serverResponse);
        }
    }

    public void put_comment (UUID user_id, UUID comment_id, String post_owner_id)
    {
        notification.setTitle ("put_comment");
        notification.setContentID (comment_id);
        notification.setUserID (user_id);
        serverResponse.getNotifications_list ().add (notification);

        if (online_clients.get (post_owner_id) != null)
        {
            online_clients.get (post_owner_id).send_response (serverResponse);
        }
    }

    public void reply_comment (UUID user_id, UUID comment_id, String comment_owner_id)
    {
        notification.setTitle ("reply_comment");
        notification.setContentID (comment_id);
        notification.setUserID (user_id);
        serverResponse.getNotifications_list ().add (notification);

        if (online_clients.get (comment_owner_id) != null)
        {
            online_clients.get (comment_owner_id).send_response (serverResponse);
        }

    }
}