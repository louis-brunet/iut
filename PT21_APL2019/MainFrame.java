import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * La classe <code>MainFrame</code> représente la fenêtre de jeu principale. Elle peut 
 * contenir un seul éran à la fois.
 */
public class MainFrame extends JFrame {
    /**
     * Constante représentant le titre de la fenêtre lorsque le mode "Construction" est 
     * actif.
     */
    public static final String CONSTRUCTION_TITLE = "Sudoku | Construction";
    /**
     * Constante représentant le titre de la fenêtre lorsque le mode "Résolution" est 
     * actif.
     */
    public static final String RESOLUTION_TITLE = "Sudoku | Resolution";
    /**
     * Constante représentant le titre de la fenêtre dans le menu principal.
     */
    public static final String DEFAULT_TITLE = "Sudoku | Thibault Barbieri - Louis Brunet";
    /**
     * Constante représentant la couleur d'arrière-plan de la fenêtre.
     */
    public static final Color BACKGROUND_COLOR = Color.WHITE;

    /**
     * L'écran affiché actuellement.
     */
    private JPanel currentScreen;

    /**
     * Constructeur qui initialise les valeurs par défaut de la fenêtre de jeu.
     * 
     * @param title le titre à donner à la fenêtre
     */
    public MainFrame(String title) {
        super(title);
        this.setSize(620, 470);
        this.setMinimumSize(new Dimension(600, 455));
        this.setBackground(MainFrame.BACKGROUND_COLOR);

        this.currentScreen = null;
    }

    /**
     * Changer l'écran affiché par la fenêtre.
     * 
     * @param panel le nouvel écran à afficher.
     */
    public void setScreen(JPanel panel) {
        if(this.currentScreen != null) {
            this.remove(this.currentScreen);
        }

        this.currentScreen = panel;
        this.add( this.currentScreen, BorderLayout.CENTER );
    }
}