#include<stdlib.h>
#include<stdio.h>
#include<graph.h>
#include"affichage.h"
#include "records.h"

/* Taille d'une cellule carrée (=taille des images) en pixels. */
#define TAILLE_CELLULE 16
/* Taille des differents textes */
/* Pour les menus */
#define TAILLE_TITRE 2
#define TAILLE_OPTION 2
#define TAILLE_TOUCHE 1 /* doit etre inférieure à 2 */
/* Pour l'affichage en bas du cadre */
#define TAILLE_INFOS_TEXTE 2
/* Epaisseur du cadre noir autour du terrain de jeu 
en nombre de cellules */
#define PADDING 1
/* Hauteur du panneau d'infos en bas de l'écran 
en nombre de cellules */
#define HAUTEUR_INFOS 3
/* Nombre total de lignes et de colonnes,
comprend le cadre et le terrain de jeu */
#define NB_TOTAL_LIGNES  (NB_LIGNES + PADDING + HAUTEUR_INFOS)
#define NB_TOTAL_COLONNES (NB_COLONNES + PADDING * 2)
/* Couleur des différents types de cellule.
Le mode graphique doit être initialisé */
#define COULEUR_CADRE CouleurParComposante(0, 0, 0) /* Noir */
#define COULEUR_VIDE CouleurParComposante(255, 255, 204) /* Jaune pale */
#define COULEUR_SNAKE CouleurParComposante(0, 150, 0) /* Vert */
#define COULEUR_SNAKE_CONTOUR CouleurParComposante(100, 255, 100) /* Noir */
#define COULEUR_PASTILLE CouleurParComposante(255, 0, 0) /* Rouge */

void afficherTableRecords(int[], char[][4], int);

void changerCouleurDessin(ContenuCellule contenu) {
	switch(contenu) {
	case CADRE: 
		ChoisirCouleurDessin(COULEUR_CADRE);
		break;
	case VIDE:
		ChoisirCouleurDessin(COULEUR_VIDE);
		break;
	case SNAKE:
		ChoisirCouleurDessin(COULEUR_SNAKE);
		break;
	case PASTILLE:
		ChoisirCouleurDessin(COULEUR_PASTILLE);
		break;
	case OBSTACLE:
		break;
	}
}

/* Initialiser affichage du cadre & terrain de jeu.
Renvoie 0 en cas d'erreur, 1 sinon. */
int initAffichage(Jeu jeu) {
	/* Initialiser fenêtre graphique */
	if(! InitialiserGraphique()) {
		printf("Echec de l'initialisation du mode graphique.\n");
		return 0;
	}

	CreerFenetre(10,10,
				 NB_TOTAL_COLONNES * TAILLE_CELLULE,
				 NB_TOTAL_LIGNES * TAILLE_CELLULE);
	ChoisirTitreFenetre("Snake -- Louis BRUNET");

	afficherCadre();
	afficherTerrainEntier(jeu.terrain);
	dessinerContoursSnake(jeu.snake);
	/* Initialiser l'affichage des informations (temps, score) */
	updateAffichageInfos(jeu);

	return 1;
}

/* Afficher le cadre noir autour du terrain, défini par PADDING et HAUTEUR_INFOS */
void afficherCadre() {
	/* Position relative à la fenêtre en nombre de cellules */
	int ligne;
	int col;

	for(ligne = 0; ligne < NB_TOTAL_LIGNES; ligne++) {
		for (col = 0; col < NB_TOTAL_COLONNES; col++) {
			/* Position relative à la fenêtre en pixels */
			int posX = col * TAILLE_CELLULE;
			int posY = ligne * TAILLE_CELLULE;

			/* Dessiner seulement les cellules du cadre. */
			int estDansCadre = ligne < PADDING || col < PADDING 
							   || ligne >= (NB_TOTAL_LIGNES - HAUTEUR_INFOS) 
							   || col >= (NB_TOTAL_COLONNES - PADDING);

			if(estDansCadre) {
				ChoisirCouleurDessin(COULEUR_CADRE);
				RemplirRectangle(posX, posY, TAILLE_CELLULE, TAILLE_CELLULE);
			}
		}
	}
}

/* Afficher toutes les cellules du terrain */
void afficherTerrainEntier(Terrain terrain){
	/* Positions successives des cellules du terrain. */
	int ligne;
	int col;

	for(ligne = 0; ligne < NB_LIGNES; ligne++) {
		for(col = 0; col < NB_COLONNES; col++) {
			dessinerCelluleTerrain(terrain.cellules[ligne][col]);
		}
	}
}


