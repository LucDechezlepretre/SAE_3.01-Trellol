package trellol.trellol.Vues;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import trellol.trellol.Controleurs.ControleurAjouterTache;
import trellol.trellol.Controleurs.ControleurModifierTache;
import trellol.trellol.Exceptions.AjoutTacheException;
import trellol.trellol.Modele.Modele;
import trellol.trellol.Tache;

import java.util.List;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class Affichage extends Application {
    public static final DataFormat customFormatListe = new DataFormat("application/x-java-serialized-object");
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
                Affichage.afficherFormulaireTache(m, null, false);
            }
        });
        racine.setLeft(conteneurBouton);

        //Vue avec des onglets
        TabPane tabPane = new TabPane();
        VueListe vueListe = new VueListe("Liste",m);
        VueBureau vueBureau = new VueBureau("Bureau", m);
        m.enregistrerObservateur(vueListe);
        m.enregistrerObservateur(vueBureau);
        m.notifierObservateurs();
        tabPane.getTabs().addAll(vueListe, vueBureau);
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
     * Methode de creation de la sous fenetre formulaire d'ajout de tache
     * @param m, le model de l'application
     */
    public static void afficherFormulaireTache(Modele m, String nomParent, boolean modif){
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
        if (modif) {
            fieldNom.setText(nomParent);
        }
        ligneNom.getChildren().addAll(tNom, fieldNom);

        ///date
        HBox ligneDate=new HBox(5);
        Text tDate=new Text("Date début : ");
        DatePicker fieldDate = new DatePicker();
        ligneDate.getChildren().addAll(tDate, fieldDate);
        if (modif) {
            fieldDate.setValue(m.findTacheByName(nomParent).getDateDebut().toInstant()
                    .atZone(java.time.ZoneId.systemDefault()).toLocalDate());
        }

        ///duree
        HBox ligneDuree=new HBox(5);
        Text tDuree=new Text("Durée : ");
        TextField fieldDuree=Affichage.createNumericField();
        if (modif) {
            fieldDuree.setText(String.valueOf(m.findTacheByName(nomParent).getDuree()));
        }

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
        if (modif) {
            fieldImportance.getSelectionModel().select(m.findTacheByName(nomParent).getImportance());
        }

        ///description
        Text tDescription=new Text("Description : ");
        TextArea fieldDescription=new TextArea();
        if (modif) {
            fieldDescription.setText(m.findTacheByName(nomParent).getDescription());
        }

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
        if (modif) {
            fieldParent.getSelectionModel().select(m.findTacheByName(nomParent).getParent().getNom());
        }

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


        if (modif) {
            ControleurModifierTache controleurModifierTache = new ControleurModifierTache(m, m.findTacheByName(nomParent), fenetreNomColonne, fieldNom, fieldDate, fieldDuree, fieldDescription, fieldImportance, fieldAnter, fieldParent, erreur);
            valider.setOnAction(controleurModifierTache);
        }else {
            //association du controleur d'ajout
            ControleurAjouterTache cAjouterTache=new ControleurAjouterTache(m, fenetreNomColonne, fieldNom, fieldDate, fieldDuree, fieldDescription, fieldImportance, fieldAnter, fieldParent, erreur);
            valider.setOnAction(cAjouterTache);
        }

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
        Scene scene = new Scene(form, 700, 500);
        fenetreNomColonne.setScene(scene);


        // Afficher la nouvelle fenêtre
        fenetreNomColonne.show();
    }

    public static Modele creationModel(){

        Modele model = new Modele();
        Tache racine = new Tache("racine", "13/12/2023", 8, "Ceci est la racine", 0);
        try {
            model.ajouterTache(racine);
            Tache racine2 = new Tache("racine2", "13/12/2023", 7, "Ceci est la racine 2 ", 0);
            racine2.setParent(racine);
            model.ajouterTache(racine2);
            Tache luc = new Tache("tacheLuc", "13/12/2023", 2, "Ceci n'est pas la racine", 0);
            luc.setParent(racine);
            model.ajouterTache(luc);
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