import javax.swing.JButton;

/**
 * La classe <code>ConstructionButtonPanel</code> est une implémentation de 
 * <code>ButtonPanel</code>. Elle y ajoute un bouton pour exporter la grille, et un bouton 
 * pour la réinitialiser.
 */
public class ConstructionButtonPanel extends ButtonPanel {
    /**
     * Constructeur qui initialise ce <code>ButtonPanel</code> avec le modèle et la 
     * fenêtre donnés. 
     * Y sont ajoutés un bouton pour exporter la grille et un bouton pour la 
     * réinitialiser.
     * 
     * @param modele <code>GameModel</code> à associer aux interactions des boutons.
     * @param container la fenêtre de jeu.
     */
    public ConstructionButtonPanel(Construction modele, MainFrame container) {
        super(modele, container);
        JButton exportButton = this.addButton("Exporter la grille", 
                                              new ExportFileListener(modele, container));
        exportButton.setToolTipText("Enregistrer cette grille dans un fichier.");
        this.addButton("Reinitialiser la grille", 
                       new ConstructionResetListener(modele, container));
    }
}