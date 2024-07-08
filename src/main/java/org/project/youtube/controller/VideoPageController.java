//package org.project.wetube.controller;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import javafx.beans.binding.Bindings;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.fxml.Initializable;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.ScrollPane;
//import javafx.scene.control.TextField;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.Priority;
//import javafx.scene.layout.VBox;
//import javafx.scene.shape.SVGPath;
//import javafx.scene.text.Text;
//import org.project.wetube.YoutubeApplication;
//import org.project.wetube.models.Request;
//import org.project.wetube.models.Response;
//import org.project.wetube.models.UserVideo;
//import org.project.wetube.models.Video;
//import javafx.scene.media.Media;
//import javafx.scene.media.MediaPlayer;
//import javafx.scene.media.MediaView;
//
//import java.io.IOException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.ResourceBundle;
//
//public class VideoPageController implements Initializable
//{
//    private Video video;
//
//    private ArrayList<Video> recommendedVideos;
//
//    private Media media;
//
//    private final ScrollPane recommandedVideosScrollPane = new ScrollPane ();
//    private final VBox vbxRecommendedVideos = new VBox ();
//
//    private final ScrollPane videoScrollPane = new ScrollPane ();
//
//    @FXML
//    private Button btnLike;
//
//    @FXML
//    private Button btnComment;
//
//    @FXML
//    private Button btnDisLike;
//
//    @FXML
//    private Text txtLikes;
//
//    @FXML
//    private Button btnSave;
//
//    @FXML
//    private AnchorPane anchrpnVideoPage;
//
//    @FXML
//    private Button btnNext;
//
//    @FXML
//    private Button btnSub;
//
//    @FXML
//    private Button btnBack;
//
//    @FXML
//    private Button btnPause;
//
//    @FXML
//    private Button btnPlayPause;
//
//    @FXML
//    private Button btnVolume;
//
//    @FXML
//    private HBox hbx;
//
//    @FXML
//    private HBox hbxChannel;
//
//    @FXML
//    private HBox hbxControls;
//
//    @FXML
//    private HBox hbxVideoDetail;
//
//    @FXML
//    private HBox hbxViewDate;
//
//    @FXML
//    private ImageView imgChannelProfile;
//
//    @FXML
//    private Text txtChannelName;
//
//    @FXML
//    private Text txtDate;
//
//    @FXML
//    private Text txtVideoDescription;
//
//    @FXML
//    private Text txtVideoTitle;
//
//    @FXML
//    private Text txtViews;
//
//    @FXML
//    private Text txtChannelSubscribers;
//
//    @FXML
//    private TextField txtComment;
//
//    @FXML
//    private VBox vbxCommentSection;
//
//    @FXML
//    private VBox vbxDescription;
//
//    @FXML
//    private VBox vbxLeft;
//
//    @FXML
//    private VBox vbxVideoDetails;
//
//    @FXML
//    private SVGPath svgLike;
//
//    @FXML
//    private SVGPath svgDisLike;
//
//    Boolean hasLiked = null;
//
//    MediaPlayer mediaPlayer;
//
//    MediaView mediaView;
//
//    @Override
//    public void initialize (URL url, ResourceBundle resourceBundle)
//    {
//        Gson                         gson              = new Gson ();
//        String                       response          = YoutubeApplication.receiveResponse ();
//        TypeToken <Response <Video>> responseTypeToken = new TypeToken <> ()
//        {
//        };
//        Response <Video>             videoResponse     = gson.fromJson (response, responseTypeToken.getType ());
//        video = videoResponse.getBody ();
//
//        Request<UserVideo> userVideoRequest = new Request <> (YoutubeApplication.socket, "CheckViewVideoExistence");
//        userVideoRequest.send (new UserVideo (YoutubeApplication.user.getUserID (), video.getVideoID ()));
//
//        response = YoutubeApplication.receiveResponse ();
//        TypeToken<Response<UserVideo>> viewResponseTypeToken = new TypeToken <> ()
//        {
//        };
//        Response<UserVideo> userVideoResponse = gson.fromJson (response, viewResponseTypeToken.getType ());
//        UserVideo userVideo = userVideoResponse.getBody ();
//        System.out.println (userVideoResponse.getMessage ());
//
//        if (userVideo != null)
//        {
//            hasLiked = userVideo.getLike ();
//        }
//
//        setVideo();
//        setPlaybackButtons();
//        displayRecommendedVideos ();
//
//        videoScrollPane.getStyleClass ().add("scroll-pane");
//        videoScrollPane.setFitToWidth (true);
//        videoScrollPane.prefWidthProperty ().bind (Bindings.multiply (anchrpnVideoPage.widthProperty (), 13.0 / 20.0));
//        videoScrollPane.prefHeightProperty ().bind (anchrpnVideoPage.heightProperty ());
//        videoScrollPane.setContent (vbxLeft);
//        videoScrollPane.setHbarPolicy (ScrollPane.ScrollBarPolicy.NEVER);
//        videoScrollPane.setVbarPolicy (ScrollPane.ScrollBarPolicy.NEVER);
//        hbx.getChildren ().addAll (videoScrollPane, recommandedVideosScrollPane);
//
//        displayComments();
//    }
//
//    private void setVideo ()
//    {
//    }
//
//    private void setPlaybackButtons ()
//    {
//    }
//
//    private void displayComments ()
//    {
//    }
//
//    private void displayMedia(Media media)
//    {
//        mediaPlayer = new MediaPlayer (media);
//        mediaView = new MediaView (mediaPlayer);
//        mediaView.setPreserveRatio (true);
//        mediaView.setSmooth (true);
//        mediaView.fitWidthProperty ().bind (Bindings.multiply (anchrpnVideoPage.widthProperty (), 13.0 / 20.0).subtract (30));
//        vbxLeft.getChildren ().addFirst (mediaView);
//        mediaPlayer.play ();
//    }
//
//    private void displayRecommendedVideos ()
//    {
//        Request<ArrayList<Video>> userRequest = new Request <> (YoutubeApplication.socket, "GetRecommendedVideos");
//        userRequest.send ();
//
//        String response = YoutubeApplication.receiveResponse ();
//        Gson gson = new Gson ();
//        TypeToken<Response<ArrayList<Video>>> responseTypeToken = new TypeToken <> ()
//        {
//        };
//        Response<ArrayList<Video>> videoResponse = gson.fromJson (response, responseTypeToken.getType ());
//        recommendedVideos = videoResponse.getBody ();
//        if (recommendedVideos != null)
//        {
//            for (var v : recommendedVideos)
//            {
//                if (video.getVideoID () == v.getVideoID ())
//                {
//                    continue;
//                }
//                FXMLLoader videoRecommendationLoader = new FXMLLoader (getClass ().getResource ("/src/main/resources/video-recommendation-view.fxml"));
//                HBox videoRecommendation;
//                try
//                {
//                    videoRecommendation = videoRecommendationLoader.load();
//                    VideoRecommendationController videoRecommendationController = videoRecommendationLoader.getController ();
//                    if (videoRecommendationController != null)
//                    {
//                        videoRecommendationController.setVideo(v);
//                    }
//                }
//                catch (IOException e)
//                {
//                    throw new RuntimeException (e);
//                }
//                Button button = new Button ();
//                button.getStyleClass ().add("btn-video");
//                button.setGraphic (videoRecommendation);
//
//                button.setOnAction (event -> getVideo(event, v));
//                vbxRecommendedVideos.getChildren ().add(button);
//                VBox.setVgrow (videoRecommendation, Priority.ALWAYS);
//            }
//        }
//    }
//
//    private void getVideo (ActionEvent event, Video v)
//    {
//    }
//}
