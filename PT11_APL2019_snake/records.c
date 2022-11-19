#include <stdlib.h>
#include <stdio.h>
#include "records.h"
#include "saisie.h"
#include "affichage.h"

/* Caractère à ajouter pour séparer les records dans records.txt */
#define CHAR_FIN_RECORD '\n'

void ajouterRecord(int, int, int);
int scorePresent(FILE*);
void demanderTag(char[TAILLE_TAG + 1]);
void getTag(FILE*, char[TAILLE_TAG + 1]);


/* Renvoie le nombre de records inscrits dans records.txt.
Renvoie 0 si le fichier n'existe pas.
Les records sont séparés par CHAR_FIN_RECORD et contiennent un score et un tag */
int getNbRecords() {
	FILE* fiRecords;
	int nbRecords = 0;	

	fiRecords = fopen("records.txt", "r");
	if(fiRecords) {
		/* Compter le nombre d'entrées selon le format:
		un int, un tag (chaine de 3 lettres [A-Z]), et CHAR_FIN_RECORD */
		while(!feof(fiRecords)) {

			if(scorePresent(fiRecords)) {
				nbRecords++;
			}
		}

		fclose(fiRecords);
	}

	return nbRecords;
}

/* Renvoie le score à la position actuelle de lecture de fiRecords
si un record y est effectivement présent, sinon, renvoie 0. */
int scorePresent(FILE* fiRecords) {
	char intStr[5];
	char tagStr[TAILLE_TAG];
	char charTemp;
	int intPresent;
	int tagPresent;
	int charFinPresent;
	
	intPresent = (fread(intStr, sizeof(char), 4, fiRecords) == 4);
	tagPresent = (fread(tagStr, sizeof(char), TAILLE_TAG, fiRecords) == TAILLE_TAG);
	charFinPresent = fread(&charTemp, sizeof(char), 1, fiRecords) && (charTemp == CHAR_FIN_RECORD);

	if(intPresent && tagPresent && charFinPresent ) {
		int score;

		intStr[4] = '\0';
		sscanf(intStr, "%d", &score);

		return score;
	} else return 0;
}


/* Renvoie le score au rang donné dans le fichier records.txt, 
ou -1 si le rang n'est pas valide ou si le fichier ne peut pas etre ouvert.
La première ligne correspong au premier rang (le score le plus haut) */
int getScoreParRang(int rang) {
	FILE* fiRecords;
	int nbRecords = getNbRecords();

	fiRecords = fopen("records.txt", "r");
	if((rang <= nbRecords) && (rang > 0) && fiRecords){
		int score;
		int count;

		for(count = 0; count < rang; count++) {
			score = scorePresent(fiRecords);
		}
		if(score) {
			return score;
		}

	}
	return -1;
}

/* Rajouter un score sur records.txt. Renvoie le rang (1-10) 
du score ou -1 si le score n'est pas dans le top 10, ou égal à zéro */
int gererCreationRecord(int score) {
	int rang;
	int scoreTemp;
	int scoreAjoute;
	int nbRecords;

	if(score == 0) {
		return 0;
	}

	scoreAjoute = 0;
	nbRecords = getNbRecords();
	if(nbRecords == 0){
		ajouterRecord(score, 1, nbRecords);
		return 1;
	}
	for(rang = 1; rang <= nbRecords && !scoreAjoute; rang++) {
		scoreTemp = getScoreParRang(rang);

		if(score > scoreTemp) {
			ajouterRecord(score, rang, nbRecords);
			scoreAjoute = 1;

			return rang;
		}else if(rang == nbRecords && rang != NB_RECORDS_MAX){
			ajouterRecord(score, rang+1, nbRecords);
			scoreAjoute = 1;

			return rang+1;
		}
	}

	return 0;

}

