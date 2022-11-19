
#include <stdlib.h>
#include <unistd.h>

int main (int argc, char * argv[]){

	
	write(1, "Hello world", 11);

	pid_t pid = fork();

	if (pid <= 0)
	{
		write(1,"\n", 1);
	}

	for (int j = 0; j < 100000000; j++) ;

	return EXIT_SUCCESS;
}

