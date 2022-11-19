import javax.swing.JPanel;

import java.awt.BorderLayout;

/**
 * La classe <code>SidePanel</code> représente le composant d'un <code>GameScreen</code>
 * qui permet d'effectuer des modifications sur la grille, ou de revenir au menu principal.
 * Ce panneau est constitué d'un 
 */
public class SidePanel extends JPanel {
	/**
	 * Composant capable de communiquer avec un <code>GameModel</code> pour effectuer 
	 * les modifications de contenu sur la cellule sélectionnée.
	 */
	private ContentControllerPanel controllerPanel;
	/**
	 * Composant contenant des boutons qui peuvent modifier un <code>GameModel</code> et
	 * un bouton permettant de revenir au menu principal.
	 */
    private ButtonPanel buttonPanel;

	/**
	 * Constructeur qui associe le <code>ContentControllerPanel</code> et le 
	 * <code>ButtonPanel</code> donnés à ce <code>SidePanel</code>.
	 * 
	 * @param controllerPanel le <code>ContentControllerPanel</code> à mettre dans ce 
	 * 						  composant.
	 * @param buttonPanel le <code>ButtonPanel</code> à mettre dans ce composant.
	 */
	public SidePanel(ContentControllerPanel controllerPanel, ButtonPanel buttonPanel) {
		this.controllerPanel = controllerPanel;
		this.buttonPanel = buttonPanel;
        
        this.setBackground(MainFrame.BACKGROUND_COLOR);

		this.setLayout(new BorderLayout());
		this.add(this.controllerPanel, BorderLayout.SOUTH);
		this.add(this.buttonPanel, BorderLayout.NORTH);
    }

	/**
	 * Mettre à jour le <code>ContentControllerPanel</code>.
	 * Utilisé pour mettre en valeur les boutons correspondant au contenu de la cellule
	 * sélectionnée.
	 */
	public void update() {
		this.controllerPanel.update();
		this.repaint();
	}
}