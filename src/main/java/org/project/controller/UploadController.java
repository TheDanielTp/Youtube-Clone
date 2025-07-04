package org.project.controller;

import com.wetube.dao.impl.CategoryDAOImpl;
import com.wetube.model.Category;
import com.wetube.model.Video;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Path;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

public class UploadController implements Initializable
{
    @FXML
    ComboBox <String> categories;

    @FXML
    TextField titleField;

    @FXML
    TextField descriptionField;

    @FXML
    CheckBox onlyComradeCheckBox;

    File videoFile;
    File thumbnailFile;

    @Override
    public void initialize (URL location, ResourceBundle resources)
    {
        CategoryDAOImpl categoryDAO    = new CategoryDAOImpl ();
        List <Category> categoriesList = categoryDAO.findAll ();

        for (Category category : categoriesList)
        {
            categories.getItems ().add (category.getTitle ());
        }
    }

    public void uploadVideoClicked (ActionEvent event)
    {
        FileChooser fileChooser = new FileChooser ();
        fileChooser.setTitle ("Open Video File");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter ("Video Files", "*.mp4", "*.avi", "*.mov", "*.mkv");
        fileChooser.getExtensionFilters ().add (extFilter);

        videoFile = fileChooser.showOpenDialog (categories.getScene ().getWindow ());
    }

    public void uploadThumbnailClicked (ActionEvent event)
    {
        FileChooser fileChooser = new FileChooser ();
        fileChooser.setTitle ("Open Thumbnail File");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter ("Image Files", "*.png", "*.jpg", "*.jpeg", "*.jpe", "*.bmp", "*.gif");
        fileChooser.getExtensionFilters ().add (extFilter);

        thumbnailFile = fileChooser.showOpenDialog (categories.getScene ().getWindow ());
    }

    public void upload (ActionEvent event)
    {
        String title       = titleField.getText ();
        String description = descriptionField.getText ();

        CategoryDAOImpl categoryDAO = new CategoryDAOImpl ();
        Category        category    = categoryDAO.findByTitle (categories.getValue ());

        if (title != null && description != null && videoFile != null && thumbnailFile != null && category != null)
        {
            File sourceFileVideo = new File (videoFile.getPath ());

            String targetDirPathVideo = "D:\\Java\\Projects\\Project_WeTube\\src\\main\\resources\\org\\project\\controller\\videos";

            File targetDirVideo = new File (targetDirPathVideo);
            if (! targetDirVideo.exists ())
            {
                targetDirVideo.mkdirs ();
            }

            File targetFileVideo = new File (targetDirVideo, sourceFileVideo.getName ());
            saveFile (sourceFileVideo, targetFileVideo);

            File sourceFileThumbnail = new File (thumbnailFile.getPath ());

            String targetDirPathThumbnail = "D:\\Java\\Projects\\Project_WeTube\\src\\main\\resources\\org\\project\\controller\\images\\thumbnails";

            File targetDirThumbnail = new File (targetDirPathThumbnail);
            if (! targetDirThumbnail.exists ())
            {
                targetDirThumbnail.mkdirs ();
            }

            File targetFileThumbnail = new File (targetDirThumbnail, targetFileVideo.getName ());
            saveFile (sourceFileThumbnail, targetFileThumbnail);

            Video video = new Video (MainApplication.currentUser.getID (), category.getID (), MainApplication.currentUser.getID (),
                    title, description, targetFileVideo.getPath (), targetFileThumbnail.getPath (), onlyComradeCheckBox.isSelected ());
            MainApplication.client.create (video);
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
