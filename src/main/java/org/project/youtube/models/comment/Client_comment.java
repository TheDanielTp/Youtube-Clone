package org.project.youtube.models.comment;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class Client_comment
{
    private Comment          comment = new Comment ();
    private DataOutputStream out;
    private ObjectMapper     mapper  = new ObjectMapper ();
    private String           jsonString;

    public Client_comment (DataOutputStream out)
    {
        this.out = out;
    }

    public void put_comment (String message, UUID post_id, int request_id)
    {
        comment.setRequest ("put_comment");
        comment.setRequest_id (request_id);
        comment.setContent (message);
        comment.setContentID (post_id);
        send_request ();
    }

    public void reply_comment (String message, UUID post_id, UUID comment_id, int request_id)
    {
        comment.setRequest ("reply_comment");
        comment.setRequest_id (request_id);
        comment.setContent (message);
        comment.setContentID (post_id);
        comment.setParentCommentID (comment_id);
        send_request ();
    }

    public void edit_comment (String new_message, UUID comment_id, int request_id)
    {
        comment.setRequest ("edit_comment");
        comment.setRequest_id (request_id);
        comment.setID (comment_id);
        comment.setContent (new_message);
        send_request ();
    }

    public void delete_comment (UUID comment_id, int request_id)
    {
        comment.setRequest ("delete_comment");
        comment.setRequest_id (request_id);
        comment.setID (comment_id);
        send_request ();
    }

    public void load_comments (UUID post_id, int request_id)
    {
        comment.setRequest ("load_comments");
        comment.setRequest_id (request_id);
        comment.setContentID (post_id);
        send_request ();
    }


    private void send_request ()
    {
        try
        {
            jsonString = mapper.writerWithDefaultPrettyPrinter ().writeValueAsString (comment);
            out.writeUTF (jsonString);
        }
        catch (IOException e)
        {
            System.out.println (e.getMessage ());
        }
        finally
        {
            comment = new Comment ();
        }
    }

}