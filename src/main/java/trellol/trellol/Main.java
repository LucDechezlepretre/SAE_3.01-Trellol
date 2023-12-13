package trellol.trellol;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Pane racine=new Pane();
        HBox header=new HBox(25);

        Button bVueBureau=new Button("Vue Bureau");
        Button bVueListe=new Button("Vue Liste");
        Text titre=new Text("Trellol");

        header.getChildren().addAll(bVueBureau, bVueListe, titre);

        racine.getChildren().addAll(header);

        Scene scene = new Scene(racine, 960, 540);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}