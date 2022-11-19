#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <unistd.h>
#include <sys/wait.h>

int compteur;
pid_t child;

void sauvegarde() {
	return;
}

void sighandler(int signum) {
	switch(signum) {
		case SIGUSR2:
			sauvegarde();
			printf("Child received SIGUSR2, compteur = %d (sighandler)\n", compteur);
			kill(getppid(), SIGCHLD);
			break;
		case SIGUSR1:
			printf("Parent received SIGUSR1 (sighandler)\n");
			kill(child, SIGUSR2);
			printf("Child sent SIGUSR2 (sighandler)\n");
			break;
		case SIGCHLD:
			printf("Parent received SIGCHLD (sighandler)\n");
			kill(child, SIGKILL);
			printf("Child killed (sighandler)\n");
			exit(0);
		default: 
			break;
	}
}

int main (int argc, char * argv[]){

	child = fork();
	if ( ! child ) {
		/* FILS */
		signal(SIGUSR2, &sighandler);
		compteur = 0;
		printf("kill me (%d)\n", getpid());
		for (int i = 0; i < 10000000000; ++i)
		{
			compteur++;
		}
		printf("Travail fini, sauvegarde du compteur = %d\n", compteur);
		sauvegarde();
		kill(getppid(), SIGUSR1);
		pause();
		printf("fin du fils\n");
		exit(0);
	} else if(child < 0) {
		perror("fork");
		exit(0);
	} else {
		/* PERE */
		signal(SIGCHLD, &sighandler);
		signal(SIGUSR1, &sighandler);
		wait();
		printf("Fin du père\n");
	}

	return EXIT_SUCCESS;
}

