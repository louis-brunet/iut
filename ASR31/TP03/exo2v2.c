#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <unistd.h>
#include <sys/wait.h>


#define SIZE 1000

int search(unsigned char * t,int start,int end){
    /* renvoie 1 s'il y a un 0 dans la tranche du tableau, 
     * 0 sinon */
	if (t[start] == 0) return 1;
	if (start == end ) return 0;

	return search(t, start + 1, end);
}



int main(int argc , char * argv[]){
    if (argc != 2) 
    {
        printf("Usage : %s <nb de processus fils entre 1 et 100>\n", argv[0]);
    }
    int nbFils = atoi(argv[1]);
    if( nbFils < 1 || nbFils > 100 )
    {
        printf("Usage : %s <nb de processus fils entre 1 et 100>\n", argv[0]);
    }

    printf("Création de %d procéssus fils prévue.\n", nbFils);


    unsigned char arr[SIZE];

    srandom(time(NULL));
    int i;
    for ( i = 0; i < SIZE; i++)
        arr[i] = (unsigned char) (random() % 255) + 1; 


    printf("Enter a number between 0 and %d: ", SIZE);
    scanf(" %d", &i);

    if (i >= 0 && i < SIZE) 
    	arr[i] = 0;

    pid_t childPid = (pid_t) 1;

    for (int i = 0; i < nbFils; ++i)
    {
        if(childPid > 0)
        {
            childPid = fork();
        } 
        else
        {   
           exit(search(arr, 0, (SIZE * i) / nbFils));   
        }
    }

    

   //  pid_t childPid = fork();
   //  if (childPid == 0)
   //  {
   //  	exit(search(arr, 0, SIZE / 2));
   //  } 
   //  else if (childPid > 0)
   //  {
   //  	if (search(arr, (SIZE / 2)+1, SIZE - 1 ))
   //  	{
   //  		printf("FOUND !\n");
   //  	}
   //  	else 
   //  	{
	  //   	int status;
	  //   	waitpid(childPid, &status, 0);
			// if (status)
			// {
			// 	printf("FOUND !\n");
			// }
			// else
			// {
			// 	printf("NOT FOUND :(\n");
			// }
   //  	}
   //  }
   //  else return EXIT_FAILURE;

	// for (int i = 0; i < SIZE; ++i)
 //    {
 //    	printf("%4d", arr[i]);
 //    }


    return EXIT_SUCCESS;
}