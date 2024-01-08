package trellol.trellol;

import java.util.ArrayList;

/**
 * Classe Historique permettant la gestion d'un historique des modifications
 * au sein d'un modèle
 */
public class Historique {
    /**
     * Attribut liste représentant la totalité des actions
     */
    private ArrayList<String> actions;

    /**
     * Attribut String représentant l'action de modification d'une tâche
     */
    public static String MODIFICATION_ACTION = "La tâche {objet} a été modifiée";
    /**
     * Attribut String représentant l'action de déplacement d'une tâche
     */
    public static String DEPLACEMENT_ACTION = "La tâche {objet} a été déplacée";
    /**
     * Attribut String représentant l'action de suppression d'une tâche
     */
    public static String SUPRESSION_ACTION = "La tâche {objet} a été supprimée";
    /**
     * Attribut String représentant l'action d'archivage d'une tâche
     */
    public static String ARCHIVAGE_ACTION = "La tâche {objet} a été archivée";
    /**
     * Attribut String représentant l'action de déarchivage d'une tâche
     */
    public static String DESARCHIVAGE_ACTION = "La tâche {objet} a été désarchivée";
    /**
     * Attribut String représentant l'action de la création d'une tâche
     */
    public static String CREATION_TACHE_ACTION = "La tâche {objet} a été créée";
    /**
     * Attribut String représentant l'action d'exportation des tâches dans un fichier
     */
    public static String EXPORTER_TABLEAU = "Le tableau {objet} a été exportée";

    /**
     * Constructeur de l'historique
     */
    public Historique(){
        this.actions = new ArrayList<String>();
    }

    /**
     * Méthode permettant l'ajout d'une action dans l'historique
     * @param action action qui sera ajoutée à l'historique
     * @param objet nom de l'objet qui subit l'action
     */
    public void addAction(String action,String objet){
        this.actions.add(action.replace("{objet}",objet));
    }

    /**
     * Getter de l'attribut qui contient toutes les actions
     * @return la liste des actions commises
     */
    public ArrayList<String> getActions(){
        return this.actions;
    }

    /**
     * Méthode toString de l'historique
     * @return String représentant l'historique
     */
    public String toString(){
        String affiche = "";
        for (String action : this.actions) {
            affiche += action + "\n";
        }
        return affiche;
    }
}
