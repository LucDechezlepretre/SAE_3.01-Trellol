package trellol.trellol.Vues;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
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
        Modele m = new Modele();

        BorderPane racine = new BorderPane();
        racine.setPadding(new Insets(10));

        //Barre d'outil
        MenuBar menuBar = new MenuBar();
        Menu menuFichier = new Menu("Fichier");
        MenuItem ouvrir = new MenuItem("Ouvrir un fichier...");
        ouvrir.setOnAction(e -> {
            // Créer une boîte de dialogue de choix de fichier
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choisir un fichier");

            // Définir l'extension de fichier obligatoire
            ExtensionFilter extFilter = new ExtensionFilter("Fichiers trellol (*.trellol)", "*.trellol");
            fileChooser.getExtensionFilters().add(extFilter);

            // Afficher la boîte de dialogue et attendre que l'utilisateur choisisse un fichier
            java.io.File selectedFile = fileChooser.showOpenDialog(stage);

            // Si un fichier est choisi, afficher le chemin du fichier
            if (selectedFile != null) {
                System.out.println("Fichier choisi : " + selectedFile.getAbsolutePath());
                Modele de = Modele.charger(selectedFile.getAbsolutePath());
            }
        });
        MenuItem enregistrer = new MenuItem("Enregistrer sous...");
        enregistrer.setOnAction(e -> {
            // Créer une boîte de dialogue de choix de fichier pour enregistrer
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer le fichier");

            // Définir l'extension de fichier obligatoire
            ExtensionFilter extFilter = new ExtensionFilter("Fichiers trellol (*.trellol)", "*.trellol");
            fileChooser.getExtensionFilters().add(extFilter);

            // Afficher la boîte de dialogue et attendre que l'utilisateur choisisse l'emplacement et le nom du fichier
            java.io.File selectedFile = fileChooser.showSaveDialog(stage);

            // Si un fichier est choisi, afficher le chemin du fichier
            if (selectedFile != null) {
                System.out.println("Fichier enregistré à : " + selectedFile.getAbsolutePath());
                m.sauvegarder(selectedFile.getAbsolutePath());
            }
        });
        menuFichier.getItems().setAll(ouvrir, enregistrer);
        menuBar.getMenus().add(menuFichier);

        racine.setTop(menuBar);

        //Bouton ajouter tache gauche
        StackPane conteneurBouton = new StackPane();
        VBox conteneur = new VBox(20);
        Button ajouterTache = new Button("Ajouter Tache");
        ajouterTache.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                FenetreTache.afficherFormulaireTache(m, null, false);
            }
        });

        VueSelecteurGantt vueSelecteurGantt = new VueSelecteurGantt(m, "Affichage Gantt");
        conteneur.getChildren().addAll(vueSelecteurGantt,ajouterTache);
        conteneur.setAlignment(Pos.CENTER);
        racine.setLeft(conteneur);

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
        m.enregistrerObservateur(vueSelecteurGantt);
        //m.notifierObservateurs();

        tabPane.getTabs().addAll(vueListe, vueBureau, vueArchive, vueHistorique, vueGantt);
        //On empêche la fermeture des onglets
        tabPane.tabClosingPolicyProperty().setValue(TabPane.TabClosingPolicy.UNAVAILABLE);
        racine.setCenter(tabPane);

        Scene scene = new Scene(racine, 600, 400);

        //CSS
        //scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        //style scene
        scene.getRoot().getStyleClass().add("scene");

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