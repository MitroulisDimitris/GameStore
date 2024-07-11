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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.model.DBConnector;
import sample.model.Game;
import sample.model.User;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class GameListController implements Initializable {

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane gridPane;
    @FXML
    private AnchorPane movieListPane;
    //private final Button profileBtn = new Button();
    //private Button backBtn = new Button();
    @FXML
    private FlowPane menuFlowPane;
    @FXML
    private TextField id_Text;
    @FXML
    private ChoiceBox<String> choiceBox;
    @FXML
    Label index;
    @FXML
    ImageView profileImage;
    @FXML
    CheckBox DiscountedOnly;

    ObservableList choiceBoxStatus = FXCollections.observableArrayList();

    public AnchorPane[] anchor = new AnchorPane[0];
    public int currentgid;


    private final User currentUser = null;
    public String title;
    String discoutedString = "%";


    ObservableList<Game> games = FXCollections.observableArrayList();
    public Button[] gameBtn;
    public int id;
    public static int[] GameNubmers = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    int count = 0;
    int loops, number;
    String rId = LoginController.getrId();

    static String tier = null;

    public static String gettier() {
        return tier;
    }

    public static void setTier(String tier) {
        GameListController.tier = tier;
    }

    boolean onlyDiscountedgames = false;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Image profile = new Image("sample/Icons/profile.png");
        profileImage.setImage(profile);
        String title = "Τίτλος";
        String rating = "Αξιολόγηση";
        String creator = "Δημιουργός";
        String category = "Κατηγορία";


        choiceBoxStatus.addAll(title, rating, creator, category);
        choiceBox.getItems().addAll(choiceBoxStatus);
        choiceBox.setValue(title);

        String Greatergid = "like  '%'";
        String choice = "title";

        try {
            loadGames(Greatergid, choice);
            CheckTier(rId);
        } catch (URISyntaxException | SQLException | IOException e) {
            e.printStackTrace();
        }


    }

    public void DiscountedGamesCheckBox() throws URISyntaxException {


        if(onlyDiscountedgames) {
             onlyDiscountedgames = false;
             discoutedString = "%";
             //loadGames("like  '%'","title");

         }else{
             onlyDiscountedgames = true;
             discoutedString = "%1%";
             //loadGames("like  '%'","title");
         }

        Search();

         }

    public void CheckTier(String rId) throws SQLException, IOException {

        String getTier = "select * from member where mid = " + rId;
        Connection conn = DBConnector.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(getTier);
        while (rs.next()) {
            tier = rs.getString("tier");

        }
        rs.close();
        stmt.close();
        conn.close();

        if (tier.equals("-1")) {
            Parent root = loadFXML("view/ChooseTier.fxml").load();
            Stage stage = loadStage(root);
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.initStyle(StageStyle.UNDECORATED);

            stage.setOnCloseRequest(e -> {
                System.out.println("hello");
            });
            stage.centerOnScreen();

            stage.show();
            //stage.showAndWait();


        }
    }

    public void loadGames(String gtitle, String choice) throws URISyntaxException {
        count = 0;


        Connection conn = null;
        try {
            String sql = "select * from games where lower(" + choice + ") " + gtitle +" and discounted like '"+discoutedString+"'";
            //select * from games where lower(title) like '%%' and discounted like '%%';
            System.out.println(sql);
            conn = DBConnector.getConnection();

            Statement stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql);

            System.out.println("connected to database");

            while (results.next()) {

                id = results.getInt(1);
                title = results.getString(2);
                String desc = results.getString(3);
                String date = results.getString(4);
                String url = results.getString(5);
                Game newGame = new Game(title, desc, date, url);

                //System.out.println(title);
                newGame.setId(id);
                newGame.setTitle(title);
                games.add(newGame);

            }
            results.close();
            stmt.close();
            conn.close();
            System.out.println("closed Connection");


        } catch (SQLException throwables) {
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
            //ancor[i].setStyle("-fx-background-color: #333333; ");


            gameBtn[i] = new Button("", imageView);
            gameBtn[i].setMinWidth(125d);
            gameBtn[i].setMinHeight(160d);
            gameBtn[i].setStyle("-fx-background-color: #333333; ");
            anchor[i].getChildren().addAll(gameBtn[i], Gtitle);

            gridPane.add(anchor[i], col, row);

            //gridPane.getChildren().remove(movieBtn[i]);
            //System.out.println("Running1");
            col++;

            if (col == 7) {
                row++;
                col = 0;
            }

            int index = i;

            int finalI1 = i;

            gameBtn[i].setOnAction(new EventHandler<ActionEvent>() {
                Parent root = null;

                @Override
                 public void handle(ActionEvent event) {
                   FXMLLoader fxmlLoader = loadFXML("view/GameProfile.fxml");
                   try {
                       Parent root = fxmlLoader.load();
                       Stage stage = loadStage(root);
                       GameProfileController stageController = fxmlLoader.getController();




                       stageController.initialize(GameNubmers[finalI1]);
                       stage.initOwner(movieListPane.getScene().getWindow());
                       stage.initModality(Modality.WINDOW_MODAL);
                       movieListPane.getScene().getWindow().hide();
                       stage.show();


                   } catch (IOException e) {
                        e.printStackTrace();
                   }



                }
            });


        }

    }

    public Stage loadStage(Parent root) {
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        //scene.getStylesheets().add(this.getClass().getResource("stylesheet/style.css").toExternalForm());
        return stage;
    }

    public FXMLLoader loadFXML(String url) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(url));
        return fxmlLoader;
    }

    public void Search() throws URISyntaxException {
        String choice = choiceBox.getValue();
        switch(choice) {
            case "Τίτλος":
                choice = "title";
                break;
            case "Αξιολόγηση":
                choice = "rating";
                break;
            case "Δημιουργός":
                choice = "creator";
                break;
            case "Κατηγορία":
                choice = "category";
                break;
            }

                games.clear();//i moved it here from inside the loop,no point deleting the games over and over
        for (int i = 0; i < loops; i++) {
            //System.out.println(anchor[i]);
            gridPane.getChildren().remove(anchor[i]);


        }

        String gid = id_Text.getText();
        gid = "like '%" + gid + "%'";
        System.out.println(gid);
        loadGames(gid, choice);

    }

    public void LogOut() throws IOException {
        Parent root = loadFXML("view/login.fxml").load();
        Stage stage = loadStage(root);
        movieListPane.getScene().getWindow().hide();
        stage.show();
    }

    public void EditProfile() throws IOException {


        Parent root = null;
        root = loadFXML("view/User_ProfileSettings.fxml").load();
        Stage stage = loadStage(root);
        movieListPane.getScene().getWindow().hide();
        stage.show();


    }

    public void Profile() throws IOException {
        if (tier.startsWith("1")) {
            Parent root = null;
            root = loadFXML("view/User_ProfileFree.fxml").load();
            Stage stage = loadStage(root);
            movieListPane.getScene().getWindow().hide();
            stage.show();

        } else {

            Parent root = null;
            root = loadFXML("view/User_Profile.fxml").load();
            Stage stage = loadStage(root);
            movieListPane.getScene().getWindow().hide();
            stage.show();

        }
    }
}