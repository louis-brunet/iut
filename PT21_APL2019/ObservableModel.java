import java.util.ArrayList;
import java.util.List;

/**
 * La classe abstraite <code>ObservableModel</code> représente un modèle qui a une liste
 * d'observeurs et qui les notifie quand une donnée à afficher est changée. 
 * Elle est notamment implémentée par <code>Cellule</code>, <code>Grille</code>, et 
 * <code>GameModel</code>.
 */
public abstract class ObservableModel {

    /**
     * La liste d'observeurs à notifier après tout changement d'une donnée à afficher.  
     */
    private List<ObserverView> observers;

    /**
     * Constructeur qui initialise la liste des observeurs vide.
     */
    public ObservableModel() {
        this.observers = new ArrayList<ObserverView>();
    }

    /**
     * Ajouter un observeur à la liste.
     * 
     * @param observer l'observeur à ajouter.
     */
    public void addObserver(ObserverView observer) {
        if(!this.observers.contains(observer))
            this.observers.add(observer);
    }

    /**
     * Retirer un observeur de la liste de la liste des observeurs de cet objet.
     * 
     * @param observer l'observeur à retirer.
     */
    public void removeObserver(ObserverView observer) {
        this.observers.remove(observer);
    }

    /**
     * Notifier tous les observeurs de la liste qu'un changement de donnée a eu lieu.
     */
    public void notifyObservers() {
        for (ObserverView obs : this.observers) 
            obs.updateView();
    }
}