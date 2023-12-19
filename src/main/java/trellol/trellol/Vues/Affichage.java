package trellol.trellol.Vues;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import trellol.trellol.Controleurs.ControleurVuePrincipale;
import trellol.trellol.Controleurs.ControleurAjouterTache;
import trellol.trellol.Modele.Model;
import trellol.trellol.Tache;

import java.io.IOException;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class Affichage extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        //CREATION DU MODELE
        Model m = creationModel();

        //Creation des controleurs
        ControleurVuePrincipale controleurVuePrincipale = new ControleurVuePrincipale(m);

        //Affichage
        VBox racine=new VBox(10);
        racine.setPadding(new Insets(0, 10, 0, 10));
        HBox header=new HBox(25);

        //BOUTONS DE VUE
        Button bVueBureau=new Button("Vue Bureau");
        Button bVueListe=new Button("Vue Liste");
        bVueListe.addEventFilter(ActionEvent.ACTION, controleurVuePrincipale);
        bVueBureau.addEventFilter(ActionEvent.ACTION, controleurVuePrincipale);

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
        bAjoutTache.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Affichage.afficherFormulaireTache(m, null);
            }
        });


        ///Ajout à gauche
        gauche.setAlignment(Pos.BOTTOM_LEFT);
        gauche.getChildren().addAll(bModif, bAjoutTache);


        ///CREATION DE LA BOX D'AFFICHAGE DES TACHES
        VuePrincipale vuePrincipale = new VuePrincipale();
        vuePrincipale.setPrefSize(300, 200);
        m.enregistrerObservateur(vuePrincipale);

        ///Ajout à la racine///
        racine.getChildren().addAll(header, gauche, vuePrincipale);
        m.notifierObservateurs();
        Scene scene = new Scene(racine);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }


    /**
     * Methode de creation de la sous fenetre formulaire d'ajout de tache
     * @param m, le model de l'application
     */
    public static void afficherFormulaireTache(Model m, String nomParent){
        //AFFICHAGE GRAPHIQUE DE LA NOUVELLE FENETRE
        Stage fenetreNomColonne = new Stage();
        fenetreNomColonne.setTitle("Configurer Tache");

        // Création du formulaire de la nouvelle fenêtre
        BorderPane form=new BorderPane();
        form.setPadding(new Insets(0, 10, 0, 10));


        ///nom
        HBox ligneNom=new HBox(5);
        Text tNom=new Text("Nom : ");
        TextField fieldNom = new TextField("Tache");
        ligneNom.getChildren().addAll(tNom, fieldNom);

        ///date
        HBox ligneDate=new HBox(5);
        Text tDate=new Text("Date début : ");
        DatePicker fieldDate = new DatePicker();
        ligneDate.getChildren().addAll(tDate, fieldDate);

        ///duree
        HBox ligneDuree=new HBox(5);
        Text tDuree=new Text("Durée : ");
        TextField fieldDuree=Affichage.createNumericField();

        ligneDuree.getChildren().addAll(tDuree, fieldDuree);

        ///importance
        HBox ligneImportance=new HBox(5);
        ObservableList<String> optionsImp = FXCollections.observableArrayList(
                "faible",
                "moyenne",
                "élevée"
        );
        Text tImportance=new Text("Importance : ");
        ComboBox<String> fieldImportance=new ComboBox<>(optionsImp);
        ligneImportance.getChildren().addAll(tImportance, fieldImportance);

        ///description
        Text tDescription=new Text("Description : ");
        TextArea fieldDescription=new TextArea();

        ///tache parent
        HBox ligneParent=new HBox(5);

        ObservableList<String> optionsTache= FXCollections.observableArrayList();
        List<Tache> ensTaches=m.getEnsTache();
        for(Tache t : ensTaches){
            optionsTache.add(t.getNom());
        }
        optionsTache.add(null);
        Text tParent=new Text("Tache parent : ");
        ComboBox<String> fieldParent=new ComboBox<>(optionsTache);
        fieldParent.getSelectionModel().select(nomParent);

        ligneParent.getChildren().addAll(tParent, fieldParent);

        ///tache anterieur
        HBox ligneAnter=new HBox(5);
        Text tAnter=new Text("Tache anterieur : ");

        ComboBox<String> fieldAnter=new ComboBox<>(optionsTache);

        ligneAnter.getChildren().addAll(tAnter, fieldAnter);



        ///validation et potentiel message d'erreur (dernière ligne)
        HBox lastline=new HBox(5);
        Button valider = new Button("Valider");
        Label erreur=new Label("");
        lastline.getChildren().addAll(valider, erreur);

        //association du controleur d'ajout
        ControleurAjouterTache cAjouterTache=new ControleurAjouterTache(m, fenetreNomColonne, fieldNom, fieldDate, fieldDuree, fieldDescription, fieldImportance, fieldAnter, fieldParent, erreur);
        valider.setOnAction(cAjouterTache);

        VBox gauche=new VBox(5);
        gauche.getChildren().addAll(ligneNom, ligneDate, ligneDuree, ligneImportance);

        VBox droite=new VBox(5);
        droite.getChildren().addAll(ligneAnter, ligneParent);

        VBox bas=new VBox(10);
        bas.getChildren().addAll(tDescription, fieldDescription, lastline);


        form.setLeft(gauche);
        form.setRight(droite);
        form.setBottom(bas);
        // Définir la scène de la nouvelle fenêtre
        Scene scene = new Scene(form);
        fenetreNomColonne.setScene(scene);


        // Afficher la nouvelle fenêtre
        fenetreNomColonne.show();
    }

    public static Model creationModel(){
        Model model = new Model();
        Tache racine = new Tache("racine", "13/12/2023", 8, "Ceci est la racine", 0);
        model.ajouterTache(racine);
        Tache racine2 = new Tache("racine2", "13/12/2023", 7, "Ceci est la racine 2 ", 0);
        racine2.setParent(racine);
        model.ajouterTache(racine2);
        Tache luc = new Tache("tacheLuc", "13/12/2023", 2, "Ceci n'est pas la racine", 0);
        luc.setParent(racine);
        model.ajouterTache(luc);
        return model;
    }


    /**
     * Methode permettant de creer un field n'autorisant QUE les chiffres
     * @return le field créé
     */
    public static TextField createNumericField(){
        TextField numericField = new TextField();

        // Utilisation d'un TextFormatter avec un filtre
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (Pattern.matches("[0-9]*", newText)) {
                return change;
            } else {
                return null; // empêche le changement
            }
        };

        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        numericField.setTextFormatter(textFormatter);

        return numericField;
    }
}