package trellol.trellol.Exceptions;

/**
 * Classe AjoutTacheException héritant de la classe Exception,
 * lancé pour un problème lors de l'ajout ou la modification d'une tache
 */
public class AjoutTacheException extends Exception{
    public AjoutTacheException(String msg){
        super(msg);
    }
}
