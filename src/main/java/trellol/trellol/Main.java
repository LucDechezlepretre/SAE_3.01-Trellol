package trellol.trellol;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Pane racine=new Pane();
        HBox header=new HBox(25);

        //BOUTONS DE VUE
        Button bVueBureau=new Button("Vue Bureau");
        Button bVueListe=new Button("Vue Liste");

        //TITRE
        Label titre=new Label("Trellol");
        Font font = Font.font("Arial", FontWeight.BOLD, 16);
        titre.setFont(font);

        //BOUTONS GANTT HISTORIQUE ARCHIVE
        Button bGantt=new Button("Gantt");
        Button bArchive=new Button("Archive");
        Button bHistorique=new Button("Historique");


        ///Ajout au header///
        header.getChildren().addAll(bVueBureau, bVueListe, titre, bGantt, bArchive, bHistorique);
        header.setAlignment(Pos.CENTER);

        ///Ajout à la racine///
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