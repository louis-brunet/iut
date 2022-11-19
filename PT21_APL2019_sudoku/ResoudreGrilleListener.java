import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

/**
 * La classe <code>ResoudreGrilleListener</code> est associée au bouton "Resoudre 
 * automatiquement" de l'écran de résolution. Elle permet d'initier la résolution 
 * automatique de la grille par le modèle de jeu.
 */
public class ResoudreGrilleListener implements ActionListener {
    
    /**
     * Le modèle de jeu associé à ce controlleur.
     */
    private Resolution modele;
    /**
     * La fenêtre de jeu principale, utilisée pour placer les dialogues créés.
     */
    private MainFrame container;

    /**
     * Constructeur qui associe ce controlleur au modèle de jeu donné et à la fenêtre 
     * donnée.
     * 
     * @param modele le modèle de jeu à associer à ce controlleur
     * @param container la fenêtre à associer à ce controlleur
     */
    public ResoudreGrilleListener(Resolution modele, MainFrame container) {
        this.modele = modele;
        this.container = container;
    }

    /**
     * Lance la résolution automatique de la grille par le modèle de jeu.
     * Si une solution est trouvée, annoncer la durée de résolution,
     * sinon, annoncer que la grille n'a pas de solution.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        boolean solved = this.modele.resoudre();

        if(solved) {
            long resolutionTime = this.modele.getResolutionTime();
            float seconds = resolutionTime / 1000000000f;
            String timeString = String.format("%.6f secondes", seconds);
            JOptionPane.showMessageDialog(this.container, 
                                          "Cette grille a pris " + timeString 
                                          + " a resoudre.", 
                                          "Resolution", 
                                          JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this.container, 
                                         "Cette grille n'a pas de solution.", 
                                         "Pas de solution", 
                                         JOptionPane.ERROR_MESSAGE);
        }
    }

}

