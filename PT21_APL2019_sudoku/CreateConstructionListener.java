import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * La classe <code>CreateConstructionListener</code> est un <code>ActionListener</code>
 * associé au bouton "Construire une grille" du menu principal. Elle permet de changer 
 * d'écran.
 */
public class CreateConstructionListener implements ActionListener {

    /**
     * La fenêtre de jeu dans laquelle mettre l'écran créé.
     */
    MainFrame frame;

    /**
     * Constructeur qui associe cet objet à la fenêtre à modifier.
     * 
     * @param frame la fenêtre à modifier.
     */
    public CreateConstructionListener(MainFrame frame) {
        this.frame = frame;
    }

    /**
     * Crée un nouveau modèle de jeu représentant une construction de grille.
     * Changer l'écran de la fenêtre pour y afficher un nouvel écran de construction.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Grille g = new Grille();
        Construction c = new Construction(g);
        ConstructionScreen screen = new ConstructionScreen(c, this.frame);
        this.frame.setScreen(screen);
        this.frame.setTitle(MainFrame.CONSTRUCTION_TITLE);
        this.frame.repaint();
        this.frame.revalidate();
    }
}