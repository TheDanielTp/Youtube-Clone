package org.project.youtube.models.channel;

import org.project.youtube.models.ClassInfo;
import org.project.youtube.models.ServerResponse;

public class Server_channel extends ClassInfo
{
    @Override
    public ServerResponse handle_request ()
    {
        if (request.equals ("subscribe"))
        {
            return subscribe ();
        }
        else if (request.equals ("unsubscribe"))
        {
            return unsubscribe ();
        }
        else if (request.equals ("get_subscribers"))
        {
            return get_subscribers ();
        }
        else if (request.equals ("change_channel_description"))
        {
            return change_channel_description ();
        }
        else if (request.equals ("change_channel_name"))
        {
            return change_channel_name ();
        }
        return null;
    }

    private ServerResponse change_channel_name ()
    {
        return null;
    }

    private ServerResponse change_channel_description ()
    {
        return null;
    }

    private ServerResponse get_subscribers ()
    {
        return null;
    }

    private ServerResponse subscribe ()
    {
        return null;
    }

    private ServerResponse unsubscribe ()
    {
        return null;
    }

}
