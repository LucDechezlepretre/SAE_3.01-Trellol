package trellol.trellol.Vues;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Tab;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import trellol.trellol.Modele.Modele;
import trellol.trellol.Modele.Sujet;
import trellol.trellol.Tache;

import java.util.ArrayList;
import java.util.List;

public class VueGantt extends Tab implements Observateur {

    private static Modele model;

    private static int TailleJour = 50;

    private int x = 1;

    private int y = 1;

    public VueGantt(String nom, Sujet s) {
        super(nom);
        VueGantt.model = (Modele)s;
        StackPane conteneur = new StackPane(this.affichageGantt());
        this.setContent(conteneur);
    }

    @Override
    public void actualiser(Sujet s) {
        this.x = 1;
        this.y = 1;
        VueGantt.model =(Modele) s;
        StackPane content = (StackPane) this.getContent();
        content.getChildren().clear();
        content.getChildren().add(this.affichageGantt());
        System.out.println("--------");
    }

    public Canvas affichageGantt() {
        Canvas canvas = new Canvas(1000,1000);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for (Tache t: model.getEnsTache()) {
            System.out.println(t.getNom()+" 1");
               List<Tache> listeSuccesseur = model.getSuccesseurs(t);
               System.out.println(listeSuccesseur.size());
               DessinerTacheRecursivement(gc,t);
        }
        return canvas;
    }

    public void DessinerTacheRecursivement(GraphicsContext gc, Tache t) {
        System.out.println(t.getNom()+ " 2");
        for (Tache parent: model.getSuccesseurs(t)) {
            //gc.setFill(new Color(Math.round(Math.random()*255),Math.round(Math.random()*255),Math.round(Math.random()*255),1.0));
            gc.strokeRect(this.x*this.TailleJour,this.y*this.TailleJour,this.TailleJour, this.TailleJour);
            gc.strokeText(parent.getNom(),this.x*this.TailleJour,this.y*this.TailleJour);
            this.y++;
            if (model.getSuccesseurs(parent).size() == 0) {
                this.x = 1;
            } else {
                for (Tache enfant: model.getEnfant(parent)) {
                    this.x++;
                    this.DessinerTacheRecursivement(gc,enfant);
                }
            }

        }

    }
}

