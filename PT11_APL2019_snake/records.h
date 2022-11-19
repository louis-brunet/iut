#ifndef RECORDS_H
#define RECORDS_H

/* Nombre de records maximum dans records.txt */
#define NB_RECORDS_MAX 10
/* Nombre de caractères constituant un tag (nom de joueur) */
#define TAILLE_TAG 3

/* Renvoie le nombre de records inscrits dans records.txt.
Les records sont séparés par CHAR_FIN_RECORD et contiennent un score et un tag */
int getNbRecords();
/* Renvoie le score au rang donné dans le fichier records.txt.
La première ligne correspong au premier rang (le score le plus haut) */
int getScoreParRang(int);
/* Remplir un tableau avec les scores du fichier records.txt.
Renvoie le nombre de scores ajoutés. */
void remplirScoresRecords(int[], int);
/* Remplir un tableau avec les tags (noms de joueur) du fichier records.txt.
Renvoie le nombre de tags ajoutés. */
void remplirTagsRecords(char[][TAILLE_TAG + 1], int);
/* Rajouter un score sur records.txt. Renvoie le 
rang (1-10) du score ou 0 si le score n'est pas dans le top 10 */
int gererCreationRecord(int);

#endif /* RECORDS_H */