package trellol.trellol.Controleurs;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import trellol.trellol.Modele.Modele;
import trellol.trellol.Tache;
import trellol.trellol.Vues.MainAffichage;

public class ControlleurArchivage implements EventHandler {
    private Modele m;

    private Stage fenetre;
    private Tache tache;

    public ControlleurArchivage(Modele m, Stage fenetre, Tache t){
        this.m=m;
        this.fenetre=fenetre;
        this.tache=t;
    }

    @Override
    public void handle(Event event) {
        this.m.archiverTache(tache);
        MainAffichage.affichageFormTache = false;
        this.fenetre.close();
    }
}
