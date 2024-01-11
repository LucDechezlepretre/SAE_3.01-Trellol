package trellol.trellol;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trellol.trellol.Exceptions.AjoutTacheException;
import trellol.trellol.Modele.Modele;

import static org.junit.jupiter.api.Assertions.*;

public class TestClaculDuree {
    Modele m;
    @BeforeEach
    void setUp() {
        m = new Modele();
    }
    /**
     * Test de la durée d'une tache
     * @throws AjoutTacheException
     */
    @Test
    public void testCalculerDureeTacheSeule() throws AjoutTacheException{
        Tache t = new Tache("tache1", "13/12/2023", 2, "Bonjour", 1);

        //ajout racine
        m.ajouterTache(t);
        assertEquals(2, this.m.calculerDureeTache(t), "la durée de la tache est deux");
    }

    /**
     * Test de la durée d'une tache avec un parent
     * @throws AjoutTacheException
     */
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

    /**
     * Test de la durée d'une tache avec un parent et un enfant
     * @throws AjoutTacheException
     */
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

    /**
     * Test de la durée d'une tache avec un parent et deux enfants
     * @throws AjoutTacheException
     */
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

    /**
     * Test du calcul de durée avec des déplacements.
     * @throws AjoutTacheException
     */
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
