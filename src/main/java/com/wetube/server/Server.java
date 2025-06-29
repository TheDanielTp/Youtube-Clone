package com.wetube.server;

import com.wetube.network.Request;
import com.wetube.network.Response;
import com.wetube.dao.impl.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    private static final int          PORT = 12345;
    private              ServerSocket serverSocket;

    public Server ()
    {
        try
        {
            serverSocket = new ServerSocket (PORT);
            System.out.println ("Server is listening on port " + PORT);
        }
        catch (IOException e)
        {
            e.printStackTrace ();
        }
    }

    public void start ()
    {
        while (true)
        {
            try
            {
                Socket clientSocket = serverSocket.accept ();
                new ClientHandler (clientSocket).start ();
            }
            catch (IOException e)
            {
                e.printStackTrace ();
            }
        }
    }

    public static void main (String[] args)
    {
        Server server = new Server ();
        server.start ();
    }
}
