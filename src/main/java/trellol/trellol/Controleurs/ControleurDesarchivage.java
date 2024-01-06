package trellol.trellol.Controleurs;

import javafx.event.Event;
import javafx.event.EventHandler;
import trellol.trellol.Modele.Modele;
import trellol.trellol.Tache;
/**
 * Classe controleur pour le desarchivage d'une tache
 */
public class ControleurDesarchivage implements EventHandler {
    /**
     * Attribut m de type Modele
     */
    private Modele m;
    /**
     * Attribut tache de type Tache représentant la tâche à desarchiver
     */
    private Tache tache;

    /**
     * Constructeur du controleur
     * @param m modele auquel on desarchivera la tâche
     * @param t tache à desarchiver
     */
    public ControleurDesarchivage(Modele m, Tache t){
        this.m=m;
        this.tache=t;
    }
    /**
     * Redéfinition de la méthode handle, appelé lors du "clique"
     * sur le bouton desarchiver depuis l'onglet "Archive"
     * @param event évènement sur le bouton associé
     */
    @Override
    public void handle(Event event) {
        this.m.desarchiverTache(tache);
    }
}
