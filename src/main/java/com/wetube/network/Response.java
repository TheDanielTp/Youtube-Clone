package com.wetube.network;

import java.io.Serializable;

public class Response implements Serializable
{
    private String status;
    private String message;
    private Object data;

    public Response (String status, String message)
    {
        this.status = status;
        this.message = message;
    }

    public Response (String status, String message, Object data)
    {
        this.status = status;
        this.message = message;
        this.data   = data;
    }

    public String getStatus ()
    {
        return status;
    }

    public String getMessage ()
    {
        return message;
    }

    public Object getData ()
    {
        return data;
    }
}