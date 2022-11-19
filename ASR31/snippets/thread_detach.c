#include <pthread.h>
#include <stdio.h>
#include <unistd.h>

void *se_detach(void *);

int main() {

    pthread_t t;


    if (pthread_create(&t, NULL, se_detach, NULL) != 0)
        perror("Thread_create");

    sleep(2);
    int r = pthread_join(t, NULL);
    printf("thread natif r = %d\n", r); 

    printf("Pas bloqué\n");
    
    return 0;

}

void *se_detach(void *s) {

    sleep(1);
    int r = pthread_detach(pthread_self());
    printf("Sedet r = %d\n", r); sleep(1);
    while(1) printf("toto ");
    
    pthread_exit(NULL);

}
