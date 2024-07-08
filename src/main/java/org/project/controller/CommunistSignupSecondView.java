package org.project.controller;

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

public class CommunistSignupSecondView
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

        firstName      = firstNameField.getText ();
        lastName = lastNameField.getText ();
        birthDate = birthField.getValue ();
        joinDate  = LocalDate.now ();

        if (firstName != null && lastName != null && birthDate != null)
        {
            checkAge (birthDate);

            if (checkAge (birthDate))
            {
                
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
        Parent root = FXMLLoader.load (Objects.requireNonNull (getClass ().getResource ("communist-signup-first-view.fxml")));

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
