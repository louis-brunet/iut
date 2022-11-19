#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>

int main (int argc, char * argv[])
{
	char buf[10];
	if (argc != 3) 
	{
		perror("2 noms de fichier attendus en argument\n");
		exit(1);
	}

	int fileR = open(argv[1], O_RDONLY);

	if(fileR == -1)
	{
		perror("fichier n'a pas pu être ouvert en lecture\n");
		exit(1);
	}
	
	int fileW = open(argv[2], O_WRONLY|O_CREAT|O_TRUNC, 0644);

	if(fileW == -1)
	{
		perror("fichier n'a pas pu être ouvert en écriture\n");
		exit(1);
	}

	int nbLu = read(fileR, buf, 10);
	int nbEcrit = write(fileW, buf, nbLu);



	pid_t pid = fork();

	if (pid <= 0)
	{
		while ( (nbLu = read(fileR, buf, 10)) > 0 )
		{
			write(fileW, buf, nbLu);
		}
	}

	return EXIT_SUCCESS;
}

