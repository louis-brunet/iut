#include <stdio.h>
#include <stdlib.h>
#include <graph.h>
#include "cellule.h"
#include "terrain.h"
#include "snake.h"
#include "jeu.h"
#include "affichage.h"
#include "saisie.h"
#include "records.h"

int main (int argc, char * argv[]){
	/* Initialiser le jeu. 
	Mettre jeu.rejouer à 1 pour rentrer dans la boucle principale. */
	Jeu jeu = initJeu();
	jeu.rejouer = 1;

	/* Initialiser affichage du cadre & terrain de jeu.
	Quitter le programme si le mode graphique n'est pas initialisable. */
	if(! initAffichage(jeu)){
		return EXIT_FAILURE;
	}

    /* Boucle d'evenements principale */
   	while(!jeu.quitter) {
   		if(jeu.rejouer) {
   			/* Si la boucle tourne car le joueur a perdu et veut rejouer,
   			alors réinitialiser le jeu et l'affichage. */
   			if(jeu.perdu){
   				jeu = initJeu();
   				afficherTerrainEntier(jeu.terrain);
   				dessinerContoursSnake(jeu.snake);
   				updateAffichageInfos(jeu);
   			} 
   			/* Sinon, la boucle tourne par la première fois, donc reinitialiser jeu.rejouer */
   			else{
   				jeu.rejouer = 0;
   			}
   			/* Boucle qui gère le jeu et le menu pause. */
   			while(!jeu.perdu){
   				/* Boucle de jeu : sort quand le joueur perd ou le jeu est en pause.
   				Quitte le programme si la touche Echappe est appuyée) */
		   		if(jouer(&jeu)){
		   			FermerGraphique();
		   			return EXIT_SUCCESS;
		   		}
		   		/* Si la boucle a quitté seulement parce que le jeu est mis en pause,
		   		gérer le choix des options du menu */
		   		if(! jeu.perdu){
		   			/* Quitter le programme si la touche Echappe est appuyée */
		   			if(gererSaisiePause(&jeu)){
		   				FermerGraphique();
		   				return EXIT_SUCCESS;
		   			}
		   		}
		   	}
		   	/* Jeu perdu */
		   	/* Remplacer le terrain par un rectangle de la couleur du cadre. */
		   	cacherTerrain();
		   	/* Demander le tag du joueur si son score est dans le top 10. */
   			if(gererCreationRecord(jeu.score))
   				cacherTerrain();
   			/* Afficher le menu PERDU et la table des records. */
   			afficherMenuPerdu();
   			afficherRecords();
   		}
   		gererSaisiePerdu(&jeu);
   	}
   	    
    FermerGraphique();

	return EXIT_SUCCESS;
}
