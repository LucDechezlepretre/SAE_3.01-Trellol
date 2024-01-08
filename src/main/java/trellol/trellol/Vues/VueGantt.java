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

    private static int TailleJour = 10;

    private int x = 0;

    private int y = 0;

    public VueGantt(String nom, Sujet s) {
        super(nom);
        VueGantt.model = (Modele)s;
        StackPane conteneur = new StackPane(this.affichageGantt());
        this.setContent(conteneur);
    }

    @Override
    public void actualiser(Sujet s) {
        VueGantt.model =(Modele) s;
        StackPane content = (StackPane) this.getContent();
        content.getChildren().clear();
        content.getChildren().add(this.affichageGantt());
    }

    public Canvas affichageGantt() {
        Canvas canvas = new Canvas(1000,1000);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for (Tache t: model.getEnsTache()) {
            System.out.println(t.getNom()+" 1");
               List<Tache> listeSuccesseur = model.getSuccesseurs(t);
               if (listeSuccesseur.size() != 0) {
                   for (Tache tache: listeSuccesseur) {
                       DessinerTacheRecursivement(gc,t);
                   }
               }
        }
        return canvas;
    }

    public void DessinerTacheRecursivement(GraphicsContext gc, Tache t) {
        for (Tache parent: model.getSuccesseurs(t)) {
            gc.setFill(new Color(Math.random()*255,Math.random()*255,Math.random()*255,1));
            gc.fillRect(this.x,this.y,this.TailleJour, this.TailleJour);
            this.x++;
            if (model.getSuccesseurs(parent).size() == 0) {
                this.y = 0;
            } else {
                for (Tache enfant: model.getEnfant(parent)) {
                    this.y++;
                    this.DessinerTacheRecursivement(gc,enfant);
                }
            }

        }

    }
}

