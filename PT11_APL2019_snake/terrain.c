#include<stdlib.h>
#include<stdio.h>
#include<time.h>
#include"terrain.h"

ContenuCellule ajouterSegmentTete(Snake*, Terrain*);
void enleverSegmentQueue(Snake*, Terrain*);
ContenuCellule changerCellule(Terrain*, Position, ContenuCellule);

/* Initialiser le joueur, les pastilles et le terrain de jeu 
en fonction des positions du joueur créé */
Terrain initTerrain(Snake player) {
	/* Positions des pastilles */
	Position positionsPastilles[NB_PASTILLES];
	int ligne;
	int col;
	Terrain terrain;

	player = initSnake(NB_CELLULES, X_INITIAL, Y_INITIAL);
	initPastilles(positionsPastilles, player.positions);

	/* Initialiser toutes les cellules de terrain, 
	dépendamment des positions de player et des pastilles */
	for (ligne = 0; ligne < NB_LIGNES; ++ligne) {
		for (col = 0; col < NB_COLONNES; ++col) {
			int estPastille;
			int estSnake;

			terrain.cellules[ligne][col].position.x = col;
			terrain.cellules[ligne][col].position.y = ligne;

			estPastille = positionDansTab(terrain.cellules[ligne][col].position,
										  positionsPastilles, NB_PASTILLES);

			estSnake =  positionDansTab(terrain.cellules[ligne][col].position, 
										player.positions, player.taille);

			if(estPastille){
				terrain.cellules[ligne][col].contenu = PASTILLE;
			} else if (estSnake){
				terrain.cellules[ligne][col].contenu = SNAKE;
			} else{
				terrain.cellules[ligne][col].contenu = VIDE;
			}
		}
	}

	terrain.nbCellulesChangees = 0;

	return terrain;
}

/* Trouver le nombre par défaut de positions de pastilles 
non occupées par le joueur dans un terrain de dimensions par défaut */
void initPastilles(Position positionsPastilles[], Position positionsSnake[]) {
	int pastCount;
	srand(time(NULL));
	for (pastCount = 0; pastCount < NB_PASTILLES; ++pastCount){
		int randX = rand() % NB_COLONNES;
		int randY = rand() % NB_LIGNES;

		int estDejaOccupe = coordonneesDansTab(randX, randY, positionsPastilles, pastCount) ||
							coordonneesDansTab(randX, randY, positionsSnake, TAILLE_INITIALE); 

		if(estDejaOccupe){
			pastCount--;
		}else{
			positionsPastilles[pastCount].x = randX;
			positionsPastilles[pastCount].y = randY;
		}
	}
}

/* Redessiner le joueur à sa nouvelle position. 
Renvoyer le contenu de la cellule écrasée par la tête du serpent */
ContenuCellule updateSnake(Snake* snakePtr, Terrain* terrainPtr) {
	ContenuCellule ancienContenu;

	snakePtr->direction = snakePtr->directionVoulue;
	/* Si la taille réelle du serpent est différente de la taille voulue
	(i.e. si le serpent a mangé une pastille), sa taille aumente de un*/
	if(snakePtr->taille == snakePtr->tailleVoulue){
		enleverSegmentQueue(snakePtr, terrainPtr);
	}

	ancienContenu = ajouterSegmentTete(snakePtr, terrainPtr);

	return ancienContenu;
}

/* Rajouter un segment en tête du serpent. 
Augmenter sa taille de 1.
Renvoyer le contenu de la cellule écrasée */
ContenuCellule ajouterSegmentTete(Snake* snakePtr, Terrain* terrainPtr) {
	int positionEstValide;

	/* prochainePos prends la valeur de la position de la tête du serpent */
	Position prochainePos = snakePtr->positions[0];

	/* prochainePos varie en fonction de la direction de snake */
	switch(snakePtr->direction) {
	case HAUT:
		prochainePos.y -= 1;
		break;
	case BAS:
		prochainePos.y += 1;
		break;
	case GAUCHE:
		prochainePos.x -= 1;
		break;
	case DROITE:
		prochainePos.x += 1;
		break;
	}

	positionEstValide = prochainePos.x >= 0 && prochainePos.x < NB_COLONNES
						&& prochainePos.y >= 0 && prochainePos.y < NB_LIGNES;
	if(!positionEstValide) {
		return CADRE;
	}

	ajouterPositionTete(snakePtr, prochainePos);

	return changerCellule(terrainPtr, prochainePos, SNAKE);
}

/* Remplacer le dernier segment de snake dans terrain par une cellule vide.
Diminuer sa taille de 1. */
void enleverSegmentQueue(Snake* snakePtr, Terrain* terrainPtr) {
	changerCellule(terrainPtr, snakePtr->positions[snakePtr->taille - 1], VIDE);

	snakePtr->positions[snakePtr->taille - 1].x = -1;
	snakePtr->positions[snakePtr->taille - 1].y = -1;
	snakePtr->taille--;
}

/* Renvoyer le contenu de la cellule de position pos avant qu'elle soit remplacée */
ContenuCellule changerCellule(Terrain* terrainPtr, Position pos, ContenuCellule contenu) {
	ContenuCellule ancienContenu = terrainPtr->cellules[pos.y][pos.x].contenu ;

	terrainPtr->cellules[pos.y][pos.x].contenu = contenu;
	terrainPtr->posCellulesChangees[terrainPtr->nbCellulesChangees] = pos;
	terrainPtr->nbCellulesChangees++;

	return ancienContenu;
}

/* Essayer de créer une nouvelle pastille dans une cellule non occupée du terrain.
Si au bout de NB_CELLULES essais, aucune position aléatoire du terrain est vide, 
alors il n'y probablement pas de cellule disponible, donc aucune pastille n'est créée */ 
void nouvellePastille(Terrain* terrainPtr) {
	int randX;
	int randY;
	int count;
	int posTrouvee = 0;

	srand(time(NULL));
	for(count = 0; !posTrouvee && (count < NB_CELLULES); count++) {
		randX = rand() % NB_COLONNES;
		randY = rand() % NB_LIGNES;

		if(terrainPtr->cellules[randY][randX].contenu == VIDE) {
			Position pos;
			pos.x = randX;
			pos.y = randY;
			posTrouvee = 1;

			changerCellule(terrainPtr, pos, PASTILLE);
		}
	}
}

/* Similairement à nouvellePastille(), éssayer de créer un nouvel obstacle sur le terrain. */
void nouvelObstacle(Terrain* terrainPtr) {
	int randX;
	int randY;
	int count;
	int posTrouvee = 0;

	srand(time(NULL));
	for(count = 0; !posTrouvee && (count < NB_CELLULES); count++) {
		randX = rand() % NB_COLONNES;
		randY = rand() % NB_LIGNES;

		if(terrainPtr->cellules[randY][randX].contenu == VIDE) {
			Position pos;
			pos.x = randX;
			pos.y = randY;
			posTrouvee = 1;

			changerCellule(terrainPtr, pos, OBSTACLE);
		}
	}
}