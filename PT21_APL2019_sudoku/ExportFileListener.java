import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

/**
 * La classe <code>ExportFileListener</code> est un <code>ActionListener</code>
 * associé au bouton "Exporter cette grille" de l'écran de construction. Elle permet de 
 * sauvegarder une grille dans un fichier choisi par l'utilisateur.
 */
public class ExportFileListener implements ActionListener {
    /**
     * Le modèle de jeu contenant la grille à exporter.
     */
    private GameModel modele;
    /**
     * La fenêtre relative à laquelle afficher le dialoque de sauvegarde.
     */
    private MainFrame frame;

    /**
     * Associe le modèle de jeu et la fen^tre donnés à ce controlleur.
     */
    public ExportFileListener(GameModel modele, MainFrame frame) {
        this.modele = modele;
        this.frame = frame;
    }
    
    /**
     * Demande à l'utililsateur un fichier où sauvegarder la grille.
     * Essayer de la sauvegarder dedans.
     */
	@Override
	public void actionPerformed(ActionEvent e) {
        JFileChooser fc = GrilleIO.fileChooser;

        int returnValue = fc.showSaveDialog(frame);

        if(returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            Grille modeleGrille = this.modele.getGrille(); 
            try {
                GrilleIO.sauvegarderGrille(modeleGrille, file);
            } catch (IOException exception) {
                System.err.println("Error writing to file " + file.getName());
            }
        }   
	}

}