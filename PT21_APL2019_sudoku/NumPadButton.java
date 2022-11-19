import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.FontMetrics;

/**
 * La classe un <code>NumPadButton</code> représente un bouton individuel d'un 
 * <code>NumPad</code>. 
 */
public class NumPadButton extends ContentControllerButton {
	/**
	 * Constante représentant la couleur par défaut de l'arrière-plan d'un bouton inactif.
	 */
	private static final Color DEFAULT_INACTIVE_COLOR = Color.GRAY;
	/**
	 * Constante représentant la couleur par défaut de l'arrière-plan d'un bouton actif.
	 */
	private static final Color DEFAULT_CIRCLE_COLOR = MainMenuScreen.BUTTON_TEXT_COLOR;
	/**
	 * Constante représentant la couleur par défaut du texte d'un bouton actif.
	 */
	private static final Color DEFAULT_TEXT_COLOR = MainMenuScreen.BUTTON_BACKGROUND_COLOR;
	/**
	 * Constante représentant la taille préférée par défaut d'un bouton.
	 */
	private static final Dimension DEFAULT_PREFERRED_SIZE = new Dimension(50,50);

	/**
	 * La couleur effective du cercle.
	 */
	private Color circleColor;

	/**
	 * Constructeur qui initialise un <code>NumPadButton</code> avec le texte donné.
	 * 
	 * @param s
	 */
	public NumPadButton(String s) {
		super(s);
		this.setPreferredSize(NumPadButton.DEFAULT_PREFERRED_SIZE);
		this.setCircleColor(NumPadButton.DEFAULT_CIRCLE_COLOR);
		this.setForeground(NumPadButton.DEFAULT_TEXT_COLOR);
		this.setBorderPainted(false);
        this.setFont(this.getFont().deriveFont(CellulePanel.FONT_SIZE));
        this.setOpaque(false);
	}

	/**
	 * Constructeur qui initialise un <code>NumPadButton</code> vide.
	 */
	public NumPadButton() {
		this(" ");
	}

	/**
	 * Change la couleur du disque.
	 * 
	 * @param c la nouvelle couleur du disque.
	 */
	private void setCircleColor(Color c) {
		this.circleColor = c;
	}

	/**
	 * Dessine un cercle et le texte associés au bouton, changeant la couleur selon si 
	 * le bouton est actif, et si le bouton représente le contenu de la cellule 
	 * sélectionnée.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		int width = this.getWidth();
		int height = this.getHeight();

		Graphics g2 = g.create();
		if(this.isOpaque()) {
			g2.setColor(this.getBackground());
			g2.fillRect(0, 0, width, height);
		}

		int circleDiameter = Math.min(width, height);
		int circleRadius = circleDiameter / 2;
		Point circleTopLeft = new Point(( width / 2 ) - circleRadius,
										( height / 2 ) - circleRadius);
		
		// choisir la couleur du texte et de l'arrière plan selon le status de ce bouton.
		Color text;										
		Color circle;
		if(!this.isActive) {
			circle = NumPadButton.DEFAULT_INACTIVE_COLOR;
			text = Color.DARK_GRAY;
		} else if(this.isCellContent) {
			circle = this.getForeground();
			text = this.circleColor;
		} else {
			circle = this.circleColor;
			text = this.getForeground();
		}
		g2.setColor( circle );
		g2.fillOval(circleTopLeft.x, circleTopLeft.y,
					circleDiameter, circleDiameter);
		
		g2.setColor( text );
		this.drawStringCenter(this.getText(), g2);
	}

	/**
	 * Ecrire une chaîne de caractère centrée dans ce composant.
	 * 
	 * @param text le texte à écrire
	 * @param g le <code>Graphics</code> utilisé pour le dessin
	 */
	private void drawStringCenter(String text, Graphics g) {
		FontMetrics metrics = g.getFontMetrics();
		int x = (this.getWidth() / 2) - (metrics.stringWidth(text) / 2);
		int y = (this.getHeight() / 2) - (metrics.getHeight() / 2) + metrics.getAscent();
		g.drawString(text, x, y);
	}
}