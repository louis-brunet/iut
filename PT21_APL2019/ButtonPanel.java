import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.GridLayout;

import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * La classe abstraite <code>ButtonPanel</code> sert à modéliser le panneau de boutons 
 * dans un <code>SidePanel</code>. Cette classe est implémentée par les classes 
 * <code>ConstructionButtonPanel</code> et <code>ResolutionButtonPanel</code>.
 */
public abstract class ButtonPanel extends JPanel {
    /**
     * Constante représentant la marge horizontale entre les boutons.
     */
    private static final int HORIZONTAL_GAP = 10;
    /**
     * Constante représentant la marge verticale entre les boutons.
     */
    private static final int VERTICAL_GAP = 10;
    /**
     * Constante représentant la taille préférée par défaut de chaque bouton. 
     */
    private static final Dimension DEFAULT_PREFERRED_SIZE = new Dimension( 50 , 38 );
    
    /**
     * Constante représentant la taille préférée effective de chaque bouton de ce panneau.
     * Utilisée pour régler la hauteur des boutons. 
     */
    protected Dimension preferredButtonSize;

    /**
     * Constructeur qui initialise un <code>ButtonPanel</code>, l'associant au 
     * <code>GameModel</code> et à la fenêtre donnéé.
     * 
     * @param modele le jeu en cours, le <code>GameModel</code> à associer aux 
     * interactions des boutons.
     * @param frame la fenêtre de jeu.
     */
    public ButtonPanel(GameModel modele, MainFrame frame) {
        this.setLayout(new GridLayout(4, 1,
                                      ButtonPanel.HORIZONTAL_GAP,
                                      ButtonPanel.VERTICAL_GAP));

        this.setBackground(MainFrame.BACKGROUND_COLOR);
        this.preferredButtonSize = ButtonPanel.DEFAULT_PREFERRED_SIZE;
        
        this.addButton("Retour au menu principal", 
                       new OpenMenuListener(frame));
        JButton importButton = this.addButton("Importer une grille", 
                                              new ImportFileListener(modele, frame));
        importButton.setToolTipText("Remplacer cette grille par une grille " 
                                    + "chargee depuis un fichier.");
    }

    /**
     * Ajouter un bouton à ce <code>ButtonPanel</code> avec le texte donné, y associant
     * le <code>ActionListener</code> et le <code>ColorHoverListener</code> donnés.
     * 
     * @param text le texte à écrire dans le bouton.
     * @param listener le <code>ActionListener</code> à associer au bouton créé.
     * @param colorHoverListener le <code>ColorHoverListener</code> à associer au bouton 
     * créé.
     * @return le <code>JButton</code> créé.
     */
    protected JButton addButton(String text, ActionListener listener, 
                                ColorHoverListener colorHoverListener) {
        JButton button = new JButton(text);
        button = MainMenuScreen.stylizeButton(button, colorHoverListener);
       
        button.addActionListener(listener);
        button.setPreferredSize(this.preferredButtonSize);

        this.addButton(button);

        return button;
    }

    /**
     * Ajouter un bouton à ce <code>ButtonPanel</code> avec le texte donné, y associant
     * le <code>ActionListener</code> donné et le <code>ColorHoverListener</code> par
     * défault de l'application.
     * 
     * @param text le texte à écrire dans le bouton.
     * @param listener le <code>ActionListener</code> à associer au bouton créé.
     * @param colorHoverListener le <code>ColorHoverListener</code> à associer au bouton 
     * créé.
     * @return le <code>JButton</code> créé.
     */
    protected JButton addButton(String text, ActionListener listener) {
        return this.addButton(text, listener, MainMenuScreen.BUTTON_HOVER_LISTENER);
    }

    /**
     * Ajouter le bouton donné à ce <code>ButtonPanel</code>, sans le modifier.
     * 
     * @param button le bouton stylisé à ajouter au <code>ButtonPanel</code>.
     */
    protected void addButton(JButton button) {
        this.add(button);
        this.repaint();
        this.revalidate();
    }

    /**
     * Renvoie la chaîne de caractère formée du nom de la classe de ce 
     * <code>ButtonPanel</code> suivi des boutons contenus sous forme de chaînes de
     * caractères.
     * 
     * @return la représentation sous forme de <code>String</code> de ce panneau.
     */
    @Override
    public String toString() {
        return this.getClass().getName() + this.getComponents();
    }
}