package trellol.trellol;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.EventListener;

public class ControlleurAjouterTache implements EventHandler<ActionEvent> {
    private Model modele;

    public ControlleurAjouterTache(Model m){
        this.modele=m;
    }

    @Override
    public void handle(ActionEvent actionEvent) {

    }
}
