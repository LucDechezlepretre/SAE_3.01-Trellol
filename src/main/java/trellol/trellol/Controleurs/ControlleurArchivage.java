package trellol.trellol.Controleurs;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import trellol.trellol.Modele.Modele;
import trellol.trellol.Tache;
import trellol.trellol.Vues.MainAffichage;

/**
 * Classe controleur pour l'archivage d'une tache
 */
public class ControlleurArchivage implements EventHandler {
    /**
     * Attribut m de type Modele
     */
    private Modele m;
    /**
     * Attribut fenetre de type Stage représentant la fenetre permettant la modification de l'état de la tache
     */
    private Stage fenetre;
    /**
     * Attribut tache de type Tache représentant la tache à archiver
     */
    private Tache tache;

    /**
     * Constructeur du controleur
     * @param m Modele qui permettra l'archivage de la tache
     * @param fenetre Stage de modification de la tache
     * @param t Tache à archiver
     */
    public ControlleurArchivage(Modele m, Stage fenetre, Tache t){
        this.m=m;
        this.fenetre=fenetre;
        this.tache=t;
    }

    /**
     * Redéfinition de la méthode handle, appelé lors du "clique"
     * sue le bouton archiver de la fenetre de modification
     * @param event évènement sur le bouton associé
     */
    @Override
    public void handle(Event event) {
        this.m.archiverTache(tache);
        MainAffichage.affichageFormTache = false;
        this.fenetre.close();
    }
}
