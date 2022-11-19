import java.util.Arrays;

/**
 * La classe Grille est utilisée pour stocker les données d'une grille de sudoku et les 
 * opérations qu'on peut lui faire subir. 
 * Notamment, une instance de Grille contient un tableau 2-dimensionnel de Cellule.
 * 
 * @author Louis Brunet 
 */
public class Grille extends ObservableModel {
	/**
	 * Constant représentant la taille d'une grille. TODO enlever ? 
	 */
	public static final int SIZE = 9;
	/**
	 * Tableau 2-dimensionnel de Cellule représentant la grille de cellules.
	 */
	private final Cellule[][] cellules;

	/**
	 * Contructeur qui initialise toutes les cellules à vide. 
	 */
	public Grille() {
        this.cellules = new Cellule[9][9];
		this.initCellules();
    }
    
    /**
     * Initialise une copie de la Grille donnée en argument.
     * @param aCopier la Grille à copier.
     */
    public Grille(Grille aCopier) {
        this.cellules = new Cellule[9][9];

        Cellule[][] cellulesACopier = aCopier.cellules;
        Cellule c;

        for (int ligne = 0; ligne < cellulesACopier.length; ligne++) {
            for (int colonne = 0; colonne < cellulesACopier[0].length; colonne++) {
                c = aCopier.cellules[ligne][colonne];
                this.cellules[ligne][colonne] = new Cellule(c);
            }
        }
    }

	/**
	 * Initialiser chaque toutes les cellules de la grille à vide.
	 */
	public void initCellules() {
        for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				this.cellules[i][j] = new Cellule(i, j);
			}
		}
    }
    
	/**
	 * @return le tableau 2-dimensionnel représentant la grille de cellules.
	 */
	public Cellule[][] getCellules() {
		return this.cellules;
	}

	/**
	 * @param pos la position de la cellule. 
	 * @return la cellule à la position donnée, ou null si la position est invalide.
	 */
	public Cellule getCelluleAt(Position pos) {
		if(pos.isPossible())
			return this.cellules[pos.ligne][pos.colonne];
		else 
			return null;
	}

	/**
	 * @param pos la position de la cellule dans la grille .
	 * @param contenu le contenu à associer à la cellule à la position donnée.
	 */
	public void setContenuAt(Position pos, int contenu) {
		this.cellules[pos.ligne][pos.colonne].setContenu(contenu);
	}

	/**
	 * @param pos la position à considérer
	 * @param contenu le contenu à considérer
	 * @return true si la cellule à la position donnée peut être remplie par le contenu 
	 * donné
	 */
	public boolean isValidMove(Position pos, int contenu) {
		if(! pos.isPossible() 
			|| !Cellule.isAcceptableContenu(contenu)) {
				return false;
		}
		// Si le contenu à tester est 0, toujours renvoyer true.
		if(contenu == 0)
			return true;

		if(this.contenuDansLigne(contenu, pos.ligne)
			|| this.contenuDansColonne(contenu, pos.colonne)
			|| this.contenuDansGroupe(contenu, pos)) {
				return false;
		}

		return true;
	} 

	// private boolean contenuDansLigne(int contenu, Position pos) {
	// 	return Arrays.stream(this.cellules[pos.ligne])
	// 				 .filter( cellule -> cellule.getPosition().ligne == pos.ligne )
	// 				 .anyMatch(cellule -> cellule.getContenu() == contenu);
	// } 

	/**
	 * @param contenu le contenu à comparer
	 * @param ligne la ligne à parcourir
	 * @return true si la ligne donnée de la grille comprend une cellule qui a le contenu 
	 * donné en paramètre, false sinon.
	 */
	private boolean contenuDansLigne(int contenu, int ligne) {
		for (int i = 0; i < this.cellules.length; i++) {
			if(this.cellules[ligne][i].getContenu() == contenu) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param contenu le contenu à comparer
	 * @param colonne la colonne à parcourir
	 * @return true si la colonne donnée de la grille comprend une cellule qui a le 
	 * contenu donné en paramètre, false sinon.
	 */
	private boolean contenuDansColonne(int contenu, int colonne) {
		for (int i = 0; i < this.cellules.length; i++) {
			if(this.cellules[i][colonne].getContenu() == contenu) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param contenu le contenu à comparer
	 * @param pos la position d'une cellule dans un groupe
	 * @return true si le groupe associé a la cellule à la position donnée de la grille 
	 * comprend une cellule qui a le contenu donné en paramètre, false sinon.
	 */
	private boolean contenuDansGroupe(int contenu, Position pos) {
        // Transformer la grille de cellules en un Stream<Cellule> unidimensionnel.
        return Arrays.stream(this.cellules)  
                     .flatMap( cArr -> Arrays.stream(cArr)) 
		// Filtrer pour garder seulement les cellules qui sont dans le même groupe que la 
		// position donnée.
                     .filter( cellule -> cellule.estDansMemeGroupe(pos) )
		// Renvoyer true si et seulement si une cellule de ce groupe contient le même 
		// contenu.
                     .anyMatch( cellule -> cellule.getContenu() == contenu );
	}

    /**
     * Calcule et renvoie le nombre de contenus valides possibles la position donnée de la 
     * grille.
     * @param pos la position où calculer le nombre de possibilités.
     * @return le nombre de possibilités.
     */
    public int nombreContenusValides(Position pos) {
        int count = 0;

        for (int contenu = 1; contenu <= Grille.SIZE ; contenu++) {
            if (this.isValidMove(pos, contenu)) 
                count++;
        }

        return count;
        
    }

    /**
     * Enlever tous les conflits dans les contenus "doute" des cellules qui sont sur la 
     * même ligne, sur la même colonne, ou dans le même groupe que la cellule à la 
     * position donnée.
     * 
     * Méthode appelée après que le joueur ajoute un contenu certain à la cellule 
     * sélectionnée. 
     * 
     * @param pos la position qui indique la ligne, la colonne, et le groupe de cellules 
     * qui doivent être mis à jour. 
     */
    public void removeDouteConflicts(Position pos) {
        int contenu = this.cellules[pos.ligne][pos.colonne].getContenu();

        Cellule c;
        // ligne
        for (int i = 0; i < cellules[0].length; i++) {
            c = this.cellules[pos.ligne][i];
            c.removeContenuDoute(contenu);
        }

        // colonne
        for (int i = 0; i < cellules.length; i++) {
            c = this.cellules[i][pos.colonne];
            c.removeContenuDoute(contenu);
        }

        // groupe
        int ligneDebut = (pos.ligne / 3) * 3;
        int ligneFin = ligneDebut + 2;
        int colonneDebut = (pos.colonne / 3) * 3;
        int colonneFin = colonneDebut + 2;

        for (int i = ligneDebut; i <= ligneFin; i++) {
            for (int j = colonneDebut; j <= colonneFin; j++) {
                c = this.cellules[i][j];
                c.removeContenuDoute(contenu);
            }
        }

        this.notifyObservers();
    }


	/**
	 * @return une représentation de cette grille sous forme de chaîne de caractères.
	 */
	@Override
	public String toString() {
		String str = "";
		for (int i = 0; i < 9; i++) {
			str += '|';
			for (int j = 0; j < 9; j++) {
				int contenu = this.cellules[i][j].getContenu();
				str += (contenu == 0 ? " " : "" + contenu);
				str += '|';
			}
			str += '\n';
		}
		return str;
	}
}