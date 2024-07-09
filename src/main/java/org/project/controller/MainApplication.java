package org.project.controller;

import com.wetube.client.Client;
import com.wetube.dao.impl.ChannelDAOImpl;
import com.wetube.dao.impl.UserDAOImpl;
import com.wetube.model.Category;
import com.wetube.model.Channel;
import com.wetube.model.User;
import com.wetube.model.Video;
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

    UserDAOImpl userDAO = new UserDAOImpl();
    static Video currentVideo;
    static User   currentUser;
    static Client client;

    static boolean DarkTheme = false;

    public MainApplication ()
    {

    }

    public MainApplication (Client client)
    {
        this.client = client;
    }

    @Override
    public void start (Stage stage) throws Exception
    {
        UserDAOImpl userDAO = new UserDAOImpl();
        currentUser = userDAO.findByUsername ("TheDanielTp");

        FXMLLoader fxmlLoader = new FXMLLoader (MainApplication.class.getResource ("communist-front-view.fxml"));
        Scene      scene      = new Scene (fxmlLoader.load ());
        stage.setTitle ("WeTube");
        stage.setScene (scene);
        stage.setWidth (960);
        stage.setHeight (540);
        stage.show ();
    }

    public static void main (String[] args) throws IOException
    {
        Socket socket = new Socket ("127.0.0.1", 12345);
        client = new Client (socket);
        launch ();
    }
}
