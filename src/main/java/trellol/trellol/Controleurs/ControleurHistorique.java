package trellol.trellol.Controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import trellol.trellol.Modele.Modele;

public class ControleurHistorique implements EventHandler<ActionEvent> {

    private Modele modele;

    public ControleurHistorique(Modele modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {

    }
}