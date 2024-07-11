package sample;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.model.DBConnector;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static javafx.scene.media.AudioClip.INDEFINITE;

public class GameProfileController {
    @FXML
    public AnchorPane Anchorpane,AnchorImage;
    @FXML
    public Label TitleTxt,DescriptionTxt,DateTxt,RatingTxt,CreatorTxt,CategoryTxt,OsTxt,CpuTxt,
            RamTxt,GpuTxt,SpaceTxt,PriceTxt;
    @FXML
    public ImageView playpauseImage,backImage,nextImage,muteImage,profileImage;
    @FXML
    Button BackToStore_Btn;
    @FXML
    BorderPane BorderVideoPlayer;
    @FXML
    Slider volumeSlider;

    String url,videoUrl;
    int discount;
    float price;
    Boolean playPause = true;
    Boolean ismute = true;
    public static int gameId;


    String tier = GameListController.gettier();

    //Image buttonpressed = new Image("sample/Icons/PlayPause.png");//
    Image play = new Image("sample/Icons/play.png");
    Image pause = new Image("sample/Icons/pause.png");
    Image next = new Image("sample/Icons/next.png");
    Image back  = new Image("sample/Icons/back.png");
    Image soundPlaying = new Image("sample/Icons/speaker.png");
    Image soundNotPlaying = new Image("sample/Icons/mute.png");
    Image profile = new Image("sample/Icons/profile.png");
    Image profile128 = new Image("sample/Icons/profile128.png");


    //video player setup
    URL mediaURL = getClass().getResource("Videos/test.mp4");
    String mediaStringUrl = mediaURL.toExternalForm();
    //Dimiourgia Media
    Media media = new Media(mediaStringUrl);
    //Media player
    MediaPlayer player = new MediaPlayer(media);




    public void initialize(int gid) {
        setupVideoPleyerElements();
        profileImage.setImage(profile);
        gameId = gid;
        System.out.println(gid);
        try {
            String sql = "select * from games where gid = " + gid;

            Connection conn = DBConnector.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            rs.next();

            TitleTxt.setText(rs.getString("title"));
            DescriptionTxt.setText(rs.getString("desciption"));
            DateTxt.setText(String.valueOf(rs.getDate("datereleased")));
            RatingTxt.setText(rs.getString("rating")+"/10");
            CreatorTxt.setText(rs.getString("creator"));
            CategoryTxt.setText(rs.getString("category"));
            OsTxt.setText(rs.getString("os"));
            CpuTxt.setText(rs.getString("cpu"));
            RamTxt.setText(rs.getString("ram")+"GB");
            GpuTxt.setText(rs.getString("gpu"));
            SpaceTxt.setText(rs.getString("discspace")+"GB");
            price = rs.getInt("price");
            url = rs.getString("profileurl");
            videoUrl = rs.getString("videourl");
            discount = rs.getInt("discounted");



            rs.close();
            stmt.close();

            conn.close();

            Image image = new Image(getClass().getResource("/sample"+url).toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(395);
            imageView.setFitHeight(190);
            AnchorImage.getChildren().add(imageView);

        } catch (SQLException | URISyntaxException throwables) {
            throwables.printStackTrace();
        }



        if (tier.startsWith("2")) {
            PriceTxt.setText(String.valueOf(price*0.85)+"€");

        }
        else if(tier.startsWith("3")){
            PriceTxt.setText(String.valueOf(price*0.75)+"€");


        }else {

            PriceTxt.setText(String.valueOf(price)+"€");

        }






        initializeVideo();
    }

    public void LogOut() throws IOException {
        player.stop();
        Parent root = loadFXML("view/login.fxml").load();
        Stage stage = loadStage(root);
        Anchorpane.getScene().getWindow().hide();
        stage.show();
    }

    public void EditProfile() throws IOException {
        player.stop();
        Parent root = null;
        root = loadFXML("view/User_ProfileSettings.fxml").load();
        Stage stage = loadStage(root);
        Anchorpane.getScene().getWindow().hide();
        stage.show();
    }

    public void Profile() throws IOException {




        if (tier.startsWith("1")) {
            player.stop();
            Parent root = null;
            root = loadFXML("view/User_ProfileFree.fxml").load();
            Stage stage = loadStage(root);
            Anchorpane.getScene().getWindow().hide();
            stage.show();

        } else {
            player.stop();
            Parent root = null;
            root = loadFXML("view/User_Profile.fxml").load();
            Stage stage = loadStage(root);
            Anchorpane.getScene().getWindow().hide();
            stage.show();

        }






    }

    public void BackToStore() throws IOException {
        player.stop();
        Parent root = null;
        root = loadFXML("view/gameList.fxml").load();
        Stage stage = loadStage(root);
        Anchorpane.getScene().getWindow().hide();
        stage.show();

    }

    public void RentNow() throws IOException {
        //show rent now window and prevent the user from clicking the gam profile window
        //before closing the rent now window
        player.stop();
        playpauseImage.setImage(play);

        Parent root = loadFXML("view/RentNow.fxml").load();
        Stage stage = loadStage(root);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        //Anchorpane.getScene().getWindow().hide();

    }

    public Stage loadStage(Parent root){
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        //scene.getStylesheets().add(this.getClass().getResource("stylesheet/style.css").toExternalForm());
        return stage;
    }

    public FXMLLoader loadFXML(String url){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(url));
        return fxmlLoader;
    }

