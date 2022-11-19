import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.LayoutManager;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Cursor;

import java.awt.event.ActionListener;

/**
 * La classe <code>MainMenuScreen</code> représente l'écran du menu principal. 
 * Depuis ce menu, le joueur peut lancer le mode "Construction" ou le mode "Résolution".
 */
public class MainMenuScreen extends JPanel {
    /**
     * Constante représentant le texte à mettre dans le bouton qui lance le mode 
     * "Construction".
     */
    private static final String CONSTRUIRE_TEXT = "Construire une grille";
    /**
     * Constante représentant le texte à mettre dans le bouton qui lance le mode 
     * "Résolution".
     */
    private static final String RESOUDRE_TEXT = "Resoudre une grille";
    /**
     * Constante représentant la hauteur en pixels du panneau qui contient les boutons.
     */
    private static final int BUTTON_PANEL_HEIGHT = 250;

    /**
     * Constante représentant la couleur normale de l'arrière-plan d'un bouton.
     */
    public static final Color BUTTON_BACKGROUND_COLOR = new Color(0x6897bb);
    /**
     * Constante représentant la couleur de l'arrière-plan d'un bouton lorque la souris 
     * est passée dessus.
     */
    public static final Color BUTTON_HOVERED_COLOR = new Color(0x335671);
    /**
     * Constante représentant la couleur normale du texte d'un bouton.
     */
    public static final Color BUTTON_TEXT_COLOR = new Color(0x293c4a) ;
    /**
     * Constante représentant la couleur du texte d'un bouton lorque la souris 
     * est passée dessus.
     */
    public static final Color BUTTON_TEXT_HOVER_COLOR = new Color(0xc2d5e3) ;
    
    /**
     * Constante représentant le <code>ColorHoverListener</code> à associé aux boutons 
     * au style par défaut de l'application.
     */
    public static final ColorHoverListener BUTTON_HOVER_LISTENER = 
        new ColorHoverListener(MainMenuScreen.BUTTON_BACKGROUND_COLOR, 
                               MainMenuScreen.BUTTON_HOVERED_COLOR,
                               MainMenuScreen.BUTTON_TEXT_COLOR,
                               MainMenuScreen.BUTTON_TEXT_HOVER_COLOR);
    /**
     * Panneau centré utilisé pour contenir les boutons.
     */
    private JPanel buttonPanel;

    /**
     * Constructeur qui initialise les boutons du menu pour pouvoir modiifer l'écran affiché par la fenêtre 
     * donnée.
     * 
     * @param container la fenêtre principale de jeu.
     */
    public MainMenuScreen(MainFrame container) { 
        this.setLayout(null);
        
        this.initButtons(container);
        this.setBackground(MainFrame.BACKGROUND_COLOR);
    }

    /**
     * Initialise et affiche les boutons pour chanegr d'écran.
     * 
     * @param frame
     */
    private void initButtons(MainFrame frame) {
        int buttonPanelSideLength = MainMenuScreen.BUTTON_PANEL_HEIGHT;
        int verticalGap = buttonPanelSideLength / 3;
        LayoutManager layout = new GridLayout(2, 1, 0, verticalGap) ;
        
        this.buttonPanel = new JPanel(layout);
        this.buttonPanel.setBackground(MainFrame.BACKGROUND_COLOR);
        
        this.addButton(MainMenuScreen.CONSTRUIRE_TEXT, 
                       new CreateConstructionListener(frame));
        this.addButton(MainMenuScreen.RESOUDRE_TEXT, 
                       new CreateResolutionListener(frame));
        
        this.buttonPanel.setSize(buttonPanelSideLength, buttonPanelSideLength);
        this.updateLayout(frame.getWidth(), frame.getHeight());
        this.add(this.buttonPanel);
    }

    /**
     * Met à jour la mise en page de l'écran, étant donné la nouvelle largeur et hauteur 
     * de ce composant.
     * 
     * @param width la largeur de ce composant
     * @param height la hauteur de ce composant
     */
    private void updateLayout(int width, int height) {
        Point location = new Point( (width - MainMenuScreen.BUTTON_PANEL_HEIGHT)/2,
                                    (height - MainMenuScreen.BUTTON_PANEL_HEIGHT)/2);

        this.buttonPanel.setLocation(location.x, location.y);
    }

    /**
     * Ajoute un <code>JButton</code> à ce menu, y écrivant le texte donné et y associant
     * le <code>ActionListener</code> donné.
     * 
     * @param text le texte à écrire dans le bouton
     * @param listener le <code>ActionListener</code> à associer au bouton
     */
    private void addButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button = MainMenuScreen.stylizeButton(button, 
                                              MainMenuScreen.BUTTON_HOVER_LISTENER);
        button.addActionListener(listener);

        this.buttonPanel.add(button);
    }

    /**
     * Méthode statique servant à donner un style par défaut à un bouton de l'application, 
     * auquel est associé le <code>ColorHoverListener</code> donné.
     * Cette fonction modifie le bouton donné en argument, et renvoie une référence au 
     * même objet.
     * 
     * @param button le bouton à modifier.
     * @param colorListener le <code>ColorHoverListener</code> à associer au bouton.
     * @return le bouton donné en argument, modifié.
     */
    public static JButton stylizeButton(JButton button, ColorHoverListener colorListener) {
        button.addMouseListener(colorListener);

        button.setFocusPainted(false);
        button.setBorder(null);
        button.setBackground(colorListener.getNormalBackground());
        button.setForeground(colorListener.getNormalText());
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return button;
    }

    /**
     * Mettre à jour les informations concernant la mise en page des boutons avant de 
     * repeindre le composant.
     */
    @Override
    public void paintComponent(Graphics g) {
        this.updateLayout(this.getWidth(), this.getHeight());
        super.paintComponent(g);
    }
}