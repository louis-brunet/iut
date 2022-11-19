#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

// void printArgc(int count) {
// 	if (!fork()) {
// 		printf("Testing for argc = %d.\n", count+1);
// 		char*  argv[count + 1];
// 		argv[0] = "print_argc";
// 		for (int i = 1; i < count; ++i)
// 		{
// 			sprintf(argv[i], "%d", i);
// 		}

// 		execvp("print_argc", argv);

// 		perror("exec");
// 	}
// }


int main (int argc, char * argv[]){

	int count = atoi(argv[1]);

	printf("Testing for argc = %d.\n", count);
	char*  args[count + 1];
	
	int i;
	for ( i = 0; i < count; ++i)
	{
		if (i == 0) 
		{
			args[0] = "print_argc";
		} else{
			sprintf(args[i], "%d", i);
			printf("ajouté %s\n", args[i]);
		}
	}
	args[i] = NULL;

	printf("i après boucle = %d\n", i);

	execv("print_argc", args);

	perror("exec");

	// printArgc(0);
	// printArgc(1);
	// printArgc(2);
	// printArgc(4);

}


