import java.util.Arrays;

/**
 * La classe <code>Cellule</code> est utilisée pour stocker les données d'une case dans 
 * une grille de sudoku et les opérations qu'on peut lui faire subir. 
 * Notamment, une instance de <code>Cellule</code> a un contenu (int) et des valeurs dont 
 * le joueur est incertain (int[]);
 * 
 * @author Louis Brunet 
 */
public class Cellule extends ObservableModel {
    /**
     * <code>enum</code> servant à représenter status de la cellule (qui détermine le mode 
     * d'affichage).
     */
	public enum Status {
        DEFINITIF,  // La cellule n'est pas modifiable.
		
		SUR,  // Le joueur est certain de la valeur rentrée, mais elle peut être modifiée.
		
		DOUTE // Le joueur est incertain des valeurs rentrées. Ces valeurs ne 
    };        // contraignent donc pas les cellules qui l'entourent.  
    

	/**
	 * La valeur maximale pour le contenu d'une cellule. 
	 */
	private static final int MAX_CONTENU = 9;
	
	/**
	 * Le nombre maximal de valeurs retenues pour le contenu de la cellule si le joueur 
	 * hésite sur cette cellule.
	 */
	public static final int MAX_DOUTE = 4;
	
	/**
	 * La position associée à cette cellule. Le compte commence à 0.
	 */
	private final Position position;
	
	/**
	 * Le contenu de cette cellule.
	 * Peut être 1-9 ou 0 pour une case vide.
	 */
	private int contenu;

	/**
	 * Tableau de maximum 4 entiers valides pour la position de cette cellule :
	 * les valeurs sur lesquels le joueur a un doute. 
	 */
	private int[] contenuDoute;

	/**
	 * Le status de cette cellule. Utile pour savoir quelles valeurs afficher : <p>
	 * s'il vaut <code>DEFINITIF</code>, alors la cellule n'est pas modifiable;<p>
	 * s'il vaut <code>SUR</code> ou <code>DEFINITIF</code>, alors la valeur de contenu 
     * sera utilisée pour l'affichage;<p>
	 * s'il vaut <code>DOUTE</code>, alors les valeurs de <code>contenuDoute</code> (au 
     * maximum 4) seront utilisées pour l'affichage.
	 */
	private Status status;

	/** 
	 * Booléen servant à savoir si la cellule doit être considérée comme sélectionnée.
	 */
	private boolean isSelected;



	/**
	 * Initialise une <code>Cellule</code> vide (avec des contenus nuls).
     * 
	 * @param ligne la ligne de cette cellule dans sa grille.
	 * @param colonne la colonne de cette cellule dans sa grille.
	 */
	public Cellule(int ligne, int colonne) {
		this(new Position(ligne, colonne));
	}

	/**
	 * Initialise une <code>Cellule</code> vide (avec des contenus nuls).
     * 
	 * @param pos La position de cette cellule dans sa grille.
	 */
	public Cellule (Position pos) {
		this.initDefaultValues();
		this.position = new Position(pos);
    }
    
    /**
     * Initialise une copie de la cellule donnée en argument.
     * 
     * @param aCopier la cellule à copier.
     */
    public Cellule(Cellule aCopier) {
        this.position = new Position(aCopier.position);
        this.contenu = aCopier.contenu;
        this.status = aCopier.status;
        this.isSelected = aCopier.isSelected;
        this.contenuDoute = Arrays.copyOf(aCopier.contenuDoute, 
                                          aCopier.contenuDoute.length);
    }

	/**
	 * Initialise les attributs de cette cellule avec des valeurs par défaut. 
	 */
	private void initDefaultValues() {
		this.contenu = 0;
		this.contenuDoute = new int[Cellule.MAX_DOUTE];
		this.status = Status.SUR;
		this.isSelected = false;
	}

	/**
	 * Renvoie la valeur de l'attribut <code>isSelected</code>.
	 * 
	 * @return true si la cellule doit être considérée comme sélectionnée, false sinon. 
	 */
	public boolean isSelected() {
		return this.isSelected;
	}

