package trellol.trellol.Vues;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import trellol.trellol.Modele.Modele;
import trellol.trellol.Modele.Sujet;

public class VueHistorique extends Tab implements Observateur{
    private static Modele model;

    public VueHistorique(String nom, Sujet s) {
        super(nom);
        VueHistorique.model = (Modele) s;
        StackPane conteneur = new StackPane(this.affichageHistorique());
        this.setContent(conteneur);
    }
    @Override
    public void actualiser(Sujet s) {
        VueHistorique.model =(Modele) s;
        StackPane content = (StackPane) this.getContent();
        content.getChildren().clear();
        content.getChildren().add(this.affichageHistorique());
    }

    private VBox affichageHistorique() {
        VBox vb = new VBox(5);
        for (String action : model.getHistorique().getActions()) {
            Label label = new Label(action);
            vb.getChildren().add(0,label);
        }
        return vb;

    }
}