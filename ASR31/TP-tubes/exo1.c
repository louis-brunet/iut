#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <wait.h>

#define R 0 
#define W 1

int main ( ) {
	int fd[2] ;
	char message[100] ;
	int nboctets ;
	char *phrase = " message envoye au pere par le fils " ;
	
	pipe ( fd ) ; /* on ne vérifie pas, c'est pas bien ! */
	if (fork() == 0) {
		if (fork() == 0) {
			/* FILS 1.1 */
			close(fd[W]);
			pid_t pidRecu;
			while(read(fd[R], &pidRecu, sizeof(pid_t)) > 0) {
				
				printf("[FILS 1.1] pid reçu:%d\n", pidRecu);
			}
		} else {
			/* FILS 1 */
			close(fd[W]);
			close(fd[R]);

			wait(NULL);
		}
	}
	else if (fork() == 0) {
		/* FILS 2 */
		close ( fd[R] ) ;
		pid_t pid = getpid();
		printf("[FILS 2] getpid():%d\n",pid);
		for (int i = 0; i < 5; ++i){
			sleep(1);

			write( fd[W], &pid, sizeof(pid_t) );
		}
	} else {
		wait(NULL);
	}
	return 0;
}