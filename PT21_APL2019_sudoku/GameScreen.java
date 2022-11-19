import javax.swing.JPanel;

import java.awt.BorderLayout;

import java.awt.event.MouseListener;

/**
 * La classe abstraite <code>GameScreen</code> sert à afficher un <code>GameModel</code>.
 * L'écran est constitué de l'affichage d'une grille, et d'un <code>SidePanel</code> qui
 * contient des boutons pour interagir avec l'utilisateur.
 */
public abstract class GameScreen extends JPanel implements ObserverView {
    /**
     * Le panneau d'affichage de la grille de jeu.
     */
    private GrillePanel grillePanel;
    /**
     * Le modèle de jeu modélisant les interactions possibles.
     */
    protected GameModel modele;
    /**
     * Le panneau contenant les boutons pour changer les contenus d'une cellule, ainsi que
     * mes autres boutons.
     */
    protected SidePanel sidePanel;
    /**
     * Le <code>MouseListener</code> à associé aux cellules de la grille.
     */
    private MouseListener celluleClickListener;

    /**
     * Constructeu qui init
     * @param modele
     * @param container
     */
	public GameScreen(GameModel modele, MainFrame container) {
        this.celluleClickListener = new CelluleSelectListener(modele);

		this.init(modele, container);   
    }
    
    /**
     * Le <code>SidePanel</code> n'est pas initialisé par <code>GameScreen</code>, c'est
     * aux classes implémentantes de le faire dans <code>initSidePanel()</code>;
     *  
     * @param modele le modèle de jeu assoicé à la partie
     * @param container la fenêtre de jeu.
     */
    private void init(GameModel modele, MainFrame container) {
        this.setLayout(new BorderLayout());

        this.sidePanel = null;
        this.modele = modele;
        this.modele.addObserver(this);
        
        this.grillePanel = new GrillePanel(modele.getGrille(), this.celluleClickListener);
        
        this.add(this.grillePanel, BorderLayout.CENTER);

        this.initSidePanel(modele, container);
    }

    /**
     * A implémenter dans les sous-classes.
     * Initialise le <code>SidePanel</code> de cet écran.
     */
    protected abstract void initSidePanel(GameModel modele, MainFrame container);

    /**
     * Met à jour l'affichage du odèle de jeu.
     */
    @Override
	public void updateView() {
        // Créer un nouveau panneau pour représenter la grille de jeu si elle a été 
        // réinitialisée dans le modèle de jeu.
        Grille grilleModele = this.modele.getGrille();
        boolean createGrille = ! this.grillePanel.getGrille().equals(grilleModele);

        if( createGrille ) {
            this.remove(this.grillePanel);
            this.grillePanel = new GrillePanel(grilleModele, this.celluleClickListener);
            this.add(this.grillePanel, BorderLayout.CENTER);
        }
    
        // Faire les mises à jour
		this.grillePanel.updateView();  
        if(this.sidePanel != null)
            this.sidePanel.update();

        this.repaint();
        if( createGrille )
            this.revalidate();
    }
    
    /**
     * Renvoie le <code>GrillePanel</code> affiché par cet écran.
     * @return le <code>GrillePanel</code> affiché par cet écran.
     */
    public GrillePanel getGrillePanel() {
        return this.grillePanel;
    }

}