	/**
	 * @param isSelected booléen servant à savoir si la cellule doit être considérée comme
	 * sélectionnée.
	 */
	public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        this.notifyObservers();
	}

	/**
	 * @return Une copie du tableau <code>contenuDoute</code>, i.e. les valeurs dont le 
	 * joueur est incertain. Les valeurs sont normalement triées par ordre croissant. 
	 */
	public int[] getContenuDoute() {
		return this.contenuDoute.clone();
	}

	/**
	 * Ajouter un entier à la liste des valeurs incertaines <code>contenuDoute</code> 
     * si celle-ci ne contient pas déjà le nombra maximal de valeurs.<p>
     * Si l'entier à ajouter est déjà dans la liste, le retirer.
     * 
	 * @param n l'entier à ajouter.
	 */
	public void addContenuDoute(int n) {
		for (int i = this.contenuDoute.length - 1; i >= 0 ; i--) {
            int doute = this.contenuDoute[i];
            if( doute == n ) {
                this.removeContenuDoute(n);
                return;
            }
			if( doute == 0 ) {
                this.contenuDoute[i] = n;
                Arrays.sort(this.contenuDoute);
                this.notifyObservers();
				return;
			}
        }
	}

	/**
	 * Enlever un entier de la liste des valeurs incertaines.
     * 
	 * @param n l'entier à enlever. 
	 */
	public void removeContenuDoute(int n) {
		for (int i = 0; i < this.contenuDoute.length; i++) {
			if(this.contenuDoute[i] == n) 
				this.contenuDoute[i] = 0;
		}
        Arrays.sort(this.contenuDoute);
        this.notifyObservers();
    }

    /**
     * Si l'entier donné en argument peut être le contenu d'une cellule, et si cette 
     * cellule est modifiable, alors mettre à jour la valeur de l'attribut <code>contenu</code>
     * de cette <code>Cellule</code> à la valeur de l'argument.<p>
     * 
     * @param contenu Le contenu (entier entre 0 en MAX_CONTENU) à associer à la cellule.
	 * @throws IllegalArgumentException si l'entier n'est pas valide
     */
	public void setContenu(int contenu) throws IllegalArgumentException {
		if(! Cellule.isAcceptableContenu(contenu)) {
			throw new IllegalArgumentException(
				"Le contenu doit être positif et inférieur ou égal à " 
				+ Cellule.MAX_CONTENU);
		}
		if(this.isModifiable()) {
			this.contenu = contenu;
        }
        this.notifyObservers();
	}

    /**
     * Renvoie <code>true</code> si l'entier donné en argument pourrait être le contenu 
     * d'une cellule, <code>false</code> sinon.
     * 
     * @param contenu l'entier à tester.
     * @return <code>true</code> si l'entier donné en argument pourrait être le contenu 
     * d'une cellule, <code>false</code> sinon.
     */
	public static boolean isAcceptableContenu(int contenu) {
		return contenu >= 0 && contenu <= Cellule.MAX_CONTENU;
	}

	/**
	 * @return le contenu "certain" de cette cellule, soit l'attribut <code>contenu</code>
     * de cette <code>Cellule</code>. 
	 */
	public int getContenu() {
		return this.contenu;
	}

    /**
     * Mettre à jour le status de cette cellule à celui donné en argument.
     * 
     * @param status à associer à cette cellule.
     */
	public void setStatus(Status status) {
        this.status = status;
        this.notifyObservers();
	}

	/**
	 * @return le status de cette cellule. 
	 */
	public Status getStatus() {
		return this.status;
	}

	/**
	 * @return une copie de la position de cette cellule.
	 */
	public Position getPosition() {
		return new Position(this.position);
	}

	/**
	 * @return <code>true</code> si la cellule est modifiable (i.e. si son status n'est 
     * pas DEFINITIF), <code>false</code> sinon.  
	 */
	public boolean isModifiable() {
		return this.status != Status.DEFINITIF;
	}

	/**
	 * Renvoie <code>true</code> si cette cellule est dans le même groupe de 9 cellules 
     * qu'une cellule à la position donnée, <code>false</code> sinon.
     * 
	 * @param pos la position qui détermine le groupe auquel comparer le groupe de la 
	 * cellule
	 * @return <code>true</code> si cette cellule est dans le même groupe que la position 
     * donnée.
	 */
	public boolean estDansMemeGroupe(Position pos) {
		return this.getPosition().getGroupId() == pos.getGroupId();
	}
}