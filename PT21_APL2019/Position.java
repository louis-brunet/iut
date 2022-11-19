/**
 * La classe <code>Position</code> est utilisée pour représenter une position constituée d'une ligne et 
 * d'une colonne. Cette position peut être une position possible dans une grille de Sudoku
 * ou non.
 * 
 * @author Louis Brunet
 */
public class Position {
	/**
	 * Les attributs ligne et colonne sont déclarés :
	 *  public car ce sont des attributs qui seront fréquemment demandés;
	 *  final pour qu'une cellule initialisée avec cette position ne puisse pas voir 
	 * sa position changée par une classe contenant une référence à la même position. 
	 */
	/**
	 * Entier représentant un indice de ligne dans les cellules d'une <code>Grille</code>.
	 */
	public final int ligne;

	/**
	 * Entier représentant un indice de colonne dans les cellules d'une <code>Grille</code>.
	 */
	public final int colonne;

	/**
	 * Constructeur qui initialise cette Position avec les indices de ligne et de colonne
	 * donnés.
	 * 
	 * @param l l'indice de ligne à associer à cette position.
	 * @param c l'indice de colonne à associer à cette position.
	 */
	public Position(final int l, final int c) {
		this.ligne = l;
		this.colonne = c;
	}

	/**
	 * Constructeur qui copie la Position pos donnée en argument.
	 * 
	 * @param pos la <code>Position</code> à copier.
	 */
	public Position(final Position pos) {
		this(pos.ligne, pos.colonne);
	}

	/**
	 * @return true si et seulement si cette Position peut représenter les indices de 
	 * ligne et de colonne d'une <code>Cellule</code> dans une <code>Grille</code>.
	 */
	public boolean isPossible() {
		return this.ligne >= 0 && this.colonne >= 0
			&& this.ligne < Grille.SIZE && this.colonne < Grille.SIZE;
	}

	/**
	 * @return le numéro (1-9) du groupe de 9 cellules associé à cette position.
	 */
	public int getGroupId() {
		// Cas où la position n'est pas dans la grille.
		if(!this.isPossible()){
			return -1;
		}

		if (this.ligne < 3) {
			if(this.colonne < 3) {
				return 1;
			} 
			if(this.colonne < 6) {
				return 2;
			}
			return 3;
		} 
		if (this.ligne < 6) {
			if(this.colonne < 3) {
				return 4;
			} 
			if(this.colonne < 6) {
				return 5;
			}
			return 6;
		} 
		
		if(this.colonne < 3) {
			return 7;
		} 
		if(this.colonne < 6) {
			return 8;
		}
		return 9;
	}

	/**
	 * Renvoie une représentation de cette <code>Position</code> sous forme de
	 * <code>String</code> avec :
	 * le nom de la classe de cet objet, 
	 * puis, encadrés par des crochets, la valeur de l'attribut ligne, une virgule, et la 
	 * valeur de l'attribut colonne.
	 */
	@Override
	public String toString() {
		return this.getClass().getName() + "["+this.ligne+", "+this.colonne + "]";
	}
}