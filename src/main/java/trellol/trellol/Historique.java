package trellol.trellol;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Classe Historique permettant la gestion d'un historique des modifications
 * au sein d'un modèle
 */
public class Historique {
    /**
     * Attribut liste représentant la totalité des actions
     */
    private LinkedHashMap<String, String> actions;

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


    private static final DateTimeFormatter FORMATTER= DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * Constructeur de l'historique
     */
    public Historique(){
        this.actions = new LinkedHashMap<>();
    }

    /**
     * Méthode permettant l'ajout d'une action dans l'historique
     * @param action action qui sera ajoutée à l'historique
     * @param objet nom de l'objet qui subit l'action
     */
    public void addAction(String action,String objet){
        LocalDateTime now=LocalDateTime.now();
        this.actions.put(FORMATTER.format(now), action.replace("{objet}",objet));
    }

    /**
     * Getter de l'attribut qui contient toutes les actions
     * @return la liste des actions commises
     */
    public Map<String,String> getActions(){
        return this.actions;
    }

    /**
     * Méthode toString de l'historique
     * @return String représentant l'historique
     */
    public String toString(){
        String affiche = "";

        Set<String> dates=this.actions.keySet();


        for (Map.Entry<String, String> entry : this.actions.entrySet()) {
            affiche+=entry.getKey()+" "+entry.getValue()+"\n";
        }
        return affiche;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Historique that = (Historique) o;
        return Objects.equals(actions, that.actions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actions);
    }
}
