package trellol.trellol.Modele;

import trellol.trellol.Vues.Observateur;

/**
 * Interface devant être implémenté par chaque modèle de l'application,
 * car elle permet une gestion conforme au patron MVC des observateurs
 */
public interface Sujet {
        /**
         * Ajoute un observateur au modèle
         */
        public void enregistrerObservateur(Observateur o);
        /**
         * Supprime un observateur au modèle
         */
        public void supprimerObservateur(Observateur o);
        /**
         * Informe tous les observateurs du modèle des
         * modifications
         */
        public void notifierObservateurs();
}