/* Afficher le bon contenu pour les cellules changées:
le segment rajouté en tête du serpent, celui enlevé en 
queue du serpent, et une éventuelle pastille apparue */
void rafraichirCellulesChangees(Terrain* terrainPtr) { 
	int cellCount;
	/* Positions successives des cellules changées du terrain. */
	Position pos;
	/* Contient successivement chaque cellule changée. */
	Cellule cell;

	for(cellCount = 0; cellCount < terrainPtr->nbCellulesChangees; cellCount++) {
		pos = terrainPtr->posCellulesChangees[cellCount];
		cell = terrainPtr->cellules[pos.y][pos.x];
		dessinerCelluleTerrain(cell);
	}
	terrainPtr->nbCellulesChangees = 0;
}

/* Dessiner une cellule du terrain à la bonne position,
prennant en compte le décalage du cadre */
void dessinerCelluleTerrain(Cellule cellule) {
	/* Position en pixels où dessiner la cellule */
	int xAbs = (cellule.position.x + PADDING) * TAILLE_CELLULE;
	int yAbs = (cellule.position.y + PADDING) * TAILLE_CELLULE;

	/* Si cellule contient une pastille ou un obstacle, charger l'image correspondante */
	if(cellule.contenu == PASTILLE){
		ChoisirCouleurDessin(COULEUR_VIDE);
		RemplirRectangle(xAbs, yAbs, TAILLE_CELLULE, TAILLE_CELLULE);
		ChargerImage("images/apple.png", xAbs, yAbs, 0, 0, TAILLE_CELLULE, TAILLE_CELLULE);
	} else if(cellule.contenu == OBSTACLE){
		ChoisirCouleurDessin(COULEUR_VIDE);
		RemplirRectangle(xAbs, yAbs, TAILLE_CELLULE, TAILLE_CELLULE);
		ChargerImage("images/fire.png", xAbs, yAbs, 0, 0, TAILLE_CELLULE, TAILLE_CELLULE);
	}
	/* Sinon, dessiner la cellule */
	else {
		changerCouleurDessin(cellule.contenu);

		/* Remplir la cellule de la couleur de son contenu  */
		RemplirRectangle(xAbs, yAbs, TAILLE_CELLULE, TAILLE_CELLULE);

		/* Si la cellule ne contient pas un segment du serpent, dessiner
		le contour de la cellule de la même couleur que son contenu.
		Utile pour effacer les contours déjà déssinés. */
		if(cellule.contenu != SNAKE) {
			DessinerRectangle(xAbs, yAbs, TAILLE_CELLULE, TAILLE_CELLULE);
		}
	}
}

/* Redessiner les contours des segments de serpent.
Permet de gérer l'effacement du contour du dernier segment du serpent après l'avoir bougé.  */
void dessinerContoursSnake(Snake snake) {
	int posCount;
	/* Position en pixels où dessiner le contour du segment actuel */
	int xAbs, yAbs;

	for(posCount = 0; posCount < snake.taille; posCount++) {
		xAbs = (snake.positions[posCount].x + PADDING) * TAILLE_CELLULE;
		yAbs = (snake.positions[posCount].y + PADDING) * TAILLE_CELLULE;

		ChoisirCouleurDessin(COULEUR_SNAKE_CONTOUR);
		DessinerRectangle(xAbs, yAbs, TAILLE_CELLULE, TAILLE_CELLULE);
	} 
}

