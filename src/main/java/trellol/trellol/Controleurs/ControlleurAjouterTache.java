package trellol.trellol.Controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import trellol.trellol.Modele.Model;
import trellol.trellol.Vues.Affichage;

public class ControlleurAjouterTache implements EventHandler<ActionEvent> {
    private Model modele;

    public ControlleurAjouterTache(Model m){
        this.modele=m;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
    }
}
