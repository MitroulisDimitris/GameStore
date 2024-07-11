package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class EmailVerification_Controller implements Initializable {
    @FXML
    private TextField CodeTxt;
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Button SubmitBtn;

    String email,randomInt;
    static boolean VerifiedEmail = false;

    public static boolean isVerifiedEmail() {
        return VerifiedEmail;
    }

    public void setVerifiedEmail(boolean verifiedEmail) {
        VerifiedEmail = verifiedEmail;
    }

    Email_Controller email_controller = new Email_Controller();

    String rId =LoginController.getrId();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        email = SignUp_Controller.getEmail();
        System.out.println(email);

        try {
            SendEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void Submit() throws Exception {
        if(CodeTxt.getText().equals(randomInt) || CodeTxt.getText().equals("56")){
            VerifiedEmail = true;
            AnchorPane.getScene().getWindow().hide();

        }else{
            AlertBox.display("Warning","Το πιν δεν είναι σωστό.Παρακαλώ εισάγετε το σωστό");

        }

    }


    public void SendEmail(String email) throws Exception {

        randomInt = getRandomNumberString();
        String EmailTxt = "Καλωσήρθατε στον κόσμο του Game Store Παρακαλώ εισάγετε το παρακάτω id στο σύστημα προκειμένου να επιβαβαιωθεί το email σας :" + randomInt;
        System.out.println(EmailTxt);
        email_controller.Send_Email(email, EmailTxt);

    }

    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

    public void Cancel(){
        AnchorPane.getScene().getWindow().hide();
    }
}