/* Afficher le menu de pause sur le terrain,
avec les options : continuer, recommencer, ou quitter. 
Couleur des options : COULEUR_CADRE */
void afficherMenuPause() {
	/* Chaines de caractères à afficher */
	/* Titre */
	char* titre = "PAUSE";
	/* Options */
	char* continuer = "Continuer";
	char* recommencer = "Recommencer";
	char* quitter = "Quitter";
	/* Touches à entrer */
	char* toucheContinuer = "<ESPACE>";
	char* toucheRecommencer = "<R>";
	char* toucheQuitter = "<ECHAPPE>";
	
	int largeurTerrain = TAILLE_CELLULE * NB_COLONNES;
	int hauteurTerrain = TAILLE_CELLULE * NB_LIGNES;
	int milieuHorizontal = largeurTerrain / 2;

	/* Textes centrés */
	int xTitre = milieuHorizontal - (TailleChaineEcran(titre, TAILLE_TITRE) / 2);
	int yTitre = hauteurTerrain / 5;

	int xContinuer = milieuHorizontal - (TailleChaineEcran(continuer, TAILLE_OPTION) / 2);
	int yContinuer = 2*hauteurTerrain / 5;
	int xToucheContinuer = milieuHorizontal - (TailleChaineEcran(toucheContinuer, TAILLE_TOUCHE) / 2);
	int yToucheContinuer = yContinuer + TailleSupPolice(TAILLE_TOUCHE + 1);

	int xRecommencer = milieuHorizontal - (TailleChaineEcran(recommencer, TAILLE_OPTION) / 2);
	int yRecommencer = 3*hauteurTerrain / 5;
	int xToucheRecommencer = milieuHorizontal - (TailleChaineEcran(toucheRecommencer, TAILLE_TOUCHE) / 2);
	int yToucheRecommencer = yRecommencer + TailleSupPolice(TAILLE_TOUCHE + 1);

	int xQuitter = milieuHorizontal - (TailleChaineEcran(quitter, TAILLE_OPTION) / 2);
	int yQuitter = 4*hauteurTerrain / 5;
	int xToucheQuitter = milieuHorizontal - (TailleChaineEcran(toucheQuitter, TAILLE_TOUCHE) / 2);
	int yToucheQuitter = yQuitter + TailleSupPolice(TAILLE_TOUCHE + 1);

	/* Afficher titre, options, touches */
	ChoisirCouleurDessin(COULEUR_PASTILLE);
	EcrireTexte(xTitre, yTitre, titre, TAILLE_TITRE);
	
	ChoisirCouleurDessin(COULEUR_CADRE);

	EcrireTexte(xContinuer, yContinuer, continuer, TAILLE_OPTION);
	EcrireTexte(xToucheContinuer, yToucheContinuer, toucheContinuer, TAILLE_TOUCHE);
	EcrireTexte(xRecommencer, yRecommencer, recommencer, TAILLE_OPTION);
	EcrireTexte(xToucheRecommencer, yToucheRecommencer, toucheRecommencer, TAILLE_TOUCHE);
	EcrireTexte(xQuitter, yQuitter, quitter, TAILLE_OPTION);
	EcrireTexte(xToucheQuitter, yToucheQuitter, toucheQuitter, TAILLE_TOUCHE);

}

/* Afficher le menu de fin de partie,
avec les options : recommencer ou quitter. 
Couleur des options : COULEUR_VIDE */
void afficherMenuPerdu() {
	/* Chaines de caractères à afficher */
	/* Titre */
	char* titre = "PERDU";
	/* Options */
	char* rejouer = "Rejouer";
	char* quitter = "Quitter";
	/* Touches à entrer */
	char* toucheRejouer = "<ENTREE>";
	char* toucheQuitter = "<ECHAPPE>";
	
	int largeurTerrain = TAILLE_CELLULE * NB_COLONNES;
	int hauteurTerrain = TAILLE_CELLULE * NB_LIGNES;
	int milieuHorizontal = largeurTerrain / 2;

	/* Centrer textes. */
	int xTitre = milieuHorizontal - (TailleChaineEcran(titre, TAILLE_TITRE) / 2);
	int yTitre = hauteurTerrain / 4;

	int xRejouer = milieuHorizontal - (TailleChaineEcran(rejouer, TAILLE_OPTION) / 2);
	int yRejouer = hauteurTerrain / 2;
	int xToucheRejouer = milieuHorizontal - (TailleChaineEcran(toucheRejouer, TAILLE_TOUCHE) / 2);
	int yToucheRejouer = yRejouer + TailleSupPolice(TAILLE_TOUCHE + 1);

	int xQuitter = milieuHorizontal - (TailleChaineEcran(quitter, TAILLE_OPTION) / 2);
	int yQuitter = 3*hauteurTerrain / 4;
	int xToucheQuitter = milieuHorizontal - (TailleChaineEcran(toucheQuitter, TAILLE_TOUCHE) / 2);
	int yToucheQuitter = yQuitter + TailleSupPolice(TAILLE_TOUCHE + 1);

	/* Afficher titre, options, et touches. */
	ChoisirCouleurDessin(COULEUR_PASTILLE);
	EcrireTexte(xTitre, yTitre, titre, TAILLE_TITRE);
	
	ChoisirCouleurDessin(COULEUR_VIDE);

	EcrireTexte(xRejouer, yRejouer, rejouer, TAILLE_OPTION);
	EcrireTexte(xToucheRejouer, yToucheRejouer, toucheRejouer, TAILLE_TOUCHE);
	EcrireTexte(xQuitter, yQuitter, quitter, TAILLE_OPTION);
	EcrireTexte(xToucheQuitter, yToucheQuitter, toucheQuitter, TAILLE_TOUCHE);

}


