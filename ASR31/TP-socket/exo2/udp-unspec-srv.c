#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define BUFF_SIZE 128

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


int sock_bind(struct addrinfo*,struct addrinfo*);
int print_where_bound(struct addrinfo*);
void srv_listen(int);
void copy(struct addrinfo*, struct addrinfo*) ;

int main(int argc, char const *argv[]) {   
	
	if (argc != 2) {
		printf("Usage: %s <port>", argv[0]);
		exit(EXIT_FAILURE);
	}
	int i = 0;

	int r, sockfd;
	struct addrinfo criteria, s;
	struct addrinfo *res;
	


	//memset(s, 0, sizeof(struct addrinfo));
	memset(&criteria, 0, sizeof(struct addrinfo));
	criteria.ai_family = AF_UNSPEC;
	criteria.ai_socktype = SOCK_DGRAM;
	//criteria.ai_flags = AI_PASSIVE;
	criteria.ai_protocol = 0;

	r = getaddrinfo(/*argv[1], argv[2]*/NULL, argv[1], &criteria, &res);
	if (r != 0) {
		fprintf(stderr, "getaddrinfo failed: %s\n", gai_strerror(r));
		exit(EXIT_FAILURE);
	}
	

	sockfd = sock_bind(res, &s);
	if (sockfd < 0) {
		perror("sock_bind failed");
		exit(EXIT_FAILURE);
	}
	

	r = print_where_bound(&s);
	if (r != 0) {
		perror("print_where_bound: getnameinfo failed");
		exit(EXIT_FAILURE);
	}
	
	

	printf("\nReady to echo...\n");
	while (1) {
		srv_listen(sockfd);
	}


	return EXIT_SUCCESS;
}

int sock_bind(struct addrinfo *res, struct addrinfo *s) {
	int sockfd, r;
	struct addrinfo *i = res;
	char host[NI_MAXHOST], service[NI_MAXSERV];
	while(i != NULL) {
		// r = getnameinfo(i->ai_addr, i->ai_addrlen, host, NI_MAXHOST, service, NI_MAXSERV, NI_NUMERICHOST);
		// printf("%s.%s\n\tfam: %d -- socktype: %d -- pro: %d -- addrlen: %d -- canonname: %s\n",
		// 		host, service, i->ai_family, i->ai_socktype, i->ai_protocol, i->ai_addrlen, i->ai_canonname );
		

		sockfd = socket(res->ai_family, res->ai_socktype, res->ai_protocol);

		if (sockfd >= 0) {

			r = bind(sockfd, res->ai_addr, res->ai_addrlen);
			if (r == 0) {
				// socket and bind successful
				copy(res, s);
				return sockfd;
			}
		}


		i = i->ai_next;
	}

	return -1;
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

int print_where_bound(struct addrinfo *s) {
	char host[NI_MAXHOST], service[NI_MAXSERV];

	int r = getnameinfo(s->ai_addr, s->ai_addrlen, host, NI_MAXHOST, service, NI_MAXSERV, 0);//NI_NUMERICHOST);

	printf("print_where_bound : Bound to %s.%s \nflags - %d\nfamily - %d\nsocktype - %d\nprotocol - %d\naddrlen - %d\ncanonname - %s\n",
		host, service, s->ai_flags, s->ai_family, s->ai_socktype, s->ai_protocol, s->ai_addrlen, s->ai_canonname);


	if (r != 0) {
		fprintf(stderr, "%s\n", gai_strerror(r));
		return -1;
	}

	return 0;
}


void srv_listen(int sockfd) {
	int received, size1, size2, r;
	char buffer[BUFF_SIZE];
	struct sockaddr_storage cltskt;
	char host[NI_MAXHOST], service[NI_MAXSERV];

	size1 = sizeof(struct sockaddr_storage);
	size2 = size1;
	
	memset(buffer, 0, 128);
	received = recvfrom(sockfd, buffer, BUFF_SIZE, 0, (struct sockaddr *)&cltskt, &size2);
	r = getnameinfo((struct sockaddr *)&cltskt, size2, host, NI_MAXHOST, service, NI_MAXSERV, NI_NUMERICHOST);

	if (r != 0) {
		fprintf(stderr, "getnameinfo failed : %s\n", gai_strerror(r));
		return;
	}

	if (received > 0) 
		printf("From %s.%s --> %s -- Total: %d bytes\n", host, service, buffer, received);

	fflush(stdout);
}

