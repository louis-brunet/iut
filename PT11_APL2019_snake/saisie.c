#include<stdlib.h>
#include<stdio.h>
#include<graph.h>
#include"saisie.h"
#include"affichage.h"

/* Changer la direction de snake si une fleche directionnelle est appuyée.
Mettre en pause et afficher menu si la touche espace est appuyée.
Renvoie 1 si la touche échappe est appuyée (si la partie doit se terminer) */
int gererSaisieJeu(Jeu* jeuPtr) {
	while(ToucheEnAttente()){
		int touche = Touche();
		/* Espace : mettre le jeu en pause et afficher le menu pause */
		if(touche == XK_space) {
			jeuPtr->pause = 1;
			afficherMenuPause();
		}
		/* Echappe : renvoyer 1,  */
		else if(touche == XK_Escape) {
			return 1;
		}
		
		/* Changer la direction du joueur seulement si 
		la direction voulue n'est pas opposée à sa direction actuelle */
		else if(!jeuPtr->pause) {
			if(touche == XK_Up && jeuPtr->snake.direction != BAS){
				jeuPtr->snake.directionVoulue = HAUT;
			} else if(touche == XK_Down && jeuPtr->snake.direction != HAUT){
				jeuPtr->snake.directionVoulue = BAS;
			} else if(touche == XK_Left && jeuPtr->snake.direction != DROITE){
				jeuPtr->snake.directionVoulue = GAUCHE;
			} else if(touche == XK_Right && jeuPtr->snake.direction != GAUCHE){
				jeuPtr->snake.directionVoulue = DROITE;
			} 
		}
	}
	return 0;
}

/* Continuer la partie si la touche espace est appuyée.
Réinitialiser le jeu si la touche R est appuyée;
Renvoie 1 si la touche échappe est appuyée (si la partie doit se terminer) */
int gererSaisiePause(Jeu* jeuPtr) {
	while(ToucheEnAttente()){
		int touche = Touche();

		if(touche == XK_space) {
			afficherTerrainEntier(jeuPtr->terrain);
			dessinerContoursSnake(jeuPtr->snake);
			jeuPtr->pause = 0;
		} else if(touche == XK_r || touche == XK_R) {
			*jeuPtr = initJeu();
			afficherTerrainEntier(jeuPtr->terrain);
			dessinerContoursSnake(jeuPtr->snake);
			updateAffichageInfos(*jeuPtr);
		} else if(touche == XK_Escape) {
			return 1;
		}
	}
	return 0;
}

/* Recommence la partie si la touche Entrée est appuyée.
Quitte le jeu si la touche Echappe est appuyée */
void gererSaisiePerdu(Jeu* jeuPtr) {
	while(ToucheEnAttente()){
		int touche = Touche();

	 	if(touche == XK_Return) {
	 		jeuPtr->rejouer = 1;
	 	}else if(touche == XK_Escape) {
			jeuPtr->quitter = 1;
		}
	}
}

/* Gérer la saisie du tag du joueur quand un score doit être rentré dans records.txt */
void saisieTag(char tagStr[TAILLE_TAG + 1]) {
	int index;
	int touche;

	for(index = 0; index < TAILLE_TAG; index++) {
		do {
			touche = Touche();
		} while((touche < XK_a || touche > XK_z) && (touche < XK_A || touche > XK_Z));
		tagStr[index] = (char) touche;

		afficherCharTag(tagStr[index], index);
	}
	tagStr[TAILLE_TAG] = '\0';

}

