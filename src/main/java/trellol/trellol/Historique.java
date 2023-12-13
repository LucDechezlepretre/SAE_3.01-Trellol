package trellol.trellol;

import java.util.ArrayList;

public class Historique {
    private ArrayList<String> actions;
    private Tache tache;

    public Historique(String action, Tache tache){
        this.actions = new ArrayList<String>();
        this.tache = tache;
    }

    public void addAction(String action){
        this.actions.add(action);
    }

    public ArrayList<String> getActions(){
        return this.actions;
    }

    public Tache getTache(){
        return this.tache;
    }
}
