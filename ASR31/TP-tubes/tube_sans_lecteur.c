#include <stdio.h>
#include <string.h>
#include <unistd.h>

#define R 0 
#define W 1

int main ( ) {
	int fd[2] ;
	char message[100] ;
	int nboctets ;
	char *phrase = " message envoye au pere par le fils " ;
	
	pipe ( fd ) ; /* on ne vérifie pas, c'est pas bien ! */
	if (fork() == 0) {
		close ( fd[R] ) ;
		printf("retour de write() : %d", write ( fd[W] , phrase , strlen (phrase) + 1 ) ) ;
		printf("Le fils attend un peu : %d\n", getppid());
		sleep(2);
		close ( fd[W] ) ;
	}
	else {
		close ( fd[W] ) ;
		close ( fd[R] ) ;
		// nboctets = read ( fd[R] , message , 100 ) ;
		// printf ( "Le père (%d) a recu le message du fils: \n Lecture de %d octets  \n Message recu : %s\n" , getpid(), nboctets , message ) ;
		// close ( fd [R] ) ;
	}
	return 0;
}