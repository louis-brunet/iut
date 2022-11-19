#ifndef AFFICHAGE_H
#define AFFICHAGE_H

#include "cellule.h"
#include "terrain.h"
#include "jeu.h"

int initAffichage(Jeu);
void afficherCadre();
void afficherTerrainEntier();
void rafraichirCellulesChangees(Terrain*);
void dessinerCelluleTerrain(Cellule cellule);
void dessinerContoursSnake(Snake);
void changerCouleurDessin(ContenuCellule);
void afficherMenuPause();
void afficherMenuPerdu();
void cacherTerrain();
void updateAffichageInfos(Jeu);
void afficherDemandeTag();
/* Afficher un caractère du tag associé à un nouveau record à la position 
corresondant à l'indice du caractère dans le nom qui est en train d'etre saisi. */
void afficherCharTag(char, int);
void afficherRecords();


#endif /* AFFICHAGE_H */