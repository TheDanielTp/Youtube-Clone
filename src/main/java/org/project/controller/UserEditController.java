package org.project.controller;

import com.wetube.dao.impl.ChannelDAOImpl;
import com.wetube.dao.impl.UserDAOImpl;
import com.wetube.model.Channel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

public class UserEditController
{
    @FXML
    private ImageView pfp;

    @FXML
    private TextField firstname;

    @FXML
    private TextField lastname;

    @FXML
    private TextField usernameField;

    @FXML
    private Label usernameError;

    @FXML
    private TextField email;

    @FXML
    private Label emailError;

    @FXML
    private TextField oldPassword;

    @FXML
    private Label passwordError;

    @FXML
    private TextField newPassword;

    @FXML
    private DatePicker birthday;

    @FXML
    private Label birthdayError;

    @FXML
    private ImageView channelpfp;

    @FXML
    public void initialize ()
    {
        usernameError.setVisible (false);
        emailError.setVisible (false);
        passwordError.setVisible (false);
        birthdayError.setVisible (false);
    }

    @FXML
    private void handleChoosePFP ()
    {
        FileChooser fileChooser = new FileChooser ();
        fileChooser.setTitle ("Choose Profile Picture");
        File file = fileChooser.showOpenDialog (new Stage ());
        if (file != null)
        {
            pfp.setImage (new Image (file.toURI ().toString ()));
            MainApplication.currentUser.setProfilePictureURL (file.toURI ().toString ());
            MainApplication.client.update (MainApplication.currentUser);
        }
    }

    @FXML
    private void handleChooseChannelPic ()
    {
        FileChooser fileChooser = new FileChooser ();
        fileChooser.setTitle ("Choose Channel Picture");
        File file = fileChooser.showOpenDialog (new Stage ());
        if (file != null)
        {
            channelpfp.setImage (new Image (file.toURI ().toString ()));
            ChannelDAOImpl channelDAO = new ChannelDAOImpl ();
            Channel channel = channelDAO.findById (MainApplication.currentUser.getID ());
            channel.setChannelPictureURL (file.toURI ().toString ());
            channelDAO.update (channel);
        }
    }

    @FXML
    private void handleJoinPremium ()
    {
        Alert alert = new Alert (Alert.AlertType.INFORMATION);
        alert.setTitle ("Join Premium");
        alert.setHeaderText ("Congratulations!");
        alert.setContentText ("You have joined Premium!");
        alert.showAndWait ();
    }

    @FXML
    private void doneClicked (ActionEvent event) throws IOException
    {
        if (! firstname.getText ().isEmpty ())
        {
            String firstName = firstname.getText ();
            MainApplication.currentUser.setFirstName (firstName);
            MainApplication.client.update (MainApplication.currentUser);
        }
        if (! lastname.getText ().isEmpty ())
        {
            String lastName = lastname.getText ();
            MainApplication.currentUser.setLastName (lastName);
            MainApplication.client.update (MainApplication.currentUser);
        }
        if (! usernameField.getText ().isEmpty ())
        {
            UserDAOImpl userDAO  = new UserDAOImpl ();
            String      username = usernameField.getText ();
            if (userDAO.findByUsername (username) == null)
            {
                MainApplication.currentUser.setUsername (username);
                MainApplication.client.update (MainApplication.currentUser);
            }
            else
            {
                usernameError.setVisible (true);
            }
        }
        if (! email.getText ().isEmpty ())
        {
            UserDAOImpl userDAO  = new UserDAOImpl ();
            String      emailText = email.getText ();
            if (userDAO.findByEmail (emailText) == null)
            {
                MainApplication.currentUser.setEmail (emailText);
                MainApplication.client.update (MainApplication.currentUser);
            }
            else
            {
                emailError.setVisible (true);
            }
        }
        if (! oldPassword.getText ().isEmpty () && ! newPassword.getText ().isEmpty ())
        {
            if (oldPassword.getText ().equals (MainApplication.currentUser.getPassword ()))
            {
                MainApplication.currentUser.setPassword (newPassword.getText ());
                MainApplication.client.update (MainApplication.currentUser);
            }
            else
            {
                passwordError.setVisible (true);
            }
        }
        if (birthday.getValue () != null )
        {
            LocalDate birthdayDate = birthday.getValue ();
            MainApplication.currentUser.setBirthdate (birthdayDate);
            MainApplication.client.update (MainApplication.currentUser);
        }

        Parent root;
        if (! MainApplication.DarkTheme)
        {
            MainApplication.DarkTheme = true;
            root = FXMLLoader.load (Objects.requireNonNull (getClass ().getResource ("communist-main-view.fxml")));
        }
        else
        {
            MainApplication.DarkTheme = false;
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

        System.out.println ("> Front: opening main page");
    }
}
