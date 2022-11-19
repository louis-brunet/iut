#include <stdio.h>
#include <stdlib.h>
#include "matrice.h"

int main (int argc, char * argv[])
{

	matrice* M = (matrice*) malloc(sizeof(matrice));


	creation(M, 2, 3);

	printMat(M, 2, 3);

	return EXIT_SUCCESS;
}
