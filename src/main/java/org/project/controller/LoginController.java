package org.project.controller;

import com.wetube.model.Channel;
import com.wetube.model.User;
import com.wetube.model.Video;
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
import static org.project.controller.MainApplication.currentUser;

public class LoginController
{
    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    public void loginButtonClicked (ActionEvent event) throws IOException
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
                    MainApplication.currentUser = (User) loginResponse[1];
                    System.out.println ("> Front: User login successful");

                    Channel channel = new Channel (currentUser.getID (), currentUser.getUsername (), "");
                    client.create (channel);

                    Video video = new Video (currentUser.getID (), currentUser.getID (), currentUser.getID (), "You were the chosen one", "Starwars Video",
                            "D:\\Java\\Projects\\Project_WeTube\\src\\main\\resources\\org\\project\\controller\\videos\\You Were The Chosen One.mp4",
                            "D:\\Java\\Projects\\Project_WeTube\\src\\main\\resources\\org\\project\\controller\\videos\\thumbnails\\You Were The Chosen One.jpg",
                            false);
                    client.create (video);

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

    //region---------------------------------------------------Quit Functions---------------------------------------------------

    public void cancelButtonClicked (ActionEvent event) throws IOException
    {
        if (! MainApplication.DarkTheme)
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
        else
        {
            Parent root = FXMLLoader.load (Objects.requireNonNull (getClass ().getResource ("dark-front-view.fxml")));

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

    public void signupButtonClicked (MouseEvent event) throws IOException
    {
        if (! MainApplication.DarkTheme)
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
        else
        {
            Parent root = FXMLLoader.load (Objects.requireNonNull (getClass ().getResource ("dark-signup-first-view.fxml")));

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
    }

    //endregion
}
