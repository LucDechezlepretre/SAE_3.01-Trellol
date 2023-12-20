package trellol.trellol;

import java.util.ArrayList;

public class Historique {
    private ArrayList<String> actions;

    public static String MODIFICATION_ACTION = "La tâche {objet} a été modifiée";
    public static String DEPLACEMENT_ACTION = "La tâche {objet} a été déplacé";

    public static String SUPRESSION_ACTION = "La tâche {objet} a été supprimé";

    public static String ARCHIVAGE_ACTION = "La tâche {objet} a été archivé";

    public static String DESARCHIVAGE_ACTION = "La tâche {objet} a été désarchivé";

    public static String CREATION_TACHE_ACTION = "La tâche {objet} a été créé";

    public static String CREATION_COLONNE = "La colonne {objet} a été créé";

    public static String EXPORTER_TABLEAU = "Le tableau {objet} a été exporté";

    public Historique(){
        this.actions = new ArrayList<String>();
    }

    public void addAction(String action,String objet){
        this.actions.add(action.replace("{objet}",objet));
    }

    public ArrayList<String> getActions(){
        return this.actions;
    }

    public String toString(){
        String affiche = "";
        for (String action : this.actions) {
            affiche += action + "\n";
        }
        return affiche;
    }
}
