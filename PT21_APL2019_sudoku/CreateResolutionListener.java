import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFileChooser;


/**
 * La classe <code>CreateResolutionListener</code> est un <code>ActionListener</code>
 * associé au bouton "Résoudre une grille" du menu principal. Elle permet de changer 
 * d'écran.
 */
public class CreateResolutionListener implements ActionListener {
    
    /**
     * La fenêtre de jeu dans laquelle mettre l'écran créé.
     */
    MainFrame frame;

    /**
     * Constructeur qui associe cet objet à la fenêtre à modifier.
     * 
     * @param frame la fenêtre à modifier.
     */
    public CreateResolutionListener(MainFrame frame) {
        this.frame = frame;
    }

    /**
     * Demande à l'utilisateur un fichier contenant une grille à charger.
     * Crée un nouveau modèle de jeu représentant une résolution de cette grille.
     * Change l'écran de la fenêtre pour y afficher un nouvel écran de résolution.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Grille g = null;

        int returnValue = GrilleIO.fileChooser.showOpenDialog(frame);
        if(returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                g = GrilleIO.chargerGrille(GrilleIO.fileChooser.getSelectedFile());
            } catch (IOException exception) { 
                System.err.println("Erreur durant le chargement du fichier.");
                return;
             }
        } 
        if(g == null) return;

        Resolution c = new Resolution(g);
        ResolutionScreen screen = new ResolutionScreen(c, this.frame);
        this.frame.setScreen(screen);
        this.frame.setTitle(MainFrame.RESOLUTION_TITLE);
        this.frame.repaint();
        this.frame.revalidate();
    }
}