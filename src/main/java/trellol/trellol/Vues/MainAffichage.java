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
import trellol.trellol.Controleurs.ControleurOuvertureFichier;
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


        //Barre d'outil
        MenuBar menuBar = new MenuBar();
        Menu menuFichier = new Menu("Fichier");
        MenuItem ouvrir = new MenuItem("Ouvrir un fichier...");

        ControleurOuvertureFichier controleurOuvertureFichier = new ControleurOuvertureFichier(m, stage);

        ouvrir.setOnAction(controleurOuvertureFichier);
        MenuItem enregistrer = new MenuItem("Enregistrer sous...");
        enregistrer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
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
            }
        });
        menuFichier.getItems().setAll(ouvrir, enregistrer);
        menuBar.getMenus().add(menuFichier);

        racine.setTop(menuBar);

        //Bouton ajouter tache gauche
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
        vueListe.getStyleClass().add("tab");

        VueBureau vueBureau = new VueBureau("Bureau", m);
        vueBureau.getStyleClass().add("tab");

        VueHistorique vueHistorique = new VueHistorique("Historique", m);
        vueHistorique.getStyleClass().add("tab");

        VueArchive vueArchive = new VueArchive("Archive", m);
        vueArchive.getStyleClass().add("tab");

        VueGantt vueGantt = new VueGantt("Gantt",m);
        vueGantt.getStyleClass().add("tab");


        //Enregistrement des observateurs auprès du modèle
        m.enregistrerObservateur(vueListe);
        m.enregistrerObservateur(vueHistorique);
        m.enregistrerObservateur(vueBureau);
        m.enregistrerObservateur(vueArchive);
        m.enregistrerObservateur(vueGantt);
        m.enregistrerObservateur(vueSelecteurGantt);
        m.notifierObservateurs();

        tabPane.getTabs().addAll(vueListe, vueBureau, vueArchive, vueHistorique, vueGantt);
        //On empêche la fermeture des onglets
        tabPane.tabClosingPolicyProperty().setValue(TabPane.TabClosingPolicy.UNAVAILABLE);
        racine.setCenter(tabPane);

        Scene scene = new Scene(racine, 600, 400);

        //CSS
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

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
        Tache racine = new Tache("racine", "13/12/2023", 0, "Ceci est la racine", Importance.FAIBLE);
        try {
            model.ajouterTache(racine);
            Tache O = new Tache("O", "14/12/2023", 2, "Ceci est la tache O", Importance.MOYENNE);
            O.setParent(racine);
            model.ajouterTache(O);

            Tache O1 = new Tache("O1", "14/12/2023", 2, "Ceci est la tache O1", Importance.MOYENNE);
            O1.setParent(O);
            model.ajouterTache(O1);

            Tache O2 = new Tache("O2", "14/12/2023", 2, "Ceci est la tache O2", Importance.MOYENNE);
            O2.setParent(O);
            model.ajouterTache(O2);

            Tache O3 = new Tache("O3", "14/12/2023", 2, "Ceci est la tache O3", Importance.MOYENNE);
            O3.setParent(O);
            model.ajouterTache(O3);

            Tache P = new Tache("P", "14/12/2023", 2, "Ceci est la tache P", Importance.FAIBLE);
            P.setParent(racine);
            model.ajouterTache(P);

            Tache P1 = new Tache("P1", "14/12/2023", 2, "Ceci est la tache P1", Importance.FAIBLE);
            P1.setParent(P);
            model.ajouterTache(P1);

            Tache P2 = new Tache("P2", "14/12/2023", 2, "Ceci est la tache P2", Importance.FAIBLE);
            P2.setParent(P);
            model.ajouterTache(P2);

            Tache P3 = new Tache("P3", "14/12/2023", 2, "Ceci est la tache P3", Importance.FAIBLE);
            P3.setParent(P);
            model.ajouterTache(P3);

            Tache Q = new Tache("Q","18/12/2023", 2, "Ceci est la tache Q", Importance.ELEVEE);
            Q.setParent(racine);
            model.ajouterTache(Q);

            Tache QD = new Tache("QD","18/12/2023", 2, "Ceci est la tache QD", Importance.ELEVEE);
            QD.setParent(Q);
            model.ajouterTache(QD);

            Tache QD1 = new Tache("QD1","18/12/2023", 2, "Ceci est la tache QD1", Importance.ELEVEE);
            QD1.setParent(QD);
            model.ajouterTache(QD1);

            Tache QD2 = new Tache("QD2","18/12/2023", 2, "Ceci est la tache QD2", Importance.ELEVEE);
            QD2.setParent(QD);
            model.ajouterTache(QD2);

            Tache R = new Tache("R","17/12/2023", 2, "Ceci est la tache R", 0);
            R.setParent(racine);
            model.ajouterTache(R);

            Tache R1 = new Tache("R1","17/12/2023", 2, "Ceci est la tache R1", 0);
            R1.setParent(R);
            model.ajouterTache(R1);

            Tache R2 = new Tache("R2","17/12/2023", 2, "Ceci est la tache R2", 0);
            R2.setParent(R);
            model.ajouterTache(R2);

            Tache R3 = new Tache("R3","17/12/2023", 2, "Ceci est la tache R3", 0);
            R3.setParent(R);
            model.ajouterTache(R3);

            Tache T = new Tache("T","17/12/2023", 2, "Ceci est la tache T", 0);
            T.setParent(racine);
            model.ajouterTache(T);

            Tache T1 = new Tache("T1","17/12/2023", 2, "Ceci est la tache T1", 0);
            T1.setParent(T);
            model.ajouterTache(T1);

            Tache T2 = new Tache("T2","17/12/2023", 2, "Ceci est la tache T2", 0);
            T2.setParent(T);
            model.ajouterTache(T2);

            Tache T3 = new Tache("T3","17/12/2023", 2, "Ceci est la tache T3", 0);
            T3.setParent(T);
            model.ajouterTache(T3);

            Tache X = new Tache("U","17/12/2023", 2, "Ceci est la tache U", 0);
            X.setParent(racine);
            model.ajouterTache(X);

            Tache Y = new Tache("Y","17/12/2023", 2, "Ceci est la tache Y", 0);
            Y.setParent(racine);
            model.ajouterTache(Y);

            Tache Z = new Tache("Z","17/12/2023", 2, "Ceci est la tache Z", 0);
            Z.setParent(racine);
            model.ajouterTache(Z);

            Tache D = new Tache("D","15/01/2023", 14, "Ceci est la tache D", 0);
            D.setParent(X);
            model.ajouterTache(D);

            Tache D1 = new Tache("D1","17/12/2023", 2, "Ceci est la tache D1", 0);
            D1.setParent(D);
            model.ajouterTache(D1);

            Tache C = new Tache("C","01/02/2023", 7, "Ceci est la tache C", 0);
            C.setParent(X);
            C.setAntecedant(D);
            model.ajouterTache(C);

            Tache A = new Tache("A","15/02/2023", 21, "Ceci est la tache A", 0);
            A.setParent(X);
            A.setAntecedant(C);
            model.ajouterTache(A);

            Tache A1 = new Tache("A1","18/02/2023", 7, "Ceci est la tache A1", 0);
            A1.setParent(A);
            model.ajouterTache(A1);

            Tache A2 = new Tache("A2","27/02/2023", 7, "Ceci est la tache A2", 0);
            A2.setParent(A);
            model.ajouterTache(A2);

            Tache B = new Tache("B","22/05/2023", 35, "Ceci est la tache B", 0);
            B.setParent(X);
            model.ajouterTache(B);

            Tache B1 = new Tache("B1","13/03/2023", 14, "Ceci est la tache B1", 0);
            B1.setParent(B);
            model.ajouterTache(B1);

            Tache B2 = new Tache("B2","12/03/2023", 7, "Ceci est la tache B2", 0);
            B2.setParent(B);
            B2.setAntecedant(A2);
            model.ajouterTache(B2);

            Tache B3 = new Tache("B3","05/03/2023", 21, "Ceci est la tache B3", 0);
            B3.setParent(B);
            model.ajouterTache(B3);

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