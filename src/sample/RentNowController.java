import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import sample.model.DBConnector;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RentNowController implements Initializable {
    @FXML
    Button Rent_Btn, Cancel_Btn;
    @FXML
    javafx.scene.layout.AnchorPane AnchorPane;
    @FXML
    Label TitleTxt, FinalPriceTxt;
    @FXML
    TextField DaysTxt, cNumTxt, CNameTxt, cvvTxt,expDate;


    @FXML
    BorderPane CreditCardPane;
    @FXML
    ImageView credidCardImage, SecureImage;
    @FXML
    Label CnumWarning, CnameWarning, CVVwarning, DateWarning;

    String userId = sample.LoginController.rId;
    int gid = sample.GameProfileController.gameId;
    double price;
    int discount;
    int days;
    Double doublePrice;
    String title, email, fileName, calculation, finalPrice, Usertier, expdateStr, cnumber, cname, cvv, daysStr;

    Stage window = new Stage();

    sample.Email_Controller email_controller = new sample.Email_Controller();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyy HH:mm:ss");

    Image secure = new Image("sample/Icons/secure.png");
    Image card = new Image("sample/Icons/credit-card.png");

    String tier = sample.GameListController.gettier();
    boolean isDaysok,isCnumOk, isCvvOK,isexpDateOK,iscNameOK;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        window.initModality(Modality.APPLICATION_MODAL);

//        isDaysok=false;
//        isCnumOk=false;
//        isCvvOK =false;
//        isexpDateOK=false;
//        iscNameOK=false;

        CnumWarning.setVisible(false);
        CnameWarning.setVisible(false);
        CVVwarning.setVisible(false);
        DateWarning.setVisible(false);

        credidCardImage.setImage(card);
        SecureImage.setImage(secure);

        String userInfo = "select * from member where mid =" + userId;
        String gameInfo = "select * from games where gid = " + gid;
        Connection conn = null;
        try {
            conn = DBConnector.getConnection();

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(gameInfo);
            rs.next();

            title = rs.getString("title");
            price = rs.getInt("price");
            discount = rs.getInt("discounted");


            rs.close();
            stmt.close();

            stmt = conn.createStatement();
            rs = stmt.executeQuery(userInfo);
            rs.next();

            email = rs.getString("email");
            Usertier = rs.getString("tier");

            rs.close();
            stmt.close();
            conn.close();
            System.out.println("Connection Closed" + email);


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if (tier.startsWith("2")) {
            price = price*0.85;
            FinalPriceTxt.setText("0x" +String.valueOf(price)+"€");

            System.out.println(price);
            doublePrice = Double.valueOf(price);
        }
        else if(tier.startsWith("3")){
            price = price*0.75;
            FinalPriceTxt.setText("0x" +String.valueOf(price)+"€");
            System.out.println(price);



        }else {

            FinalPriceTxt.setText("0x" +String.valueOf(price)+"€");
            System.out.println(price);

        }


        TitleTxt.setText(title);

        //Sets credit card pane visible if user is not premium
        CreditCardPane.setVisible(Usertier.startsWith("1"));

    }

    @FXML
    private void Rent() throws Exception {
        expdateStr = expDate.getText();
        cnumber = cNumTxt.getText();
        cname = CNameTxt.getText();
        cvv = cvvTxt.getText();
        daysStr = DaysTxt.getText();



        CVVwarning.setVisible(false);
        CnameWarning.setVisible(false);
        DateWarning.setVisible(false);
        CnumWarning.setVisible(false);

        System.out.println(expDate.getText());
        if (Usertier.startsWith("1")) {
            System.out.println("Aplos user");
            if (DaysTxt.getText().equals("0") || DaysTxt.getText() ==null || (DaysTxt.getText().length()>0) ) {
                if (!cnumber.equals("")) {
                    if (!expdateStr.equals("")) {
                        if (!cname.equals("")) {
                            if (!cvv.equals("")) {



                                    SendEmail();

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
            } else {
                sample.AlertBox.display("Warning", "Παρακαλώ εισάγετε σωστό αριθμό ημερών");

            }
        } else if (Usertier.startsWith("2") || Usertier.startsWith("3")) {
            System.out.println("Premiun user");
            if (DaysTxt.getText().equals("0") || DaysTxt.getText() ==null || (DaysTxt.getText().length()>0)) {
                SendEmail();
            } else {
                sample.AlertBox.display("Warning", "Παρακαλώ εισάγετε σωστό αριθμό ημερών");
            }

        }
    }

    private void ExcecuteInsertRents() throws SQLException {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String currDate = dateFormat.format(now);

        String sql = "insert into rents(rentid,mid,gid,rentdate)" +
                "values(rents_increment.nextval , '"+userId+"','"+gid+"',to_date('"+java.sql.Date.valueOf(currDate)+"', 'YYYY/MM/DD')) ";

        Connection conn = DBConnector.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        rs.close();
        stmt.close();
        conn.close();
        System.out.println("Added rent");


    }

    public void SendEmail() throws Exception {

        ExcecuteInsertRents();


        String priceStr = String.valueOf(price);

        try {
            createPDF();
        } catch (IOException e) {
            e.printStackTrace();
        }

        email_controller.Send_emailWithPdf(email, "Ευχαριστούμε για την αγορα!Το παιχνίδι θα σας έρθει μεσα σε 5 εργάσιμες μέρες", fileName);
        File file = new File(fileName);
        file.delete();

        sample.AlertBox.display("", "Ευχαριστούμε για την προτίμηση!\n" +
                "Η απόδειξη σας έχει αποσταλθεί στο email σας!\n" +
                "Παρακαλώ παραλάβετε το παιχνίδι απο τον υπεύθυνο του καταστήματος");

        AnchorPane.getScene().getWindow().hide();

    }

    public void Cancel() {

        AnchorPane.getScene().getWindow().hide();

    }

    public void CalculateFPrice() throws NullPointerException {
        CheckdateTextField();

        int number = Integer.parseInt(DaysTxt.getText());
        if (DaysTxt.getText() != null) {
            days = Integer.parseInt(DaysTxt.getText());
            calculation = days + " x " + price + " =";
            finalPrice = String.valueOf(calculation + (days * price));
            FinalPriceTxt.setText(finalPrice + "€");

        } else if (number == 0) {
            days = Integer.parseInt(DaysTxt.getText());
            String calculation = days + " x " + price + " =";
            FinalPriceTxt.setText(finalPrice + "€");
        }
    }

    public void createPDF() throws IOException {

        //Δεν επιτρέπονται τα ελληνικά στο email!
        LocalDateTime now = LocalDateTime.now();
        fileName = "Receipt.pdf";
        PDDocument document = new PDDocument();
        //add new page to the pdf document
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream content = new PDPageContentStream(document, page);

        content.beginText();
        content.setFont(PDType1Font.TIMES_BOLD, 26);
        content.newLineAtOffset(150, 700);
        content.showText("Game Store 'The best games' ");
        content.endText();

        content.beginText();
        content.setFont(PDType1Font.TIMES_ROMAN, 14);
        content.newLineAtOffset(150, 680);
        content.showText("ADRESS: Papandrou 23,Thessaloniki");
        content.endText();

        content.beginText();
        content.setFont(PDType1Font.TIMES_ROMAN, 14);
        content.newLineAtOffset(150, 660);
        content.showText("Phone: +306987532145 ");
        content.endText();


        content.beginText();
        content.setFont(PDType1Font.TIMES_ROMAN, 14);
        content.newLineAtOffset(150, 625);
        int randomInt = (int) Math.floor(Math.random() * (1000 - 100 + 1) + 1);
        content.showText("No. " + randomInt);
        content.endText();

        content.beginText();
        content.setFont(PDType1Font.TIMES_ROMAN, 14);
        content.newLineAtOffset(150, 610);
        content.showText("Date: " + dtf.format(now));
        content.endText();


        content.beginText();
        content.setFont(PDType1Font.TIMES_ROMAN, 14);
        content.newLineAtOffset(150, 590);
        content.showText("-----------------------------------------------------------");
        content.endText();

        content.beginText();
        content.setFont(PDType1Font.TIMES_ROMAN, 14);
        content.newLineAtOffset(150, 580);
        content.showText("Items: " + title + ": " + (days * price) + "€");
        content.endText();


        content.beginText();
        content.setFont(PDType1Font.TIMES_ROMAN, 14);
        content.newLineAtOffset(150, 560);
        content.showText("-----------------------------------------------------------");
        content.endText();

        content.beginText();
        content.setFont(PDType1Font.TIMES_BOLD, 14);
        content.newLineAtOffset(150, 550);
        content.showText("Total Price: " + (days * price) + "€");
        content.endText();

        content.beginText();
        content.setFont(PDType1Font.TIMES_BOLD, 14);
        content.newLineAtOffset(150, 530);
        content.showText("Tax(Included 25%): " + ((days * price) * 0.25) + "€");
        content.endText();

        content.close();
        document.save(fileName);
        document.close();

    }

    public void CheckdateTextField(){
        if (!isStringDaysNumber(DaysTxt.getText())) {
            DaysTxt.setStyle("-fx-text-inner-color: red;");
            isDaysok = false;
            Disablebuttons();

        } else {
            DaysTxt.setStyle("-fx-text-inner-color: black;");
            isDaysok=true;
            EnableButtons();

        }


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

    public boolean isAllOk(){

        return(isDaysok && isCnumOk && isCvvOK && isexpDateOK && iscNameOK );


    }

    public static boolean isStringCardNumber(String str) {
        String regularExpression = "[0-9]{16}";
        Pattern pattern = Pattern.compile(regularExpression);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static boolean isStringCVVNumber(String str) {
        return str.matches("[0-9]{3}");
    }

    public static boolean isStringDaysNumber(String str) {
        return str.matches("[0-9]{0,5}");
    }

    public static boolean isStringDate(String str) {
        return str.matches("^([0]{1}[0-9]{1}|[1]{1}[0-2]{1})[/]{1}[2-3]{1}[0-9]{1}$");
    }

    public boolean isAlpha(String name) {
        return name.matches("[a-zA-Zα-ωΑ-Ω ]{0,20}$");
    }

    public void Disablebuttons() {
        Rent_Btn.setDisable(true);


    }

    public void EnableButtons() {

        System.out.println(isAllOk());
        System.out.println("days: "+isDaysok +" cnum: "+isCnumOk +" cvv: "+ isCvvOK +" expdate: "+isexpDateOK +" cname: "+iscNameOK );
        if(isAllOk()) {
            System.out.println("enabling");

            Rent_Btn.setDisable(false);
        }

    }
}
