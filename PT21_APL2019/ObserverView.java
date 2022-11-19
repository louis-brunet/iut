
/**
 * L'interface <code>ObserverView</code> est à utiliser avec la classe abstraite 
 * <code>ObservableModel</code>. Elle représente un observeur qui doit être notifié à 
 * chaque changement d'une donnée de son modèle à afficher.
 */
public interface ObserverView {

    /**
     * Mettre à jour l'affichage des données du modèle.
     */
    public void updateView();
}