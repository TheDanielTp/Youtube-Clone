package org.project.controller;

import com.wetube.client.Client;
import com.wetube.dao.impl.ChannelDAOImpl;
import com.wetube.dao.impl.UserDAOImpl;
import com.wetube.model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;

public class MainApplication extends Application
{
    public static  Socket         socket;
    private static BufferedReader bufferedReader;
    private static BufferedWriter bufferedWriter;

    UserDAOImpl userDAO = new UserDAOImpl ();
    public static Playlist currentPlaylist;
    public static Video    currentVideo;
    public static User     currentUser;
    public static Client   client;

    static boolean DarkTheme = true;

    public MainApplication ()
    {

    }

    public MainApplication (Client client)
    {
        MainApplication.client = client;
    }

    @Override
    public void start (Stage stage) throws Exception
    {
        FXMLLoader fxmlLoader = new FXMLLoader (MainApplication.class.getResource ("dark-front-view.fxml"));
        Scene      scene      = new Scene (fxmlLoader.load ());
        stage.setTitle ("WeTube");
        stage.setScene (scene);
        stage.setWidth (1280);
        stage.setHeight (720);
        stage.show ();
    }

    public static void main (String[] args) throws IOException
    {
        Socket socket = new Socket ("127.0.0.1", 12345);
        client = new Client (socket);
        launch ();
    }
}
