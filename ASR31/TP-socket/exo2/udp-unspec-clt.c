#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#define BUFF_SIZE 128

int try_socket(struct addrinfo *, struct addrinfo *) ;
void copy(struct addrinfo *, struct addrinfo *) ;
int print_ready(struct addrinfo);
void clt_listen(int sockfd, struct addrinfo *dest) ;

/*

struct addrinfo {
	int              ai_flags;
	int              ai_family;
	int              ai_socktype;
	int              ai_protocol;
	socklen_t        ai_addrlen;
	struct sockaddr *ai_addr;
	char            *ai_canonname;
	struct addrinfo *ai_next;
};

*/


int main(int argc, char const *argv[]) {   
	
	if (argc != 3) {
		printf("Usage: %s <host name> <port>", argv[0]);
		exit(EXIT_FAILURE);
	}

	int r, sockfd;
	struct addrinfo criteria, s;
	struct addrinfo *res;

	//memset(s, 0, sizeof(struct addrinfo));
	memset(&criteria, 0, sizeof(struct addrinfo));
	criteria.ai_family = AF_UNSPEC;
	criteria.ai_socktype = SOCK_DGRAM;
	//criteria.ai_flags = AI_PASSIVE;
	criteria.ai_protocol = 0;

	r = getaddrinfo(argv[1], argv[2], &criteria, &res);
	if (r != 0) {
		fprintf(stderr, "getaddrinfo failed: %s\n", gai_strerror(r));
		exit(EXIT_FAILURE);
	}

	sockfd = try_socket(res, &s);
	if (sockfd < 0) {
		printf("try_socket failed, sockfd = %d", sockfd);
		exit(EXIT_FAILURE);
	}

	print_ready(s);

	clt_listen(sockfd, &s);

	return EXIT_SUCCESS;
}

int try_socket(struct addrinfo *res, struct addrinfo *s) {
	int r, sockfd = -1;
	struct addrinfo *i = res; 

	while (i != NULL) {
		sockfd = socket(i->ai_family, i->ai_socktype, i->ai_protocol);
		if (sockfd >= 0) {
			copy(res, s);
			break;
		}

		i = i->ai_next;
	}

	freeaddrinfo(res);

	return sockfd;
}

void copy(struct addrinfo *from, struct addrinfo *to) {
	to->ai_flags = from->ai_flags;
	to->ai_family = from->ai_family;
	to->ai_socktype = from->ai_socktype;
	to->ai_protocol = from->ai_protocol;
	to->ai_addrlen = from->ai_addrlen;
	to->ai_addr = from->ai_addr;
	to->ai_canonname = from->ai_canonname;
	to->ai_next = from->ai_next;
}

int print_ready(struct addrinfo s) {
	char host[NI_MAXHOST], service[NI_MAXSERV];

	int r = getnameinfo(s.ai_addr, s.ai_addrlen, host, NI_MAXHOST, service, NI_MAXSERV, 0);//NI_NUMERICHOST);

	printf("print_ready : Ready to send datagrams to server %s.%s\n", host, service);


	if (r != 0) {
		fprintf(stderr, "%s\n", gai_strerror(r));
		return -1;
	}

	return 0;
}

void prompt() {
	printf("Enter a message: ");
	fflush(stdout);
}

void clt_listen(int sockfd, struct addrinfo *dest) {
	int received, r;
	char buffer[BUFF_SIZE];

	prompt();

	while (
		memset(buffer, 0, BUFF_SIZE) && (received = read(0, buffer, BUFF_SIZE)) != 0) {

		printf("Sending message: %s", buffer);

		r = sendto(sockfd, buffer, strlen(buffer) - 1, 0, dest->ai_addr, dest->ai_addrlen);

		if (r < 0) {
			fprintf(stderr, "sendto failed.\n");
			exit(EXIT_FAILURE);
		} else
			printf("Sent %d bytes.\n", r);

		prompt();
	}

}



