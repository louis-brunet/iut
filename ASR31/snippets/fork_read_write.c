/* Auteur :  PV          */

#include <fcntl.h>
#include <unistd.h>
#include <errno.h>

#include <stdio.h>
#include <stdlib.h>


int main(int argc, char *argv[]) {
	

	/*if (argc != xxxxx) {
		printf("\n");
		exit(1);
	}*/
	
	if ((desc_read = open(argv[1], O_RDONLY)) == -1) {
		perror(argv[1]);
		exit(1);
	}

	if ((desc_write = open(argv[2], O_WRONLY|O_CREAT|O_TRUNC, 0666)) == -1) {
		perror(argv[2]);
		exit(1);
	}
	
	pid_t pid = fork();
	if ( ! pid) {
		/* FILS */
	} else {
		/* PERE */
	}

	close(desc_read);
	close(desc_write);
	return EXIT_SUCCESS;
}