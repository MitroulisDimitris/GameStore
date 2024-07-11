package sample;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {

    public static void display(String title,String message ){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(400);
        window.setMinHeight(250);


        Label label1= new Label();
        label1.setText(message);
        Button closebutton = new Button("OK");
        closebutton.setOnAction(e -> window.close());

        VBox layout1 = new VBox();
        layout1.setSpacing(20d);
        layout1.getChildren().addAll(label1,closebutton);
        layout1.setAlignment(Pos.CENTER);

        Scene scene1 = new Scene(layout1);
        window.setScene(scene1);
        window.showAndWait();


    }



}