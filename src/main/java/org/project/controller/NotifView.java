package org.project.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.text.View;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class NotifView implements Initializable
{
    @FXML
    VBox notifBox;

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle)
    {
        FXMLLoader loader;
        if (! MainApplication.DarkTheme)
        {
            loader = new FXMLLoader (getClass ().getResource ("notif.fxml"));
        }
        else
        {
            loader = new FXMLLoader (getClass ().getResource ("dark-notif.fxml"));
        }
        AnchorPane notifNode = null;
        try
        {
            notifNode = loader.load ();
        }
        catch (IOException e)
        {
            throw new RuntimeException (e);
        }

        Text channelName = (Text) notifNode.lookup ("#channelName");
        Text date        = (Text) notifNode.lookup ("#date");

        channelName.setText (MainApplication.currentUser.getUsername ());
        date.setText (String.valueOf (LocalDate.now ()));

        notifBox.getChildren ().add (notifNode);
    }
}
