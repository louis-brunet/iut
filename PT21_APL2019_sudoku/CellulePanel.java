import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

/**
 * La classe CellulePanel est utilisée pour représenter les données contenues
 * dans une Cellule.
 * 
 * @author Louis Brunet
 */
public class CellulePanel extends JPanel implements ObserverView {
    /**
     * Constante représentant la hauteur de la Font utilisée pour le contenu
     * "certain" d'une cellule.
     */
    public static final float FONT_SIZE = 18.0f;
    /**
     * Constante resprésentant la couleur du contenu des cases non modifiables.
     */
    private static final Color DEFINITIF_COLOR = Color.BLACK;
    /**
     * Constante resprésentant la couleur du contenu des cases dont le joueur est
     * sûr.
     */
    private static final Color SUR_COLOR = Color.BLUE;
    /**
     * Constante resprésentant la couleur du contenu des cases dont le joueur est
     * incertain.
     */
    public static final Color DOUTE_COLOR = new Color(0xbe926f);//MainMenuScreen.BUTTON_BACKGROUND_COLOR;
    /**
     * Constante resprésentant la couleur du fond normale d'une case non
     * sélectionnée par le joueur.
     */
    private static final Color NORMAL_BACKGROUND_COLOR = Color.WHITE;
    /**
     * Constante resprésentant la couleur du fond de la case selectionnée par le
     * joueur.
     */
    private static final Color SELECTED_BACKGROUND_COLOR = new Color(210, 210, 210);
    /**
     * Constante représentant la police à utiliser pour l'affichage le contenu principal.
     */
    private static final Font NORMAL_FONT = new Font("Verdana", Font.PLAIN, 22);
    /**
     * Constante représentant la police à utiliser pour l'affichage le contenu incertain.
     */
    private static final Font DOUTE_FONT = new Font("Verdana", Font.ITALIC, 12);
    /**
     * La <code>Cellule</code> à prendre comme modèle.
     */
    private Cellule cellule;
    /**
     * Etiquette servant à afficher le contenu "sûr" du modèle.
     */
    private JLabel contenuLabel;
    /**
     * Panneau servant à contenir les etiquettes affichant le contenu "doute" du
     * modèle.
     */
    private JPanel doutePanel;
    /**
     * Tableau d'étiquettes servant à contenir les valeurs dont le joueur est
     * incertain.
     */
    private JLabel[] douteLabels;

    /**
     * Constructeur qui initialise ce composant avec les données du modèle donné en
     * argument.
     * 
     * @param modele la Cellule associée à ce composant.
     */
    public CellulePanel(Cellule modele) {
        this.setLayout(new BorderLayout());

        this.cellule = modele;
        this.cellule.addObserver(this);

        this.initContenuLabel();
        this.initDouteDisplay();

        this.updateView();
    }

    /**
     * Initialise les étiquettes servant à contenir le contenu sûr et définitif de
     * la Cellule.
     */
    private void initContenuLabel() {
        this.contenuLabel = new JLabel();
        this.contenuLabel.setHorizontalAlignment(JLabel.CENTER);

        this.contenuLabel.setFont(CellulePanel.NORMAL_FONT);
    }

    /**
     * Initialise les étiquettes servant à contenir le contenu incertain de la
     * Cellule, et le JPanel qui les contient.
     */
    private void initDouteDisplay() {
        this.douteLabels = new JLabel[Cellule.MAX_DOUTE];

        int nbLignes = (int) Math.ceil(Cellule.MAX_DOUTE / 2.0);
        this.doutePanel = new JPanel(new GridLayout(nbLignes, 2));
        this.doutePanel.setOpaque(false);
        this.doutePanel.setBorder(
            BorderFactory.createLineBorder(CellulePanel.DOUTE_COLOR, 3, false));

        for (int i = 0; i < douteLabels.length; i++) {
            douteLabels[i] = new JLabel();
            douteLabels[i].setHorizontalAlignment(JLabel.CENTER);
            douteLabels[i].setForeground(CellulePanel.DOUTE_COLOR);

            douteLabels[i].setFont(CellulePanel.DOUTE_FONT);
            this.doutePanel.add(douteLabels[i]);
        }
    }

    /**
     * Met à jour le contenu de ce composant selon de contenu et le status du
     * modèle. Méthode appelée par le modèle à tout changement d'informations à afficher.
     */
    @Override
    public void updateView() {
        this.updateContenu();
        this.refreshStatus();
        this.updateBackground();

        this.repaint();
    }

    /**
     * Dépendemment du status du modèle, afficher le paneau comprenant les valeurs
     * incertaines, ou seulement l'étiquette contenant la valeur sure.
     */
    public void refreshStatus() {
        switch (this.cellule.getStatus()) {
            case DEFINITIF:
                this.remove(this.doutePanel);
                this.add(this.contenuLabel, BorderLayout.CENTER);
                this.contenuLabel.setForeground(CellulePanel.DEFINITIF_COLOR);
                break;
            case DOUTE:
                this.remove(this.contenuLabel);
                this.add(this.doutePanel, BorderLayout.CENTER);
                break;
            case SUR:
                this.remove(this.doutePanel);
                this.add(this.contenuLabel, BorderLayout.CENTER);
                this.contenuLabel.setForeground(CellulePanel.SUR_COLOR);
                break;
            default:
                break;
        }
    }

    /**
     * Met à jour le contenu de toutes les étiquettes de cette cellule.
     */
    private void updateContenu() {
        int contenu = this.cellule.getContenu();
        String contenuString = CellulePanel.toString(contenu);
        this.contenuLabel.setText(contenuString);
        this.updateContenuDoute();
    }

    /**
     * Met à jour seulement les étiquettes qui affichent le contenu incertain de cette 
     * cellule.
     */
    private void updateContenuDoute() {
        int[] contenuDoute = this.cellule.getContenuDoute();

        for (int i = 0; i < this.douteLabels.length; i++) {
            if (i >= contenuDoute.length) {
                this.douteLabels[i].setText("");
            } else {
                String contenuString = CellulePanel.toString(contenuDoute[i]);

                this.douteLabels[i].setText(contenuString);
            }
        }
    }

    /**
     * Met à jour la couleur de fond selon si la cellule est sélectionnée ou non.
     */
    private void updateBackground() {
        if (this.cellule.isSelected())
            this.setBackground(CellulePanel.SELECTED_BACKGROUND_COLOR);
        else
            this.setBackground(CellulePanel.NORMAL_BACKGROUND_COLOR);
    }

    /**
     * @param contenu l'entier donné
     * @return La chaîne à afficher pour une cellule contenant l'entier donné.
     */
    private static String toString(int contenu) {
        return contenu == 0 ? "" : "" + contenu;
    }

    /**
     * @return la Cellule modèle pour ce composant.
     */
    public Cellule getCellule() {
        return this.cellule;
    }
}