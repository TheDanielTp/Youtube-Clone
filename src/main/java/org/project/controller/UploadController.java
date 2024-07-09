package org.project.controller;

import com.wetube.dao.impl.CategoryDAOImpl;
import com.wetube.model.Category;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UploadController implements Initializable
{
    @FXML
    ComboBox <String> categories;

    @Override
    public void initialize (URL location, ResourceBundle resources)
    {
        CategoryDAOImpl categoryDAO = new CategoryDAOImpl();
        List<Category> categoriesList = categoryDAO.findAll ();

        for (Category category : categoriesList)
        {
            categories.getItems().add(category.getTitle ());
        }
    }

    public void uploadClicked (ActionEvent event)
    {
        Stage stage = (Stage) categories.getScene().getWindow ();
        stage.close();
    }
}
