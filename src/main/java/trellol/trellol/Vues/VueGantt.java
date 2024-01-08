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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static trellol.trellol.Tache.dateFormat;

public class VueGantt extends Tab implements Observateur {

    private static Modele model;

    private static int TailleJour = 70;

    private double y = 0.5;

    private static double ecartVertical = 1.3;

    private static double decalage = 15;

    private Date debutProjet;

    public VueGantt(String nom, Sujet s) {
        super(nom);
        VueGantt.model = (Modele)s;
        this.debutProjet = this.model.getRacine().getDateDebut();
        StackPane conteneur = new StackPane(this.affichageGantt());
        this.setContent(conteneur);
    }

    @Override
    public void actualiser(Sujet s) {
        this.y = 1;
        VueGantt.model =(Modele) s;
        StackPane content = (StackPane) this.getContent();
        content.getChildren().clear();
        content.getChildren().add(this.affichageGantt());
        this.debutProjet = this.model.getRacine().getDateDebut();
    }

    public Canvas affichageGantt() {
        Canvas canvas = new Canvas(10000,10000);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        List<Tache> listeTache = model.getEnsTache();
        Collections.sort(listeTache);
        for (Tache t: listeTache) {
               if (t.getAntecedant() == null && !t.equals(model.getRacine())) {
                   DessinerTacheRecursivement(gc, t);
               }
        }
        this.creerTimeline(gc,model.getRacine());
        return canvas;
    }

    public void DessinerTacheRecursivement(GraphicsContext gc, Tache t) {
        //gc.setFill(new Color(Math.round(Math.random()*255),Math.round(Math.random()*255),Math.round(Math.random()*255),1.0));
        gc.strokeRect(VueGantt.TailleJour*this.diffDatesProjet(t.getDateDebut())+VueGantt.decalage,this.y*VueGantt.TailleJour,VueGantt.TailleJour*t.getDuree(), VueGantt.TailleJour);
        gc.strokeText(t.getNom(),this.diffDatesProjet(t.getDateDebut())*VueGantt.TailleJour+VueGantt.decalage,this.y*VueGantt.TailleJour+10);
        this.y += VueGantt.ecartVertical;
        for (Tache enfant: model.getEnfant(t)) {
            this.DessinerTacheRecursivement(gc,enfant);
        }
    }

    public int diffDatesProjet(Date date) {
        long diffInMillies = Math.abs(date.getTime() - this.debutProjet.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return (int) diff;
    }

    public void creerTimeline(GraphicsContext gc, Tache racine) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM");
        Date date = racine.getDateDebut();
        int position = 0;
        for(int i = 0; i<model.calculerDureeTache(racine); i++) {
            gc.strokeText(df.format(date),position,10);
            position += VueGantt.TailleJour;
            date = new Date(date.getTime() + (1000 * 60 * 60 * 24));
        }
    }

}

