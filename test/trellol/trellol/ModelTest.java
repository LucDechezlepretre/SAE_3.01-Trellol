package trellol.trellol;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trellol.trellol.Exceptions.AjoutTacheException;
import trellol.trellol.Modele.Modele;

import static org.junit.jupiter.api.Assertions.*;

class ModelTest {
    /**
     * vérification de l'archivage d'une tache
     */
    Modele m;
    @BeforeEach
    void setUp() {
        m = new Modele();
    }

    @Test
    void archiverTache() throws AjoutTacheException{
        Tache t = new Tache("tache1", "13/12/2023", 2, "Bonjour", 1);
        m.ajouterTache(t);
        m.archiverTache(t);
        assertEquals(Tache.ETAT_ARCHIVE, t.getEtat(),"La tache devrait etre archivée");
    }

    /**
     * vérification du desarchivage d'une tache
     */
    @Test
    void desarchiverTache() throws AjoutTacheException {
        Tache t = new Tache("tache1", "13/12/2023", 2, "Bonjour", 1);
        m.ajouterTache(t);
        m.archiverTache(t);
        assertEquals(Tache.ETAT_ARCHIVE, t.getEtat(), "La tache devrait etre archivée");
        m.desarchiverTache(t);
        assertEquals(Tache.ETAT_NON_ARCHIVE, t.getEtat(), "La tache devrait etre desarchivée");
    }

    /**
     * vérification de la suppression d'une tache
     */
    @Test
    void suppressionTache() throws AjoutTacheException {
        Tache t = new Tache("tache1", "13/12/2023", 2, "Bonjour", 1);
        m.ajouterTache(t);
        m.suppressionTache(t);
        assertEquals(Tache.ETAT_SUPPRIME, t.getEtat(),"La tache devrait etre supprimée");
    }

    /**
     * vérification du déplacement d'une tache
     */
    @Test
    void deplacerTache() throws AjoutTacheException{
        Tache t = new Tache("tache1", "13/12/2023", 2, "enfant", 1);
        Tache t2 = new Tache("tache2", "13/12/2023", 2, "parent", 1);
        Tache t3 = new Tache("tache3", "13/12/2023", 2, "enfant2", 1);
        m.ajouterTache(t);
        t2.setParent(t3);
        t3.setParent(t);
        //t2 est enfant de t3 et t3 est enfant de t

        m.ajouterTache(t2);
        m.ajouterTache(t3);
        //on déplace la tache t2 pour qu'elle devienne enfant de t
        m.deplacerTache(t2, t);
        assertEquals(t, t2.getParent(),"La tache devrait etre déplacée");
    }


    /**
     * vérification de l'ajout d'une tache
     * Pour cela il faut que la tache soi la première tache (racine) ou bien qu'elle ai un parent pour pouvoir etre ajoutée
     */
    @Test
    public void testAjouterTache() throws AjoutTacheException{
        Tache t = new Tache("tache1", "13/12/2023", 2, "Bonjour", 1);

        //ajout racine
        m.ajouterTache(t);
        assertEquals(1,m.getEnsTache().size(), "La tache devrait etre ajoutée");

        //ajout tache sans parent
        Tache t2 = new Tache("tache2", "13/12/2023", 2, "Bonjour", 1);
        m.ajouterTache(t2);
        assertEquals(1,m.getEnsTache().size(), "La tache ne devrait pas etre ajoutée");
        t2.setParent(t);

        //ajout tache avec parent
        m.ajouterTache(t2);
        assertEquals(2,m.getEnsTache().size(), "La tache devrait etre ajoutée");
    }
    @Test
    public void testGetRacine() throws AjoutTacheException{
        Tache t = new Tache("tache1", "13/12/2023", 2, "Bonjour", 1);
        m.ajouterTache(t);
        assertEquals(t, m.getRacine(), "Les deux tâches devraient être identiques");
    }

    @Test
    public void testCalculerDureeTacheSeule() throws AjoutTacheException{
        Tache t = new Tache("tache1", "13/12/2023", 2, "Bonjour", 1);

        //ajout racine
        m.ajouterTache(t);
        assertEquals(2, this.m.calculerDureeTache(t), "la durée de la tache est deux");
    }
    @Test
    public void testCalculerDureeDeuxTache() throws AjoutTacheException{
        Tache racine = new Tache("racine", "13/12/2023", 2, "Bonjour", 1);
        //ajout racine
        m.ajouterTache(racine);

        Tache tache = new Tache("tache1", "13/12/2023", 3, "Bonjour", 1);
        tache.setParent(racine);
        m.ajouterTache(tache);
        assertEquals(5, this.m.calculerDureeTache(racine), "la durée de la tache est 5");
    }

    @Test
    public void testCalculerDureeTroisTache() throws AjoutTacheException{
        Tache racine = new Tache("racine", "13/12/2023", 2, "Bonjour", 1);
        //ajout racine
        m.ajouterTache(racine);

        Tache tache = new Tache("tache1", "13/12/2023", 3, "Bonjour", 1);
        tache.setParent(racine);
        m.ajouterTache(tache);

        Tache tache2 = new Tache("tache2", "13/12/2023", 3, "Bonjour", 1);
        tache2.setParent(tache);
        m.ajouterTache(tache2);
        assertEquals(8, this.m.calculerDureeTache(racine), "la durée de la tache est 5");
    }

    @Test
    public void testCalculerDureeQuatreTache() throws AjoutTacheException{
        Tache racine = new Tache("racine", "13/12/2023", 2, "Bonjour", 1);
        //ajout racine
        m.ajouterTache(racine);

        Tache tache = new Tache("tache1", "13/12/2023", 3, "Bonjour", 1);
        tache.setParent(racine);
        m.ajouterTache(tache);

        Tache tache2 = new Tache("tache2", "13/12/2023", 3, "Bonjour", 1);
        tache2.setParent(racine);
        m.ajouterTache(tache2);

        Tache tache3 = new Tache("tache3", "13/12/2023", 4, "Bonjour", 1);
        tache3.setParent(tache2);
        m.ajouterTache(tache3);
        assertEquals(12, this.m.calculerDureeTache(racine), "la durée de la tache est 5");
        assertEquals(7, this.m.calculerDureeTache(tache2), "La durée de la tache2 est 7");
    }
    @Test
    public void testCalculerDureeApresDeplacement() throws AjoutTacheException{
        Tache racine = new Tache("racine", "13/12/2023", 2, "Bonjour", 1);
        //ajout racine
        m.ajouterTache(racine);

        Tache tache = new Tache("tache1", "13/12/2023", 3, "Bonjour", 1);
        tache.setParent(racine);
        m.ajouterTache(tache);

        assertEquals(3, m.calculerDureeTache(tache), "la taille de la tache est 3");

        Tache tache2 = new Tache("tache2", "13/12/2023", 3, "Bonjour", 1);
        tache2.setParent(racine);
        m.ajouterTache(tache2);

        m.deplacerTache(tache2, tache);
        assertEquals(6, m.calculerDureeTache(tache), "la taille de la tache est 6");
    }
}