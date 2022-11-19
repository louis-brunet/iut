/**
 * La classe Construction gère les opérations de construction d'une grille par un         
 * utilisateur. 
 * 
 * @author Louis Brunet
 */
public class Construction  extends GameModel {
	/**
	 * Initialise cet objet selon le constructeur de <code>GameModel</code>.
	 * 
	 * @param g la grille à associer aux intéractions avec l'utilisateur.
	 */
	public Construction(Grille g) {
		super(g);
	}

	/**
	 * Réinitialise toutes les cellules de la grille.
	 */
	public void reset() {
        this.getGrille().initCellules();
        this.notifyObservers();
	}
}