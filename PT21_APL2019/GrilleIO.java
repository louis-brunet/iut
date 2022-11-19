import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;

/**
 * La classe GrilleIO est utilisée pour ses méthodes statiques permettant de créer une 
 * Grille à partir d'un fichier formaté, ou d'enregistrer une Grille dans un fichier, dans 
 * le même format.
 * 
 * @author Louis Brunet
 */
public class GrilleIO {
	/**
	 * Le format des données d'un fichier représentant une Grille est le suivant :
	 * Neuf entiers consécutifs représentant les contenus des 9 lignes dans l'ordre du 
	 * haut vers le bas. 
	 * Les chiffres de chacun de ces entiers représentent le contenu de chaque cellule de 
	 * la ligne en question. 
	 * i.e. le chiffre des unités est le contenu de la dernière case d'une ligne. 
	 * e.g. l'entier 6009 représente une ligne avec les contenus {0,0,0,0,0,6,0,0,9}. 
	 */

    /**
     * Le JFileChooser qui sera utilisé pour toutes les opérations de chargement et  
     * enregitrement de fichiers. 
     */
    public static final JFileChooser fileChooser = new JFileChooser();

	/**
	 * Crée et renvoie une Grille selon les données dans le fichier dont le nom est donné
	 * en argument. 
	 * 
	 * @param fileName le chemin relatif ou absolu vers le fichier contenant les données 
	 * de la Grille à créer.
	 * @return  la Grille c correpondant au fichier fileName.
	 * @throws FileNotFoundException si le fichier n'est pas trouvé.
	 * @throws IOException s'il y a une erreur dans la lecture du DataInputStream créé.
	 */
	public static Grille chargerGrille(File file) throws IOException {
		Grille result = new Grille();

		FileInputStream fInputStream = new FileInputStream(file);
		DataInputStream dInputStream = new DataInputStream(fInputStream);

		int ligneData;
		for (int index = 0; index < Grille.SIZE && dInputStream.available() > 0; index++) {
			ligneData = dInputStream.readInt();
			GrilleIO.createLigne(ligneData, result, index);
		}

		fInputStream.close();
		dInputStream.close();

		return result;
	}

	/**
	 * Initialise le contenu de la ligne ligneIndex de la Grille g selon les chiffres de 
	 * l'entier ligneData.
	 * 
	 * @param ligneData l'entier représentant les contenus de la ligne à ajouter.
	 * @param g la Grille à modifier.
	 * @param ligneIndex l'indice de la ligne de g à modifier. 
	 */
	private static void createLigne(int ligneData, Grille g, int ligneIndex) {
		// Entier qui contient successivement le contenu de chaque cellule de la ligne à 
		// créer.
		int contenu;
		// Le numéro de la colonne sur laquelle mettre le nouveau contenu.
		int colonne;
		
		Position pos;
		for (int offset = 8; offset >= 0; offset--) {
			// contenu prend la valeur du chiffre à la position offset de ligneData
			// Où le chiffre à la position 0 est le chiffre de poids faible. 
			contenu = GrilleIO.getDigitAt(offset, ligneData);
			colonne = 8 - offset;
			pos = new Position(ligneIndex, colonne);
			g.setContenuAt(pos, contenu);
		}
	}

	/**
	 * Renvoie le chiffre à la position offset de l'entier value. Une position de 0 
	 * indique le chiffre de poids faible.
	 * 
	 * @param offset la position du chiffre à retourner. 
	 * @param value l'entier à considérer.
	 * @return le chiffre à la position offset de l'entier value.
	 */
	private static int getDigitAt(int offset, int value) {
		return (value / (int) Math.pow(10.0, offset)) % 10;
	}

	/**
	 * Enregistre la Grille donnée dans un fichier avec le nom fileName.
	 * Ecrase le fichier existant s'il existe déjà.
	 * 
	 * @param g la Grille à enregistrer dans un fichier.
	 * @param fileName le nom du fichier à créer / remplacer. 
	 */
	public static void sauvegarderGrille(Grille g, File file) throws IOException{
		FileOutputStream fOutputStream = new FileOutputStream(file);
		DataOutputStream dOutputStream = new DataOutputStream(fOutputStream);

		Cellule[][] cellules = g.getCellules();
		for (int ligneIndex = 0; ligneIndex < cellules.length; ligneIndex++) {
			int representation = GrilleIO.getIntRepresentation(cellules[ligneIndex]);
			dOutputStream.writeInt(representation);
		}

		dOutputStream.close();
		fOutputStream.close();
	}

	/**
	 * Renvoie une représentation de la ligne ce cellules donnée sour la forme d'un entier
	 * au format approprié.
	 * 
	 * @param ligne la ligne de cellules à représenter. 
	 * @return l'entier décrivant la ligne donnée.
	 */
	private static int getIntRepresentation(Cellule[] ligne) {
		int resultat = 0;
		int offset;
		for (int colonne = 0; colonne < ligne.length; colonne++) {
			offset = 8 - colonne;
			resultat += ligne[colonne].getContenu() * Math.pow(10.0, offset);
		}

		return resultat;
	}
}