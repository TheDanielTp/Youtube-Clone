package org.project.youtube;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class MainApplication extends Application
{
    public static  Socket         socket;
    public static  User           user;
    private static BufferedReader bufferedReader;
    private static BufferedWriter bufferedWriter;

    public MainApplication ()
    {

    }

    public MainApplication (Socket socket)
    {
        try
        {
            MainApplication.socket = socket;
            bufferedReader            = new BufferedReader (new InputStreamReader (socket.getInputStream ()));
            bufferedWriter            = new BufferedWriter (new OutputStreamWriter (socket.getOutputStream ()));
        }
        catch (IOException e)
        {
            close (socket, bufferedReader, bufferedWriter);
        }
    }

    public static String receiveResponse ()
    {
        String response = null;
        try
        {
            response = bufferedReader.readLine ();
        }
        catch (IOException e)
        {
            throw new RuntimeException (e);
        }
        return response;
    }

    private static void close (Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter)
    {
        try
        {
            if (socket != null)
            {
                socket.close ();
            }
            if (bufferedReader != null)
            {
                bufferedReader.close ();
            }
            if (bufferedWriter != null)
            {
                bufferedWriter.close ();
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException (e);
        }
    }

    @Override
    public void start (Stage stage) throws Exception
    {
        FXMLLoader fxmlLoader = new FXMLLoader (MainApplication.class.getResource ("communist-front-view.fxml"));
        Scene scene = new Scene (fxmlLoader.load ());
        stage.setTitle ("WeTube");
        stage.setScene (scene);
        stage.setWidth (960);
        stage.setHeight (540);
        stage.show();
    }

    private void write (String content)
    {
        try
        {
            bufferedWriter.write (content);
            bufferedWriter.newLine ();
            bufferedWriter.flush ();
        }
        catch (IOException e)
        {
            throw new RuntimeException (e);
        }
    }

    public static void main(String[] args) throws IOException
    {
        Socket socket = new Socket ("localhost", 12345);
        MainApplication client = new MainApplication (socket);
        launch ();
    }
}
