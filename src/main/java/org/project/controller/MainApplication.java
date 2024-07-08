package org.project.controller;

import com.wetube.client.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class MainApplication extends Application
{
    public static  Socket         socket;
    private static BufferedReader bufferedReader;
    private static BufferedWriter bufferedWriter;

    static Client client;

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
        FXMLLoader fxmlLoader = new FXMLLoader (MainApplication.class.getResource ("dark-front-view.fxml"));
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