/* Remplacer le terrain par un rectangle de couleur COULEUR_CADRE */
void cacherTerrain() {
	int xTerrain = PADDING * TAILLE_CELLULE;
	int yTerrain = PADDING * TAILLE_CELLULE;
	int largeurTerrain = TAILLE_CELLULE * NB_COLONNES;
	int hauteurTerrain = TAILLE_CELLULE * NB_LIGNES;

	/* Rectangle de la couleur du cadre sur le terrain */
	ChoisirCouleurDessin(COULEUR_CADRE);
	RemplirRectangle(xTerrain, yTerrain, largeurTerrain, hauteurTerrain);
}

/* Mettre à jour le temps restant (format mm : ss) et le 
score (sur 4 chiffres) dans la section en dessous du terrain */ 
void updateAffichageInfos(Jeu jeu) {
	/* Coordonnées du score */
	int xScore;
	int yScore;
	/* Chaines de caractères à afficher */
	char scoreStr[5], tempsStr[8];
	/* Valeurs à afficher pour les minutes et secondes restantes */
	int tempsRestantSec = jeu.tempsRestant/1000000;
	int min = tempsRestantSec / 60;
	int sec = tempsRestantSec % 60;
	/* Coordonnées du temps restant : en bas à gauche */
	int xTemps = PADDING * TAILLE_CELLULE;
	int yTemps = (NB_TOTAL_LIGNES - PADDING) * TAILLE_CELLULE; 
	
	/* Remplir les chaines pour l'affichage. */
	sprintf(scoreStr, "%04d", jeu.score);
	sprintf(tempsStr, "%02d : %02d", min, sec);

	/* Coordonnées du score : en bas à droite */
	xScore = (NB_TOTAL_COLONNES * TAILLE_CELLULE) 
			 - ((PADDING * TAILLE_CELLULE) + TailleChaineEcran(scoreStr, TAILLE_INFOS_TEXTE));
	yScore = (NB_TOTAL_LIGNES - PADDING) * TAILLE_CELLULE; 

	afficherCadre();

	/* Ecrire les infos de la couleur du terrain. */
	ChoisirCouleurDessin(COULEUR_VIDE);
	EcrireTexte(xTemps, yTemps, tempsStr, TAILLE_INFOS_TEXTE);
	EcrireTexte(xScore, yScore, scoreStr, TAILLE_INFOS_TEXTE);

}

/* Demander le nom à associer à un score.
Appelé en cas de score à ajouter aux records,  */ 
void afficherDemandeTag() {
	/* Chaines de caractères à afficher */
	char* titre = "Nouveau record dans le top 10 !";
	char* instruction = "Entrer le nom a associer au score [3 lettres de A a Z] :";

	int largeurTerrain = TAILLE_CELLULE * NB_COLONNES;
	int hauteurTerrain = TAILLE_CELLULE * NB_LIGNES;
	int milieuHorizontal = largeurTerrain / 2;

	/* Coordonnées du titre : en haut, centré. */
	int xTitre = milieuHorizontal - (TailleChaineEcran(titre, TAILLE_TITRE) / 2);
	int yTitre = hauteurTerrain / 3;

	/* Coordonnées de l'instruction : sous le titre, centrée. */
	int xInstruction = milieuHorizontal - (TailleChaineEcran(instruction, TAILLE_OPTION) / 2);
	int yInstruction = yTitre + TailleSupPolice(TAILLE_OPTION) + TailleInfPolice(TAILLE_TITRE) + 4;

	/* Ecrire demande tag */
	ChoisirCouleurDessin(COULEUR_PASTILLE);
	EcrireTexte(xTitre, yTitre, titre, TAILLE_TITRE);

	ChoisirCouleurDessin(COULEUR_VIDE);
	EcrireTexte(xInstruction, yInstruction, instruction, TAILLE_OPTION);

}

