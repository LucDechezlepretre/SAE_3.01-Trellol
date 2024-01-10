package trellol.trellol.Vues;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import trellol.trellol.Modele.Modele;
import trellol.trellol.Modele.Sujet;
import trellol.trellol.Tache;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class VueGantt extends Tab implements Observateur{

    private Modele model;

    private int tailleJour = 60;

    private int hauteurTache = 25;

    private double y = 0.5;

    private double ecartVertical = 1.3;

    private double decalage = 15;

    private List<String> couleurs = new ArrayList<>(Arrays.asList("green","orange","red"));

    private HashMap<Tache, List<Double>> coordonneesParents;
    private Date debutProjet;

    public VueGantt(String nom, Sujet s) {
        super(nom);
        coordonneesParents = new HashMap<>();
        this.model = (Modele)s;
        StackPane conteneur = new StackPane();
        this.setContent(conteneur);

    }

    
    @Override
    public void actualiser(Sujet s) {
        this.y = 1;
        coordonneesParents = new HashMap<>();
        this.model =(Modele) s;
        this.debutProjet = this.model.getRacine().getDateDebut();
        model.filtrerGantt();
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
        double longueurCanva = tailleJour * this.diffDatesProjet(model.getDateFinProjet())+this.decalage*2;
        double hauteurCanva = (model.getTacheSelectGantt().size()+3)*(this.hauteurTache +this.ecartVertical*10);
        Canvas canvas = new Canvas(longueurCanva,hauteurCanva);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        List<Tache> listeTache = new ArrayList<>(model.getTacheSelectGantt());
        Collections.sort(listeTache);
        this.creerTimeline(gc,model.getRacine());
        for (Tache t: listeTache) {
               if (!model.getTacheSelectGantt().contains(t.getAntecedant()) && !t.equals(model.getRacine())) {
                   DessinerTacheRecursivement(gc, t);
               }
        }
        return canvas;
    }

    public void DessinerTacheRecursivement(GraphicsContext gc, Tache t) {
            gc.setFill(Paint.valueOf(couleurs.get(t.getImportance())));
            gc.fillRect(this.tailleJour * this.diffDatesProjet(t.getDateDebut()) + this.decalage, this.y * this.hauteurTache, this.tailleJour * model.calculerDureeTache(t), this.hauteurTache);
            gc.strokeText(t.getNom(), this.diffDatesProjet(t.getDateDebut()) * this.tailleJour + this.decalage, this.y * this.hauteurTache + 10);

            ArrayList<Double> coordonnees = new ArrayList<Double>();
            coordonnees.add(this.tailleJour * this.diffDatesProjet(t.getDateDebut()) + this.decalage + this.tailleJour * model.calculerDureeTache(t));
            coordonnees.add(this.y * this.hauteurTache + this.hauteurTache/ 2);
            this.coordonneesParents.put(t, coordonnees);

            if (coordonneesParents.containsKey(t.getAntecedant())) {
                Double parentX = this.coordonneesParents.get(t.getAntecedant()).get(0);
                Double parentY = this.coordonneesParents.get(t.getAntecedant()).get(1);
                Double enfantX = this.tailleJour * this.diffDatesProjet(t.getDateDebut()) + this.decalage;
                Double enfantY = this.y * this.hauteurTache + this.hauteurTache / 2;

                gc.strokeLine(parentX, parentY, enfantX, enfantY);
            }

            this.y += this.ecartVertical;
        if (model.getSuccesseurs(t).size() != 0) {
            for (Tache enfant : model.getSuccesseurs(t)) {
                if (model.getTacheSelectGantt().contains(enfant)) {
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
            position += this.tailleJour;
            date = new Date(date.getTime() + (1000 * 60 * 60 * 24));
        }
    }

}

