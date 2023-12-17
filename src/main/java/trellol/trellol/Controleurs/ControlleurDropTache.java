package trellol.trellol.Controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import trellol.trellol.Modele.Model;

public class ControlleurDropTache implements EventHandler<ActionEvent> {
    private Model modele;

    public ControlleurDropTache(Model m){
        this.modele=m;
    }

    @Override
    public void handle(ActionEvent actionEvent) {

    }
}
