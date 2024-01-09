package trellol.trellol.Controleurs;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import trellol.trellol.Modele.Modele;
import trellol.trellol.Tache;

public class ControleurSelectionGantt implements EventHandler {
    private Modele m;
    /**
     * Attribut tache de type Tache représentant la tâche à supprimer
     */
    private Tache tache;

    public ControleurSelectionGantt(Modele m, Tache tache) {
        this.m = m;
        this.tache = tache;
    }

    @Override
    public void handle(Event event) {
        if (m.getTacheSelectGantt().contains(tache)) {
            m.supprimerListeGantt(tache);
            ((MenuItem)event.getTarget()).setText(((MenuItem) event.getTarget()).getText().replace("X ",""));
        } else {
            m.ajouterListeGantt(tache);
            ((MenuItem)event.getTarget()).setText("X " + ((MenuItem) event.getTarget()).getText());
        }

    }
}
