package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.model.DBConnector;
import sample.model.Game;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class UserProfileController implements Initializable {

    @FXML AnchorPane MainPane;

    @FXML GridPane gridPane;

    @FXML ScrollPane scrollPane;

    @FXML private Label UserNameTxt;

    @FXML private Label FnameTxt;

    @FXML private Label LnameTxt;

    @FXML private Label DateTxt;

    @FXML private Label MonthTxt;

    @FXML ImageView ProfilePic;

    @FXML Label TierLbl;





    public String tier = GameListController.gettier();
    Button[] gameBtn;
    AnchorPane[] anchor = new AnchorPane[0];
    public static int[] GameNubmers = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    String Imgurl;


    int currentgid, id, loops;
    ObservableList<Game> games = FXCollections.observableArrayList();
    String rId = LoginController.getrId();
    Connection conn ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Refresh();
            loadGames(1);
        }
        catch (SQLException | URISyntaxException throwables) {
            throwables.printStackTrace();
        }

    }



    private void Refresh() throws SQLException {
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
        }else{
            String path = "file:" + Imgurl;
            Image profileImage = new Image(path);
            ProfilePic.setImage(profileImage);
        }

    }

    public void loadGames(int month) throws URISyntaxException {



        try {

            String sql = "select games.gid , games.title ,games.desciption ,games.datereleased,EXTRACT(MONTH FROM Rents.rentdate), Games.title, Games.profileurl \n" +
                    "from Rents \n" +
                    "inner join Games on Rents.gid= Games.gid \n" +
                    "where EXTRACT(MONTH FROM Rents.rentdate) = " + month + "";

            conn = DBConnector.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql);

            System.out.println("connected to database");

            while (results.next()) {
                id = results.getInt("gid");
                String title = results.getString("title");
                String desc = results.getString("desciption");
                String date = results.getString("datereleased");
                String url = results.getString("profileurl");

                Game newGame = new Game(title, desc, date, url);

                newGame.setId(id);
                newGame.setTitle(title);
                games.add(newGame);

            }

            results.close();
            stmt.close();
            conn.close();
            System.out.println("closed Connection");

        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        gridPane.setHgap(15);
        gridPane.setVgap(15);
        currentgid = id;
        int col = 0;
        int row = 0;

        gameBtn = new Button[games.size()];
        anchor = new AnchorPane[games.size()];

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        loops = games.size();//to erase things when searching

        for (int i = 0; i < games.size(); i++) {
            GameNubmers[i] = games.get(i).getId();
            String url = games.get(i).getUrl();
            String title = games.get(i).getTitle();

            Image image = new Image(getClass().getResource("/sample" + url).toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(125d);
            imageView.setFitHeight(160d);

            anchor[i] = new AnchorPane();
            anchor[i].setMinWidth(125d);
            anchor[i].setMinHeight(200d);

            Label Gtitle = new Label();
            Gtitle.setText(title);
            Gtitle.setTranslateY(160d);

            Gtitle.setTranslateX(10d);
            Gtitle.setTextFill(Paint.valueOf("#ffffff"));
            Gtitle.setFont(Font.font(15));

            gameBtn[i] = new Button("", imageView);
            gameBtn[i].setMinWidth(125d);
            gameBtn[i].setMinHeight(160d);
            gameBtn[i].setStyle("-fx-background-color: #333333; ");
            anchor[i].getChildren().addAll(gameBtn[i], Gtitle);

            gridPane.add(anchor[i], col, row);


            col++;

            if (col == 3) {
                row++;
                col = 0;
            }



            int finalI1 = i;


            gameBtn[i].setOnAction(new EventHandler<ActionEvent>() {


                @Override
                public void handle(ActionEvent event) {
                    FXMLLoader fxmlLoader = loadFXML("view/GameProfile.fxml");
                    try {
                        Parent root = fxmlLoader.load();
                        Stage stage = loadStage(root);
                        GameProfileController stageController = fxmlLoader.getController();


                        stageController.initialize(GameNubmers[finalI1]);

                        stage.initOwner(MainPane.getScene().getWindow());
                        stage.initModality(Modality.WINDOW_MODAL);
                        MainPane.getScene().getWindow().hide();
                        stage.show();

                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            });

        }

    }


    @FXML
    private void OpenSettings() throws IOException {
        Parent root = null;
        root = loadFXML("view/User_ProfileSettings.fxml").load();
        Stage stage = loadStage(root);
        MainPane.getScene().getWindow().hide();
        stage.show();

    }


    public void BackToStore() throws IOException {
        Parent root = null;
        root = loadFXML("view/gameList.fxml").load();
        Stage stage = loadStage(root);
        MainPane.getScene().getWindow().hide();
        stage.show();

    }

    //====================================================
    public void JanBtn() throws URISyntaxException {
        games.clear();

        for (int i = 0; i < loops; i++) {
            gridPane.getChildren().remove(anchor[i]);
        }

        loadGames(1);
        MonthTxt.setText("Ιανουάριος");
    }

    public void FebBtn() throws URISyntaxException {
        games.clear();

        for (int i = 0; i < loops; i++) {
            gridPane.getChildren().remove(anchor[i]);
        }

        loadGames(2);
        MonthTxt.setText("Φεβρουάριος");
    }

    public void MarBtn() throws URISyntaxException {
        games.clear();

        for (int i = 0; i < loops; i++) {
            gridPane.getChildren().remove(anchor[i]);
        }

        loadGames(3);
        MonthTxt.setText("Μάρτιος");

    }

    public void AprBtn() throws URISyntaxException {
        games.clear();

        for (int i = 0; i < loops; i++) {
            gridPane.getChildren().remove(anchor[i]);
        }

        loadGames(4);
        MonthTxt.setText("Απρίλιος");

    }

    public void MayBtn() throws URISyntaxException {
        games.clear();

        for (int i = 0; i < loops; i++) {
            gridPane.getChildren().remove(anchor[i]);
        }

        loadGames(5);
        MonthTxt.setText("Μαίος");

    }

    public void JunBtn() throws URISyntaxException {
        games.clear();

        for (int i = 0; i < loops; i++) {
            gridPane.getChildren().remove(anchor[i]);
        }

        loadGames(6);
        MonthTxt.setText("Ιούνιος");

    }

    public void JulBtn() throws URISyntaxException {
        games.clear();

        for (int i = 0; i < loops; i++) {
            gridPane.getChildren().remove(anchor[i]);
        }

        loadGames(7);
        MonthTxt.setText("Ιούλιος");

    }

    public void AugBtn() throws URISyntaxException {
        games.clear();

        for (int i = 0; i < loops; i++) {
            gridPane.getChildren().remove(anchor[i]);
        }

        loadGames(8);
        MonthTxt.setText("Αύγουστος");

    }

    public void SeptBnt() throws URISyntaxException {
        games.clear();

        for (int i = 0; i < loops; i++) {
            gridPane.getChildren().remove(anchor[i]);
        }

        loadGames(9);
        MonthTxt.setText("Σεπτέμβριος");

    }

    public void OctBtn() throws URISyntaxException {
        games.clear();

        for (int i = 0; i < loops; i++) {
            gridPane.getChildren().remove(anchor[i]);
        }

        loadGames(10);
        MonthTxt.setText("Οκτώβριος");

    }

    public void NovBtn() throws URISyntaxException {
        games.clear();

        for (int i = 0; i < loops; i++) {
            gridPane.getChildren().remove(anchor[i]);
        }

        loadGames(11);
        MonthTxt.setText("Νοέμβριος");

    }

    public void DecBtn() throws URISyntaxException {
        games.clear();

        for (int i = 0; i < loops; i++) {
            gridPane.getChildren().remove(anchor[i]);
        }

        loadGames(12);
        MonthTxt.setText("Δεκέμβριος");

    }
    //====================================================

    public Stage loadStage(Parent root) {
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        return stage;

    }


    public FXMLLoader loadFXML(String url) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(url));
        return fxmlLoader;

    }


}