#include <stdio.h>
#include <stdlib.h>
#include<signal.h>
#include<unistd.h>

/*
Test: 
Est-ce que le handler de signaux d'un processus est hérité par son fils ?

Ici, le fils répond aussi à SIGUSR1, donc réponse = OUI. 
*/


void sighandler(int signum) {
	switch(signum) {
		case SIGUSR1:
			printf("SIGUSR1 reçu (getpid(): %d)\n", getpid());
			break;
		default: break;
	}
}

int main (int argc, char * argv[]){
	printf("\npid père: %d\n", getpid());

	signal(SIGUSR1, &sighandler);

	if ( ! fork()) {
		pause();
		printf("fin du fils\n");
	} else {
		pause();
		printf("fin du père\n");	
	}
	
	return EXIT_SUCCESS;
}

