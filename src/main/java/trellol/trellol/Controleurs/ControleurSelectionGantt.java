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
     * Attribut tache de type Tache représentant la tâche à ajouter ou supprimer dans la liste des tâches à afficher dans le Gantt
     */
    private Tache tache;

    /**
     * Constructeur pour le controleur
     * @param m le modele à mettre à jour
     * @param tache tâche à ajouter ou supprimer de la liste des tâches à ajouter dans le Gantt
     */
    public ControleurSelectionGantt(Modele m, Tache tache) {
        this.m = m;
        this.tache = tache;
    }

    /**
     * Redéfinition de la méthode handle, appelé lors du "clique"
     * sur le bouton supprimer
     * @param event évènement sur l'option du selector associé
     */
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
