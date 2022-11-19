CC = gcc
EXE = main
OFILES = matrice.o \
         main.o

but: ${EXE}

matrice.o: matrice.h

main.o: main.c matrice.h


${EXE}: ${OFILES}
	${CC} ${OFILES} -o ${EXE}

run: ${EXE}
	./${EXE}

