package trellol.trellol.Controleurs;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import trellol.trellol.Modele.Modele;
import trellol.trellol.Tache;
import trellol.trellol.Vues.MainAffichage;

public class ControlleurSuppression implements EventHandler {
    private Modele m;

    private Stage fenetre;
    private Tache tache;

    public ControlleurSuppression(Modele m, Stage fenetre, Tache t){
        this.m=m;
        this.fenetre=fenetre;
        this.tache=t;
    }

    public ControlleurSuppression(Modele m, Tache t){
        this.m=m;
        this.fenetre=null;
        this.tache=t;
    }

    @Override
    public void handle(Event event) {
        this.m.suppressionTache(this.tache);

        if(this.fenetre!=null){
            MainAffichage.affichageFormTache = false;
            this.fenetre.close();
        }
    }
}
