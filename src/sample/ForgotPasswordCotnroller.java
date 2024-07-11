package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import sample.model.DBConnector;

import java.net.URL;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ForgotPasswordCotnroller implements Initializable {
    @FXML
    private TextField EmailTxt;

    @FXML
    private
    AnchorPane AnchorPane;

    @FXML
    private Button SubmitBtn;

    @FXML
    private Label NotFoundWarning;
    String email, newPass;

    Email_Controller email_controller = new Email_Controller();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NotFoundWarning.setVisible(false);
    }

    public void Submit() throws Exception {
        NotFoundWarning.setVisible(false);

        if (!EmailTxt.getText().equals("")) {


            if (EmailExists()) {

                SendEmail(EmailTxt.getText());
                UpdateTable();
                AlertBox.display("", "Σας στάλθηκαν οδηγίες στο email σας.");
            } else {
                NotFoundWarning.setVisible(true);
                AlertBox.display("Warning", "Το email αυτό δεν αντισοιτχεί σε κανέναν χρήστη!");}

        } else { AlertBox.display("Warning", "Παρακαλώ εισάγετε το email σας");}
    }

    public boolean EmailExists() throws SQLException {
        Connection connection = DBConnector.getConnection();
        String select = "Select * from member where email = '" + EmailTxt.getText() + "' ";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(select);
        boolean resultSet = rs.next();
        rs.close();
        statement.close();
        connection.close();
        return (resultSet);

    }

    public void SendEmail(String email) throws Exception {

        newPass = generateRandomPassword(8);
        String EmailTxt = "Ενεργοποιήθηκε η λειτουργία επαναφοράς κωδικού για τον λογαριασμό σας. O Καινούργιος σας κωδικός είναι :" + newPass +
                " \n Σε περίπτωση που δεν ενεργοποιήσατε εσείς αυτή την λειτουργία αλάξτε τον κωδικό άμεσα. ";
        System.out.println(EmailTxt);
        email_controller.Send_Email(email, EmailTxt);


        AnchorPane.getScene().getWindow().hide();

    }

    public void UpdateTable() throws SQLException {
        Connection conn = DBConnector.getConnection();
        Statement stmt = conn.createStatement();

        String sql = "update member set password = '" + newPass + "' where email = '" + EmailTxt.getText() + "'";
        System.out.println(sql);

        ResultSet rs = stmt.executeQuery(sql);
        rs.close();
        stmt.close();
        conn.close();
        System.out.println("New pass is set");
    }

    //Generates a random password of length len
    public static String generateRandomPassword(int len) {
        // ASCII range – alphanumeric (0-9, a-z, A-Z)
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        // each iteration of the loop randomly chooses a character from the given
        // ASCII range and appends it to the `StringBuilder` instance

        for (int i = 0; i < len; i++) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        return sb.toString();
    }


}

