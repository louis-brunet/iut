import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JPanel;

/**
 * La classe abstraite <code>ContentControllerPanel</code> représente un panneau servant 
 * à changer le contenu de la cellule sélectionnée d'un <code>GameModel</code>, à l'aide 
 * de ces boutons.
 */
public abstract class ContentControllerPanel extends JPanel {
    /**
	 * Le modèle de jeu à modifier lorsqu'un bouton est cliqué.
	 */
	protected GameModel modele;
	/**
	 * Les boutons cliquables par l'utilisateur pour ajouter un contenu à la cellule 
	 * sélectionnée.
	 */
	protected ContentControllerButton[] numberButtons;
	/**
	 * Bouton servant à vider la cellule sélectionnée.
	 */
	protected ContentControllerButton emptyButton;
	/**
	 * Le controlleur associé à un clic de bouton.
	 */
	protected ContentControllerListener addContenuListener;

	/**
	 * Constructeur qui initialise ce panneau pour pouvoir modifier le 
	 * <code>GameModel</code> donné.
	 * 
	 * @param modele le modèle de jeu à associer aux interactions avec l'utilisateur. 
	 */
    public ContentControllerPanel(GameModel modele) {
        this.modele = modele;
        this.addContenuListener = new ContentControllerListener(modele, this);
        
        this.setBackground(MainFrame.BACKGROUND_COLOR);
    }

	/** 
	 * Mettre à jour les boutons surlignés selon le contenu de la cellule sélectionnée.
	 */
	public void update() {
		if(this.modele.isCelluleModifiable()) {
			this.setActive(true);
			
			switch (this.modele.getCelluleSelectionnee().getStatus()) {
				case SUR:
					this.highlightContenu();
					break;
				
				case DOUTE:
					this.highlightContenuDoute();
					break;
			
				case DEFINITIF:
				default:
					this.clearHighlights();
					break;
			}
		} else {
			this.setActive(false);
		}

		this.repaint();
	}

	/**
	 * Mettre en valeur les boutons correspondant au contneu "certain" de la cellule 
	 * sélectionnée.
	 */
    private void highlightContenu() {
		int contenu = this.modele.getCelluleSelectionnee().getContenu();

		for (int i = 1; i <= this.numberButtons.length; i++) {
			
			if(i == contenu) {
				this.numberButtons[i - 1].setIsCellContent(true);
			} else {
				this.numberButtons[i - 1].setIsCellContent(false);
			}
		}
	}


	/**
	 * Mettre en valeur les boutons correspondant au contneu "incertain" de la cellule 
	 * sélectionnée.
	 */
    private void highlightContenuDoute() {
        int[] contenuDoute = this.modele.getCelluleSelectionnee().getContenuDoute();
		// Convertir le tableau en une List pour utiliser la fonction contains(Integer i) 
		// de List<Integer>.
		List<Integer> listeDoute = Arrays.stream(contenuDoute)
										 .boxed()
										 .collect(Collectors.toList());

		for (int i = 1; i <= this.numberButtons.length; i++) {
			if(listeDoute.contains((Integer) i)) { 
				this.numberButtons[i - 1].setIsCellContent(true);
			}else {
				this.numberButtons[i - 1].setIsCellContent(false);
			}
		}
    }

	/**
	 * Enlever toute surbrillance sur les boutons de ce panneau.
	 */
    private void clearHighlights() {
		for (int i = 0; i < this.numberButtons.length; i++) {
			this.numberButtons[i].setIsCellContent(false);
		}
	}

	/**
	 * Activer ou désactiver tous les boutons de ce panneau.
	 * @param bool <code>true</code> si les boutons doivent être actifs, 
	 * <code>false</code> sinon.
	 */
	private void setActive(boolean bool) {
		for (int i = 0; i < this.numberButtons.length; i++) {
			this.numberButtons[i].setActive(bool);
		}
		this.emptyButton.setActive(bool);
	}

}