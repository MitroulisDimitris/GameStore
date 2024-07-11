package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.model.DBConnector;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class UserProfileSettingsController implements Initializable {

    @FXML private AnchorPane MainPane;

    @FXML private TextField FnameTxt;

    @FXML private TextField PassTxt1;

    @FXML private TextField Email1Txt;

    @FXML private TextField LnameTxt;

    @FXML private TextField PassTxt2;

    @FXML private TextField Email2Txt;

    @FXML private TextField UserNameTxt;

    @FXML ImageView ProfilePic;

    @FXML Button SaveBtn,ChangeImageBtn;




    String rId = LoginController.getrId();
    String Imgurl;
    String tier;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            tier = GameListController.gettier();
            System.out.println("Tier is "+tier);
            Refresh();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }


    public void ChooseFile() throws SQLException {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG Αρχεία", "*.png"));

        File selectedFile = fc.showOpenDialog(null);
        System.out.println(selectedFile);
        if (selectedFile != null) {
            UpdateDb(selectedFile);
            Refresh();

        }

    }


    public void UpdateDb(File url) throws SQLException {
        String update = "UPDATE MEMBER SET IMGURL = '" + url.toString() + "' WHERE MID = '" + rId + "' ";
        System.out.println(update);
        Connection conn = DBConnector.getConnection();
        Statement stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery(update);

        System.out.println("Updated!");

        rs.close();
        stmt.close();
        conn.close();

    }


    public void Refresh() throws SQLException {

        if (tier.equals("1")){

            ChangeImageBtn.setVisible(false);
        }
        String getAll = "SELECT * FROM MEMBER WHERE MID = " + rId;
        Connection conn = DBConnector.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(getAll);

        while (rs.next()) {
            FnameTxt.setText(rs.getString("fname"));
            PassTxt1.setText(rs.getString("password"));
            Email1Txt.setText(rs.getString("email"));
            LnameTxt.setText(rs.getString("lname"));
            UserNameTxt.setText(rs.getString("username"));
            Imgurl = rs.getString("imgurl");
            PassTxt2.setText(PassTxt1.getText());
            Email2Txt.setText(Email1Txt.getText());

        }
        rs.close();
        stmt.close();
        conn.close();

        if (Imgurl.equals("/Icons/DefaultProfileImage.png")) {
            Image profileImage = new Image("sample/" + Imgurl);
            ProfilePic.setImage(profileImage);

        }
        else {
            String path = "file:" + Imgurl;
            Image profileImage = new Image(path);
            ProfilePic.setImage(profileImage);

        }

    }


    public void SaveOnAction() throws SQLException {
        if (CheckNull()) {
            if (PasswordMatches() && EmailMatches()) {
                if (!EmailExists()) {
                    if (!UsernameExists()) {
                        ExcecuteUpdate();
                        AlertBox.display("", "Τα στοιχεία σας ενημερώθηκαν επιτυχώς!");
                    }
                    else {
                        AlertBox.display("Προσοχή", "Αυτό το Username χρησιμοποιείται απο άλλο χρήστη!");
                    }
                }
                else {
                    AlertBox.display("Προσοχή", "Αυτό το email χρησιμοποιείται απο άλλο χρήστη!");
                }
            }
            else {
                AlertBox.display("Προσοχή", "Τα email ή οι κωδικοί δεν ταιρίαζουν!");
            }
        }
        else {
            AlertBox.display("Προσοχή", "Παρακαλώ συμπληρώστε όλα τα πεδία!");
        }

        Refresh();
    }


    public void ExcecuteUpdate() throws SQLException {
        String update = "UPDATE MEMBER SET" + " USERNAME ='" + UserNameTxt.getText() +
                "', FNAME = '" + FnameTxt.getText() + "',LNAME =  '" + LnameTxt.getText() + "', PASSWORD = '" + PassTxt1.getText() + "' " +
                ",EMAIL  = '" + Email1Txt.getText() + "' WHERE MID = " + rId;


        Connection conn = DBConnector.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(update);

        rs.close();
        stmt.close();
        conn.close();

    }


    public boolean PasswordMatches() {
        return (PassTxt2.getText().equals(PassTxt1.getText()));
    }


    public boolean EmailMatches() {
        return (Email1Txt.getText().equals(Email2Txt.getText()));
    }


    public void ChooseTier() throws IOException {
        Parent root = loadFXML("view/ChooseTier.fxml").load();
        Stage stage = loadStage(root);
        // αναγκαζει τον χρηστη να κανει ακυρο ή να επιλεξει νεο tier
        stage.initModality(Modality.APPLICATION_MODAL);
        // να μην εμγανιζει το Χ maximize minimize πανω δεξιά
        stage.initStyle(StageStyle.UNDECORATED);
        stage.centerOnScreen();

        stage.show();

    }


    public void BackToStore() throws IOException {
        if (GameListController.gettier().startsWith("1")) {
            Parent root = null;
            root = loadFXML("view/User_ProfileFree.fxml").load();
            Stage stage = loadStage(root);
            MainPane.getScene().getWindow().hide();
            stage.show();

        }
        else {
            Parent root = null;
            root = loadFXML("view/User_Profile.fxml").load();
            Stage stage = loadStage(root);
            MainPane.getScene().getWindow().hide();
            stage.show();

        }


    }


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


    public boolean EmailExists() throws  SQLException {
        String select = "SELECT * FROM MEMBER WHERE EMAIL = '" + Email1Txt.getText() + "' AND MID <> '"+ rId +"'";
        Connection conn = DBConnector.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(select);

        boolean result = rs.next();
        rs.close();
        stmt.close();
        conn.close();
        return (result);

    }


    public boolean UsernameExists() throws  SQLException {
        String select = "SELECT * FROM MEMBER WHERE USERNAME = '" + UserNameTxt.getText() + "' AND MID <> '"+ rId +"'";
        Connection conn = DBConnector.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(select);

        boolean result = rs.next();
        rs.close();
        stmt.close();
        conn.close();
        return (result);

    }


    public boolean isAlpha(String name) {
        return name.matches("[a-zA-Zα-ωΑ-Ω]{0,20}$");
    }


    public boolean isUserName(String name) {
        return name.matches("^.{0,20}");
    }


    public boolean isEmail(String name) {
        return name.matches("^\\S+@\\S+\\.\\S+$");
    }


    public void CheckFname() {
        if (!isAlpha(FnameTxt.getText())) {
            FnameTxt.setStyle("-fx-text-inner-color: red;");
            Disablebuttons();
        }
        else {
            FnameTxt.setStyle("-fx-text-inner-color: black;");
            EnableButtons();
        }
    }


    public void CheckLname() {
        if (!isAlpha(LnameTxt.getText())) {
            LnameTxt.setStyle("-fx-text-inner-color: red;");
            Disablebuttons();
        }
        else {
            LnameTxt.setStyle("-fx-text-inner-color: black;");
            EnableButtons();
        }
    }


    public void CheckPass() {
        if (!isUserName(PassTxt1.getText())) {
            PassTxt1.setStyle("-fx-text-inner-color: red;");
            Disablebuttons();
        }
        else {
            PassTxt1.setStyle("-fx-text-inner-color: black;");
            EnableButtons();
        }

    }


    public void CheckPass2() {
        if (!isUserName(PassTxt2.getText())) {
            PassTxt2.setStyle("-fx-text-inner-color: red;");
            Disablebuttons();
        }
        else {
            PassTxt2.setStyle("-fx-text-inner-color: black;");
            EnableButtons();
        }

    }


    public void CheckEmail() {
        if (!isEmail(Email1Txt.getText())) {
            Email1Txt.setStyle("-fx-text-inner-color: red;");
            Disablebuttons();
        }
        else {
            Email1Txt.setStyle("-fx-text-inner-color: black;");
            EnableButtons();
        }
    }


    public void CheckEmail2() {
        if (!isEmail(Email2Txt.getText())) {
            Email2Txt.setStyle("-fx-text-inner-color: red;");
            Disablebuttons();
        }
        else {
            Email2Txt.setStyle("-fx-text-inner-color: black;");
            EnableButtons();
        }
    }



    public void CheckUsername() {
        if (!isUserName(UserNameTxt.getText())) {
            UserNameTxt.setStyle("-fx-text-inner-color: red;");
            Disablebuttons();
        }
        else {
            UserNameTxt.setStyle("-fx-text-inner-color: black;");
            EnableButtons();
        }
    }


    public boolean CheckNull() {
        return (FnameTxt.getText().length() > 0 && PassTxt1.getText().length() > 0 && Email1Txt.getText().length() > 0 && LnameTxt.getText().length() > 0 &&
                PassTxt2.getText().length() > 0 && Email2Txt.getText().length() > 0 && UserNameTxt.getText().length() > 0);

    }


    public void Disablebuttons() {
        SaveBtn.setDisable(true);
    }


    public void EnableButtons() {
        SaveBtn.setDisable(false);
    }


}