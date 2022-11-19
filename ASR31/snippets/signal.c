#include <stdio.h> 
#include <stdlib.h>
#include <signal.h> 
#include <unistd.h>


/* Optionnel : 

typedef void (*sig_t) (int);
sig_t signal(int sig, sig_t func);
*/


void sighandler (int signum);


int main(int argc, char *argv[]) {
    
    char buffer[256];
    
    if (signal(SIGTERM, &sighandler) == SIG_ERR) {
        printf("Ne peut pas manipuler le signal\n");
        exit(1);
    }

    while (1) {
        fgets(buffer, sizeof(buffer), stdin);
        printf("Input : %s", buffer);
    }
    return EXIT_SUCCESS;
}

void sighandler (int signum) {
    printf("Masquage du signal SIGTERM\n");
}
