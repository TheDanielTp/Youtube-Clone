package org.project.wetube.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommunistSignupFirstView
{
    //region--------------------------------------------------SignUp Functions--------------------------------------------------

    @FXML
    TextField usernameField;

    @FXML
    TextField emailField;

    @FXML
    PasswordField passwordField;

    @FXML
    Label usernameError1;

    @FXML
    Label emailError1;

    @FXML
    Label passwordError1;

    public static String username;
    public static String email;
    public static String password;

    public void signUpButtonClicked (ActionEvent event) throws IOException
    {
        hideErrors ();

        username = usernameField.getText ();
        email    = emailField.getText ();
        password = passwordField.getText ();

        if (username != null && email != null && password != null)
        {
            checkUsername (username);
            checkEmail (email);
            checkPassword (password);

            if (checkUsername (username) == 0 && checkEmail (email) == 0 && checkPassword (password) == 0)
            {
                Parent root = FXMLLoader.load (Objects.requireNonNull (getClass ().getResource ("communist-signup-second-view.fxml")));

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

                System.out.println ("> opening sign up panel");
            }
        }
    }

    public int checkUsername (String username)
    {
        if (username.length () < 6)
        {
            usernameError1.setVisible (true);
            return 1;
        }

        return 0;
    }

    public int checkEmail (String email)
    {
        String regex = "^[a-zA-Z0-9_+.&*-]+(?:\\.[a-zA-Z0-9_+.&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile (regex);
        Matcher matcher = pattern.matcher (email);

        if (! matcher.find ())
        {
            emailError1.setVisible (true);
            return 1;
        }

        return 0;
    }

    public int checkPassword (String password)
    {
        String regex = "^(.{0,7}|[^0-9]*|[^A-Z]*|[^a-z]*|[a-zA-Z0-9]*)$";

        Pattern pattern = Pattern.compile (regex);
        Matcher matcher = pattern.matcher (password);

        if (matcher.find ())
        {
            passwordError1.setVisible (true);
            return 1;
        }

        return 0;
    }

    public void hideErrors ()
    {
        usernameError1.setVisible (false);
        emailError1.setVisible (false);
        passwordError1.setVisible (false);
    }

    //endregion

    //region---------------------------------------------------Quit Functions---------------------------------------------------

    public void cancelButtonClicked (ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load (Objects.requireNonNull (getClass ().getResource ("communist-front-view.fxml")));

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
        Parent root = FXMLLoader.load (Objects.requireNonNull (getClass ().getResource ("communist-login-view.fxml")));

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

        System.out.println ("> opening login panel");
    }

    //endregion
}
