import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * La classe <code>OpenMenuListener</code> est un <code>ActionListener</code>
 * associé aux boutons "Retour au menu" des écrans de jeu. Elle permet de revenir à 
 * l'écran du menu principal.
 */
public class OpenMenuListener implements ActionListener {
    /**
     * La fenêtre principale de jeu, dont l'écran va changer.
     */
    private MainFrame frame;

    /**
     * Constructeur qui initialise ce controlleur pour pouvoir changer l'écran de la
     * fenêtre donnée.
     * 
     * @param frame La fenêtre principale de jeu, dont l'écran va changer.
     */
    public OpenMenuListener(MainFrame frame) {
        this.frame = frame;
    }

    /**
     * Changer l'écran de la fenêtre à l'écran de menu principal.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.frame.setScreen(new MainMenuScreen(this.frame));
        this.frame.setTitle(MainFrame.DEFAULT_TITLE);
        this.frame.repaint();
        this.frame.revalidate();
    }

}