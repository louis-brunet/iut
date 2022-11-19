/* Auteur :  PV          */

#include <fcntl.h>
#include <unistd.h>
#include <errno.h>

#include <stdio.h>
#include <stdlib.h>


int desc_read, desc_write;
char c;

int main(int argc, char *argv[]) {
	

	if (argc != 3) {
		printf("Deux noms de fichiers comme arguments\n");
		exit(1);
	}
	
	if ((desc_read = open(argv[1], O_RDONLY)) == -1) {
		perror(argv[1]);
		exit(1);
	}

	if ((desc_write = open(argv[2], O_WRONLY|O_CREAT|O_TRUNC, 0666)) == -1) {
			perror(argv[2]);
			exit(1);
	}
	
	fork();

	for(;;) {
		if (read(desc_read, &c, 1) != 1) exit(0);
		write(desc_write, &c, 1);
	}
	close(desc_read);
	close(desc_write);
	return EXIT_SUCCESS;
}