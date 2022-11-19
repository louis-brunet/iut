import javax.swing.JButton;

import java.awt.Color;

/**
 * La classe <code>ResolutionButtonPanel</code> est une implémentation de 
 * <code>ButtonPanel</code>. Elle y ajoute un bouton pour résoudre la grille
 * automatiquement, et un bouton pour basculer le mode "doute" sur la cellule 
 * sélectionnée.
 */
public class ResolutionButtonPanel extends ButtonPanel {
    /**
     * Constructeur qui initialise ce <code>ButtonPanel</code> avec le modèle et la 
     * fenêtre donnés. 
     * Y sont ajoutés un bouton pour résoudre la grille automatiquement, et un bouton pour
     * basculer le mode "doute" sur la cellule sélectionnée.
     * 
     * @param modele <code>GameModel</code> à associer aux interactions des boutons.
     * @param container la fenêtre de jeu.
     */
    public ResolutionButtonPanel(Resolution modele, MainFrame container) {
        super(modele, container);
        JButton button;

        button = this.addButton("Resolution automatique",
                                new ResoudreGrilleListener(modele, container));
        button.setToolTipText("Essayer de trouver une solution a cette grille.");
        Color incertitudeHoveredColor = new Color(0xd6baa4);
        Color incertitudeTextColor = new Color(0x725742);
        button = this.addButton("Incertitude", 
                                new IncertitudeButtonListener(modele),
                                new ColorHoverListener(CellulePanel.DOUTE_COLOR, 
                                                       incertitudeHoveredColor, 
                                                       incertitudeTextColor, 
                                                       CellulePanel.DOUTE_COLOR));
        
        button.setToolTipText("<html>Basculer le mode incertitude sur la cellule selectionnee."
                              +"<br>Aucun effet si aucune cellule n'est selectionnee.</html>");
    }
}