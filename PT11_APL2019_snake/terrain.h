#ifndef TERRAIN_H
#define TERRAIN_H

/* Dimensions du terrain de jeu */
#define NB_LIGNES 40
#define NB_COLONNES 60
/* Nombre total possible de cellules */
#define NB_CELLULES NB_LIGNES * NB_COLONNES
/* Nombre de pastilles à un moment donné */
#define NB_PASTILLES 5
/* Position initiale de la tête du serpent */
#define X_INITIAL NB_COLONNES / 2
#define Y_INITIAL NB_LIGNES / 2

#include "cellule.h"
#include "snake.h"

typedef struct {
	Cellule cellules[NB_LIGNES][NB_COLONNES];
	Position posCellulesChangees[5];
	int nbCellulesChangees;
} Terrain;

void initPastilles(Position[], Position[]);
void nouvellePastille(Terrain*);
void nouvelObstacle(Terrain*);

#ifdef SNAKE_DEFINI
Terrain initTerrain(Snake); 
ContenuCellule updateSnake(Snake*, Terrain*);
#endif

#endif