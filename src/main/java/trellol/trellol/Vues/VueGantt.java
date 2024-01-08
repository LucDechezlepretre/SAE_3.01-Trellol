package trellol.trellol.Vues;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
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

    private static double ecartVertical = 0.3;

    private static double ecartHorizontal = 0.5;

    private static double decalage = 15;

    private HashMap<Tache, List<Double>> coordonneesParents;
    private Date debutProjet;

    public VueGantt(String nom, Sujet s) {
        super(nom);
        VueGantt.model = (Modele)s;
        this.debutProjet = this.model.getRacine().getDateDebut();
        StackPane conteneur = new StackPane(this.affichageGantt());
        this.setContent(conteneur);
        //coordonneesParents = new HashMap<Tache, List<Integer>>();
    }

    @Override
    public void actualiser(Sujet s) {
        this.y = 1;
        VueGantt.model =(Modele) s;
        StackPane content = (StackPane) this.getContent();
        content.getChildren().clear();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(this.affichageGantt());
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        content.getChildren().add(scrollPane);
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
                   this.y += VueGantt.ecartVertical;
               }
        }
        this.creerTimeline(gc,model.getRacine());
        return canvas;
    }

    public void DessinerTacheRecursivement(GraphicsContext gc, Tache t) {

        //gc.setFill(new Color(Math.round(Math.random()*255),Math.round(Math.random()*255),Math.round(Math.random()*255),1.0));

        gc.strokeRect(VueGantt.TailleJour*this.diffDatesProjet(t.getDateDebut())+VueGantt.decalage,this.y*VueGantt.TailleJour,VueGantt.TailleJour*model.calculerDureeTache(t), VueGantt.TailleJour);
        gc.strokeText(t.getNom(),this.diffDatesProjet(t.getDateDebut())*VueGantt.TailleJour+VueGantt.decalage,this.y*VueGantt.TailleJour+10);

        /**this.coordonneesParents.put(t, new ArrayList<>(Arrays.asList( VueGantt.TailleJour*this.diffDatesProjet(t.getDateDebut())+VueGantt.decalage + VueGantt.TailleJour*model.calculerDureeTache(t),this.y*VueGantt.TailleJour + VueGantt.TailleJour/2 )));

        if (coordonneesParents.containsKey(t)) {

        }
         */


        this.y++;
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

