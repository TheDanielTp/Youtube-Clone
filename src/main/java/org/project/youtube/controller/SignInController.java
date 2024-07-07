package org.project.wetube.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import org.project.wetube.YoutubeApplication;
import org.project.wetube.models.Request;
import org.project.wetube.models.Response;
import org.project.wetube.models.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.project.wetube.models.UserVideo;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;
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
        nextBtn.setOnAction (this::verifyCredentials);
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

    private void verifyCredentials (ActionEvent event)
    {
        String input = inputField.getText ();
        String password = DigestUtils.sha256Hex(passField.getText ());

        Boolean isEmail = determineInput(input);

        User user;

        if (isEmail == null || password.isEmpty ())
        {
            inputLog.setText ("Invalid entry.");
            inputLog.getParent ().setVisible (true);
            return;
        }
        else if (isEmail)
        {
            user = new User (input, "", password);
        }
        else
        {
            user = new User ("", input, password);
        }

        if (signIn(user))
        {
            exitSignInSignUp (event);
            System.out.println ("Signed In");
        }
        else
        {
            inputLog.getParent ().setVisible (true);
        }
    }

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

    private boolean signIn (User user)
    {
        Request<User> userRequest = new Request <> (YoutubeApplication.socket, "SignIn");
        userRequest.send ();

        String response = YoutubeApplication.receiveResponse ();
        Gson gson = new Gson ();
        TypeToken<Response<User>> responseTypeToken = new TypeToken <> ()
        {
        };
        Response<User> userResponse = gson.fromJson (response, responseTypeToken.getType ());

        User responseUser = userResponse.getBody ();

        if (responseUser != null)
        {
            YoutubeApplication.user = responseUser;
            System.out.println (userResponse.getMessage ());
            return true;
        }
        else
        {
            inputLog.setText (userResponse.getMessage ());
            return false;
        }
    }


}
