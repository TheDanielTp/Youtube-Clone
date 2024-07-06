package org.project.youtube.models.comment;

import org.project.youtube.models.Content;

import java.time.LocalDateTime;
import java.util.UUID;

public class Comment extends Content
{
    //region [ - Attributes - ]

    UUID contentID;
    UUID parentCommentID;

    String content;

    int replyCount;

    boolean isReply;

    //endregion

    //region [ - Constructor - ]

    public Comment ()
    {
        super("comment");
    }

    public Comment (UUID contentID, UUID creatorID, String content, boolean isReply)
    {
        super (creatorID, creatorID, false);

        this.contentID = contentID;
        this.content   = content;
        this.isReply   = isReply;

        replyCount = 0;
    }

    public Comment (UUID contentID, UUID creatorID, UUID parentCommentID, String content, boolean isReply)
    {
        super (creatorID, creatorID, false);

        this.contentID       = contentID;
        this.parentCommentID = parentCommentID;
        this.content         = content;
        this.isReply         = isReply;

        replyCount = 0;
    }

    //endregion

    //region [ - Getters & Setters - ]

    public UUID getContentID ()
    {
        return contentID;
    }

    public void setContentID (UUID contentID)
    {
        this.contentID = contentID;
    }

    public UUID getParentCommentID ()
    {
        return parentCommentID;
    }

    public void setParentCommentID (UUID parentCommentID)
    {
        this.parentCommentID = parentCommentID;
    }

    public String getContent ()
    {
        return content;
    }

    public void setContent (String content)
    {
        this.content = content;
    }

    public int getReplyCount ()
    {
        return replyCount;
    }

    public void setReplyCount (int replyCount)
    {
        this.replyCount = replyCount;
    }

    public boolean isReply ()
    {
        return isReply;
    }

    public void setReply (boolean reply)
    {
        isReply = reply;
    }

    //endregion
}
