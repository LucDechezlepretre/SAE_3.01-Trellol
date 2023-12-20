package trellol.trellol.Vues;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import trellol.trellol.Modele.Modele;
import trellol.trellol.Modele.Sujet;
import trellol.trellol.Tache;

public class VueBureau extends Tab implements Observateur {
    private static Modele model;


    public VueBureau(String nom, Sujet s) {
        super(nom);
        VueBureau.model = (Modele) s;
        StackPane conteneur = new StackPane(this.createRecursiveGridPane(model.getRacine()));
        this.setContent(conteneur);
    }

    @Override
    public void actualiser(Sujet s) {
        VueBureau.model =(Modele) s;
        StackPane content = (StackPane) this.getContent();
        content.getChildren().clear();
        content.getChildren().add(this.createRecursiveGridPane(VueBureau.model.getRacine()));
    }

    private GridPane createRecursiveGridPane(Tache tache) {

        GridPane gp = new GridPane();
        Button modif = new Button("Modifier");
        modif.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Affichage.afficherFormulaireTache(VueBureau.model,tache.getNom(), true);
            }
        });

        this.model.getEnfant(this.model.getRacine());
        gp.add(new Label(tache.getNom()),1,1);
        gp.add(modif,2,1);

        gp.setHgap(5);
        gp.setVgap(5);
        gp.setPadding(new Insets(10));

        gp.setStyle("-fx-border-color: black;"); // Ajout d'une bordure pour mieux visualiser
        Boolean rang2 = this.model.getEnfant(this.model.getRacine()).contains(tache);
        Boolean racine = tache == this.model.getRacine();
        int nbColonne = 0;
        if (this.model.getEnfant(tache).size() > 0) {
            int colonne = 1;
            if (racine) {
                colonne++;
            }
            int ligne = 2;
            // Appel récursif pour le VBox interne
            for (Tache t : this.model.getEnfant(tache)) {
                if (!racine || (racine && (nbColonne <= this.model.getNumColonneAffiche()+5) && nbColonne >= this.model.getNumColonneAffiche())) {
                    gp.add(createRecursiveGridPane(t), colonne, ligne);
                    if (racine) {
                        colonne++;
                    } else {
                        ligne++;
                    }
                }
                nbColonne++;
            }
        }
        if (racine) {
            if (this.model.getNumColonneAffiche() > 0) {
                gp.add(new Button("<-"), 1, gp.getRowCount());
            }
            if (nbColonne > this.model.getNumColonneAffiche() + 5) {
                gp.add(new Button("->"), gp.getColumnCount(), gp.getRowCount() - 1);
            }
        }
        if (rang2) {
            Button bAjoutTache = new Button("Ajouter Tache");
            gp.add(bAjoutTache,1,gp.getRowCount());
            bAjoutTache.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    Affichage.afficherFormulaireTache(VueBureau.model,tache.getNom(), false);
                }
            });
        }
        return gp;
    }
}
