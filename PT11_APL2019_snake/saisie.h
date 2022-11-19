#ifndef SAISIE_H
#define SAISIE_H

#include"jeu.h"
#include"records.h"

int gererSaisieJeu(Jeu*);
int gererSaisiePause(Jeu*);
void gererSaisiePerdu(Jeu*);
void saisieTag(char[TAILLE_TAG + 1]);

#endif /* SAISIE_H */