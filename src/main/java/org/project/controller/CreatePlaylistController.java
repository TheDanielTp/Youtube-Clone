package org.project.controller;

import com.wetube.dao.impl.CategoryDAOImpl;
import com.wetube.model.Category;
import com.wetube.model.Playlist;
import com.wetube.model.Video;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CreatePlaylistController implements Initializable
{
    @FXML
    TextField titleField;

    @FXML
    TextField descriptionField;

    @FXML
    CheckBox privateCheckBox;

    @FXML
    CheckBox isOnlyComradeCheckBox;

    @Override
    public void initialize (URL location, ResourceBundle resources)
    {

    }

    public void upload (ActionEvent event)
    {
        String title       = titleField.getText ();
        String description = descriptionField.getText ();


        if (title != null && description != null)
        {
            Playlist playlist = new Playlist (MainApplication.currentUser.getID (), MainApplication.currentUser.getID (),
                    title, description, ! privateCheckBox.isSelected (), isOnlyComradeCheckBox.isSelected ());
            MainApplication.client.create (playlist);
        }

        Stage stage = (Stage) titleField.getScene ().getWindow ();
        stage.close ();
    }
}
