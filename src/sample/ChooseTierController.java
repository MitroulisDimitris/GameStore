package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.model.DBConnector;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ChooseTierController  implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    ImageView BackgroundImage;
    String mid = LoginController.getrId();
    public String tier = GameListController.gettier();

    Image background = new Image("sample/Icons/game_store_plans.png");
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        BackgroundImage.setImage(background);


    }

    public void ChooseBronze() throws SQLException {
            System.out.println("trying");


            String update = "update member set tier = 1 where mid = " + mid;
            Connection conn = DBConnector.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(update);
            stmt.close();
            conn.close();

            GameListController.setTier("1");
            anchorPane.getScene().getWindow().hide();



    }

    public void ChooseSilver() throws SQLException, IOException {

        Parent root = loadFXML("view/Tier_VerifyCard.fxml").load();
        Stage stage = loadStage(root);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        if (VerifyCardController.isConfirmedCard()) {
            String update = "update member set tier = 2 where mid = " + mid;
            Connection conn = DBConnector.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(update);
            stmt.close();
            conn.close();

            GameListController.setTier("2");
            anchorPane.getScene().getWindow().hide();

        }



    }

    public void ChooseGold() throws SQLException, IOException {


        Parent root = loadFXML("view/Tier_VerifyCard.fxml").load();
        Stage stage = loadStage(root);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        if (VerifyCardController.isConfirmedCard()) {
            String update = "update member set tier = 3 where mid = " + mid;
            Connection conn = DBConnector.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(update);
            stmt.close();
            conn.close();

            GameListController.setTier("3");

            anchorPane.getScene().getWindow().hide();

        }
    }

    public void Cancel() throws SQLException {
        if(tier.equals("-1")) {
            ChooseBronze(); }

        anchorPane.getScene().getWindow().hide();

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






}
