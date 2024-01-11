package trellol.trellol;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trellol.trellol.Exceptions.AjoutTacheException;
import trellol.trellol.Modele.Modele;

import static org.junit.jupiter.api.Assertions.*;

class ModelTest {

    Modele m;
    @BeforeEach
    void setUp() {
        m = new Modele();
    }

    /**
     * vérification de la suppression d'une tache
     */
    @Test
    void suppressionTache() throws AjoutTacheException {
        Tache t = new Tache("tache1", "13/12/2023", 2, "Bonjour", 1);
        m.ajouterTache(t);
        m.suppressionTache(t);
        assertEquals(0,m.getEnfant(t).size());
    }


    @Test
    public void testAjouterTache() throws AjoutTacheException{
        Tache t = new Tache("tache1", "13/12/2023", 2, "Bonjour", 1);

        //ajout racine
        m.ajouterTache(t);
        assertEquals(1,m.getEnsTache().size(), "La tache devrait etre ajoutée");

        //ajout tache sans parent
        Tache t2 = new Tache("tache2", "13/12/2023", 2, "Bonjour", 1);
        assertThrows(AjoutTacheException.class, () -> m.ajouterTache(t2), "La tache ne devrait pas etre ajoutée");
        t2.setParent(t);

        //ajout tache avec parent
        m.ajouterTache(t2);
        assertEquals(2,m.getEnsTache().size(), "La tache devrait etre ajoutée");
    }

    /**
     * Test recupération de la racine
     * @throws AjoutTacheException
     */
    @Test
    public void testGetRacine() throws AjoutTacheException{
        Tache t = new Tache("tache1", "13/12/2023", 2, "Bonjour", 1);
        m.ajouterTache(t);
        assertEquals(t, m.getRacine(), "Les deux tâches devraient être identiques");
    }

    /**
     * Test de la récupération des taches successeurs
     * @throws AjoutTacheException
     */
    @Test
    public void testGetSuccesseurs() throws AjoutTacheException{
        Tache racine = new Tache("racine", "13/12/2023", 2, "Bonjour", 1);
        //ajout racine
        m.ajouterTache(racine);

        Tache tache = new Tache("tache1", "13/12/2023", 3, "Bonjour", 1);
        tache.setParent(racine);
        tache.setAntecedant(racine);
        m.ajouterTache(tache);
        assertEquals(tache, m.getSuccesseurs(racine).get(0), "La racine devrait avoir pour successeur tache");
    }

    /**
     * Test de la récupération d'une tache avec son nom
     * @throws AjoutTacheException
     */
    @Test
    public void testFindTacheByName() throws AjoutTacheException {
        Tache racine = new Tache("racine", "13/12/2023", 2, "Bonjour", 1);
        //ajout racine
        m.ajouterTache(racine);

        Tache tache = new Tache("tache1", "13/12/2023", 3, "Bonjour", 1);
        tache.setParent(racine);
        tache.setAntecedant(racine);
        m.ajouterTache(tache);
        assertEquals(tache, m.findTacheByName("tache1"), "La tache devrait etre trouvée");
    }

    @Test
    public void testSauvergarderEtCharger() throws AjoutTacheException{
        Modele test = new Modele();
        Tache racine = new Tache("racine", "13/12/2023", 2, "Bonjour", 1);
        //ajout racine
        m.ajouterTache(racine);
        this.m.sauvegarder("./save/test.trellol");
        test.setEnsTache(Modele.charger("./save/test.trellol"));
        assertEquals(this.m, test, "le modèle devrait être correctement chargé");
    }
    @Test
    public void testSauvegardePuisCalcul() throws AjoutTacheException{
        Modele test = new Modele();
        Tache racine = new Tache("racine", "13/12/2023", 10, "Bonjour", 1);
        Tache luc = new Tache("lcu", "19/01/2024", 2,"Bonjour", 1);
        luc.setParent(racine);

        //ajout racine
        m.ajouterTache(racine);
        m.ajouterTache(luc);
        this.m.sauvegarder("./save/test.trellol");
        this.m.setEnsTache(Modele.charger("./save/test.trellol"));
        assertEquals(12, this.m.calculerDureeTache(this.m.getRacine()), "La durée de la racine devrait être 12");
    }
}