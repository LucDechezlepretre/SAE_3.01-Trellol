package trellol.trellol;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        VBox racine=new VBox(10);
        racine.setPadding(new Insets(0, 10, 0, 10));
        HBox header=new HBox(25);

        //BOUTONS DE VUE
        Button bVueBureau=new Button("Vue Bureau");
        Button bVueListe=new Button("Vue Liste");

        //TITRE
        Label titre=new Label("TRELLOL");
        Font font = Font.font("Arial", FontWeight.BOLD, 35);
        titre.setFont(font);
        titre.setTextFill(Color.WHITE);

        //BOUTONS GANTT HISTORIQUE ARCHIVE
        Button bGantt=new Button("Gantt");
        Button bArchive=new Button("Archive");
        Button bHistorique=new Button("Historique");

        ///Ajout au header///
        header.setAlignment(Pos.CENTER);
        header.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));


        header.setPadding(new Insets(0,30,0,30));
        header.getChildren().addAll(bVueBureau, bVueListe, titre, bGantt, bArchive, bHistorique);

        ///CREATION DE LA BOX A GAUCHE
        VBox gauche=new VBox(5);

        Button bModif=new Button("Modifier");
        Button bAjoutTache=new Button("Ajouter tache");


        ///Ajout à gauche
        gauche.setAlignment(Pos.BOTTOM_LEFT);
        gauche.getChildren().addAll(bModif, bAjoutTache);

        ///Ajout à la racine///
        racine.getChildren().addAll(header, gauche);

        Scene scene = new Scene(racine);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static Model creationModel(){
        Model model = new Model();
        Tache racine = new Tache("racine", "13/12/2023", 8, "Ceci est la racine", 0);
        model.ajouterTache(racine);
        Tache racine2 = new Tache("racine2", "13/12/2023", 8, "Ceci est la racine", 0);
        model.ajouterTache(racine2);
        Tache tache = new Tache("tache", "13/12/2023", 8, "Ceci est la racine", 0);
        model.ajouterTache(tache);
        return model;
    }
}