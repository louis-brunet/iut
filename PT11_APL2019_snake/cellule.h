#ifndef CELLULE_H
#define CELLULE_H

typedef struct {
	int x;
	int y;
} Position;

typedef enum {
	SNAKE, /*Vert*/
	PASTILLE, /*Pomme*/
	OBSTACLE, /*Pomme empoisonnée*/
	VIDE,  /*Blanc*/
	CADRE /*Noir*/
} ContenuCellule;

typedef struct{
	ContenuCellule contenu;
	Position position;
} Cellule;


int positionDansTab(Position, Position[], int);

int coordonneesDansTab(int, int, Position[], int);

#endif /* CELLULE_H */