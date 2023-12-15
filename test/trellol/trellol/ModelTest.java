package trellol.trellol;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModelTest {
    /**
     * vérification de l'archivage d'une tache
     */
    Model m;
    @BeforeEach
    void setUp() {
        m = new Model();
    }

    @Test
    void archiverTache() {
        Tache t = new Tache("tache1", "13/12/2023", 2, "Bonjour", 1);
        m.ajouterTache(t);
        m.archiverTache(t);
        assertEquals(Tache.ETAT_ARCHIVE, t.getEtat(),"La tache devrait etre archivée");
    }

    /**
     * vérification du desarchivage d'une tache
     */
    @Test
    void desarchiverTache() {
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
    void suppressionTache() {
        Tache t = new Tache("tache1", "13/12/2023", 2, "Bonjour", 1);
        m.ajouterTache(t);
        m.suppressionTache(t);
        assertEquals(Tache.ETAT_SUPPRIME, t.getEtat(),"La tache devrait etre supprimée");
    }

    /**
     * vérification du déplacement d'une tache
     */
    @Test
    void deplacerTache() {
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
    public void testAjouterTache(){
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
    public void testGetRacine(){
        Tache t = new Tache("tache1", "13/12/2023", 2, "Bonjour", 1);
        m.ajouterTache(t);
        assertEquals(t, m.getRacine(), "Les deux tâches devraient être identiques");
    }
}