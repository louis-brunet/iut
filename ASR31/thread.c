#include <pthread.h>
#include <stdio.h>
#include <unistd.h>

void *a_detacher(void *);

int main () {
	
	pthread_t t;
	
	if (pthread_create(&t, NULL, a_detacher, NULL) != 0)
		perror("pthread_create");
		
	sleep(1);
	pthread_join(t, NULL);
	printf("On n'est pas bloqué\n");	
	return 1;
}

void *a_detacher(void *v) {

	
	int r = pthread_detach(pthread_self());
	printf("%d\n", r); sleep(1);
	while (1) printf("toto ");
	return NULL;
}