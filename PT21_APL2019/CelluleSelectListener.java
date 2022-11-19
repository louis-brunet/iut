import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * La classe <code>CelluleSelectListener</code> est un <code>MouseListener</code> associé
 * aux cellules de la grille. Il permet de signaler au <code>GameModel</code> quelle 
 * cellule a été sélectionnée.
 */
public class CelluleSelectListener extends MouseAdapter {
	/**
	 * Le modèle de jeu à notifier quand une cellule est cliquée.
	 */
	private GameModel modele;

	/**
	 * Constructeur qui associe ce <code>CelluleSelectListener</code> au modèle de jeu 
	 * donné.
	 * 
	 * @param model le modèle de jeu à notifier quand une cellule est cliquée
	 */
	public CelluleSelectListener(GameModel model) {
		this.modele = model;
	}

	/**
	 * Notifier le modèle de jeu qu'une cellule a été cliquée, la séléctionner.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		Cellule clicked = ((CellulePanel) e.getSource()).getCellule();
		
		this.modele.select(clicked);
	}
}