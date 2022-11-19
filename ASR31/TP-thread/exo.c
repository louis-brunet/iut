#include <time.h>
#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;
int nbThreads = 3;
int printedLast = 0;

void *annexe(void *);

int main () {

	pthread_t threads[nbThreads];

	int v1 = 1;
	if (pthread_create(&threads[0], NULL, annexe, &v1) != 0)
			perror("phread_create");

	int v2 = 2;
	if (pthread_create(&threads[1], NULL, annexe, &v2) != 0)
			perror("phread_create");

	int v3 = 3;
	if (pthread_create(&threads[2], NULL, annexe, &v3) != 0)
			perror("phread_create");

	for (int i = 0; i < nbThreads; ++i)
	{
		pthread_join(threads[i], NULL);
	}
	return EXIT_SUCCESS;
}

void *annexe (void* p) {
	// pthread_mutex_lock(&mutex);
	// int v = *(int*)vp;
	// pthread_mutex_unlock(&mutex);
	for (int i = 0; i < 10; ++i)
	{
		pthread_mutex_lock(&mutex);
		if (printedLast == ((*(int*)p - 1) % nbThreads) ) {
			printf("%d", *(int*)p);
			printf("%d", *(int*)p);
			printf("%d", *(int*)p);
			printf("%d", *(int*)p);
			printf("\n" );
			printedLast = (*(int*)p) % nbThreads;
		} else {
			i--;
			pthread_mutex_unlock(&mutex);
			continue;
		}

			
		pthread_mutex_unlock(&mutex);
	}

}
