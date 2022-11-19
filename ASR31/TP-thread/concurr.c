#include <stdio.h>
#include <pthread.h>

int var_ex = 0;

pthread_t tid[2];

void *annexe(void *ordre) {
	int boucle;
	printf("Pthread %ld, adr de boucle = %p, adr de var_ex = %p\n", (int)pthread_self(), &boucle, &var_ex);

	for(boucle = 0; boucle < 10000000; boucle++) var_ex++;
}

void main(void) {
	annexe(NULL);
	pthread_create(tid, NULL, annexe, NULL);
	pthread_create(tid+1, NULL, annexe, NULL);
	pthread_join(tid[0], NULL);
	pthread_join(tid[1], NULL);
	
	printf("Valeur finale de var_ex : %d\n", var_ex);
}