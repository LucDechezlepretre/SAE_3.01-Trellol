package trellol.trellol.Vues;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import trellol.trellol.Modele.Modele;
import trellol.trellol.Modele.Sujet;
import trellol.trellol.Tache;

public class VueArchive extends Tab implements Observateur{
    private static Modele model;

    public VueArchive(String nom, Sujet s) {
        super(nom);
        VueArchive.model = (Modele) s;
        StackPane conteneur = new StackPane(this.affichageArchive());
        this.setContent(conteneur);
    }
    @Override
    public void actualiser(Sujet s) {
        VueArchive.model =(Modele) s;
        StackPane content = (StackPane) this.getContent();
        content.getChildren().clear();
        content.getChildren().add(this.affichageArchive());
    }

    private VBox affichageArchive() {
        VBox vb = new VBox(10);
        vb.setPadding(new Insets(20,0,0,10));

        for (Tache t : model.getEnsTache()) {
            if(t.getEtat()==Tache.ETAT_ARCHIVE) {
                HBox ligneArchive = new HBox(10);
                ligneArchive.setPadding(new Insets(0, 0, 10,0));
                Label label = new Label(t.getNom());

                //Creation des boutons
                Button bDesarchiv=new Button("Désarchiver");
                Button bSup=new Button("Supprimer");

                //Controlleurs
                ////////A COMPLETER/////////

                //Association des controlleurs
                /////A COMPLETER

                ligneArchive.getChildren().addAll(label, bDesarchiv, bSup);
                vb.getChildren().add(0, ligneArchive);
            }
        }
        return vb;

    }
}
