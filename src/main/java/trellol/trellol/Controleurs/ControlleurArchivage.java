package trellol.trellol.Controleurs;

import javafx.event.Event;
import javafx.event.EventHandler;
import trellol.trellol.Modele.Modele;
import trellol.trellol.Tache;

public class ControlleurArchivage implements EventHandler {
    private Modele m;

    private Tache tache;

    @Override
    public void handle(Event event) {
        this.m.archiverTache(tache);
    }
}