/* Afficher un caractère du tag associé à un nouveau record à la position 
corresondant à l'indice du caractère dans le nom qui est en train d'etre saisi. */
void afficherCharTag(char inputChar, int pos) {
	/* Coordonnées du caractère à afficher. */
	int xChar, yChar;
	/* Chaine de caractère à afficher. */
	char nvCharStr[2];

	int milieuHorizontal = (TAILLE_CELLULE * NB_COLONNES) / 2;
	int hauteurTerrain = TAILLE_CELLULE * NB_LIGNES;

	/* Position verticale de l'instruction qui demande le tag. */
	int yInstruction = (hauteurTerrain / 3) + TailleSupPolice(TAILLE_OPTION) 
					   + TailleInfPolice(TAILLE_TITRE) + 4;

	/* Mettre le caractère inputChar en majuscule dans nvCharStr. */
	if(inputChar >= 'a' && inputChar <= 'z') {
		sprintf(nvCharStr, "%c", inputChar - 32);
	} else {
		sprintf(nvCharStr, "%c", inputChar);
	}

	/* Initialiser position du caractère selon son indice. */
	xChar = milieuHorizontal - (TailleChaineEcran(nvCharStr, TAILLE_OPTION) / 2) 
			+ (pos - 1) * TailleChaineEcran(nvCharStr, TAILLE_OPTION);
	yChar = yInstruction + TailleSupPolice(TAILLE_OPTION) + TailleInfPolice(TAILLE_OPTION) + 4;

	/* Afficher le caractère entré. */
	ChoisirCouleurDessin(COULEUR_VIDE);
	EcrireTexte(xChar, yChar, nvCharStr, TAILLE_OPTION);
}

/* Afficher la liste des records (score et nom) dans records.txt */
void afficherRecords() {
	int nbRecords = getNbRecords();
	/* Tableaux pour stocker les scores et tags associés aux records. */
	int scoresRecords[NB_RECORDS_MAX];
	char tagsRecords[NB_RECORDS_MAX][4];

	if(nbRecords > 0) {
		/* Prendre les valeurs des scores et tag de records.txt */
		remplirScoresRecords(scoresRecords, nbRecords); 
		remplirTagsRecords(tagsRecords, nbRecords);

		/* Les afficher. */
		afficherTableRecords(scoresRecords, tagsRecords, nbRecords);
	}
}

/* Afficher la liste des records, étant donné les scores, les tags associés et le nombre de records */
void afficherTableRecords(int scoresRecords[], char tagsRecords[][4], int nbRecords) {
	/* Compteur pour itération de boucle for */
	int count;
	/* Chaines de caractères à afficher */
	char* titreStr = "RECORDS";
	char scoreStr[6];
	char tagStr[4];
	/* Positions des chaines à afficher */
	int xTitre;
	int yTitre;
	int xScore;
	int yScore;
	int xTag;
	int yTag;

	if(nbRecords == 0) return;
	
	/* Titres de colonne */
	scoreStr[0] = 'S';
	scoreStr[1] = 'C';
	scoreStr[2] = 'O';
	scoreStr[3] = 'R';
	scoreStr[4] = 'E';
	scoreStr[5] = '\0';
	tagStr[0] = 'T';
	tagStr[1] = 'A';
	tagStr[2] = 'G';
	tagStr[3] = '\0';

	/* Initialiser positions des titres de colonne. */
	xScore = (4 * (TAILLE_CELLULE * NB_TOTAL_COLONNES) / 5) - (TailleChaineEcran(scoreStr, 1) / 2);
	yScore = (TAILLE_CELLULE * NB_TOTAL_LIGNES) / 4;
	xTag = xScore + TailleChaineEcran(scoreStr, 1) + 20;
	yTag = yScore;
	xTitre = ((xScore + xTag + TailleChaineEcran(tagStr, 1)) / 2) - (TailleChaineEcran(titreStr, 2) / 2);
	yTitre = yScore - TailleSupPolice(1) - TailleInfPolice(2);	
	
	/* Afficher le titre de la table. */
	ChoisirCouleurDessin(COULEUR_PASTILLE);
	EcrireTexte(xTitre, yTitre, titreStr, 2);
	/* Afficher les titres des colonnes. */
	ChoisirCouleurDessin(COULEUR_VIDE);
	EcrireTexte(xScore, yScore, scoreStr, 1);
	EcrireTexte(xTag, yTag, tagStr, 1);

	/* Afficher les valeurs des scores et tags dans les tableaux fournis,
	en dessous des titres de colonne. */
	for(count = 0; count < nbRecords; count++) {
		yScore += TailleSupPolice(1) + TailleInfPolice(1) + 4;
		yTag += TailleSupPolice(1) + TailleInfPolice(1) + 4;

		sprintf(scoreStr, "%04d", scoresRecords[count]);
		sprintf(tagStr, "%s", tagsRecords[count]);

		EcrireTexte(xScore, yScore, scoreStr, 1);
		EcrireTexte(xTag, yTag, tagStr, 1);
	}
}