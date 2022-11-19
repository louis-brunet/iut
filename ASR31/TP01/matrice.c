#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include "matrice.h"

void printLigne(element* , unsigned short int );

void ajoutFinLigne (element** ligne, element* elem) 
{
	element* l = *ligne;

	if (l == NULL) 
	{
		*ligne = elem;
	}
	else
	{
		while ( l->li != NULL ); 
		{
			l = l->li;
		}
		l->li = elem;
	}
}

void ajoutFinColonne (element** col, element* elem) 
{
	element* c = *col;

	if (c == NULL) 
	{
		*col = elem;
	}
	else
	{
		while ( c->co != NULL ); 
		{
			c = c->co;
		}
		c->co = elem;
	}
}


void creation(matrice* M, unsigned short int nb_ligne, unsigned short int nb_col) 
{
	srand(time(NULL));

	M->nb_l = nb_ligne;
	M->nb_c = nb_col;

	M->ligne = (element**) malloc(nb_ligne * sizeof(element*));
	M->colonne = (element**) malloc(nb_col * sizeof(element*));


	for (int i = 0; i < nb_ligne; i++) 
		M->ligne[i] = NULL;
	
	for (int j = 0; j < nb_col; j++)
		M->colonne[j] = NULL;
	


	for (int i = 0; i < nb_ligne; i++) 
	{
		for (int j = 0; j < nb_col; j++)
		{
			if ( rand() % 10 == 0 )
			{
				char valeur = (rand() % 21) - 10;
				if ( valeur != 0 ) 
				{
					element* e = (element*) malloc(sizeof(element));
					e->i = i;
					e->j = j;
					e->valeur = valeur;
					e->li = NULL;
					e->co = NULL;

					ajoutFinLigne(&M->ligne[i], e);
					ajoutFinColonne(&M->colonne[j],  e);
				}
			}
			
		}
	}


 

	// (rand() % 21) - 10;

}

void printMat(matrice* M, unsigned short int nb_ligne, unsigned short int nb_col) 
{
	for (int i = 0; i < nb_ligne; ++i)
	{
		printLigne(M->ligne[i], nb_col);
		printf("\n");
	}
}

void printLigne(element* ligne, unsigned short int nb_col) 
{
	element* e = ligne;
	int col = 0;
	if(e == NULL) {
		for(int i=0; i<nb_col; i++) printf("  0 ");
		printf("\n");
		return;
	}
	while ( e->li != NULL) 
	{
		if ( e->j == col )
		{
			printf("%3d ", e->valeur);
			e = e->li;
		} else 
		{
			printf("  0 ");
		}
		col++;
	}
}