/* Insérer un record au rang donné, en déplaçant les autres d'un rang,
et en gardant le nombre de records incrits inférieur ou égal à NB_RECORDS_MAX */
void ajouterRecord(int score, int rang, int nbRecords) {
	FILE* fiRecords;
	char tagStr[TAILLE_TAG + 1];
	char* finFichierStr;
	char nvRecordStr[9];
	int tailleRecord = 5 * (sizeof (char)) + (TAILLE_TAG * sizeof(char));
	int nbRecordsDeplaces = (nbRecords < NB_RECORDS_MAX ? nbRecords + 1 : NB_RECORDS_MAX) - rang;
	
	finFichierStr = (char*) calloc(nbRecordsDeplaces , tailleRecord);
	
	/* Demander tag au joueur */
	demanderTag(tagStr);
	/*sprintf(tagStr, "%s%c", tagStr, CHAR_FIN_RECORD);*/
	sprintf(nvRecordStr, "%04d%s%c", score, tagStr, CHAR_FIN_RECORD);

	fiRecords = fopen("records.txt", "r+");
	/* Cas où le fichier existe déjà */
	if(fiRecords) {
		/* Positioner la lecture du fichier à l'endroit
		où le record doit etre écrit. */
		fseek(fiRecords, (rang-1) * tailleRecord, SEEK_SET);
		/* Stocker les records à déplacer dans finFichierStr */
		fread(finFichierStr, tailleRecord, nbRecordsDeplaces, fiRecords);

		/* Repositionner le pointer fiRecords pour écrire le nouveau 
		record et les records stockés */
		fseek(fiRecords, (rang-1) * tailleRecord, SEEK_SET);
		fwrite(nvRecordStr, tailleRecord, 1, fiRecords);
		
		fwrite(finFichierStr, sizeof(finFichierStr), 1, fiRecords);

		fclose(fiRecords);
	} 
	/* Cas où le fichier doit etre créé */
	else {
		fiRecords = fopen("records.txt", "w");
		if(fiRecords) {
			fwrite(nvRecordStr, tailleRecord, 1, fiRecords);

			fclose(fiRecords);
		}
	}

	free(finFichierStr);
}

void demanderTag(char tagStr[TAILLE_TAG + 1]){

	afficherDemandeTag();

	saisieTag(tagStr);
}

/* Remplir un tableau avec les scores du fichier records.txt */
void remplirScoresRecords(int scoresRecords[], int nbRecords) {
	FILE* fiRecords;
	
	fiRecords = fopen("records.txt", "r");
	if(fiRecords){
		int count;
		for (count = 0; count < nbRecords; count++) {
			scoresRecords[count] = scorePresent(fiRecords);
		}

		fclose(fiRecords);
	}
}

/* Remplir un tableau avec les tags (noms de joueur) du fichier records.txt */
void remplirTagsRecords(char tagsRecords[][TAILLE_TAG + 1], int nbRecords) {
	FILE* fiRecords;
	
	fiRecords = fopen("records.txt", "r");
	if(fiRecords){
		int tagCount;
		int charCount;

		for (tagCount = 0; tagCount < nbRecords; tagCount++) {
			getTag(fiRecords, tagsRecords[tagCount]);

			/* Si une lettre du tag est miniuscule, la rendre majuscule pour l'affichage */
			for(charCount = 0; charCount < TAILLE_TAG; charCount++) {
				if(tagsRecords[tagCount][charCount] >= 'a' && tagsRecords[tagCount][charCount] <= 'z') {
					tagsRecords[tagCount][charCount] -= 32;
				}
			}
		}

		fclose(fiRecords);
	}
}

/* Lire une ligne de fiRecords et mettre le tag (nom de joueur) lu dans str */
void getTag(FILE* fiRecords, char str[TAILLE_TAG + 1]) {
	char intStr[4];
	char tagStr[TAILLE_TAG + 1];
	char charTemp;
	int intPresent;
	int tagPresent;
	int charFinPresent;
	
	intPresent = (fread(intStr, sizeof(char), 4, fiRecords) == 4);
	tagPresent = (fread(tagStr, sizeof(char), TAILLE_TAG, fiRecords) == TAILLE_TAG);
	charFinPresent = fread(&charTemp, sizeof(char), 1, fiRecords) && (charTemp == CHAR_FIN_RECORD);

	if(intPresent && tagPresent && charFinPresent ) {
		tagStr[TAILLE_TAG] = '\0';
		sprintf(str, "%s", tagStr);
	}
}