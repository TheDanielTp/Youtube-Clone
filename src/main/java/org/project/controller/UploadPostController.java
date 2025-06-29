package org.project.controller;

import com.wetube.dao.impl.CategoryDAOImpl;
import com.wetube.dao.impl.ChannelDAOImpl;
import com.wetube.dao.impl.CommunityDAOImpl;
import com.wetube.model.*;
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

public class UploadPostController implements Initializable
{
    @FXML
    TextField titleField;

    @FXML
    TextField descriptionField;

    @FXML
    CheckBox onlyComradeCheckBox;

    File imageFile;

    @Override
    public void initialize (URL location, ResourceBundle resources)
    {

    }

    public void uploadVideoClicked (ActionEvent event)
    {
        FileChooser fileChooser = new FileChooser ();
        fileChooser.setTitle ("Open Video File");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter ("Image Files", "*.jpg", "*.png", "*.jpeg", "*.jpe", "*.bmp", "*.gif");
        fileChooser.getExtensionFilters ().add (extFilter);

        imageFile = fileChooser.showOpenDialog (descriptionField.getScene ().getWindow ());
    }

    public void upload (ActionEvent event)
    {
        String title       = titleField.getText ();
        String description = descriptionField.getText ();

        CategoryDAOImpl categoryDAO = new CategoryDAOImpl ();

        if (title != null && description != null && imageFile != null)
        {
            File sourceFileVideo = new File (imageFile.getPath ());

            String targetDirPathImage = "D:\\Java\\Projects\\Project_WeTube\\src\\main\\resources\\org\\project\\controller\\images\\posts";

            File targetDirImage = new File (targetDirPathImage);
            if (! targetDirImage.exists ())
            {
                targetDirImage.mkdirs ();
            }

            File targetFileImage = new File (targetDirImage, sourceFileVideo.getName ());
            saveFile (sourceFileVideo, targetFileImage);

            Post post = new Post (MainApplication.currentUser.getID (), MainApplication.currentUser.getID (), MainApplication.currentUser.getID (),
                    title, description, targetFileImage.getPath (), onlyComradeCheckBox.isSelected ());
            MainApplication.client.create (post);
        }

        Stage stage = (Stage) titleField.getScene ().getWindow ();
        stage.close ();
    }

    private static void saveFile (File sourceFile, File targetFile)
    {
        try (FileInputStream fis = new FileInputStream (sourceFile);
             FileOutputStream fos = new FileOutputStream (targetFile))
        {

            byte[] buffer = new byte[1024];
            int    length;
            while ((length = fis.read (buffer)) > 0)
            {
                fos.write (buffer, 0, length);
            }

            System.out.println ("> Uploader: File saved successfully to " + targetFile.getAbsolutePath ());

        }
        catch (IOException e)
        {
            e.printStackTrace ();
        }
    }
}
