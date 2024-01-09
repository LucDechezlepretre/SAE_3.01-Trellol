package trellol.trellol.Vues;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import trellol.trellol.Exceptions.AjoutTacheException;
import trellol.trellol.Importance;
import trellol.trellol.Modele.Modele;
import trellol.trellol.Tache;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

/**
 * Classe MainAffichage qui se charge de lancer l'application et d'une partie de la création IHM
 */
public class MainAffichage extends Application {
    /**
     * Attribut costumFormatListe utilisé pour le transfert des données lors du
     * Drag and Drop (donc ici des objet Tache)
     */
    public static final DataFormat customFormatListe = new DataFormat("application/x-java-serialized-object");
    /**
     * Attribut affichageFormTache qui aide à garantir l'ouverture d'une
     * seule fenetre lors d'un clique de l'utilisateur pour modifier une
     * tache, sans cela par récursivité les taches parentes de la
     * tache s'ouvrent aussi pour la modification
     */
    public static boolean affichageFormTache = false;

    /**
     * Redéfinition de la méthode start pour la construction de la scene
     * principale
     * @param stage objet Stage sur lequel construire la scene
     */
    @Override
    public void start(Stage stage){
        //CREATION DU MODELE
        Modele m = creationModel();

        BorderPane racine = new BorderPane();
        racine.setPadding(new Insets(10));


        //Bandeau nom appli
        StackPane conteneurNomAppli = new StackPane();
        conteneurNomAppli.setPadding(new Insets(5));
        conteneurNomAppli.setStyle("-fx-background-color: #ff0099;");
        Label nomAppli = new Label("Trellol");
        nomAppli.setFont(javafx.scene.text.Font.font(18));
        conteneurNomAppli.getChildren().add(nomAppli);
        racine.setTop(conteneurNomAppli);

        //Bouton ajouter tache gauche
        StackPane conteneurBouton = new StackPane();
        Button ajouterTache = new Button("Ajouter Tache");
        conteneurBouton.getChildren().add(ajouterTache);
        ajouterTache.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                FenetreTache.afficherFormulaireTache(m, null, false);
            }
        });
        racine.setLeft(conteneurBouton);

        //Vue avec des onglets
        TabPane tabPane = new TabPane();
        VueListe vueListe = new VueListe("Liste",m);
        VueBureau vueBureau = new VueBureau("Bureau", m);
        VueHistorique vueHistorique = new VueHistorique("Historique", m);
        VueArchive vueArchive = new VueArchive("Archive", m);
        VueGantt vueGantt = new VueGantt("Gantt",m);

        //Enregistrement des observateurs auprès du modèle
        m.enregistrerObservateur(vueListe);
        m.enregistrerObservateur(vueHistorique);
        m.enregistrerObservateur(vueBureau);
        m.enregistrerObservateur(vueArchive);
        m.enregistrerObservateur(vueGantt);
        m.notifierObservateurs();

        tabPane.getTabs().addAll(vueListe, vueBureau, vueArchive, vueHistorique, vueGantt);
        //On empêche la fermeture des onglets
        tabPane.tabClosingPolicyProperty().setValue(TabPane.TabClosingPolicy.UNAVAILABLE);
        racine.setCenter(tabPane);

        Scene scene = new Scene(racine, 600, 400);
        stage.setTitle("Trellol");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }

    /**
     * Méthode créant un modèle pré-construit pour gagner du temps lors
     * des phase de test du DnD
     * @return un objet Modele contenant plusieurs tâches
     */
    public static Modele creationModel(){

        Modele model = new Modele();
        Tache racine = new Tache("racine", "13/12/2023", 15, "Ceci est la racine", Importance.FAIBLE);
        try {
            model.ajouterTache(racine);
            Tache racine2 = new Tache("racine2", "14/12/2023", 2, "Ceci est la racine 2 ", Importance.MOYENNE);
            racine2.setParent(racine);
            model.ajouterTache(racine2);

            Tache luc = new Tache("tacheLuc", "14/12/2023", 2, "Ceci n'est pas la racine", Importance.FAIBLE);
            luc.setParent(racine);
            model.ajouterTache(luc);

            Tache t2 = new Tache("tache2","18/12/2023", 2, "Ceci n'est pas la racine", Importance.ELEVEE);
            t2.setParent(luc);
            t2.setAntecedant(racine2);
            model.ajouterTache(t2);

            Tache t3 = new Tache("tache3","17/12/2023", 2, "Ceci n'est pas la racine", 0);
            t3.setParent(luc);
            t3.setAntecedant(racine2);
            model.ajouterTache(t3);

            Tache t4 = new Tache("tache4","17/12/2023", 2, "Ceci n'est pas la racine", 0);
            t4.setParent(t3);
            t4.setAntecedant(racine2);
            model.ajouterTache(t4);

        }
        catch(AjoutTacheException e){
            e.getMessage();
        }
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