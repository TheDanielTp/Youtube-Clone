package org.project.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInController implements Initializable
{
    @FXML
    private SVGPath inputError;

    @FXML
    private TextField inputField;

    @FXML
    private PasswordField passField;

    @FXML
    private Text inputLog;

    @FXML
    private Label lblTitle;

    @FXML
    private Button nextBtn;

    @FXML
    private Text txtDescription;

    @FXML
    private VBox vbxLeft;

    @FXML
    private VBox vbxRight;

    @Override
    public void initialize (URL location, ResourceBundle resources)
    {
        inputError.getParent ().setVisible (false);
//        nextBtn.setOnAction (this::verifyCredentials);
    }

    @FXML
    public void exitSignInSignUp(ActionEvent event)
    {
        Stage stage;
        Scene  scene;
        Parent root;
        FXMLLoader loader = new FXMLLoader (getClass ().getResource ("/src/main/resources/communist-front-view.fxml"));
        try
        {
            root = loader.load ();
        }
        catch (IOException e)
        {
            throw new RuntimeException (e);
        }
        stage = (Stage) ((Node) event.getSource ()).getScene ().getWindow ();
        scene = new Scene (root);
        stage.setScene (scene);
        stage.show ();
    }

//    private void verifyCredentials (ActionEvent event)
//    {
//        String input = inputField.getText ();
//        String password = DigestUtils.sha256Hex(passField.getText ());
//
//        Boolean isEmail = determineInput(input);
//
//        User currentUser;
//
//        if (isEmail == null || password.isEmpty ())
//        {
//            inputLog.setText ("Invalid entry.");
//            inputLog.getParent ().setVisible (true);
//            return;
//        }
//        else if (isEmail)
//        {
//            currentUser = new User (input, "", password);
//        }
//        else
//        {
//            currentUser = new User ("", input, password);
//        }
//
//        if (signIn(currentUser))
//        {
//            exitSignInSignUp (event);
//            System.out.println ("Signed In");
//        }
//        else
//        {
//            inputLog.getParent ().setVisible (true);
//        }
//    }

    Boolean determineInput(String input)
    {
        String emailRegex = "^[a-zA-Z0-9_+.&*-]+(?:\\.[a-zA-Z0-9_+.&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern emailPattern = Pattern.compile (emailRegex);
        Matcher emailMatcher = emailPattern.matcher (input);

        String usernameRegex = "^[A-Za-z0-9_]+$";
        Pattern usernamePattern = Pattern.compile (usernameRegex);
        Matcher usernameMatcher = usernamePattern.matcher (input);

        if (emailMatcher.matches ())
        {
            return true;
        }
        else if (usernameMatcher.matches ())
        {
            return false;
        }

        inputLog.setText ("Invalid entry");
        return null;
    }
}
