package org.project.controller;

import com.wetube.dao.impl.UserDAOImpl;
import com.wetube.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static org.project.controller.MainApplication.client;

public class CommunistLoginController
{
    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    public void loginButtonClicked (ActionEvent event)
    {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username != null && password != null)
        {
            Object[] loginResponse = client.login(username, password);

            if (loginResponse != null)
            {
                if ((int) loginResponse[0] == 1)
                {
                    return;
                }
                if ((int) loginResponse[0] == 2)
                {
                    return;
                }
                if ((int) loginResponse[0] == 0)
                {
                    User user = (User) loginResponse[1];
                    System.out.println ("> Front: User login successful");
                }
            }
        }
    }

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

    public void signupButtonClicked (MouseEvent event) throws IOException
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

        System.out.println ("> opening signup panel");
    }

    //endregion
}
