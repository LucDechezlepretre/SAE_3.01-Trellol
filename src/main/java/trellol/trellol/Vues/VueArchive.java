package trellol.trellol.Vues;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import trellol.trellol.Controleurs.ControleurDesarchivage;
import trellol.trellol.Controleurs.ControleurSuppression;
import trellol.trellol.Modele.Modele;
import trellol.trellol.Modele.Sujet;
import trellol.trellol.Tache;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Classe VueArchive permettant la représentation des tâches archivées d'un modèle
 * héritant de la classe Tab pour permettre un affichage sous la forme d'un onglet
 * et implémentant Observateur pour pouvoir être enregistrer auprès d'un Sujet
 */
public class VueArchive extends Tab implements Observateur{
    /**
     * Attribut model, représentant le modèle sur lequel se basera la vue pour se
     * construire
     */
    private Modele model;

    /**
     * Constructeur de la vue
     * @param nom nom de l'onglet
     * @param s modèle sur lequel se basera la vue
     */
    public VueArchive(String nom, Sujet s) {
        super(nom);
        this.model = (Modele) s;
        StackPane conteneur = new StackPane(this.affichageArchive());
        this.setContent(conteneur);


    }

    /**
     * Redéfinition de la méthode reçu de l'interface Observateur
     * @param s sujet qui servira à la modélisation des données
     */
    @Override
    public void actualiser(Sujet s) {
        this.model =(Modele) s;
        StackPane content = (StackPane) this.getContent();
        content.getChildren().clear();

        VBox affichage=this.affichageArchive();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(affichage);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        content.getChildren().addAll(affichage, scrollPane);
    }

    /**
     * Méthode contruisant une VBox qui affiche les tâches archivées
     * @return la VBox représentant les tâches archivées
     */
    private VBox affichageArchive() {
        VBox vb = new VBox(10);
        vb.getStyleClass().add("boiteTexte");

        vb.setPadding(new Insets(20,0,0,10));

        for (Tache t : model.getEnsTache()) {
            if(t.getEtat().equals(Tache.ETAT_ARCHIVE)) {
                HBox ligneArchive = new HBox(10);
                ligneArchive.setPadding(new Insets(0, 0, 0,10));
                Label label = new Label(t.getNom());

                //Creation des boutons
                Button bDesarchiv=new Button("Désarchiver");
                Button bSup=new Button("Supprimer");
                //Controlleurs
                ControleurSuppression cSup=new ControleurSuppression(model, t);
                ControleurDesarchivage cDesarchiv=new ControleurDesarchivage(model, t);

                //Association des controlleurs
                bSup.setOnAction(cSup);
                bDesarchiv.setOnAction(cDesarchiv);

                ligneArchive.getChildren().addAll(label, bDesarchiv, bSup);

                //Affichage des sous taches de la tache archivée (s'il y en a)
                if(model.getEnfant(t).size()>0){
                    VBox sousTaches=new VBox(4);
                    sousTaches=afficherSousTaches(sousTaches, t);

                    vb.getChildren().add(0, sousTaches);
                }
                vb.getChildren().add(0, ligneArchive);
            }
        }
        return vb;
    }

    /**
     * Methode recursive retournant une VBox contenant les soustaches de la tache tache
     * @param bloc Vbox que l'on va remplir
     * @param tache tache dont on veut les soustaches
     * @return le bloc de sous taches
     */
    private VBox afficherSousTaches(VBox bloc, Tache tache){
        ArrayList<Tache> enfants=(ArrayList<Tache>) model.getEnfant(tache);
        bloc.setPadding(new Insets(0,0,0,  30));
        bloc.setStyle("-fx-border-color: black; -fx-border-width: 0 0 0 1;");
        for(Tache t : enfants){
            HBox ligneSousTache=new HBox(12);
            Label nomSousTache=new Label("- "+t.getNom());

            nomSousTache.getStyleClass().add("sousTache");

            Button bSup=new Button("Supprimer");
            ControleurSuppression cSup=new ControleurSuppression(model, t);
            bSup.setOnAction(cSup);

            ligneSousTache.getChildren().addAll(nomSousTache, bSup);

            bloc.getChildren().add(ligneSousTache);
            //Affichage des sous tache de la sous tache t
            if(model.getEnfant(t).size()>0){
                VBox bloc2=new VBox();
                bloc2=this.afficherSousTaches(bloc2, t);
                bloc.getChildren().add(bloc2);
            }
        }
        return bloc;
    }
}
