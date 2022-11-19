import java.awt.BorderLayout;

/**
 * La classe <code>ConstructionScreen</code> est une implémentation d'un 
 * <code>GameScreen</code>. Le <code>ContentControllerPanel</code> associé est un 
 * <code>NumPad</code>. Le <code>ButtonPanel</code> associé est un 
 * <code>ConstructionButtonPanel</code>.
 * 
 * @author Louis Brunet
 */
public class ConstructionScreen extends GameScreen {

    /**
     * Constructeur qui crée écran pour afficher le modèle de jeu donné dans la fenêtre 
     * donnée.
     *  
     * @param modele le modèle de jeu à afficher.
     * @param container la fenêtre où l'afficher.
     */
	public ConstructionScreen(Construction modele, MainFrame container) {
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
        ButtonPanel buttonPanel = new ConstructionButtonPanel((Construction) modele, container);
        this.sidePanel = new SidePanel(controllerPanel, buttonPanel);
        		
        this.add(this.sidePanel, BorderLayout.EAST);
    }
}