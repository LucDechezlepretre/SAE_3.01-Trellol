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
        assertEquals(Tache.ETAT_INITIAL, t.getEtat(), "La tache devrait etre desarchivée");
    }

    /**
     * vérification de l'archivage et du desarchivage d'une tache avec des enfants
     * @throws AjoutTacheException
     */
    @Test
    //fais un test sur 1 tache parent avec 2 enfants
    public void testArchiverDesarchiverEnfants() throws AjoutTacheException {
        Tache racine = new Tache("racine", "13/12/2023", 2, "Bonjour", 1);
        Tache enfant1 = new Tache("enfant1", "13/12/2023", 2, "Bonjour", 1);
        Tache enfant2 = new Tache("enfant2", "13/12/2023", 2, "Bonjour", 1);
        Tache enfant3 = new Tache("enfant3", "13/12/2023", 2, "Bonjour", 1);
        enfant1.setParent(racine);
        enfant2.setParent(racine);
        enfant3.setParent(enfant1);
        m.ajouterTache(racine);
        m.ajouterTache(enfant1);
        m.ajouterTache(enfant2);
        m.ajouterTache(enfant3);

        //Test pour l'archivage des taches parentes et enfants
        m.archiverTache(racine);
        assertEquals(Tache.ETAT_ARCHIVE, racine.getEtat(), "La racine devrait etre archivée");
        assertEquals(Tache.ETAT_PARENT_ARCHIVE, enfant1.getEtat(), "L'enfant1 devrait etre archivée");
        assertEquals(Tache.ETAT_PARENT_ARCHIVE, enfant2.getEtat(), "L'enfant2 devrait etre archivée");
        assertEquals(Tache.ETAT_PARENT_ARCHIVE, enfant3.getEtat(), "L'enfant3 devrait etre archivée");

        //Test pour le desarchivage des taches parentes et enfants
        m.desarchiverTache(racine);
        assertEquals(Tache.ETAT_INITIAL, racine.getEtat(), "La racine devrait etre desarchivée");
        assertEquals(Tache.ETAT_INITIAL, enfant1.getEtat(), "L'enfant1 devrait etre desarchivée");
        assertEquals(Tache.ETAT_INITIAL, enfant2.getEtat(), "L'enfant2 devrait etre desarchivée");
        assertEquals(Tache.ETAT_INITIAL, enfant3.getEtat(), "L'enfant3 devrait etre desarchivée");
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

    /**
     * vérification du déplacement d'une tache
     * @throws AjoutTacheException
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
     * Vérification du changement d'antériorité lors du déplacement
     * d'une tâche dans une tâche dont le parent
     * est antécédent de la tâche déplacé
     * @throws AjoutTacheException
     */
    @Test
    public void deplacerTacheParentAntecedent() throws AjoutTacheException{
        Tache t = new Tache("tache1", "13/12/2023", 2, "enfant", 1);
        Tache t2 = new Tache("tache2", "13/12/2023", 2, "parent", 1);
        Tache t3 = new Tache("tache3", "13/12/2023", 2, "enfant2", 1);

        m.ajouterTache(t);
        t2.setParent(t);
        t2.setAntecedant(t3);
        m.ajouterTache(t2);
        t3.setParent(t);
        m.ajouterTache(t3);
        m.deplacerTache(t3, t2);
        assertEquals(t2, t3.getParent(), "la tâche t3 devrait avoir la tâche t comme parent");
        assertNull(t2.getAntecedant(), "la tâche t2 ne devrait plus avoir d'antécédent");
    }

    /**
     * Vérification du changement d'antériorité lors du déplacement d'une tâche
     * dans une tâche dont l'un des parent
     * est antécédent de la tâche déplacé
     * @throws AjoutTacheException
     */
    @Test
    public void deplacerTacheParentDeParentAntecedent() throws AjoutTacheException{
        Tache t = new Tache("tache1", "13/12/2023", 2, "enfant", 1);
        Tache t2 = new Tache("tache2", "13/12/2023", 2, "parent", 1);
        Tache t3 = new Tache("tache3", "13/12/2023", 2, "enfant2", 1);
        Tache t4 = new Tache("tache4", "14/12/2023", 2, "enfant3", 1);
        m.ajouterTache(t);

        t4.setParent(t);
        t4.setAntecedant(t3);
        m.ajouterTache(t4);

        t2.setParent(t4);
        m.ajouterTache(t2);

        t3.setParent(t);
        m.ajouterTache(t3);
        m.deplacerTache(t3, t2);
        assertEquals(t2, t3.getParent(), "la tâche t3 devrait avoir la tâche t comme parent");
        assertNull(t4.getAntecedant(), "la tâche 4 ne devrait plus avoir d'antécédent");
    }
    /**
     * vérification de l'ajout d'une tache
     * Pour cela il faut que la tache soi la première tache (racine) ou bien qu'elle ai un parent pour pouvoir etre ajoutée
     * @throws AjoutTacheException
     */
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
        Tache racine = new Tache("racine", "13/12/2023", 2, "Bonjour", 1);
        //ajout racine
        m.ajouterTache(racine);
        this.m.sauvegarder("./save/test.trellol");
        assertEquals(this.m, Modele.charger("./save/test.trellol"), "le modèle devrait être correctement chargé");
    }
}