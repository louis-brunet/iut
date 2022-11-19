import java.awt.BorderLayout;

import javax.swing.JOptionPane;

/**
 * La classe <code>ResolutionScreen</code> est une implémentation d'un 
 * <code>GameScreen</code>. Le <code>ContentControllerPanel</code> associé est un 
 * <code>NumPad</code>. Le <code>ButtonPanel</code> associé à son <code>SidePanel</code>
 * est un <code>ResolutionButtonPanel</code>.
 */
public class ResolutionScreen extends GameScreen {

    /**
     * Constructeur qui crée écran pour afficher le modèle de jeu donné dans la fenêtre 
     * donnée.
     *  
     * @param modele le modèle de jeu à afficher.
     * @param container la fenêtre où l'afficher.
     */
	public ResolutionScreen(GameModel modele, MainFrame container) {
        super(modele, container);
    }

    /**
     * Initialiser les particularités du <code>SidePanel</code> de ce 
     * <code>GameScreen</code>.
     *  
     * @param modele le modèle de jeu à afficher.
     * @param container la fenêtre où l'afficher.
     */
    @Override
    protected void initSidePanel(GameModel modele, MainFrame container) {
        ContentControllerPanel controllerPanel = new NumPad(modele);
        ButtonPanel buttonPanel = new ResolutionButtonPanel((Resolution) modele, 
                                                            container);
        this.sidePanel = new SidePanel(controllerPanel, buttonPanel);		
        this.add(this.sidePanel, BorderLayout.WEST);
    }

    /**
     * Quand cet <code>ObserverView</code> est notifié, tester si la grille a été résolue
     * manuellement depuis la dernière notifcation. Si oui, féliciter le joueur.
     */
    @Override
    public void updateView() {
        super.updateView();
        Resolution resolution = (Resolution) this.modele;
        boolean wasSolved = resolution.isSolved();

        if( resolution.updateIsSolved() 
                && !wasSolved ) {
            JOptionPane.showMessageDialog(this, 
                                          "Bravo, vous avez resolu cette grille !",
                                          "Felicitations", 
                                          JOptionPane.PLAIN_MESSAGE);
        }
    }
    
}


