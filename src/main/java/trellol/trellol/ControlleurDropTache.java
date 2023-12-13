package trellol.trellol;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ControlleurDropTache implements EventHandler<ActionEvent> {
    private Model modele;

    public ControlleurDropTache(Model m){
        this.modele=m;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        
    }
}
