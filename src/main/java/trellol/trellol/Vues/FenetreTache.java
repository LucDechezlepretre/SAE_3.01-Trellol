package trellol.trellol.Vues;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import trellol.trellol.Controleurs.ControleurAjouterTache;
import trellol.trellol.Controleurs.ControleurModifierTache;
import trellol.trellol.Controleurs.ControleurArchivage;
import trellol.trellol.Controleurs.ControleurSuppression;
import trellol.trellol.Modele.Modele;
import trellol.trellol.Tache;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Classe FenetreAjoutTache permettant la création d'une fenetre pour l'ajout ou la modification d'une tache
 */
public class FenetreTache {


    /**
     * Methode de creation de la sous fenetre formulaire d'ajout de tache
     * @param m, le model de l'application
     */
    public static synchronized void afficherFormulaireTache(Modele m, String nomParent, boolean modif){
        if(modif && MainAffichage.affichageFormTache){
            return;
        }
        MainAffichage.affichageFormTache = true;
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
        else{
            fieldNom.setText("Tache "+Tache.NUMERO);
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
        else{ //par defaut date d'aujourd'hui
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.FRENCH);
            fieldDate.setValue(LocalDate.now());
        }

        ///duree
        HBox ligneDuree=new HBox(5);
        Text tDuree=new Text("Durée : ");
        TextField fieldDuree= MainAffichage.createNumericField();
        if (modif) {
            fieldDuree.setText(String.valueOf(m.findTacheByName(nomParent).getDuree()));
        }
        else{ //par defaut
            fieldDuree.setText("1");
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
        else{ //par defaut
            fieldImportance.setValue("faible");
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
            if(t.getEtat().equals(Tache.ETAT_INITIAL)){
                optionsTache.add(t.getNom());
            }
        }
        optionsTache.add(null);
        Text tParent=new Text("Tache parent : ");
        ComboBox<String> fieldParent=new ComboBox<>(optionsTache);
        fieldParent.getSelectionModel().select(nomParent);
        if (modif) {
            fieldParent.getSelectionModel().select(m.findTacheByName(nomParent).getParent().getNom());
        }
        else{ //par defaut
            fieldParent.setValue(m.getRacine().getNom());
        }

        ligneParent.getChildren().addAll(tParent, fieldParent);

        ///tache anterieur
        HBox ligneAnter=new HBox(5);
        Text tAnter=new Text("Tache anterieure : ");

        ComboBox<String> fieldAnter=new ComboBox<>(optionsTache);

        if(modif){
            if(m.findTacheByName(nomParent).getAntecedant()==null){
                fieldAnter.setValue(null);
            }
            else{
                fieldAnter.setValue(m.findTacheByName(nomParent).getAntecedant().getNom());
            }
        }

        ligneAnter.getChildren().addAll(tAnter, fieldAnter);

        ///boutons archiver et sup (UNIQUEMENT EN CAS DE MODIF)
        HBox ligneModif=new HBox(5);

        if(modif){
            Button bSup=new Button("Supprimer");
            Button bArchiv=new Button("Archiver");

            //Creation de leurs controlleurs
            ControleurArchivage cArchivage=new ControleurArchivage(m, fenetreNomColonne, m.findTacheByName(nomParent));
            ControleurSuppression cSup=new ControleurSuppression(m, fenetreNomColonne, m.findTacheByName(nomParent));

            //Association des controlleurs
            bArchiv.setOnAction(cArchivage);
            bSup.setOnAction(cSup);

            ligneModif.getChildren().addAll(bSup, bArchiv);
        }


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

        droite.getChildren().addAll(ligneAnter, ligneParent, ligneModif);


        VBox bas=new VBox(10);
        bas.getChildren().addAll(tDescription, fieldDescription, lastline);


        form.setLeft(gauche);
        form.setRight(droite);
        form.setBottom(bas);
        // Définir la scène de la nouvelle fenêtre
        Scene scene = new Scene(form, 700, 500);
        fenetreNomColonne.setScene(scene);
        fenetreNomColonne.setOnCloseRequest(windowEvent -> {
            MainAffichage.affichageFormTache = false;
        });


        // Afficher la nouvelle fenêtre
        fenetreNomColonne.show();
    }
}
