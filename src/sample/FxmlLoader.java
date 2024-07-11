//package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;

public class FxmlLoader {

    private Pane view;

    public Pane getPage(String filename) throws IOException {

        URL file = RentNowController.class.getResource(filename + ".fxml");
        if (filename == null) {
            System.out.println("fxml file cannot be found");
            return null;
        }
        view = FXMLLoader.load(file);

        return view;
    }


}
