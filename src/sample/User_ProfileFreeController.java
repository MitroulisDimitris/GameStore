package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.model.DBConnector;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class User_ProfileFreeController implements Initializable {

    @FXML AnchorPane MainPane;

    @FXML private Label UserNameTxt;

    @FXML private Label FnameTxt;

    @FXML private Label LnameTxt;

    @FXML private Label DateTxt;

    @FXML ImageView ProfilePic;

    @FXML Label TierLbl;


    //======================================================
    //===============AUTHOR ARISTOTELIS PALLASIDIS ===============
    //======================================================

    public String tier = GameListController.gettier();

    String rId = LoginController.getrId();

    String Imgurl;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Refresh();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public void Refresh() throws SQLException {
        String getAll = "SELECT * FROM MEMBER WHERE MID = " + rId;
        Connection conn  = DBConnector.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(getAll);



        while(rs.next()){
            FnameTxt.setText(rs.getString("fname"));
            LnameTxt.setText(rs.getString("lname"));
            UserNameTxt.setText(rs.getString("username"));
            DateTxt.setText(rs.getDate("datejoined").toString());
            Imgurl=rs.getString("imgurl");
        }

        rs.close();
        stmt.close();
        conn.close();

        if(tier.equals("1")){
            TierLbl.setText("Απλό Μέλος");
            TierLbl.setStyle("-fx-background-radius: 6;" +
                    "-fx-background-color: #CD7F32;"
            );
        }else if (tier.equals("2")){
            TierLbl.setText("Ασημένιο Μέλος");
            TierLbl.setStyle("-fx-background-radius 6;" +
                    "-fx-background-color: #BEC2CB;"
            );
        }else{
            TierLbl.setText("Χρυσό Μέλος");
            TierLbl.setStyle("-fx-background-radius 6;" +
                    "-fx-background-color: #FFD700;"
            );
        }

        if(Imgurl.equals("/Icons/DefaultProfileImage.png")) {
            Image profileImage = new Image("sample/" + Imgurl);
            ProfilePic.setImage(profileImage);

        }
        else{
            String path = "file:" + Imgurl;
            Image profileImage = new Image(path);
            ProfilePic.setImage(profileImage);

        }

    }


    public void backOnAction() throws IOException {
        Parent root = null;
        root = loadFXML("view/gameList.fxml").load();
        Stage stage = loadStage(root);
        MainPane.getScene().getWindow().hide();
        stage.show();

    }


    public void UserSettings() throws IOException {
        Parent root = null;
        root = loadFXML("view/User_ProfileSettings.fxml").load();
        Stage stage = loadStage(root);
        MainPane.getScene().getWindow().hide();
        stage.show();

    }


    public Stage loadStage(Parent root){
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        return stage;

    }


    public FXMLLoader loadFXML(String url){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(url));
        return fxmlLoader;

    }


}