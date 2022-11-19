#include<stdlib.h>
#include"snake.h"

/* Crée et renvoie un Snake avec un tableau de Position
pouvant contenir nbCellules valeurs, initialisé avec TAILLE_INITIALE 
positions relatives à la position (xTeteInitial; yTeteInitial) */
Snake initSnake(int nbCellules, int xTeteInitial, int yTeteInitial) {
	Snake res;

	int i;
	for (i = 0; i < nbCellules; ++i) {
		if(i < TAILLE_INITIALE){
			switch(DIRECTION_INITIALE){
			case HAUT:
				res.positions[i].x = xTeteInitial;
				res.positions[i].y = yTeteInitial + i;
				break;
			case BAS:
				res.positions[i].x = xTeteInitial;
				res.positions[i].y = yTeteInitial - i;
				break;
			case GAUCHE:
				res.positions[i].x = xTeteInitial + i;
				res.positions[i].y = yTeteInitial;
				break;
			case DROITE:
				res.positions[i].x = xTeteInitial - i;
				res.positions[i].y = yTeteInitial;
				break;
			}
		} else{
			res.positions[i].x = -1;
			res.positions[i].y = -1;
		}
	}

	res.tailleVoulue = TAILLE_INITIALE;
	res.taille = TAILLE_INITIALE;
	res.direction = DIRECTION_INITIALE;
	res.directionVoulue = DIRECTION_INITIALE;
	res.vitesse = 1;
	return res;
}

/* Ajoute la position donnée en argument en tête de snake,
i.e. au début du tableau snake.positions, en décalant les 
autres valeurs de ce tableau d'un indice */
void ajouterPositionTete(Snake* snakePtr, Position prochainePos) {
	/* Debug */
	/*printf("ajouterPositionTete : yInit{%d,%d,%d,%d,%d,%d,%d,%d,%d,%d} ", snakePtr->positions[0].y,
																		   snakePtr->positions[1].y,
																		   snakePtr->positions[2].y,
																		   snakePtr->positions[3].y,
																		   snakePtr->positions[4].y,
																		   snakePtr->positions[5].y,
																		   snakePtr->positions[6].y,
																		   snakePtr->positions[7].y,
																		   snakePtr->positions[8].y,
																		   snakePtr->positions[9].y);
*/
	int segment;
	for(segment = snakePtr->taille; segment > 0; segment--) {
		snakePtr->positions[segment] = snakePtr->positions[segment - 1];
	}
	snakePtr->positions[0] = prochainePos;
	snakePtr->taille++;

	/* Debug */
/*	printf("yFinal{%d,%d,%d,%d,%d,%d,%d,%d,%d,%d} \n", snakePtr->positions[0].y,
												      snakePtr->positions[1].y,
												      snakePtr->positions[2].y,
												      snakePtr->positions[3].y,
												      snakePtr->positions[4].y,
												      snakePtr->positions[5].y,
												      snakePtr->positions[6].y,
												      snakePtr->positions[7].y,
												      snakePtr->positions[8].y,
												      snakePtr->positions[9].y);
*/
}