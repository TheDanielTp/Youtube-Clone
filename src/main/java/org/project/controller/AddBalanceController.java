package org.project.controller;

import com.wetube.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddBalanceController
{
    @FXML
    TextField amount;

    public void done (ActionEvent event)
    {
        int money = Integer.parseInt(amount.getText());
        MainApplication.currentUser.setBalance (MainApplication.currentUser.getBalance() + money);
        MainApplication.client.update (MainApplication.currentUser);

        Stage stage = (Stage) amount.getScene().getWindow();
        stage.close();
    }
}
