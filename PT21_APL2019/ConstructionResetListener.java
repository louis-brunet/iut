import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * La classe <code></code> est un <code></code> associé au bouton "Reinitialiser la 
 * grille".
 */
public class ConstructionResetListener implements ActionListener {
    /**
     * La modèle de jeu associé.
     */
    private Construction modele;
    /**
     * La fenêtre de jeu.
     */
    private MainFrame frame;

    /**
     * Constructeur qui associe ce controlleur à un modèle de jeu et une fenêtre.
     * 
     * @param modele le modèle de jeu à utiliser
     * @param frame la fenêtre de jeu
     */
    public ConstructionResetListener (Construction modele, MainFrame frame) {
        this.modele = modele;
        this.frame = frame;
    }

    /**
     * Réinitialiser le modèle de jeu. Afficher un nouveau <code>ConstructionScreen</code>.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(this.modele != null) {
            this.modele.reset();
            //this.modele.notifyObservers();
            this.frame.setScreen(new ConstructionScreen(this.modele, frame));
            this.frame.repaint();
            this.frame.revalidate();
        }
    }
}