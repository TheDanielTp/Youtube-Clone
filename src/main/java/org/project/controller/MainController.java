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
import javafx.scene.layout.StackPane;
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

public class MainController implements Initializable
{
    //region------------------------------------------------Stage Size Functions------------------------------------------------

    @FXML
    FlowPane videosPane;

    @FXML
    ScrollPane videoScrollPane;

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle)
    {
        Object[]     responseObject = MainApplication.client.getAllVideos ();
        List <Video> videos         = (List <Video>) responseObject[1];

        for (Video video : videos)
        {
            try
            {
                FXMLLoader loader    = new FXMLLoader (getClass ().getResource ("/org/project/controller/video-thumbnail-view.fxml"));
                AnchorPane videoNode = loader.load ();

                ImageView thumbnail   = (ImageView) videoNode.lookup ("#thumbnail");
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

                // Create a Media object from the temporary videoFile
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

        Platform.runLater (() ->
        {
            Stage stage = (Stage) menuButton.getScene ().getWindow ();
            setupStageSizeListeners (stage);
            bindVideosPaneWidth (stage);
        });
    }

    private String formatTime (Duration time)
    {
        int minutes = (int) time.toMinutes ();
        int seconds = (int) time.toSeconds () % 60;
        return String.format ("%02d:%02d", minutes, seconds);
    }

    private final double widthThreshold = 960;

    private void setupStageSizeListeners (Stage stage)
    {
        ChangeListener <Number> stageSizeListener = (observable, oldValue, newValue) ->
        {
            if (stage.getWidth () < widthThreshold)
            {
                if (isMenuOpen)
                {
                    closeMenu ();
                }
            }
            else if (stage.getWidth () > widthThreshold)
            {
                if (! isMenuOpen)
                {
                    openMenu ();
                }
            }
        };

        stage.widthProperty ().addListener (stageSizeListener);
        stage.heightProperty ().addListener (stageSizeListener);
    }

    private void bindVideosPaneWidth (Stage stage)
    {
        videosPane.prefWidthProperty ().bind (
                Bindings.createDoubleBinding (() ->
                                stage.getWidth () - (isMenuOpen ? leftMenu.getWidth () : leftMenuSmall.getWidth ()),
                        stage.widthProperty (), leftMenu.widthProperty (), leftMenuSmall.widthProperty ()
                )
        );

        leftMenu.widthProperty ().addListener ((obs, oldVal, newVal) -> updateVideosPaneWidth (stage));
        leftMenuSmall.widthProperty ().addListener ((obs, oldVal, newVal) -> updateVideosPaneWidth (stage));
    }

    private void updateVideosPaneWidth (Stage stage)
    {
        videosPane.prefWidthProperty ().bind (
                Bindings.createDoubleBinding (() ->
                                stage.getWidth () - (isMenuOpen ? leftMenu.getWidth () : leftMenuSmall.getWidth ()),
                        stage.widthProperty (), leftMenu.widthProperty (), leftMenuSmall.widthProperty ()
                )
        );
        videoScrollPane.setMinViewportWidth (videosPane.getWidth () + 200);
    }

    //endregion

    //region---------------------------------------------------Head Functions---------------------------------------------------

    @FXML
    ImageView menuButton;

    @FXML
    ImageView menuHover;

    public void menuButtonHover ()
    {
        menuButton.setVisible (false);
        menuHover.setVisible (true);
    }

    public void menuButtonUnHover ()
    {
        menuButton.setVisible (true);
        menuHover.setVisible (false);
    }

    @FXML
    ImageView micButton;

    @FXML
    ImageView micHover;

    public void micButtonHover ()
    {
        micButton.setVisible (false);
        micHover.setVisible (true);
    }

    public void micButtonUnHover ()
    {
        micButton.setVisible (true);
        micHover.setVisible (false);
    }

    @FXML
    ImageView themeButton;

    @FXML
    ImageView themeHover;

    public void themeButtonClicked (MouseEvent event) throws IOException
    {
        if (! MainApplication.DarkTheme)
        {

        }
        else
        {
            MainApplication.DarkTheme = false;
            Parent root = FXMLLoader.load (Objects.requireNonNull (getClass ().getResource ("communist-main-view.fxml")));

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

            System.out.println ("> Front: opening light theme front page");
        }
    }

    public void themeButtonHover ()
    {
        themeButton.setVisible (false);
        themeHover.setVisible (true);
    }

    public void themeButtonUnHover ()
    {
        themeButton.setVisible (true);
        themeHover.setVisible (false);
    }

    @FXML
    ImageView notificationButton;

    @FXML
    ImageView notificationHover;

    public void notificationButtonHover ()
    {
        notificationButton.setVisible (false);
        notificationHover.setVisible (true);
    }

    public void notificationButtonUnHover ()
    {
        notificationButton.setVisible (true);
        notificationHover.setVisible (false);
    }

    @FXML
    ImageView createButton;

    @FXML
    ImageView createHover;

    public void createButtonHover ()
    {
        createButton.setVisible (false);
        createHover.setVisible (true);
    }

    public void createButtonUnHover ()
    {
        createButton.setVisible (true);
        createHover.setVisible (false);
    }

    public void createButtonClick (MouseEvent event) throws IOException
    {
        Parent root = FXMLLoader.load (Objects.requireNonNull (getClass ().getResource ("upload_page.fxml")));

        Stage stage = new Stage ();

        double x      = stage.getX ();
        double y      = stage.getY ();

        Scene scene = new Scene (root);

        stage.setScene (scene);

        stage.setWidth (600);
        stage.setHeight (400);
        stage.setX (x);
        stage.setY (y);

        System.out.println ("> Front: opening upload page");
        stage.show ();
    }

    //endregion

    //region-----------------------------------------------Main Sidebar Functions-----------------------------------------------

    @FXML
    SVGPath homeButtonSVG;

    @FXML
    SVGPath homeButtonSVGSmall;

    @FXML
    Label homeLabel;

    public void homeButtonHover ()
    {
        homeButtonSVG.setFill (Color.web ("#000000"));
        homeButtonSVGSmall.setFill (Color.web ("#000000"));
        homeLabel.setTextFill (Color.web ("#000000"));
    }

    public void homeButtonUnHover ()
    {
        homeButtonSVG.setFill (Color.web ("#ffffff"));
        homeButtonSVGSmall.setFill (Color.web ("#ffffff"));
        homeLabel.setTextFill (Color.web ("#ffffff"));
    }

    @FXML
    SVGPath shortsButtonSVG;

    @FXML
    SVGPath shortsButtonSVGSmall;

    @FXML
    Label shortsLabel;

    public void shortsButtonHover ()
    {
        shortsButtonSVG.setFill (Color.web ("#000000"));
        shortsButtonSVGSmall.setFill (Color.web ("#000000"));
        shortsLabel.setTextFill (Color.web ("#000000"));
    }

    public void shortsButtonUnHover ()
    {
        shortsButtonSVG.setFill (Color.web ("#ffffff"));
        shortsButtonSVGSmall.setFill (Color.web ("#ffffff"));
        shortsLabel.setTextFill (Color.web ("#ffffff"));
    }

    @FXML
    SVGPath subsButtonSVG;

    @FXML
    SVGPath subsButtonSVGSmall;

    @FXML
    Label subsLabel;

    public void subsButtonHover ()
    {
        subsButtonSVG.setFill (Color.web ("#000000"));
        subsButtonSVGSmall.setFill (Color.web ("#000000"));
        subsLabel.setTextFill (Color.web ("#000000"));
    }

    public void subsButtonUnHover ()
    {
        subsButtonSVG.setFill (Color.web ("#ffffff"));
        subsButtonSVGSmall.setFill (Color.web ("#ffffff"));
        subsLabel.setTextFill (Color.web ("#ffffff"));
    }

    @FXML
    SVGPath youButtonSVG;

    @FXML
    SVGPath youButtonSVGSmall;

    @FXML
    Label youLabel;

    public void youButtonHover ()
    {
        youButtonSVG.setFill (Color.web ("#000000"));
        youButtonSVGSmall.setFill (Color.web ("#000000"));
        youLabel.setTextFill (Color.web ("#000000"));
    }

    public void youButtonUnHover ()
    {
        youButtonSVG.setFill (Color.web ("#ffffff"));
        youButtonSVGSmall.setFill (Color.web ("#ffffff"));
        youLabel.setTextFill (Color.web ("#ffffff"));
    }

    @FXML
    SVGPath historyButtonSVG;

    @FXML
    SVGPath historyButtonSVGSmall;

    @FXML
    Label historyLabel;

    public void historyButtonHover ()
    {
        historyButtonSVG.setFill (Color.web ("#000000"));
        historyButtonSVGSmall.setFill (Color.web ("#000000"));
        historyLabel.setTextFill (Color.web ("#000000"));
    }

    public void historyButtonUnHover ()
    {
        historyButtonSVG.setFill (Color.web ("#ffffff"));
        historyButtonSVGSmall.setFill (Color.web ("#ffffff"));
        historyLabel.setTextFill (Color.web ("#ffffff"));
    }

    //endregion

    //region-----------------------------------------------Down Sidebar Functions-----------------------------------------------

    @FXML
    SVGPath trendingButtonSVG;

    public void trendingButtonHover ()
    {
        trendingButtonSVG.setFill (Color.web ("#000000"));
    }

    public void trendingButtonUnHover ()
    {
        trendingButtonSVG.setFill (Color.web ("#ffffff"));
    }

    @FXML
    SVGPath shopButtonSVG;

    public void shopButtonHover ()
    {
        shopButtonSVG.setFill (Color.web ("#000000"));
    }

    public void shopButtonUnHover ()
    {
        shopButtonSVG.setFill (Color.web ("#ffffff"));
    }

    @FXML
    SVGPath musicButtonSVG;

    public void musicButtonHover ()
    {
        musicButtonSVG.setFill (Color.web ("#000000"));
    }

    public void musicButtonUnHover ()
    {
        musicButtonSVG.setFill (Color.web ("#ffffff"));
    }

    @FXML
    SVGPath moviesButtonSVG;

    public void moviesButtonHover ()
    {
        moviesButtonSVG.setFill (Color.web ("#000000"));
    }

    public void moviesButtonUnHover ()
    {
        moviesButtonSVG.setFill (Color.web ("#ffffff"));
    }

    @FXML
    SVGPath liveButtonSVG;

    public void liveButtonHover ()
    {
        liveButtonSVG.setFill (Color.web ("#000000"));
    }

    public void liveButtonUnHover ()
    {
        liveButtonSVG.setFill (Color.web ("#ffffff"));
    }

    @FXML
    SVGPath gamingButtonSVG;

    public void gamingButtonHover ()
    {
        gamingButtonSVG.setFill (Color.web ("#000000"));
    }

    public void gamingButtonUnHover ()
    {
        gamingButtonSVG.setFill (Color.web ("#ffffff"));
    }

    @FXML
    SVGPath newsButtonSVG;

    public void newsButtonHover ()
    {
        newsButtonSVG.setFill (Color.web ("#000000"));
    }

    public void newsButtonUnHover ()
    {
        newsButtonSVG.setFill (Color.web ("#ffffff"));
    }

    @FXML
    SVGPath sportsButtonSVG;

    public void sportsButtonHover ()
    {
        sportsButtonSVG.setFill (Color.web ("#000000"));
    }

    public void sportsButtonUnHover ()
    {
        sportsButtonSVG.setFill (Color.web ("#ffffff"));
    }

    @FXML
    SVGPath coursesButtonSVG;

    public void coursesButtonHover ()
    {
        coursesButtonSVG.setFill (Color.web ("#000000"));
    }

    public void coursesButtonUnHover ()
    {
        coursesButtonSVG.setFill (Color.web ("#ffffff"));
    }

    @FXML
    SVGPath fashionButtonSVG;

    public void fashionButtonHover ()
    {
        fashionButtonSVG.setFill (Color.web ("#000000"));
    }

    public void fashionButtonUnHover ()
    {
        fashionButtonSVG.setFill (Color.web ("#ffffff"));
    }

    @FXML
    SVGPath podcastsButtonSVG;

    public void podcastsButtonHover ()
    {
        podcastsButtonSVG.setFill (Color.web ("#000000"));
    }

    public void podcastsButtonUnHover ()
    {
        podcastsButtonSVG.setFill (Color.web ("#ffffff"));
    }

    @FXML
    SVGPath playableButtonSVG;

    public void playableButtonHover ()
    {
        playableButtonSVG.setFill (Color.web ("#000000"));
    }

    public void playableButtonUnHover ()
    {
        playableButtonSVG.setFill (Color.web ("#ffffff"));
    }

    //endregion

    //region------------------------------------------------Transition Functions------------------------------------------------

    @FXML
    ScrollPane leftMenu;

    @FXML
    VBox leftMenuSmall;

    private boolean isMenuOpen = true;

    public void menuClick ()
    {
        Stage stage = (Stage) menuButton.getScene ().getWindow ();

        if (stage.getWidth () < widthThreshold)
        {
            return;
        }
        if (isMenuOpen)
        {
            closeMenu ();
        }
        else
        {
            openMenu ();
        }
    }

    private void openMenu ()
    {
        TranslateTransition openTransition = new TranslateTransition (Duration.seconds (0.25), leftMenu);

        openTransition.setFromX (- leftMenu.getWidth ());
        openTransition.setToX (0);
        openTransition.play ();

        TranslateTransition closeTransition = new TranslateTransition (Duration.seconds (0.25), leftMenuSmall);

        closeTransition.setFromX (0);
        closeTransition.setToX (- leftMenuSmall.getWidth ());
        closeTransition.play ();

        TranslateTransition closeTransition3 = new TranslateTransition (Duration.seconds (0.25), videoScrollPane);

        closeTransition3.setFromX (- leftMenu.getWidth () + 105);
        closeTransition3.setToX (0);
        closeTransition3.play ();

        isMenuOpen = true;
        updateVideosPaneWidth ((Stage) menuButton.getScene ().getWindow ());
    }

    private void closeMenu ()
    {
        TranslateTransition openTransition = new TranslateTransition (Duration.seconds (0.25), leftMenuSmall);

        openTransition.setFromX (- leftMenuSmall.getWidth ());
        openTransition.setToX (0);
        openTransition.play ();

        TranslateTransition closeTransition = new TranslateTransition (Duration.seconds (0.25), leftMenu);

        closeTransition.setFromX (0);
        closeTransition.setToX (- leftMenu.getWidth ());
        closeTransition.play ();

        TranslateTransition openTransition3 = new TranslateTransition (Duration.seconds (0.25), videoScrollPane);

        openTransition3.setFromX (0);
        openTransition3.setToX (- leftMenu.getWidth () + 105);
        openTransition3.play ();

        isMenuOpen = false;
        updateVideosPaneWidth ((Stage) menuButton.getScene ().getWindow ());
    }

    //endregion
}