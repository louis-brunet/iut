import javax.swing.JPanel;

import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Color;

import java.awt.event.MouseListener;

/**
 * La classe GrillePanel est utilisée pour représenter les données contenues dans une 
 * Grille.
 * 
 * @author Thibault Barbieri, Louis Brunet
 */
public class GrillePanel extends JPanel implements ObserverView {
	/**
	 * Constante représentant la largeur en pixels de la bordure entre chaque cellule.
	 */
	private static final int SMALL_BORDER = 4;
	/**
	 * Constante représentant la largeur en pixels de la bordure entre chaque groupe de 9
	 * cellules.
	 */
	private static final int LARGE_BORDER = 6;
	/**
	 * Constante représentant la couleur des bordures larges de la grille.
	 */
	private static final Color BIG_BORDER_COLOR = new Color(0x3e5a70);
	/**
	 * Constante représentant la couleur des bordures dans les sous-gorupes de la grille.
	 */
	private static final Color SMALL_BORDER_COLOR = new Color(0x537895);

	/**
	 * Le modèle associé à ce composant contenant les données d'une grille de jeu.
	 */
	private final Grille grille;

	/**
	 * Les composants associés à chaque cellule de la grille modèle. 
	 */
	private CellulePanel[][] cellulePanels;

	/**
	 * Constructeur qui initialise ce composant avec les données de la Grille modèle 
	 * donnée en argument.
	 * 
	 * @param modele la Grille associée à ce composant.
	 */
	public GrillePanel(Grille modele) {
		this(modele, null);
	}

	/**
	 * Constructeur qui initialise ce composant avec les données de la Grille modèle 
	 * donnée en argument, et qui ajoute à chaque cellule le MouseListener fourni (s'il 
	 * n'est pas null).
	 * 
	 * @param modele la Grille associée à ce composant.
	 * @param mouseListener le MouseListener à associer à chaque cellule.
	 */
	public GrillePanel(Grille modele, MouseListener mouseListener) {
        this.grille = modele;
        this.grille.addObserver(this);

		this.setBackground(GrillePanel.BIG_BORDER_COLOR);
		this.initCellPanels(mouseListener);
	}

	/**
	 * Initialise et ajoute à ce panneau neufs sous-grilles contenant chacune 9 composants
	 * qui correspondent chacun à des cellules de la grille.
	 */
	private void initCellPanels(MouseListener mouseListener) {
		this.cellulePanels = new CellulePanel[9][9];
		LayoutManager layout = new GridLayout(3, 3, 
                                              GrillePanel.LARGE_BORDER, 
                                              GrillePanel.LARGE_BORDER);
		this.setLayout(layout);

		Position posMin, posMax;
		for (int i = 0; i < 9; i += 3) {
			for (int j = 0; j < 9; j += 3) {
				posMin = new Position(i, j);
				posMax = new Position(i + 2, j + 2);
				JPanel subPanel = createSubPanel(posMin,
                                                 posMax,
                                                 mouseListener);
				this.add(subPanel);
			}
		}
	}

	/**
	 * Initialise et renvoie un JPanel avec les cellules dans l'intervale
	 * donné de la grille modèle.  
	 * 
	 * @param posMin la position en haut à gauche de l'intervale de cellules à considérer.
	 * @param posMax la position en bas à droite de l'intervale de cellules à considérer.
	 * @return le JPanel créé.
	 */
	private JPanel createSubPanel(Position posMin, Position posMax, MouseListener mouseListener) {
		int lineCount = posMax.ligne - posMin.ligne + 1;
		int columnCount = posMax.colonne - posMin.colonne + 1;
		LayoutManager layout = new GridLayout(lineCount, 
                                              columnCount,
                                              GrillePanel.SMALL_BORDER, 
                                              GrillePanel.SMALL_BORDER);
		JPanel panel = new JPanel(layout);
		panel.setBackground(GrillePanel.SMALL_BORDER_COLOR);

		Cellule[][] cellules = this.grille.getCellules();

		for (int i = posMin.ligne; i <= posMax.ligne; i++) {
			for (int j = posMin.colonne; j <= posMax.colonne; j++) {
				Cellule modele = cellules[i][j];

				this.cellulePanels[i][j] = new CellulePanel(modele);
				this.cellulePanels[i][j].addMouseListener(mouseListener);  
				panel.add(this.cellulePanels[i][j]);
			}
		}

		return panel;
	}

	/**
	 * Met à jour l'affichage de chaque cellule de cette grille.  
	 */
    @Override
    public void updateView() {
		for (int i = 0; i < this.cellulePanels.length; i++) {
			for (int j = 0; j < this.cellulePanels[0].length; j++) {
				this.cellulePanels[i][j].updateView();
			}
        }
        this.repaint();
    }

    /**
     * @return la grille que ce composanta pour modèle. 
     */
    public Grille getGrille() {
        return this.grille;
    }
}