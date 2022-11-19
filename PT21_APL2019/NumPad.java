import javax.swing.JPanel;

import java.awt.GridLayout;

/**
 * La classe <code>NumPad</code> est une implémentation de <code>ContentControllerPanel</code>,
 * ce qui signifie que c'est un controlleur qui permet de modifier le contenu de la 
 * cellule sélectionnée par l'utilisateur. Cette classe apporte des informations 
 * d'affichage propres à ce type de <code>ContentControllerPanel</code>.
 */
public class NumPad extends ContentControllerPanel {
	/**
	 * Constante représentant l'espace en pixels à laisser entre chaque bouton. 
	 */
    private static final int MARGIN = 10;

	/**
	 * Constructeur qui intialise ce panneau pour pouvoir modifier le contenu de la 
	 * cellule sélectionnée par le joueur.
	 * 
	 * @param modele le modèle de jeu à modifier lorsqu'un bouton est activé.
	 */
	public NumPad(GameModel modele) {
        super(modele);

        this.setLayout(new GridLayout(4, 3,
                                      NumPad.MARGIN, NumPad.MARGIN));

		this.initNumberButtons();
		this.initEmptyButton();
	}

	/**
	 * Initialise les boutons servant à ajouter/changer le contenu d'une cellule. 
	 */
	private void initNumberButtons() {
		this.numberButtons = new NumPadButton[9];

		for (int i = 0; i < numberButtons.length; i++) {
			String btnName = "" + (i + 1);
			this.numberButtons[i] = new NumPadButton(btnName);
			this.numberButtons[i].setActionCommand(btnName);
			this.numberButtons[i].addActionListener(this.addContenuListener);
			this.add(this.numberButtons[i]);
		}
	}

	/**
	 * Initialise le bouton servant à retirer le contenu d'une cellule.
	 */
	private void initEmptyButton() {
		this.emptyButton = new NumPadButton("X");
		this.emptyButton.setActionCommand("0");
		this.emptyButton.addActionListener(this.addContenuListener);
		this.add(NumPad.emptyPanel());
		this.add(this.emptyButton);
		this.add(NumPad.emptyPanel());
    }
	
	/**
	 * Renvoie un <code>JPanel</code> vide et transparent, utilisé pour l'espacement du 
	 * bouton qui sert à vider la cellule.
	 * 
	 * @return un <code>JPanel</code> vide et transparent
	 */
    private static JPanel emptyPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        return panel;
    }


} 