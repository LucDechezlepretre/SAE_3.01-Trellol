package trellol.trellol;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trellol.trellol.Exceptions.AjoutTacheException;
import trellol.trellol.Modele.Modele;

import static org.junit.jupiter.api.Assertions.*;

public class TestDeplacerTache {
    Modele m;
    @BeforeEach
    void setUp() {
        m = new Modele();
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
}
