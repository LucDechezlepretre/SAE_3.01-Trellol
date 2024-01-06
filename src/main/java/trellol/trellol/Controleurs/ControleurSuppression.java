package trellol.trellol.Controleurs;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import trellol.trellol.Modele.Modele;
import trellol.trellol.Tache;
import trellol.trellol.Vues.MainAffichage;

/**
 * Classe controleur pour la suppression d'une tache
 */
public class ControleurSuppression implements EventHandler {
    /**
     * Attribut m de type Modele
     */
    private Modele m;

    private Stage fenetre;
    /**
     * Attribut tache de type Tache représentant la tâche à supprimer
     */
    private Tache tache;

    /**
     * Constructeur pour le controleur
     * @param m le modele
     * @param fenetre la fenetre si le controleur est dans la fenetre de modification d'une tache
     * @param t
     */
    public ControleurSuppression(Modele m, Stage fenetre, Tache t){
        this.m=m;
        this.fenetre=fenetre;
        this.tache=t;
    }

    public ControleurSuppression(Modele m, Tache t){
        this.m=m;
        this.fenetre=null;
        this.tache=t;
    }

    /**
     * Redéfinition de la méthode handle, appelé lors du "clique"
     * sur le bouton supprimer
     * @param event évènement sur le bouton associé
     */
    @Override
    public void handle(Event event) {
        this.m.suppressionTache(this.tache);

        if(this.fenetre!=null){
            MainAffichage.affichageFormTache = false;
            this.fenetre.close();
        }
    }
}
