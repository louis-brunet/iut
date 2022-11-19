/**
 * La classe abstraite <code>GameModel</code> sert à modéliser une série d'interactions 
 * avec une grille de Sudoku. Elle permet d'effectuer des opérations d'ajout ou 
 * d'effacement du contenu de la cellule sélectionnée par l'utilisateur. 
 * 
 * @author Louis Brunet
 */
public abstract class GameModel extends ObservableModel {
    /**
     * La grille de jeu associée aux intéractions avec l'utilisateur.
     */
    protected Grille grille;
    
    /**
     * La cellule sélectionnée par l'utilisateur.
     */
	protected Cellule celluleSelectionnee;
	
    /**
     * Constructeur qui initialise le jeu avec la grille donnée.
     * 
     * @param g la grille à utiliser pour les interactions avec l'utilisateur.
     */
	public GameModel(Grille g) {
		this.setGrille(g);
	}

    /**
     * Associer la grille donnée au jeu en cours. Toutes les prochaines opérations se 
     * feront sur cette grille. 
     * Le paramètre celluleSelectionnee est initialisé à <code>null</code>. 
     * 
     * @param g la grille de jeu à utiliser.
     */
	public void setGrille(Grille g) {
		this.grille = g;
        this.select((Cellule) null);
        this.notifyObservers();
	}

	public Grille getGrille() {
		return this.grille;
	}

	/**
     * Sélectionne la cellule donnée. Méthode appelée après un clic sur une cellule.
     * 
	 * @param c la cellule à selectionner.
	 */
	public void select(Cellule c) {
        if( this.celluleSelectionnee != null ) {
            this.celluleSelectionnee.setSelected(false);
        }

        this.celluleSelectionnee = c;
		if( c != null ) {
			this.celluleSelectionnee.setSelected(true);
        }
        
        this.notifyObservers();
	}

    /**
     * @return <code>true</code> si une cellule est sélectionnée qui est modifiable, 
     * <code>false</code> sinon.
     */
	protected boolean isCelluleModifiable() {
		return this.celluleSelectionnee != null 
		    && this.celluleSelectionnee.isModifiable();
	}
	
    /**
     * Ajouter le contenu donné à la cellule sélectionnée si celle-ci est modifiable et si
     * le contenu est valide dans la grille de jeu.
     * Si la cellule sélectionnée a un status SUR, remplacer le contenu de cette cellule.
     * Si la cellule sélectionnée a un status DOUTE, ajouter le contenu aux valeurs 
     * incertaines de cette cellule, ou le retirer s'il y est déjà.
     * 
     * @param contenu 
     */
	public void setCelluleContenu(int contenu) {
        if(!this.isCelluleModifiable()
            || !this.grille.isValidMove(this.celluleSelectionnee.getPosition(), contenu)) {
                return;
        }
        Cellule.Status status = this.celluleSelectionnee.getStatus(); 
        if(status == Cellule.Status.SUR) {
            this.celluleSelectionnee.setContenu(contenu);
            if(contenu != 0) {
                Position pos = this.celluleSelectionnee.getPosition();
                this.grille.removeDouteConflicts(pos);
            }
		} else if(status == Cellule.Status.DOUTE) {
            this.celluleSelectionnee.addContenuDoute(contenu);
        }

        this.notifyObservers();
	}

	/**
	 * @return la cellule selectionnée par l'utilisateur, ou <code>null</code> si aucune 
     * cellule n'est sélectionnée. 
	 */
	public Cellule getCelluleSelectionnee() {
		return this.celluleSelectionnee;
    }
    
}