package trellol.trellol.Modele;

import trellol.trellol.Vues.Observateur;

public interface Sujet {
        public void enregistrerObservateur(Observateur o);
        public void supprimerObservateur(Observateur o);
        public void notifierObservateurs();
}
