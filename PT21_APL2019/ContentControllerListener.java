import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * La classe <code>ContentControllerListener</code> est un <code>ActionListener</code>
 * qui lie un <code>ContentControllerPanel</code> à un <code>GameModel</code>.
 */
public class ContentControllerListener implements ActionListener {
	/**
	 * Le modèle de jeu sur lequel effectuer les modifications.
	 */
	protected GameModel modele;
	/**
	 * Le <code>ContentControllerPanel</code> contenant les boutons pour modifier les 
	 * contenus d'une celule.
	 */
    protected ContentControllerPanel panel;

	/**
	 * Constructeur qui associe le modèle de jeu donné au <code>ContentControllerPanel</code>.
	 */
	public ContentControllerListener(GameModel modele, 
									 ContentControllerPanel contentControllerPanel) {
        this.modele = modele;
        this.panel = contentControllerPanel;
    }

    /**
	 * Si le bouton cliqué est actif, indiquer au modèle de jeu qu'un contenu est en cours
	 * de modification par l'utilisateur.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		boolean isActive = ((ContentControllerButton) e.getSource()).isActive();
		if(!isActive)
			return;

		int chiffre = Integer.parseInt(e.getActionCommand());
		this.modele.setCelluleContenu(chiffre);
		this.panel.update();
	}

}