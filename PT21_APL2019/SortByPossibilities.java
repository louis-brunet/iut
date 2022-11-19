import java.util.Comparator;

/**
 * La classe <code>SortbyPossibilities</code> est un comparateur de cellules. Il permet
 * d'ordonner une liste de cellules selon leur nombre de contenus valides possibles.
 */
public class SortByPossibilities implements Comparator<Cellule> {

    /**
     * La grille dans laquelle faire la validation d'un contenu pour une cellule.
     */
    private Grille grille;

    /**
     * Constructeur qui associe à ce comparateur une grille à utiliser pour la validation
     * des contenus d'une cellule.
     * 
     * @param g la grille à utiliser pour la validation des contenus d'une cellule.
     */
    public SortByPossibilities(Grille g) {
        this.grille = g;
    }

    /**
     * Comparer deux cellules selon leur nombre de contenus valides possibles. <p>
     * 
     * Par exemple, si <code>c1</code> a 5 contenus valides possibles, et <code>c2</code>
     * en a 3, alors la valeur renvoyée est positive, donc <code>c2</code> sera ordonné
     * avant <code>c1</code>. <p>
     * 
     * Ce comparateur est bien cohérent avec <code>.equals()</code>.
     */
    @Override
    public int compare(Cellule c1, Cellule c2) {
        int nbC1 = this.grille.nombreContenusValides(c1.getPosition());
        int nbC2 = this.grille.nombreContenusValides(c2.getPosition());
        if(!c1.equals(c2) && nbC1 == nbC2) {
            return 1; // choix arbitraire pour rester cohérent avec .equals().
        } 

        return nbC1 - nbC2;
    }

}
