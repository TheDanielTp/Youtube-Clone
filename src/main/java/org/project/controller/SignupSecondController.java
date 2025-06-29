package org.project.controller;

import com.wetube.model.Channel;
import com.wetube.model.Community;
import com.wetube.model.Playlist;
import com.wetube.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

import static org.project.controller.MainApplication.client;

public class SignupSecondController
{
    //region--------------------------------------------------SignUp Functions--------------------------------------------------

    @FXML
    TextField firstNameField;

    @FXML
    TextField lastNameField;

    @FXML
    DatePicker birthField;

    @FXML
    Label ageError;

    public static String    firstName;
    public static String    lastName;
    public static LocalDate birthDate;
    public static LocalDate joinDate;

    public void signUpButtonClicked (ActionEvent event) throws IOException
    {
        hideErrors ();

        firstName = firstNameField.getText ();
        lastName  = lastNameField.getText ();
        birthDate = birthField.getValue ();
        joinDate  = LocalDate.now ();

        if (firstName != null && lastName != null && birthDate != null)
        {
            checkAge (birthDate);

            if (checkAge (birthDate))
            {
                User user = new User (firstName, lastName, SignupFirstController.username, SignupFirstController.email, SignupFirstController.password, birthDate);
                Object[] signUpResponse = client.create (user);
                if (signUpResponse != null)
                {
                    if ((int) signUpResponse[0] == 1)
                    {
                        return;
                    }
                    if ((int) signUpResponse[0] == 2)
                    {
                        return;
                    }
                    if ((int) signUpResponse[0] == 0)
                    {
                        MainApplication.currentUser = user;
                        System.out.println ("> Front: User sign up successful");

                        Channel channel = new Channel (user.getID (), user.getUsername (), "");
                        client.create (channel);

                        Playlist playlist = new Playlist (user.getID (), user.getID (), "History", "History", false, false);
                        client.create (playlist);

                        Playlist playlist1 = new Playlist (user.getID (), user.getID (), "Liked", "Liked", false, false);
                        client.create (playlist1);

                        Playlist playlist2 = new Playlist (user.getID (), user.getID (), "Saved", "Saved", false, false);
                        client.create (playlist2);

                        Community community = new Community (user.getID ());
                        client.create (community);

                        Parent root;
                        if (! MainApplication.DarkTheme)
                        {
                            root = FXMLLoader.load (Objects.requireNonNull (getClass ().getResource ("communist-main-view.fxml")));
                        }
                        else
                        {
                            root = FXMLLoader.load (Objects.requireNonNull (getClass ().getResource ("dark-main-view.fxml")));
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

                        System.out.println ("> opening front panel");
                    }
                }
            }
        }
    }

    private boolean checkAge (LocalDate birthDate)
    {
        LocalDate today          = LocalDate.now ();
        LocalDate twelveYearsAgo = today.minusYears (12);

        if (! birthDate.isBefore (twelveYearsAgo) && ! birthDate.isEqual (twelveYearsAgo))
        {
            ageError.setVisible (true);
            return false;
        }

        return true;
    }

    public void hideErrors ()
    {
        ageError.setVisible (false);
    }

    //endregion

    //region---------------------------------------------------Quit Functions---------------------------------------------------

    public void cancelButtonClicked (ActionEvent event) throws IOException
    {
        Parent root;
        if (! MainApplication.DarkTheme)
        {
            root = FXMLLoader.load (Objects.requireNonNull (getClass ().getResource ("communist-signup-first-view.fxml")));
        }
        else
        {
            root = FXMLLoader.load (Objects.requireNonNull (getClass ().getResource ("dark-signup-first-view.fxml")));
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

        System.out.println ("> opening front panel");
    }

    public void loginButtonClicked (MouseEvent event) throws IOException
    {
        Parent root;
        if (! MainApplication.DarkTheme)
        {
            root = FXMLLoader.load (Objects.requireNonNull (getClass ().getResource ("communist-login-view.fxml")));
        }
        else
        {
            root = FXMLLoader.load (Objects.requireNonNull (getClass ().getResource ("dark-login-view.fxml")));
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

        System.out.println ("> Front: opening login page");
    }

    //endregion
}
