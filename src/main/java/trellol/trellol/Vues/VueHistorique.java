package trellol.trellol.Vues;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import trellol.trellol.Modele.Modele;
import trellol.trellol.Modele.Sujet;

import java.io.Serializable;

/**
 * Classe VueBureau permettant la représentation de l'historique
 * d'un modèle, héritant de la classe Tab pour permettre
 * un affichage sous la forme d'un onglet
 * et implémentant Observateur pour pouvoir être enregistrer auprès d'un Sujet
 */
public class VueHistorique extends Tab implements Observateur, Serializable {
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

    public VueHistorique(String nom, Sujet s) {
        super(nom);
        this.model = (Modele) s;
        StackPane conteneur = new StackPane(this.affichageHistorique());
        this.setContent(conteneur);
    }

    /**
     *  Redéfinition de la méthode reçu de l'interface Observateur
     *  qui met à jour la vue grâce aux données du modèle
     * @param s sujet sur lequel se base la vue
     */
    @Override
    public void actualiser(Sujet s) {
        this.model =(Modele) s;
        StackPane content = (StackPane) this.getContent();
        content.getChildren().clear();
        content.getChildren().add(this.affichageHistorique());
    }

    /**
     * Méthode créant une visualisation de l'historique
     * sous forme de VBox contenant des Label
     * @return Vbox représentant l'historique
     */
    private VBox affichageHistorique() {
        VBox vb = new VBox(5);
        for (String action : model.getHistorique().getActions()) {
            Label label = new Label(action);
            vb.getChildren().add(0,label);
        }
        return vb;

    }
}
