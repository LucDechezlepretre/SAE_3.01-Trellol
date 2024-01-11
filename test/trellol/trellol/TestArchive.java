package trellol.trellol;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trellol.trellol.Exceptions.AjoutTacheException;
import trellol.trellol.Modele.Modele;

import static org.junit.jupiter.api.Assertions.*;

class TestArchive {

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
}