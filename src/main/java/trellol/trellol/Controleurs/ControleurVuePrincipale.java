package trellol.trellol.Controleurs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import trellol.trellol.Modele.Model;

public class ControleurVuePrincipale implements EventHandler<ActionEvent> {

    private Model modele;

    public ControleurVuePrincipale(Model modele) {
        this.modele = modele;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        String textBouton = button.getText();
        if(textBouton.equals("Vue Bureau")){
            this.modele.activerVueBureau();
        }
        else{
            this.modele.activerVueListe();
        }
    }
}
