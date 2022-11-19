#include<stdlib.h>
#include<stdio.h>
#include<graph.h>
#include"jeu.h"
#include"cellule.h"
#include"saisie.h"
#include"affichage.h"

/* Longueur d'un cycle de rafraichissement de l'écran en microsecondes 
0.5 sec */
#define DUREE_CYCLE /*500000L*/300000L
/* Temps restant initial en secondes
3 minutes*/
#define TEMPS_MAX 600
/* Score par pastille mangée */
#define PASTILLE_SCORE 5
/* Taille gagnée par le serpent par pastille mangée,
en nombre de cellules */
#define PASTILLE_GAIN_TAILLE 2

void gererCreationObstacle(Jeu*);
void gererModificationVitesse(Jeu*);

/* Boucle d'evenements principale.
S'arrête si la partie est perdue ou si le jeu est mis en pause.
Renvoie 1 si la touche echappe est appuyée. */
int jouer(Jeu* jeuPtr) {	
	/* Durée entre chaque frame */
	unsigned int tempsDelta;
	/* date en microsecondes, prend successivement la date de chaque frame comme valeur */
	unsigned long dateFrame = Microsecondes();

	/* date minimum de la prochaine frame */
	unsigned long dateSuivante = dateFrame + (DUREE_CYCLE / jeuPtr->vitesseModif);
	
    while(!jeuPtr->pause && !jeuPtr->perdu){
    	if(Microsecondes() > dateSuivante) {

 			tempsDelta = Microsecondes() - dateFrame;
 			dateFrame = Microsecondes();
    		/* Mettre à jour date de la prochaine frame */
    		dateSuivante = dateFrame + (DUREE_CYCLE / jeuPtr->vitesseModif);
    		

    		/* Changer jeuPtr->snake.direction selon la saisie du clavier.
    		Mettre le jeu en pause et afficher le menu PAUSE si la touche Espace est appuyée.
    		Quitter si la touche Echappe est appuyée. */
    		if(gererSaisieJeu(jeuPtr))
    			return 1;

    		/* Mettre à jour le jeu selon le temps écoulé. */
    		updateJeu(jeuPtr, tempsDelta);

    		/* Redessiner les cellules du terrain qui ont changé. */
    		rafraichirCellulesChangees(&jeuPtr->terrain);

    		/* Si le jeu n'est pas en pause, redessiner les contours du serpent. */
    		if(! jeuPtr->pause )
    			dessinerContoursSnake(jeuPtr->snake);

    		/* Si le jeu est perdu, quitter. Sinon, afficher le temps restant et le score. */
    		if(jeuPtr->tempsRestant <= 0) {
    			jeuPtr->perdu = 1;
    		} else {
    			updateAffichageInfos(*jeuPtr);
    		}
    	}
    }
    return 0;
}

Jeu initJeu() {
	/* Le Jeu à renvoyer */
	Jeu res;
	/* Le serpent */
	Snake player = initSnake(NB_CELLULES, X_INITIAL, Y_INITIAL);
	/* Terrain de jeu */
	Terrain terrain = initTerrain(player);

	res.snake = player;
	res.terrain = terrain;
	res.score = 0;
	res.tempsRestant = TEMPS_MAX * 1000000;
	res.pause = 0;
	res.perdu = 0;
	res.rejouer = 0;
	res.quitter = 0;
	res.vitesseModif = 1;

	return res;
}
 
void updateJeu(Jeu* jeuPtr, unsigned int tempsDelta){
	if(!jeuPtr->pause && !jeuPtr->perdu) {

		/* Bouger snake selon sa direction et 
		stocker le contenu de la cellule écrasée */
		ContenuCellule contenuEcrase = updateSnake(&jeuPtr->snake, &jeuPtr->terrain);

		/* Diminuer le temps restant à cette partie 
		par le temps entre cette frame et celle d'avant */
		jeuPtr->tempsRestant -= tempsDelta;

		/* Quitter la boucle si le serpent sort du terrain 
		ou se mord la queue */
		if(contenuEcrase == SNAKE || contenuEcrase == CADRE 
		   || contenuEcrase == OBSTACLE || jeuPtr->tempsRestant <= 0) {
			jeuPtr->perdu = 1;
		} 
		/* Augmenter la taille voulue du serpent
		et générer une  nouvelle pastille
		s'il mange en mange une */
		else if(contenuEcrase == PASTILLE) {
			jeuPtr->snake.tailleVoulue += PASTILLE_GAIN_TAILLE;
			jeuPtr->score += PASTILLE_SCORE;
			nouvellePastille(&jeuPtr->terrain);
		}

		gererModificationVitesse(jeuPtr);

		gererCreationObstacle(jeuPtr);
	} 
}

/* Augmenter la vitesse de jeu selon le temps restant. 
Niveau deux : 2x plus vite
Niveau trois : 3x plus vite */
void gererModificationVitesse(Jeu* jeuPtr) {
	/* Niveau deux si le temps restant est inférieur à 2/3 du temps max */
	int changerNiveauDeux = (jeuPtr->vitesseModif < 2) 
							&& (jeuPtr->tempsRestant / 1000000) < (2*TEMPS_MAX/3);
	/* Niveau trois si le temps restant est inférieur à 1/3 du temps max */
	int changerNiveauTrois = (jeuPtr->vitesseModif < 3) 
							&& (jeuPtr->tempsRestant / 1000000) < (TEMPS_MAX/3);
	if(changerNiveauDeux) {
		jeuPtr->vitesseModif = 2;
	}
	if(changerNiveauTrois) {
		jeuPtr->vitesseModif = 3;
	}

}

/* Créer un nouvel obstacle toutes les trois pastilles mangées */
void gererCreationObstacle(Jeu* jeuPtr) {
	int vientDeManger = jeuPtr->snake.taille == (jeuPtr->snake.tailleVoulue - PASTILLE_GAIN_TAILLE);
	int nbPastillesMangees = (jeuPtr->snake.tailleVoulue - TAILLE_INITIALE) / PASTILLE_GAIN_TAILLE;

	if( vientDeManger && (nbPastillesMangees % 3 == 0) ) {
		nouvelObstacle(&jeuPtr->terrain);
	}
}