#include <sys/socket.h>
#include <sys/types.h>
#include <netdb.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

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


int main(int argc, char **argv) {
	if (argc != 3) {
		printf("Usage: ./%s <host> <service>\n", argv[0]);
		return EXIT_FAILURE;
	}

	int r;
	struct addrinfo criteria;
	struct addrinfo *res, *resp;
	char host[NI_MAXHOST], service[NI_MAXSERV];// = argv[1];
	//char *service = argv[2];

	memset(&criteria, 0, sizeof(struct addrinfo));
	criteria.ai_family = AF_UNSPEC;
	criteria.ai_socktype = 0;
	criteria.ai_flags = AI_CANONNAME;
	criteria.ai_protocol = 0;

	r = getaddrinfo(argv[1], argv[2], &criteria, &res);
	if (r != 0) {
		fprintf(stderr, "getaddrinfo failed: %s\n", gai_strerror(r));
		exit(EXIT_FAILURE);
	}

	resp = res;
	while(resp != NULL) {
		r = getnameinfo(resp->ai_addr, resp->ai_addrlen, host, NI_MAXHOST, service, NI_MAXSERV, NI_NUMERICHOST);
		printf("%s.%s\n\tfam: %d -- socktype: %d -- pro: %d -- addrlen: %d -- canonname: %s\n",
				host, service, resp->ai_family, resp->ai_socktype, resp->ai_protocol, resp->ai_addrlen, resp->ai_canonname );
		resp = resp->ai_next;
	}

	freeaddrinfo(res);

	return EXIT_SUCCESS;
}