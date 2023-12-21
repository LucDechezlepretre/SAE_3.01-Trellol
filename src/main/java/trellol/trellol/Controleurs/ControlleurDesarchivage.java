package trellol.trellol.Controleurs;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import trellol.trellol.Modele.Modele;
import trellol.trellol.Tache;
import trellol.trellol.Vues.MainAffichage;

public class ControlleurDesarchivage implements EventHandler {
    private Modele m;
    private Tache tache;

    public ControlleurDesarchivage(Modele m, Tache t){
        this.m=m;
        this.tache=t;
    }

    @Override
    public void handle(Event event) {
        this.m.desarchiverTache(tache);
    }
}
