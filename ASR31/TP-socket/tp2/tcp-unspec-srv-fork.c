#include <stdlib.h>
#include <stdio.h>
#include <netdb.h>
#include <signal.h>
#include <string.h>
#include <unistd.h>
#include <wait.h>

#define BUFFER_SIZE 256


int sock_bind(struct addrinfo*,struct addrinfo*);
int print_where_bound(struct addrinfo*);
void serv_client( struct sockaddr *, socklen_t );
void copy(struct addrinfo*, struct addrinfo*) ;
void accept_connection(int);
void sighandler(int);
void wait_for_children();



int listen_sockfd, accepted_sockfd = -1;




int main ( int argc, char **argv ) {

	if (argc != 2) {
		printf("Usage: %s <port>\n", argv[0]);
		exit(EXIT_FAILURE);
	}

	int r; //, sockfd;
	struct addrinfo criteria, s;
	struct addrinfo *res;
	
	memset(&criteria, 0, sizeof(struct addrinfo));
	criteria.ai_family = AF_UNSPEC;
	criteria.ai_socktype = SOCK_STREAM;
	//criteria.ai_flags = AI_PASSIVE;
	criteria.ai_protocol = 0;

	r = getaddrinfo(/*argv[1], argv[2]*/NULL, argv[1], &criteria, &res);
	if (r != 0) {
		fprintf(stderr, "getaddrinfo failed: %s\n", gai_strerror(r));
		exit(EXIT_FAILURE);
	}
	

	listen_sockfd = sock_bind(res, &s);
	if (listen_sockfd < 0) {
		perror("sock_bind failed");
		exit(EXIT_FAILURE);
	}
	

	r = print_where_bound(&s);
	if (r != 0) {
		perror("print_where_bound: getnameinfo failed");
		exit(EXIT_FAILURE);
	}

	listen(listen_sockfd, 0);


	signal(SIGINT, &sighandler);
	signal(SIGCHLD, &sighandler);

	while (1) {
		accept_connection(listen_sockfd);
	}

	return EXIT_SUCCESS;
}


int sock_bind(struct addrinfo* res, struct addrinfo* s) {
	int sockfd = -1, r;
	struct addrinfo *i = res;
	char host[NI_MAXHOST], service[NI_MAXSERV];
	while(i != NULL) {
		sockfd = socket(i->ai_family, i->ai_socktype, i->ai_protocol);

		if (sockfd >= 0) {

			r = bind(sockfd, i->ai_addr, i->ai_addrlen);
			if (r == 0) {
				// socket and bind successful
				copy(res, s);
				break;
				// freeaddrinfo(res);
				// return sockfd;
			}
		}


		i = i->ai_next;
	}

	freeaddrinfo(res);
	return sockfd;
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

void accept_connection(int listen_sockfd) {
	// TODO 
	struct sockaddr peer_addr;
	socklen_t addrlen;
	accepted_sockfd = accept(listen_sockfd, &peer_addr, &addrlen);

	if (accepted_sockfd < 0) {
		perror("accept_connection failed");
		exit(EXIT_FAILURE);
	}
	

	if (!fork()) {
		printf("child %d accepted connection\n", getpid());

		close(listen_sockfd);
		serv_client(&peer_addr, addrlen);
	} else
		accepted_sockfd = -1;
}

void serv_client(struct sockaddr *peer_socket, socklen_t addrlen) {
	int nb_read, nb_written;
	char buffer[BUFFER_SIZE];
	while (1) {
		memset(buffer, 0, BUFFER_SIZE);
		nb_read = recv(accepted_sockfd, buffer, BUFFER_SIZE, 0);

		if (nb_read <= 0) break;

		printf("Received %d bytes from %s : %s\n", nb_read, peer_socket->sa_data, buffer);
		nb_written = send(accepted_sockfd, buffer, nb_read, 0);
		printf("Sent %d bytes.\n", nb_written);
	}

	printf("serv_client closing sockfd\n");
	close(accepted_sockfd);
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

void sighandler(int sig) {
	switch (sig) {
		case SIGINT:
			if (accepted_sockfd < 0) {
				close(listen_sockfd);
				// wait_for_children();
			}
			else
				close(accepted_sockfd);
			break;
		case SIGCHLD:
			wait_for_children();
			break;
		default:
			break;
	}
}

void wait_for_children() {
	while(waitpid(-1, NULL, WNOHANG) > 0);
}