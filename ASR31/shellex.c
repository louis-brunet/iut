#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/wait.h>

// Nb maximal d'arg à parse dans la commande 
#define MAXARGS 30
// Nb maximal de chars par ligne
#define MAXLINE 80
// but why
#define FOREVER while(1)

#define BACKGROUND 0x1
#define AND 0x2
#define OR 0x4

// variables d'environnement système
extern char **environ;

void eval(char *cmdline);
int parseline(char *buf, char **argv);
int builtin_cmd(char **argv);

int main() {
	// buffer qui contient la ligne de commande tapée
	char cmdline[MAXLINE];
	
	FOREVER {
		// Demander ligne de commande
		printf("> ");
		fgets(cmdline, MAXLINE, stdin);
		// Fermer le programme si EOF reçu
		if (feof(stdin)) exit(0);
		// Interpreter et executer ligne de commande reçue
		eval(cmdline);
	}
		return EXIT_SUCCESS;
}


void eval(char *cmdline) {
	// tableau pour contenir les arguments parsed 
	char *argv[MAXARGS];
	char buf[MAXLINE];
	int flags, bg, and, or;
	// Child pid, resultat du fork
	pid_t pid;
	
	// Recopier cmdline dans buf
	strcpy(buf, cmdline);
	// Remplir le tableau argv à partir de buf
	flags = parseline(buf, argv);

	bg = ((flags & BACKGROUND) == BACKGROUND);
	and = ((flags & AND) == AND);
	or = ((flags & OR) == OR);

	// Ne rien faire si argv n'a été rempli que d'un NULL
	// (si ligne vide ?)
	if (argv[0] == NULL) {
		return;
	}
	
	if (!builtin_cmd(argv)) {
		// Créer processus
		if ((pid = fork()) == 0) {
			// Le processus fils créé tente d'éxecuter la commande donnée
			// argv[0] est une commande executable dans PATH.
			if (execvp(argv[0], argv) < 0) {
				// Si commande pas trouvée, fermer le programme
				printf("Cette commande n'existe pas : %s\n", argv[0]);
				exit(0);
			}
		}
		
		// Si cmd pas lancée avec "&", wait() jusqu'à la fin du fils 
		if (!bg) {
			int status;
			if (waitpid(pid, &status, 0) < 0) {
				perror("waitfg: waitpid error"); exit(0);
			}
			if ( ! status && and ) {

			} else if ( ! status && or ) {

			}
		}
		// Si commande lancée avec "&", print child pid + launched command
		else {
			printf("%d %s", pid, cmdline);
		}
	}
	return;
}

// Quitte le programme si argv[0] == "quit"
// Renvoie 1 si argv[0] == "&"
// Renvoie 0 sinon
int builtin_cmd(char **argv) {
	if (!strcmp(argv[0], "quit"))
		exit(0);
	if (!strcmp(argv[0], "&"))
		return 1;
	return 0;
}

// TODO fix check for && and ||, should not be on last argv
// Renvoie 1 si le dernier arg est "&" (ou si ligne vide), 0 sinon
int parseline(char *buf, char **argv) {
	// Contiendra pointeur vers le premier caractère délimiteur (' ')
	char *delim;
	// Compter args
	int argc;
	// Valeur de retour, contiendra 1 si le dernier arg est "&" (ou si ligne vide), 0 sinon
	int bg; 
	
	int flags;

	// Remplace le dernier caractère de buf ('\0') par le délimiteur (' ')
	buf[strlen(buf)-1] = ' ';
	// Décaler le pointeur buf vers la droite pour enlever les espaces au début
	while (*buf && (*buf == ' ')) {
		buf++;
	}
	
	// Tant qu'un espace est trouvable dans buf
	argc = 0;
	while ((delim = strchr(buf, ' '))) {
		// Ajouter un élémt dans argv
		// La valeur ajoutée est la string commençant à l'adresse buf
		// qui se termine par '\0' à l'adresse où a été trouvé le délimiteur
		argv[argc++] = buf;
		*delim = '\0';
		// Le pointeur buf doit pointer sur le caractère suivant l'epace trouvé
		buf = delim+1;

		// Décaler le pointeur buf vers la droite pour enlever les espaces au début
		while (*buf && (*buf == ' ')) buf++;
	}
	// Le dernier elmt de argv est NULL
	argv[argc] = NULL;
	
	// Return 1 si aucun arguments trouvés (ligne vide/espaces)
	if (argc == 0) return 1;
	
	// // pareil que if(bg = (*argv[argc-1] == '&'))
	// if ((bg = (*argv[argc-1] == '&')) != 0) {
	// 	// Remplacer ce dernier arg par NULL
	// 	argv[--argc] = NULL;
	// }
	
	// return bg;	

	flags = 0;
	if ( ! strcmp(argv[argc-1],"&")) {
		flags = flags|BACKGROUND;
	}
	if( ! strcmp(argv[argc-1],"&&")) {
		flags = flags|AND;
	}
	if( ! strcmp(argv[argc-1],"||")) {
		flags = flags|OR;
	}
	return flags;
}

