#ifndef SNAKE_H
#define SNAKE_H

#include "cellule.h"
#include "terrain.h"

/* Direction initiale du joueur */
#define DIRECTION_INITIALE HAUT
/* Taille initiale du joueur - le nombre de segments du serpent */
#define TAILLE_INITIALE 10

typedef enum {HAUT, BAS, GAUCHE, DROITE} Direction;

typedef struct {
	unsigned int taille;
	unsigned int tailleVoulue;
	Position positions[NB_CELLULES];
	Direction direction;
	Direction directionVoulue;
	int vitesse; /* le nombre de cellules parcourues par itération de la boucle principale */
} Snake;

#define SNAKE_DEFINI

/* Crée et renvoie un Snake avec un tableau de Position pouvant contenir nbCellules valeurs,
initialisé avec TAILLE_INITIALE positions relatives à la position (xTeteInitial; yTeteInitial) */
Snake initSnake(int, int, int);

/* Ajoute la position donnée en argument en tête de snake
i.e. au début du tableau snake.positions, en décalant les
autres valeurs de snake.positions d'un indice */
void ajouterPositionTete(Snake*, Position);


#endif /* SNAKE_H */