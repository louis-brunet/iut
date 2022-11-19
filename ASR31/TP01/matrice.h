#ifndef MATRICE_H
#define MATRICE_H

typedef struct element
{
	char valeur;
	short i, j;
	struct element* li;
	struct element* co;
} element;


typedef struct matrice 
{
	unsigned short int nb_l, nb_c;
	element** ligne;
	element** colonne;
} matrice;

void creation(matrice*, unsigned short int, unsigned short int);
void printMat(matrice*, unsigned short int, unsigned short int);

#endif