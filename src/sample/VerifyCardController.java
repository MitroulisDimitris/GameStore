package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import sample.model.DBConnector;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerifyCardController implements Initializable {

@FXML
AnchorPane anchorPane;
@FXML
TextField  cNumTxt, CNameTxt, cvvTxt,expDate;

@FXML
ImageView credidCardImage, SecureImage;
@FXML
Button OKBtn,CancelBtn;
@FXML
private Label CnumWarning,DateWarning,CnameWarning,CVVwarning;

@FXML
private Button SettingsBtn;

boolean isCnumOk, isCvvOK,isexpDateOK,iscNameOK;
String expdateStr,cnumber,cname,cvv;
String mid = LoginController.getrId();
public static boolean ConfirmedCard = false;

    public static boolean isConfirmedCard() {
        return ConfirmedCard;
    }

    public void setConfirmedCard(boolean confirmedCard) {
        ConfirmedCard = confirmedCard;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CnumWarning.setVisible(false);
        CnameWarning.setVisible(false);
        CVVwarning.setVisible(false);
        DateWarning.setVisible(false);
    }



    public void Cancel(){
        ConfirmedCard = false;
        anchorPane.getScene().getWindow().hide();

    }

    public void Ok_OnAction() throws SQLException {
        CnumWarning.setVisible(false);
        CnameWarning.setVisible(false);
        CVVwarning.setVisible(false);
        DateWarning.setVisible(false);

        expdateStr = String.valueOf(expDate.getText());
         cnumber = cNumTxt.getText();
         cname = CNameTxt.getText();
         cvv = cvvTxt.getText();

        if (!cnumber.equals("")) {
            if (!expdateStr.equals("")) {
                if (!cname.equals("")) {
                    if (!cvv.equals("")) {
                        InsertToDataBase();
                        ConfirmedCard = true;
                        anchorPane.getScene().getWindow().hide();

                    } else {
                        CVVwarning.setVisible(true);
                    }
                } else {
                    CnameWarning.setVisible(true);
                }
            } else {
                DateWarning.setVisible(true);
            }

        } else {
            CnumWarning.setVisible(true);
        }

    }

    private void InsertToDataBase() throws SQLException {

        String value1 = cNumTxt.getText();
        String value2 = expDate.getText();
        String value3 = CNameTxt.getText();
        String value4 = cvvTxt.getText();

        Connection conn = DBConnector.getConnection();
        Statement stmt = conn.createStatement();

        String insert ="update member set cname='" + value3 + "' ,credidnum=" + value1 + " ,expdate=TO_DATE('" + value2 +"', 'MM/YY'), cvv=" + value4 +
                " where mid = "+ mid;

        ResultSet rs = stmt.executeQuery(insert);

        rs.close();
        stmt.close();
        conn.close();


    }

    public void CheckCNum() {
        if (!isStringCardNumber(cNumTxt.getText())) {
            cNumTxt.setStyle("-fx-text-inner-color: red;");
            isCnumOk=false;
            Disablebuttons();

        } else {
            cNumTxt.setStyle("-fx-text-inner-color: black;");
            isCnumOk=true;
            EnableButtons();

        }

    }

    public void CheckName() {
        if (!isAlpha(CNameTxt.getText())) {
            CNameTxt.setStyle("-fx-text-inner-color: red;");
            Disablebuttons();
            iscNameOK=false;
        } else {
            CNameTxt.setStyle("-fx-text-inner-color: black;");
            EnableButtons();
            iscNameOK=true;
        }

    }

    public void CheckCVV() {
        if (!isStringCVVNumber(cvvTxt.getText())) {
            cvvTxt.setStyle("-fx-text-inner-color: red;");
            isCvvOK =false;
            Disablebuttons();

        } else {
            cvvTxt.setStyle("-fx-text-inner-color: black;");
            isCvvOK =true;
            EnableButtons();

        }

    }

    public void CheckExpDate() {
        if (!isStringDate(expDate.getText())) {
            expDate.setStyle("-fx-text-inner-color: red;");
            isexpDateOK=false;
            Disablebuttons();

        } else {
            expDate.setStyle("-fx-text-inner-color: black;");
            isexpDateOK=true;
            EnableButtons();

        }

    }

    public static boolean isStringCardNumber(String str) {
        String regularExpression = "[0-9]{16}";
        Pattern pattern = Pattern.compile(regularExpression);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static boolean isStringCVVNumber(String str) {
        //String regularExpression = "[0-9]{3}";
        //Pattern pattern = Pattern.compile(regularExpression);
        //Matcher matcher = pattern.matcher(str);
        return str.matches("[0-9]{3}");
    }

    public boolean isAlpha(String name) {
        return name.matches("[a-zA-Zα-ωΑ-Ω ]{0,20}$");
    }

    public static boolean isStringDate(String str) {
        return str.matches("^([0]{1}[0-9]{1}|[1]{1}[0-2]{1})[/]{1}[2-3]{1}[0-9]{1}$");
    }

    public boolean isAllOk(){

        return( isCnumOk && isCvvOK && isexpDateOK && iscNameOK );


    }
    public void Disablebuttons() {
        OKBtn.setDisable(true);


    }

    public void EnableButtons() {

        if(isAllOk()){
            OKBtn.setDisable(false);
        }




    }


}
