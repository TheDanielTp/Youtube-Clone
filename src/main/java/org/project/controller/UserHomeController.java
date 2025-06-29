package org.project.controller;

import com.wetube.model.Video;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class UserHomeController implements Initializable
{
    //region------------------------------------------------Stage Size Functions------------------------------------------------

    @FXML
    FlowPane videosPane;

    @FXML
    FlowPane lastVideosPane;

    @FXML
    FlowPane reverseVideosPane;

    @FXML
    FlowPane popularVideosPane;

    @FXML
    ScrollPane videoScrollPane;

    @FXML
    public void editButtonClick (ActionEvent event) throws IOException
    {
        Parent root;
        if (! MainApplication.DarkTheme)
        {
            root = FXMLLoader.load (Objects.requireNonNull (getClass ().getResource ("communist-user-edit.fxml")));
        }
        else
        {
            root = FXMLLoader.load (Objects.requireNonNull (getClass ().getResource ("dark-user-edit.fxml")));
        }
        Stage stage = (Stage) ((Node) event.getSource ()).getScene ().getWindow ();

        double width  = stage.getWidth ();
        double height = stage.getHeight ();
        double x      = stage.getX ();
        double y      = stage.getY ();

        Scene scene = new Scene (root);

        stage.setScene (scene);

        stage.setWidth (width);
        stage.setHeight (height);
        stage.setX (x);
        stage.setY (y);

        System.out.println ("> Front: opening setting page");
    }

    @FXML
    public void createPost (ActionEvent event) throws IOException
    {
        Parent root;
        if (! MainApplication.DarkTheme)
        {
            root = FXMLLoader.load (Objects.requireNonNull (getClass ().getResource ("upload-post-page.fxml")));
        }
        else
        {
            root = FXMLLoader.load (Objects.requireNonNull (getClass ().getResource ("dark-upload-post-page.fxml")));
        }

        Stage stage = new Stage ();

        double x = stage.getX ();
        double y = stage.getY ();

        Scene scene = new Scene (root);

        stage.setScene (scene);

        stage.setWidth (650);
        stage.setHeight (450);
        stage.setX (x);
        stage.setY (y);

        System.out.println ("> Front: opening upload page");
        stage.show ();
    }

    public void addBalance (ActionEvent event) throws IOException
    {
        Parent root;
        if (! MainApplication.DarkTheme)
        {
            root = FXMLLoader.load (Objects.requireNonNull (getClass ().getResource ("add-balance.fxml")));
        }
        else
        {
            root = FXMLLoader.load (Objects.requireNonNull (getClass ().getResource ("dark-add-balance.fxml")));
        }

        Stage stage = new Stage ();

        double x = stage.getX ();
        double y = stage.getY ();

        Scene scene = new Scene (root);

        stage.setScene (scene);

        stage.setWidth (650);
        stage.setHeight (450);
        stage.setX (x);
        stage.setY (y);

        System.out.println ("> Front: opening balance page");
        stage.show ();
    }

    public void initialize (URL url, ResourceBundle resourceBundle)
    {
        Object[]     responseObject = MainApplication.client.getUserVideos (MainApplication.currentUser);
        List <Video> videos         = (List <Video>) responseObject[1];
        List <Video> popularVideos  = videos;
        popularVideos.sort ((v1, v2) -> Integer.compare (v2.getLikesCount (), v1.getLikesCount ()));

        if (videos.isEmpty ())
        {
            //show no videos
        }
        else
        {
            for (Video video : videos)
            {
                try
                {
                    FXMLLoader loader    = new FXMLLoader (getClass ().getResource ("/org/project/controller/video-thumbnail-view.fxml"));
                    AnchorPane videoNode = loader.load ();

                    ImageView thumbnail   = (ImageView) videoNode.lookup ("#thumbnailFile");
                    Label     title       = (Label) videoNode.lookup ("#title");
                    Button    videoButton = (Button) videoNode.lookup ("#videoButton");
                    Label     duration    = (Label) videoNode.lookup ("#duration");

                    Media media;
                    File  tempFile;
                    try
                    {
                        File   videoFile = new File (video.getVideoURL ());
                        byte[] fileBytes = Files.readAllBytes (Path.of (videoFile.toURI ()));

                        tempFile = File.createTempFile ("videoFile", ".mp4");
                        tempFile.deleteOnExit ();

                        // Write the byte array to the temporary videoFile
                        try (FileOutputStream fos = new FileOutputStream (tempFile); ByteArrayInputStream bais = new ByteArrayInputStream (fileBytes))
                        {
                            byte[] buffer = new byte[1024];
                            int    length;
                            while ((length = bais.read (buffer)) != - 1)
                            {
                                fos.write (buffer, 0, length);
                            }
                        }

                    }
                    catch (IOException e)
                    {
                        e.printStackTrace ();
                        return;
                    }

                    media = new Media (tempFile.toURI ().toString ());

                    Label    totalTimeLabel = new Label ("00:00");
                    Duration total          = media.getDuration ();
                    totalTimeLabel.setText (formatTime (total));

                    duration.setText (totalTimeLabel.getText ());

                    if (thumbnail != null)
                    {
                        File file = new File (video.getThumbnailURL ());
                        thumbnail.setImage (new Image (file.toURI ().toString ()));
                    }
                    else
                    {
                        System.err.println ("Thumbnail ImageView not found in FXML.");
                    }

                    if (title != null)
                    {
                        title.setText (video.getTitle ());
                    }
                    else
                    {
                        System.err.println ("Title Label not found in FXML.");
                    }

                    if (videoButton != null)
                    {
                        videoButton.setOnAction (event ->
                        {
                            MainApplication.currentVideo = video;
                            Stage      stage;
                            Scene      scene;
                            Parent     root;
                            FXMLLoader loader2 = new FXMLLoader (getClass ().getResource ("/org/project/controller/video-page.fxml"));
                            try
                            {
                                root = loader2.load ();
                            }
                            catch (IOException e)
                            {
                                throw new RuntimeException (e);
                            }
                            stage = (Stage) ((Node) event.getSource ()).getScene ().getWindow ();
                            scene = new Scene (root);
                            stage.setScene (scene);
                            stage.show ();
                        });
                    }

                    videosPane.getChildren ().add (videoNode);
                }
                catch (IOException e)
                {
                    e.printStackTrace ();
                }
            }

            for (int i = videos.size () - 1; i >= 0; i--)
            {
                Video video = videos.get (i);
                try
                {
                    FXMLLoader loader    = new FXMLLoader (getClass ().getResource ("/org/project/controller/video-thumbnail-view.fxml"));
                    AnchorPane videoNode = loader.load ();

                    ImageView thumbnail   = (ImageView) videoNode.lookup ("#thumbnailFile");
                    Label     title       = (Label) videoNode.lookup ("#title");
                    Button    videoButton = (Button) videoNode.lookup ("#videoButton");
                    Label     duration    = (Label) videoNode.lookup ("#duration");

                    Media media;
                    File  tempFile;
                    try
                    {
                        File   videoFile = new File (video.getVideoURL ());
                        byte[] fileBytes = Files.readAllBytes (Path.of (videoFile.toURI ()));

                        tempFile = File.createTempFile ("videoFile", ".mp4");
                        tempFile.deleteOnExit ();

                        try (FileOutputStream fos = new FileOutputStream (tempFile); ByteArrayInputStream bais = new ByteArrayInputStream (fileBytes))
                        {
                            byte[] buffer = new byte[1024];
                            int    length;
                            while ((length = bais.read (buffer)) != - 1)
                            {
                                fos.write (buffer, 0, length);
                            }
                        }

                    }
                    catch (IOException e)
                    {
                        e.printStackTrace ();
                        return;
                    }

                    media = new Media (tempFile.toURI ().toString ());

                    Label    totalTimeLabel = new Label ("00:00");
                    Duration total          = media.getDuration ();
                    totalTimeLabel.setText (formatTime (total));

                    duration.setText (totalTimeLabel.getText ());

                    if (thumbnail != null)
                    {
                        File file = new File (video.getThumbnailURL ());
                        thumbnail.setImage (new Image (file.toURI ().toString ()));
                    }
                    else
                    {
                        System.err.println ("Thumbnail ImageView not found in FXML.");
                    }

                    if (title != null)
                    {
                        title.setText (video.getTitle ());
                    }
                    else
                    {
                        System.err.println ("Title Label not found in FXML.");
                    }

                    if (videoButton != null)
                    {
                        videoButton.setOnAction (event ->
                        {
                            MainApplication.currentVideo = video;
                            Stage      stage;
                            Scene      scene;
                            Parent     root;
                            FXMLLoader loader2 = new FXMLLoader (getClass ().getResource ("/org/project/controller/video-page.fxml"));
                            try
                            {
                                root = loader2.load ();
                            }
                            catch (IOException e)
                            {
                                throw new RuntimeException (e);
                            }
                            stage = (Stage) ((Node) event.getSource ()).getScene ().getWindow ();
                            scene = new Scene (root);
                            stage.setScene (scene);
                            stage.show ();
                        });
                    }

                    reverseVideosPane.getChildren ().add (videoNode);
                }
                catch (IOException e)
                {
                    e.printStackTrace ();
                }
            }

            for (int i = 1; i <= 5; i++)
            {
                Video video = videos.get (videos.size () - i);
                try
                {
                    FXMLLoader loader    = new FXMLLoader (getClass ().getResource ("/org/project/controller/video-thumbnail-view.fxml"));
                    AnchorPane videoNode = loader.load ();

                    ImageView thumbnail   = (ImageView) videoNode.lookup ("#thumbnailFile");
                    Label     title       = (Label) videoNode.lookup ("#title");
                    Button    videoButton = (Button) videoNode.lookup ("#videoButton");
                    Label     duration    = (Label) videoNode.lookup ("#duration");

                    Media media;
                    File  tempFile;
                    try
                    {
                        File   videoFile = new File (video.getVideoURL ());
                        byte[] fileBytes = Files.readAllBytes (Path.of (videoFile.toURI ()));

                        tempFile = File.createTempFile ("videoFile", ".mp4");
                        tempFile.deleteOnExit ();

                        try (FileOutputStream fos = new FileOutputStream (tempFile); ByteArrayInputStream bais = new ByteArrayInputStream (fileBytes))
                        {
                            byte[] buffer = new byte[1024];
                            int    length;
                            while ((length = bais.read (buffer)) != - 1)
                            {
                                fos.write (buffer, 0, length);
                            }
                        }

                    }
                    catch (IOException e)
                    {
                        e.printStackTrace ();
                        return;
                    }

                    media = new Media (tempFile.toURI ().toString ());

                    Label    totalTimeLabel = new Label ("00:00");
                    Duration total          = media.getDuration ();
                    totalTimeLabel.setText (formatTime (total));

                    duration.setText (totalTimeLabel.getText ());

                    if (thumbnail != null)
                    {
                        File file = new File (video.getThumbnailURL ());
                        thumbnail.setImage (new Image (file.toURI ().toString ()));
                    }
                    else
                    {
                        System.err.println ("Thumbnail ImageView not found in FXML.");
                    }

                    if (title != null)
                    {
                        title.setText (video.getTitle ());
                    }
                    else
                    {
                        System.err.println ("Title Label not found in FXML.");
                    }

                    if (videoButton != null)
                    {
                        videoButton.setOnAction (event ->
                        {
                            MainApplication.currentVideo = video;
                            Stage      stage;
                            Scene      scene;
                            Parent     root;
                            FXMLLoader loader2 = new FXMLLoader (getClass ().getResource ("/org/project/controller/video-page.fxml"));
                            try
                            {
                                root = loader2.load ();
                            }
                            catch (IOException e)
                            {
                                throw new RuntimeException (e);
                            }
                            stage = (Stage) ((Node) event.getSource ()).getScene ().getWindow ();
                            scene = new Scene (root);
                            stage.setScene (scene);
                            stage.show ();
                        });
                    }

                    lastVideosPane.getChildren ().add (videoNode);
                }
                catch (IOException e)
                {
                    e.printStackTrace ();
                }
            }

            for (Video video : popularVideos)
            {
                try
                {
                    FXMLLoader loader    = new FXMLLoader (getClass ().getResource ("/org/project/controller/video-thumbnail-view.fxml"));
                    AnchorPane videoNode = loader.load ();

                    ImageView thumbnail   = (ImageView) videoNode.lookup ("#thumbnailFile");
                    Label     title       = (Label) videoNode.lookup ("#title");
                    Button    videoButton = (Button) videoNode.lookup ("#videoButton");
                    Label     duration    = (Label) videoNode.lookup ("#duration");

                    Media media;
                    File  tempFile;
                    try
                    {
                        File   videoFile = new File (video.getVideoURL ());
                        byte[] fileBytes = Files.readAllBytes (Path.of (videoFile.toURI ()));

                        tempFile = File.createTempFile ("videoFile", ".mp4");
                        tempFile.deleteOnExit ();

                        try (FileOutputStream fos = new FileOutputStream (tempFile); ByteArrayInputStream bais = new ByteArrayInputStream (fileBytes))
                        {
                            byte[] buffer = new byte[1024];
                            int    length;
                            while ((length = bais.read (buffer)) != - 1)
                            {
                                fos.write (buffer, 0, length);
                            }
                        }

                    }
                    catch (IOException e)
                    {
                        e.printStackTrace ();
                        return;
                    }

                    media = new Media (tempFile.toURI ().toString ());

                    Label    totalTimeLabel = new Label ("00:00");
                    Duration total          = media.getDuration ();
                    totalTimeLabel.setText (formatTime (total));

                    duration.setText (totalTimeLabel.getText ());

                    if (thumbnail != null)
                    {
                        File file = new File (video.getThumbnailURL ());
                        thumbnail.setImage (new Image (file.toURI ().toString ()));
                    }
                    else
                    {
                        System.err.println ("Thumbnail ImageView not found in FXML.");
                    }

                    if (title != null)
                    {
                        title.setText (video.getTitle ());
                    }
                    else
                    {
                        System.err.println ("Title Label not found in FXML.");
                    }

                    if (videoButton != null)
                    {
                        videoButton.setOnAction (event ->
                        {
                            MainApplication.currentVideo = video;
                            Stage      stage;
                            Scene      scene;
                            Parent     root;
                            FXMLLoader loader2 = new FXMLLoader (getClass ().getResource ("/org/project/controller/video-page.fxml"));
                            try
                            {
                                root = loader2.load ();
                            }
                            catch (IOException e)
                            {
                                throw new RuntimeException (e);
                            }
                            stage = (Stage) ((Node) event.getSource ()).getScene ().getWindow ();
                            scene = new Scene (root);
                            stage.setScene (scene);
                            stage.show ();
                        });
                    }

                    popularVideosPane.getChildren ().add (videoNode);
                }
                catch (IOException e)
                {
                    e.printStackTrace ();
                }
            }
        }
    }

    private String formatTime (Duration time)
    {
        int minutes = (int) time.toMinutes ();
        int seconds = (int) time.toSeconds () % 60;
        return String.format ("%02d:%02d", minutes, seconds);
    }

    private final double widthThreshold = 960;

    //endregion
}