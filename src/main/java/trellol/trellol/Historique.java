package trellol.trellol;

import java.util.ArrayList;

public class Historique {
    private ArrayList<String> actions;

    public Historique(String action, Tache tache){
        this.actions = new ArrayList<String>();
    }

    public void addAction(String action){
        this.actions.add(action);
    }

    public ArrayList<String> getActions(){
        return this.actions;
    }

    public String afficherDerniereAction(){
        return this.actions.get(this.actions.size()-1);
    }
}
