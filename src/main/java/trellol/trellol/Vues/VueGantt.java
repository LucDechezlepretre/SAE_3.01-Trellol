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

    private static int TailleJour = 60;

    private double y = 0.5;

    private static double ecartVertical = 1.3;

    private static double ecartHorizontal = 0.5;

    private static double decalage = 15;

    private static List<String> couleurs = new ArrayList<>(Arrays.asList("green","orange","red"));;

    private HashMap<Tache, List<Double>> coordonneesParents;
    private Date debutProjet;

    public VueGantt(String nom, Sujet s) {
        super(nom);
        coordonneesParents = new HashMap<>();
        VueGantt.model = (Modele)s;
        StackPane conteneur = new StackPane();
        this.setContent(conteneur);

    }

    @Override
    public void actualiser(Sujet s) {
        this.y = 1;
        coordonneesParents = new HashMap<>();
        VueGantt.model =(Modele) s;
        this.debutProjet = this.model.getRacine().getDateDebut();
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
        Canvas canvas = new Canvas(5000,5000);
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
        if (t.getEtat().equals(Tache.ETAT_INITIAL)) {
            gc.setFill(Paint.valueOf(couleurs.get(t.getImportance())));
            gc.fillRect(VueGantt.TailleJour * this.diffDatesProjet(t.getDateDebut()) + VueGantt.decalage, this.y * VueGantt.TailleJour, VueGantt.TailleJour * model.calculerDureeTache(t), VueGantt.TailleJour);
            gc.strokeText(t.getNom(), this.diffDatesProjet(t.getDateDebut()) * VueGantt.TailleJour + VueGantt.decalage, this.y * VueGantt.TailleJour + 10);

            ArrayList<Double> coordonnees = new ArrayList<Double>();
            coordonnees.add(VueGantt.TailleJour * this.diffDatesProjet(t.getDateDebut()) + VueGantt.decalage + VueGantt.TailleJour * model.calculerDureeTache(t));
            coordonnees.add(this.y * VueGantt.TailleJour + VueGantt.TailleJour / 2);
            this.coordonneesParents.put(t, coordonnees);

            if (coordonneesParents.containsKey(t.getAntecedant())) {
                Double parentX = this.coordonneesParents.get(t.getAntecedant()).get(0);
                Double parentY = this.coordonneesParents.get(t.getAntecedant()).get(1);
                Double enfantX = VueGantt.TailleJour * this.diffDatesProjet(t.getDateDebut()) + VueGantt.decalage;
                Double enfantY = this.y * VueGantt.TailleJour + VueGantt.TailleJour / 2;

                gc.strokeLine(parentX, parentY, enfantX, enfantY);
            }

            this.y += VueGantt.ecartVertical;
        }
        if (model.getSuccesseurs(t).size() != 0) {
            for (Tache enfant : model.getSuccesseurs(t)) {
                if (enfant.getEtat().equals(Tache.ETAT_INITIAL)) {
                    this.DessinerTacheRecursivement(gc, enfant);
                }
            }
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
        int dureeProjet = diffDatesProjet(model.getDateFinProjet());
        for(int i = 0; i<=dureeProjet; i++) {
            gc.strokeText(df.format(date),position,10);
            position += VueGantt.TailleJour;
            date = new Date(date.getTime() + (1000 * 60 * 60 * 24));
        }
    }

}

