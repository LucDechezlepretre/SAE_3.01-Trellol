package trellol.trellol.Vues;

import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import trellol.trellol.Controleurs.ControleurSelectionGantt;
import trellol.trellol.Modele.Modele;
import trellol.trellol.Modele.Sujet;
import trellol.trellol.Tache;

import java.io.Serializable;

public class VueSelecteurGantt extends SplitMenuButton implements Observateur, Serializable {
    private Modele m;

    public VueSelecteurGantt(Sujet s, String nom) {
        this.setText(nom);
        this.actualiser(s);
    }
    @Override
    public void actualiser(Sujet s) {
        this.m = (Modele) s;
        this.getItems().clear();
        MenuItem mi;
        for (Tache t: m.getEnsTache()) {
            if (t.getEtat().equals(Tache.ETAT_INITIAL)) {
                String nomTache = t.getNom();
                if (t.equals(m.getRacine())) {
                    nomTache = "Afficher tout";
                }
                if (this.m.getTacheSelectGantt().contains(t)) {
                    nomTache = "X " + nomTache;
                }
                mi = new MenuItem(nomTache);
                mi.setOnAction(new ControleurSelectionGantt(m, t));
                this.getItems().add(mi);
            }
        }
    }
}
