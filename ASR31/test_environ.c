#include <stdio.h>
#include <stdlib.h>

extern char **environ;

int main (int argc, char * argv[]){

	printf("%s\n", environ);
	return EXIT_SUCCESS;
}

