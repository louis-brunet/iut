#include <time.h>
#include <pthread.h>
#include <stdio.h>
#include <unistd.h>

void *incremente(void *);
void *decremente(void *);


int main () {

	pthread_t t1, t2, t3;
	int valeur = 0;
	
	if (pthread_create(&t3, NULL, decremente, &valeur) != 0)
		perror("phread_create");
	if (pthread_create(&t1, NULL, incremente, &valeur) != 0)
		perror("pthread_create");
	if (pthread_create(&t2, NULL, decremente, &valeur) != 0)
		perror("phread_create");
	pthread_join(t1, NULL);
	pthread_join(t2, NULL);
	pthread_join(t3, NULL);
	printf("valeur : %d\n", valeur);
}


void *incremente(void *v) {
	
	(*((int *)v))++;
	printf("dans inc : %d\n", *(int*)v);
	sleep(1);
	pthread_exit(NULL);
}


void *decremente(void *v) {
	
	(*((int *)v))--;
	printf("dans dec : %d\n", *(int*)v);
	sleep(1);
	pthread_exit(NULL);
}
