package trellol.trellol;

import java.util.ArrayList;

public class Historique {
    private ArrayList<String> actions;

    public Historique(){
        this.actions = new ArrayList<String>();
    }

    public void addAction(String action){
        this.actions.add(action);
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
