package org.project.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class UserEditController {
        @FXML
        private VBox mainPane;

        @FXML
        private HBox topBar;

        @FXML
        private ImageView pfp;

        @FXML
        private TextField firstname;

        @FXML
        private TextField lastname;

        @FXML
        private TextField username;

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
        private Button choosePFP;

        @FXML
        private Button chooseChannelPic;

        @FXML
        private Button joinPremium;

        @FXML
        private ImageView channelPic;

        @FXML
        private Button done;

        @FXML
        private Label editProfileLabel;

        @FXML
        public void initialize() {
            // Initialize method, called after all @FXML fields are injected
            usernameError.setVisible(false);
            emailError.setVisible(false);
            passwordError.setVisible(false);
            birthdayError.setVisible(false);
        }

        @FXML
        private void handleChoosePFP() {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Profile Picture");
            File file = fileChooser.showOpenDialog(new Stage());
            if (file != null) {
                pfp.setImage(new Image(file.toURI().toString()));
            }
        }

        @FXML
        private void handleChooseChannelPic() {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Channel Picture");
            File file = fileChooser.showOpenDialog(new Stage());
            if (file != null) {
                channelPic.setImage(new Image(file.toURI().toString()));
            }
        }

        @FXML
        private void handleJoinPremium() {
            // Implement the logic to handle joining premium
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Join Premium");
            alert.setHeaderText(null);
            alert.setContentText("You have joined Premium!");
            alert.showAndWait();
        }

        @FXML
        private void handleDone() {
            // Implement the logic to handle the "Done" button click
            boolean valid = validateForm();
            if (valid) {
                saveUserData();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Profile Updated");
                alert.setHeaderText(null);
                alert.setContentText("Your profile has been updated successfully!");
                alert.showAndWait();
            }
        }

        private boolean validateForm() {
            boolean valid = true;

            if (username.getText().isEmpty()) {
                usernameError.setVisible(true);
                valid = false;
            } else {
                usernameError.setVisible(false);
            }

            if (email.getText().isEmpty()) {
                emailError.setVisible(true);
                valid = false;
            } else {
                emailError.setVisible(false);
            }

            if (!oldPassword.getText().equals("correctPassword")) { // Replace with actual password validation logic
                passwordError.setVisible(true);
                valid = false;
            } else {
                passwordError.setVisible(false);
            }

            if (birthday.getValue() == null) {
                birthdayError.setVisible(true);
                valid = false;
            } else {
                birthdayError.setVisible(false);
            }

            return valid;
        }

        private void saveUserData() {
            // Implement the logic to save user data
            String firstName = firstname.getText();
            String lastName = lastname.getText();
            String userName = username.getText();
            String emailAddress = email.getText();
            String newPasswordText = newPassword.getText();
            String birthdayDate = birthday.getValue().toString();

            // Use this data to save to your database or application logic
        }

}
