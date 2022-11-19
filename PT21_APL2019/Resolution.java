import java.util.AbstractQueue;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * La classe <code>Resolution</code> gère les opérations de résolution d'une grille par un         
 * utilisateur.
 */
public class Resolution extends GameModel {
    /**
     * La durée de résolution en ns.
     */
    private long resolutionTime;
    /**
     * Booléen mis à jour selon si la grille est résolue ou pas.
     */
    private boolean isSolved;

    /**
	 * Initialise cet objet selon le constructeur de <code>GameModel</code>.
     * Initialise le temps de résolution à 0.
	 * 
	 * @param g la grille à associer aux intéractions avec l'utilisateur.
	 */
	public Resolution(Grille g) {
        super(g);
        this.resolutionTime = 0;
    } 

    /**
     * Lorsque ce modèle est associé à une grille, marquer tous les contenus non vides de 
     * la grille DEFINITIF.
     */
    @Override
    public void setGrille(Grille g) {
        super.setGrille(g);

        this.setCellulesNonVidesDefinitif();
    }

    /**
     * Mettre à jour le booléen indiquant si la grille est résolue. Renvoyer ce booléen.
     * 
     * @return la nouvelle valeur de <code>isSolved</code>.
     */
    public boolean updateIsSolved() {
        boolean solved = true;

        Cellule[][] cellules = this.grille.getCellules();

        for (int ligne = 0; ligne < cellules.length && solved; ligne++) {
            for (int colonne = 0; colonne < cellules[0].length && solved; colonne++) {
                int contenu = cellules[ligne][colonne].getContenu();
                if(contenu == 0) {
                    solved = false;
                }
            }
        }

        this.isSolved = solved;
        return solved;
    }

    /**
     * @return l'attribut isSolved, soit true si et seulement si chaque cellule de la 
     * grille de ce modèle possède un contenu. 
     */
    public boolean isSolved() {
        return this.isSolved;
    }
    
    /**
     * Marquer tous les contenus non vides (non nuls) de la grille DEFINITIF.
     * Méthode appelée lorsque ce modèle est associé à une <code>Grille</code>. 
     */
    private void setCellulesNonVidesDefinitif() {
        Cellule[][] cellules = this.grille.getCellules();
        Cellule c;

        for (int i = 0; i < cellules.length; i++) {
            for (int j = 0; j < cellules[0].length; j++) {
                c = cellules[i][j];
                if(c.getContenu() != 0) {
                    c.setStatus(Cellule.Status.DEFINITIF);
                }
            }
        }
    }
    
    /**
     * Basculer le mode "doute" sur la cellule sélectionnée, si celle-ci est modifiable.
     */
    public void toggleCelluleDoute() {
        if(! this.isCelluleModifiable()) {
			return;
		}

        boolean isDoute = (this.celluleSelectionnee.getStatus() == Cellule.Status.DOUTE);
        
        if(isDoute) {
			this.celluleSelectionnee.setStatus(Cellule.Status.SUR);
		} else {
			this.celluleSelectionnee.setStatus(Cellule.Status.DOUTE);
        }
        
        this.notifyObservers();
    }

    /**
     * Essayer de résoudre la grille automatiquement.
     * 
     * @return <code>true</code> si la grille a été résolue, <code>false</code> sinon.
     */
	public boolean resoudre() {
        long startTime = System.nanoTime();

        if(!this.isSolved) {
            Grille g = this.grille;
            Cellule[][] cellules = g.getCellules();
            
            // Ordonner les cellules vides de la grille selon leur nombre de contenus 
            // possibles croissant.
            Comparator<Cellule> comparator = new SortByPossibilities(g);
            AbstractQueue<Cellule> queue = new PriorityQueue<Cellule>(comparator);
                
            Cellule c;
            for (int i = 0; i < cellules.length; i++) {
                for (int j = 0; j < cellules[0].length; j++) {
                    c = cellules[i][j];
                    if(c.getContenu() == 0) {
                        queue.add(c);
                    }
                }
            }
            
            // Essayer de résoudre la grille avec l'aide d'une grille temporaire (une 
            // copie de la grille associée à cette Resolution).
            Grille copy = new Grille(g);
            boolean result = solved(g, queue, copy);
            
            this.isSolved = result;
            this.notifyObservers();
        }
        
        // Mettre à jour la durée de résolution. 
        this.resolutionTime = System.nanoTime() - startTime;
        
        return this.isSolved;
    }
    
    /**
     * Méthode récursive qui essaye de résoudre la grille <code>original</code> en 
     * essayant de placer un contenu valide dans <code>tempGrille</code> pour toutes les 
     * cellules de <code>queue</code>.
     * 
     * @param original la grille à résoudre
     * @param queue une liste de cellules non remplies.
     * @param tempGrille une grille temporaire, initialisée comme une copie de 
     *                   <code>original</code>
     * @return <code>true</code> si la grille a été résolue, <code>false</code> sinon.
     */
    private boolean solved(Grille original, AbstractQueue<Cellule> queue, Grille tempGrille) {
        // Si la liste est vide, alors toutes les cellules ont un contenu valide, et la 
        // grille est résolue.
        if(queue.size() == 0) {
            return true;
        }

        // garder une référence à la cellule retirée de la Queue. 
        Cellule currentCellule = queue.remove();
        Position pos = currentCellule.getPosition();

        for (int contenu = 1; contenu <= Grille.SIZE; contenu++) {
            if( ! tempGrille.isValidMove(pos, contenu) ) { // contenu non placeable 
                continue;
            }

            // placer dans tempGrille
            tempGrille.setContenuAt(pos, contenu);
            
            // si le placement de la prochaine cellule de la queue dans tempGrille renvoie
            // true,
            if( solved(original, queue, tempGrille) ) { 
                // placer le contenu en cours d'évaluation dans la grille originale.
                original.setContenuAt(pos, contenu);

                Cellule c = original.getCelluleAt(pos);
                if(c.isModifiable()) {
                    c.setStatus(Cellule.Status.SUR);
                }
                return true;
            }

            // enlever le contenu rajouté à tempGrille.
            tempGrille.setContenuAt(pos, 0);
           
        }
        // remettre la cellule retirée dans queue si aucun contenu valide trouvé
        queue.add(currentCellule);
        return false;
    }

	/**
     * @return la durée de résolution en nanosecondes.
     */
    public long getResolutionTime() {
        return this.resolutionTime;
    }

}