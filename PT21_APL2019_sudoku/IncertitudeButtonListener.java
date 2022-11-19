import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * La classe <code>IncertitudeButtonListener</code> est associée au bouton "Incertitude"
 * de l'écran de résolution. Elle permet de basculer le mode incertitude sur la cellule
 * sélectionnée.
 */
public class IncertitudeButtonListener implements ActionListener {

    /**
     * Le modèle de jeu associé au controlleur.
     */
    private Resolution modele;


    /**
     * Associe ce controlleur au modèle de jeu donné.
     * 
     * @param modele le modèle de jeu à associer à ce controlleur
     */
    public IncertitudeButtonListener(Resolution modele) {
        this.modele = modele;
    }

    /**
     * Si une cellule est séléctionnée par l'utilisateur, basculer le mode incertitude.
     * Effacer le contenu certain de la cellule.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Cellule c = modele.getCelluleSelectionnee();
        if(c == null)
            return;
        if(c.getStatus() == Cellule.Status.SUR) {
            modele.setCelluleContenu(0);
        }
        modele.toggleCelluleDoute();
    }

}