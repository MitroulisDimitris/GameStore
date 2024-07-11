package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.model.DBConnector;


import java.io.IOException;
import java.sql.*;

public class LoginController {
    public static String rId = "Login";
    public static String tier;
    @FXML
    Button LoginBtn, SignUpBtn, Go_backBtn;
    @FXML
    TextField UsernameTxt;
    @FXML
    PasswordField PasswordTxt;
    @FXML
    Label label1;
    @FXML
    Hyperlink ForgotPass;

    public static String getrId() {
        return rId;
    }

    public static void setrId(String rId) {
        LoginController.rId = rId;
    }

    public void Sign_upOnAction() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("view/SignUp.fxml"));
        Stage window = (Stage)SignUpBtn.getScene().getWindow();
        window.centerOnScreen();
        window.setScene(new Scene (root,847,692));

    }

    public void LoginOnAction() throws Exception {

        try {
            //get username and password from textfields
            String usernameStr = UsernameTxt.getText();
            String passwordStr = PasswordTxt.getText();

            //check if either username or password is emty
            if (usernameStr.isEmpty() == false || passwordStr.isEmpty() == false) {

                //create connection
                Connection connection = DBConnector.getConnection();
                System.out.println("Connected to Oracle Database");


                //run select command with fname and password from user input
                String select = "Select * from MEMBER where username = '" + usernameStr + "' and PASSWORD ='" + passwordStr+"' ";

                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(select);
                //check if result set has at least one row,if yes succesefully authenitcated

                if (result.next() == true) {


                    rId = result.getString("MID");
                    tier = result.getString("tier");

                    System.out.println(rId);


                    //change screen to Main_screen.fxml
                    Parent root = FXMLLoader.load(getClass().getResource("view/gameList.fxml"));//Email_Screen
                    Stage window = (Stage) LoginBtn.getScene().getWindow();

                    window.setScene(new Scene(root, 1104, 700));
                    //System.out.println("Swsta stoixeia");

                } else {
                    System.out.println("Array is empty");
                    //label1.setText("Username or password is false");
                    sample.AlertBox.display("Warning","Ο κωδικός ή το Όνομα χρήστη είναι λάθος!");

                } statement.close();
                connection.close();
                System.out.println("Connection closed");
            }
        } catch (
                SQLException e) {
            System.out.println("Unsuccessful connection");
            e.printStackTrace();
        }
    }

    public void ForgotPassWord() throws IOException {
        Parent root = loadFXML("view/ForgotPassWord.fxml").load();
        Stage stage = loadStage(root);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

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