    public void initializeVideo(){
        URL mediaURL = getClass().getResource(videoUrl);
        String mediaStringUrl = mediaURL.toExternalForm();
        //Dimiourgia Media
        Media media = new Media(mediaStringUrl);
        //Media player
        player = new MediaPlayer(media);

        player.setAutoPlay(true);
        player.setCycleCount(INDEFINITE);


        //MediaView
        MediaView mediaView = new MediaView(player);
        mediaView.setFitWidth(640);
        mediaView.setFitHeight(360);
        mediaView.setSmooth(true);//na kanei smooth otan kanei scale to video player,mallon axristo gia emas

        BorderVideoPlayer.setCenter(mediaView);


    }

    public void play(){
        if(playPause){
            player.pause();
            playpauseImage.setImage(play);
            //PlayVideo_Btn.setText("Play");
            playPause = false;

        }else{
            player.play();
            playpauseImage.setImage(pause);
            //PlayVideo_Btn.setText("Pause");
            playPause = true;
        }
    }

    public void setupVideoPleyerElements(){
        volumeSlider.setMin(0);
        volumeSlider.setMax(100);
        volumeSlider.setValue(player.getVolume() * 100);
        volumeSlider.setShowTickMarks(true);
        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable){
                player.setVolume(volumeSlider.getValue()/100);
                if(volumeSlider.getValue()/100 == 0){
                    muteImage.setImage(soundNotPlaying);
                    ismute = false;
                }else {
                    muteImage.setImage(soundPlaying);
                    ismute = true;
                }


            }



        });

        playpauseImage.setImage(pause);
        backImage.setImage(back);
        nextImage.setImage(next);
        muteImage.setImage(soundPlaying);
        //imageView.setImage(pause);
    }

    public void next(){
        player.seek(player.getCurrentTime().add(Duration.seconds(10)));
    }

    public void back(){
        player.seek(player.getCurrentTime().add(Duration.seconds(-10)));

    }


    public void mute(){
        if(ismute) {
            muteImage.setImage(soundNotPlaying);
            player.setVolume(0);
            volumeSlider.setValue(0);
            ismute = false;
            System.out.println("now sound is muted");
        }else{
            muteImage.setImage(soundPlaying);
            player.setVolume(1);
            volumeSlider.setValue(100);
            ismute = true;
            System.out.println("now sound is playing");
        }

    }
}