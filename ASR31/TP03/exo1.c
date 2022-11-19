#include <stdlib.h>
#include <stdio.h>

#include <unistd.h>
#include <sys/wait.h>

int main(int argc, char** args)
{
	int childPid = fork();

	if (childPid > 0)
	{
		printf("PERE [fork: %d; getpid: %d; getppid: %d]\n", 
			childPid, 
			(int) getpid(),
			(int) getppid());
		wait(NULL);
		execl("/bin/ps", "ps", "-ef", NULL);
	} 
	else
	{
		printf("FILS [fork: %d; getpid: %d; getppid: %d]\n", 
			childPid, 
			(int) getpid(),
			(int) getppid());
		sleep(4);
		exit(2);
	}
}