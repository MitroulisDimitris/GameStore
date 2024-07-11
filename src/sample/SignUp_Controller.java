package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.model.DBConnector;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;



public class SignUp_Controller implements Initializable {
    @FXML
    BorderPane borderPane;
    @FXML
    private ImageView GamestoreImageView;
    @FXML
    private ImageView SignUpImageView;
    @FXML
    private Button CancelBtn;
    @FXML
    private Label SignUpMessageLbl;
    @FXML
    private TextField EmailTextField;
    @FXML
    private TextField ConfirmEmail;
    @FXML
    private Label ConfirmEmailLbl;
    @FXML
    private PasswordField setPasswordField;
    @FXML
    private PasswordField ConfirmPasswordField;
    @FXML
    private Label ConfirmPasswordLbl;
    @FXML
    private TextField FirstnameTextField;
    @FXML
    private TextField LastnameTextField;
    @FXML
    private TextField UsernameTextfield;
    @FXML
    private Button SignUpBtn;

    static String Email;

    public static String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image gameStore = new Image("sample/Icons/gamestore.png");
        Image userLogo = new Image("sample/Icons/SignUpLogo.png");
        ConfirmPasswordLbl.setText("Ο κωδικός πρόσβασης δεν ταιριάζει!");
        ConfirmEmailLbl.setText("Η ηλεκτρονική διεύθυνση δεν ταιριάζει!");

        ConfirmEmailLbl.setVisible(false);
        ConfirmPasswordLbl.setVisible(false);

        GamestoreImageView.setFitWidth(365);
        GamestoreImageView.setFitHeight(692);
        GamestoreImageView.setPreserveRatio(false);
        GamestoreImageView.setImage(gameStore);

