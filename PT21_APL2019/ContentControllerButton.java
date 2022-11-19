import java.awt.Cursor;

import javax.swing.JButton;

/**
 * La classe abstraite <code>ContentControllerButton</code> représente les opérations 
 * d'affichage d'un bouton qui sert à changer le contenu d'une cellule.
 */
public abstract class ContentControllerButton extends JButton {
	/**
	 * Booléen indiquant si ce bouton représente le contenu de la cellule séléctionnée.
	 */
    protected boolean isCellContent;
	/**
	 * Booléen indiquant si ce bouton est actif.
	 */
    protected boolean isActive;
	
	/**
	 * Initialise un bouton avec le texte donné.
	 * 
	 * @param s le texte à inscire dans le bouton.
	 */
    public ContentControllerButton(String s) {
        super(s);

        this.isCellContent = false;
		this.isActive = false;
    }
	
	/**
	 * Initialise un bouton vide.
	 */
    public ContentControllerButton() {
        this(" ");
    }

	/**
	 * Indiquer si ce bouton représente le contenu de la cellule séléctionnée.
	 * 
	 * @param b <code>true</code> si ce bouton a la même contenu que la cellule 
	 * selectionnée, <code>false</code> sinon.
	 */
	public void setIsCellContent(boolean b) {
		this.isCellContent = b;
		this.repaint();
		this.revalidate();
    }
    
    
	/**
	 * Activer ou déactiver ce bouton.
	 * 
	 * @param isActive <code>true</code> si actif, <code>false</code> sinon
	 */
	public void setActive(boolean isActive) {
        this.isActive = isActive;
        
        if(isActive) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else {
            this.setCursor(Cursor.getDefaultCursor());
        }
	}

	/**
	 * Renvoie <code>true</code> si ce bouton est actif, <code>false</code> sinon.
	 * 
	 * @return  isActive <code>true</code> si ce bouton est actif, <code>false</code> 
	 * sinon.
	 */
	public boolean isActive() {
		return this.isActive;
	}
}