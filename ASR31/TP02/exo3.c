#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>

int main (int argc, char * argv[]){

	char pidStr[50];
	for (int i = 0; i < 50; ++i)
	{
		pidStr[i] = '\0';
	}
	sprintf( pidStr, "Je suis le père (pid = %d).\n", (int) getpid());

	write(1, pidStr, 50);

	pid_t pid = fork();

	if (pid <= 0)
	{
		


		char ppidStr[50];
		for (int i = 0; i < 50; ++i)
		{
			ppidStr[i] = '\0';
		}

		sprintf(ppidStr, "Fils de %d.\n", (int) getppid());
		write(1, ppidStr, 50);

		sleep(1);

		for (int i = 0; i < 50; ++i)
		{
			ppidStr[i] = '\0';
		}

		sprintf(ppidStr, "Fils de %d.\n", (int) getppid());
		write(1, ppidStr, 50);
	} 

	// else {
	// 	write(1, "Père mort.\n", 12);
	// }

	
	return EXIT_SUCCESS;
}

