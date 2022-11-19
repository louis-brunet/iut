import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import java.awt.event.MouseAdapter;

import java.awt.Color;

/**
 * La classe <code>ColorHoverListener</code> est un <code>MouseListener</code> associé
 * aux boutons de l'application. Cette classe gère les changements de couleurs lorque 
 * l'utilisateur passe sa souris sur un des boutons.
 */
public class ColorHoverListener extends MouseAdapter {

    /**
     * La couleur d'arrière-plan normale.
     */
    private Color background;
    /**
     * La couleur d'arrière-plan lorsque la souris est sur le composant.
     */
    private Color hoveredBackground;
    /**
     * La couleur de texte normale.
     */
    private Color text;
    /**
     * La couleur d'arrière-plan lorsque la souris est sur le composant.
     */
    private Color hoveredText;

    /**
     * Constructeur qui initialise les couleurs à utiliser.
     * 
     * @param normalBackground La couleur d'arrière-plan normale.
     * @param hoveredBackground La couleur d'arrière-plan lorsque la souris est sur 
     *                          le composant.
     * @param normalText La couleur de texte normale.
     * @param hoveredText La couleur d'arrière-plan lorsque la souris est sur le composant.
     */
    public ColorHoverListener(Color normalBackground, Color hoveredBackground, 
                              Color normalText, Color hoveredText) {
        this.background = normalBackground;
        this.hoveredBackground = hoveredBackground;
        this.text = normalText;
        this.hoveredText = hoveredText;
    }

    /**
     * Renvoie la couleur d'arrière-plan normale.
     * @return La couleur d'arrière-plan normale.
     */
    public Color getNormalBackground() {
        return this.background;
    }

    /**
     * Renvoie la couleur de texte normale.
     * @return La couleur de texte normale.
     */
    public Color getNormalText() {
        return this.text;
    }
    
    /**
     * Changer la couleur du texte du composant lorsque la souris est cliqueé.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        JComponent comp = (JComponent) e.getSource();
        comp.setForeground(this.text);
    }
    
    /**
     * Changer la couleur du texte du composant lorsque la souris est relachés.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        JComponent comp = (JComponent) e.getSource();
        comp.setForeground(this.hoveredText);
    }

    /**
     * Changer la couleur du texte et de l'arrère-plan du composant lorsque la souris 
     * passe au dessus du composant.
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        JComponent comp = (JComponent) e.getSource();
        comp.setBackground(this.hoveredBackground);
        comp.setForeground(this.hoveredText);
    }

    /**
     * Changer la couleur du texte et de l'arrère-plan du composant lorsque la souris sort 
     * du composant.
     */
    @Override
    public void mouseExited(MouseEvent e) {
        JComponent comp = (JComponent) e.getSource();
        comp.setBackground(this.background);
        comp.setForeground(this.text);
    }

}