        SignUpImageView.setImage(userLogo);
    }


    public void SignUpBtnOnAction(ActionEvent event) throws SQLException {
        ConfirmEmailLbl.setVisible(false);
        ConfirmPasswordLbl.setVisible(false);
        if(CheckNull()){
        if (setPasswordField.getText().equals(ConfirmPasswordField.getText())) {


            if (EmailTextField.getText().equals(ConfirmEmail.getText())) {
                try {
                    SignUpUser();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ConfirmEmailLbl.setStyle("-fx-text-inner-color: green;");


            } else {
                ConfirmEmailLbl.setVisible(true);
            }

        } else
            ConfirmPasswordLbl.setVisible(true);

    }else{
        AlertBox.display("Warning","Παρακαλώ συμπληρώστε όλα τα στοιχεία σας!");
        }
    }

    public boolean UsernameCheckFromDb(String Username) throws SQLException {
        String select = "Select * from MEMBER where username = '" + Username + "' ";
        System.out.println(Username);
        Connection conn = DBConnector.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(select);
        return !rs.next();



    }

    public boolean EmailCheckFromDb(String Email) throws SQLException {
        String select = "Select * from MEMBER where email = '" + Email + "' ";
        System.out.println(Email);
        Connection conn = DBConnector.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(select);
        return !rs.next();



    }

    public void CancelBtnOnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("view/Login.fxml"));
        Stage window = (Stage)SignUpBtn.getScene().getWindow();
        window.centerOnScreen();
        window.setScene(new Scene(root,600,400));
    }

    public void SignUpUser() throws SQLException, IOException {




        if (UsernameCheckFromDb(UsernameTextfield.getText())) {
            if (EmailCheckFromDb(EmailTextField.getText())) {

                Email = EmailTextField.getText();

                Parent root = loadFXML("view/EmailVerification.fxml").load();
                Stage stage = loadStage(root);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

                if (EmailVerification_Controller.isVerifiedEmail()) {



                    DBConnector connectNow = new DBConnector();
                    Connection connectDB = connectNow.getConnection();

                    String Firstname = FirstnameTextField.getText();
                    String Lastname = LastnameTextField.getText();
                    Email = EmailTextField.getText();
                    String Password = setPasswordField.getText();
                    String Username = UsernameTextfield.getText();
                    DateTimeFormatter DateJoined = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDateTime now = LocalDateTime.now();

                    String InsertFields = "INSERT INTO Member(mid,Fname,Lname,email,password,username,dateJoined,tier,imgurl) VALUES(";
                    String InsertValues = "mid.nextval" + ",'" + Firstname + "','" + Lastname + "','" + Email + "','" + Password + "','" + Username + "',TO_DATE('" + DateJoined.format(now) + "','DD/MM/YYYY'),-1,'/Icons/DefaultProfileImage.png')";
                    String InsertToSignUp = InsertFields + InsertValues;

                    try {
                        Statement statement = connectDB.createStatement();
                        statement.executeUpdate(InsertToSignUp);
                        statement.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                        e.getCause();
                    }

                    String select_id = "Select * from MEMBER where username = '" + Username + "' ";
                    System.out.println(Username);
                    Connection conn = DBConnector.getConnection();
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(select_id);
                    while (rs.next()) {
                        String rId = rs.getString("mid");
                        LoginController.setrId(rId);

                    }
                    root = loadFXML("view/Login.fxml").load();
                    stage = loadStage(root);
                    borderPane.getScene().getWindow().hide();
                    stage.show();
                }
            } else {
                AlertBox.display("Warning", "Το email χρησιμοποιείται ήδη!");
            }
        } else {
            AlertBox.display("Warning", "Το Όνομα χρήστη χρησιμοποιείται ήδη!");
        }


    }

    public void CheckFname() {
        if (!isAlpha(FirstnameTextField.getText())) {
            FirstnameTextField.setStyle("-fx-text-inner-color: red;");
            Disablebuttons();
        } else {
            FirstnameTextField.setStyle("-fx-text-inner-color: black;");
            EnableButtons();
        }
    }

    public void CheckLname() {
        if (!isAlpha(LastnameTextField.getText())) {
            LastnameTextField.setStyle("-fx-text-inner-color: red;");
            Disablebuttons();
        } else {
            LastnameTextField.setStyle("-fx-text-inner-color: black;");
            EnableButtons();
        }
    }

    public void CheckUsername(){
        if (!isUserName(UsernameTextfield.getText())) {
            UsernameTextfield.setStyle("-fx-text-inner-color: red;");
            Disablebuttons();
        } else {
            UsernameTextfield.setStyle("-fx-text-inner-color: black;");
            EnableButtons();
        }
    }

    public void CheckEmail(){
        if (!isEmail(EmailTextField.getText())) {
            EmailTextField.setStyle("-fx-text-inner-color: red;");
            Disablebuttons();
        } else {
            EmailTextField.setStyle("-fx-text-inner-color: black;");
            EnableButtons();
        }
    }

    public void CheckPassword(){
        if (!isUserName(setPasswordField.getText())) {
            setPasswordField.setStyle("-fx-text-inner-color: red;");
            Disablebuttons();
        } else {
            setPasswordField.setStyle("-fx-text-inner-color: black;");
            EnableButtons();
        }

    }

    public boolean CheckNull(){
        return (EmailTextField.getText().length() >0  && ConfirmEmailLbl.getText().length() >0  && setPasswordField.getText().length() >0 && ConfirmPasswordField.getText().length() >0
                && FirstnameTextField.getText().length() >0  && LastnameTextField.getText().length() >0 && UsernameTextfield.getText().length() >0  );


    }

    public boolean isUserName(String name){
        return name.matches("^.{0,20}");

    }

    public boolean isAlpha(String name) {
        return name.matches("[a-zA-Z]{0,20}$");
    }

    public boolean isEmail(String name){

        return name.matches("^\\S+@\\S+\\.\\S+$");
    }

    public void Disablebuttons() {
        SignUpBtn.setDisable(true);


    }

    public void EnableButtons() {
        SignUpBtn.setDisable(false);


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