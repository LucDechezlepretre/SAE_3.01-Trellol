package trellol.trellol.Vues;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import trellol.trellol.Modele.Model;
import trellol.trellol.Modele.Sujet;
import trellol.trellol.Tache;

import java.util.ArrayList;

public class VueBureau extends StackPane implements Observateur{
    private Model m;

    @Override
    public void actualiser(Sujet s) {
        this.m = (Model) s;
        Tache racine = m.getRacine();
        GridPane gp = createRecursiveGridPane(racine);
        this.getChildren().remove(0);
        this.getChildren().add(gp);
    }

    private GridPane createRecursiveGridPane(Tache tache) {

        GridPane gp = new GridPane();

        m.getEnfant(m.getRacine());
        gp.add(new Label(tache.getNom()),1,1);

        gp.setHgap(5);
        gp.setVgap(5);
        gp.setPadding(new Insets(10));

        gp.setStyle("-fx-border-color: black;"); // Ajout d'une bordure pour mieux visualiser
        Boolean rang2 = m.getEnfant(m.getRacine()).contains(tache);
        Boolean racine = tache == m.getRacine();
        int nbColonne = 0;
        if (m.getEnfant(tache).size() > 0) {
            int colonne = 1;
            if (racine) {
                colonne++;
            }
            int ligne = 2;
            // Appel récursif pour le VBox interne
            for (Tache t : m.getEnfant(tache)) {
                if (!racine || (racine && (nbColonne <= m.getNumColonneAffiche()+5) && nbColonne >= m.getNumColonneAffiche())) {
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
            if (m.getNumColonneAffiche() > 0) {
                gp.add(new Button("<-"), 1, gp.getRowCount());
            }
            if (nbColonne > m.getNumColonneAffiche() + 5) {
                gp.add(new Button("->"), gp.getColumnCount(), gp.getRowCount() - 1);
            }
        }
        if (rang2) {
            gp.add(new Button("Ajouter Tache"),1,gp.getRowCount());
        }
        return gp;
    }
}
