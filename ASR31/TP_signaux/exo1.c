#include <stdio.h>
#include <stdlib.h>
#include<signal.h>
#include<unistd.h>

void sighandler(int signum) {
	switch(signum) {
		case SIGQUIT:
			printf("SIGQUIT ignoré\n");
			break;
		case SIGINT:
			printf("SIGINT ignoré\n");
			break;
		default: break;
	}
}

int main (int argc, char * argv[]){

	if ( ! fork()) {
		signal(SIGQUIT, &sighandler);
		signal(SIGINT, &sighandler);
		pause();
		pause();
		pause();
	}

	return EXIT_SUCCESS;
}

