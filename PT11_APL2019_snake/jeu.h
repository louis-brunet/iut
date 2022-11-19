#ifndef JEU_H
#define JEU_H

#include "terrain.h"
#include "snake.h"

typedef struct{
	Snake snake;
	Terrain terrain;
	/* Temps restant en microsecondes,
	s'écoule quand le jeu a commencé et le jeu n'est pas en pause */
	int tempsRestant; 
	unsigned int score;
	/* Coefficient de vitesse du jeu. La durée d'un cycle de la
	boucle de jeu est divisée par cette valeur. */
	unsigned short vitesseModif;
	unsigned int pause:1;
	unsigned int perdu:1;
	unsigned int rejouer:1;
	unsigned int quitter:1;
} Jeu;

int jouer(Jeu*);
Jeu initJeu();
void updateJeu(Jeu*, unsigned int);

#endif /* JEU_H */