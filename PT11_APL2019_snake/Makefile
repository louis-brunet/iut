CC = gcc
CFLAGS = -Wall \
         -ansi \
         -pedantic
EXE = main
OFILES = records.o \
		 jeu.o \
		 saisie.o \
		 affichage.o \
		 terrain.o \
		 snake.o \
		 cellule.o \
         main.o
 
### BUT PAR DEFAUT ###
 
but : ${EXE}

### REGLES ESSENTIELLES ###
 
cellule.o : cellule.h

snake.o : snake.h cellule.h

terrain.o : cellule.h snake.h terrain.h

affichage.o: affichage.h terrain.h cellule.h jeu.h

jeu.o : jeu.h cellule.h terrain.h snake.h saisie.h affichage.h
 
saisie.o : saisie.h jeu.h affichage.h

records.o : records.h saisie.h affichage.h

main.o : terrain.h cellule.h snake.h affichage.h jeu.h saisie.h records.h
 
${EXE} : ${OFILES}
	$(CC) $(CFLAGS) -o ${EXE} ${OFILES} -lgraph

### BUTS FACTICES ###

.PHONY : but clean run cleanmake cleanrun

clean :
	rm -f ${OFILES} ${EXE} *.h.gch records.txt

run : ${EXE}
	./${EXE}

cleanmake : clean but

cleanrun : but 
	rm -f ${OFILES} *.h.gch records.txt
	./${EXE}