import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

/**
 * La classe <code>ImportFileListener</code> est un <code>ActionListener</code>
 * associé aux boutons "Importer une grille" des écrans de jeu. Elle permet de 
 * charger une grille depuis un fichier choisi par l'utilisateur.
 */
public class ImportFileListener implements ActionListener {
     /**
     * Le modèle de jeu contenant la grille à exporter.
     */
    private GameModel modele;
    
    /**
     * La fenêtre relative à laquelle afficher le dialoque d'ouverture.
     */
    private MainFrame frame;

    /**
     * Associe le modèle de jeu et la fen^tre donnés à ce controlleur.
     */ 
    public ImportFileListener(GameModel modele, MainFrame frame) {
        this.modele = modele;
        this.frame = frame;
    }
    
	/**
     * Demande à l'utililsateur un fichier depuis lequel charger la grille.
     * Essayer de la charger.
     */
	@Override
	public void actionPerformed(ActionEvent e) {
        JFileChooser fc = GrilleIO.fileChooser;

        int returnValue = fc.showOpenDialog(frame);

        if(returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
                Grille fileGrille = GrilleIO.chargerGrille(file);
                this.modele.setGrille(fileGrille);
            } catch (IOException exception) {
                System.err.println("Error loading file " + file.getName());
            }
        }   
	}

}