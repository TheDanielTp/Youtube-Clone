package org.project.youtube.controller;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class CommunistFrontController implements Initializable
{
    @FXML
    private VBox mainPane;

    private void getVideoPage (ActionEvent event)
    {
        Stage      stage;
        Scene      scene;
        Parent     root;
        FXMLLoader loader = new FXMLLoader (getClass ().getResource ("/src/main/resource/video-section.fxml"));
        try
        {
            root = loader.load ();
        }
        catch (IOException e)
        {
            throw new RuntimeException (e);
        }
        stage = (Stage) ((Node) event.getSource ()).getScene ().getWindow ();
        scene = new Scene (root, mainPane.getScene ().getWidth (), mainPane.getScene ().getHeight ());
        stage.setScene (scene);
        stage.show ();
    }

    //region------------------------------------------------Stage Size Functions------------------------------------------------

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle)
    {
        Platform.runLater (() ->
        {
            Stage stage = (Stage) menuButton.getScene ().getWindow ();
            setupStageSizeListeners (stage);
        });
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
    ImageView settingButton;

    @FXML
    ImageView settingHover;

    public void settingButtonHover ()
    {
        settingButton.setVisible (false);
        settingHover.setVisible (true);
    }

    public void settingButtonUnHover ()
    {
        settingButton.setVisible (true);
        settingHover.setVisible (false);
    }

    @FXML
    ImageView signInButton;

    @FXML
    ImageView signInHover;

    public void signInButtonHover ()
    {
        signInButton.setVisible (false);
        signInHover.setVisible (true);
    }

    public void signInButtonUnHover ()
    {
        signInButton.setVisible (true);
        signInHover.setVisible (false);
    }

    public void signInButtonClicked (MouseEvent event) throws IOException
    {
        Parent root = FXMLLoader.load (Objects.requireNonNull (getClass ().getResource ("org/project/wetube/communist-signup-first-view.fxml")));

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

        System.out.println ("> opening signup panel");
    }

    @FXML
    ImageView signInButton1;

    @FXML
    ImageView signInHover1;

    public void signInButtonHover1 ()
    {
        signInButton1.setVisible (false);
        signInHover1.setVisible (true);
    }

    public void signInButtonUnHover1 ()
    {
        signInButton1.setVisible (true);
        signInHover1.setVisible (false);
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

        isMenuOpen = true;
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

        isMenuOpen = false;
    }

    //endregion
}