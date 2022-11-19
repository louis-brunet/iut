#include<stdio.h>
#include"cellule.h"

int positionDansTab(Position position, Position arr[], int taille){
	int i;
	for (i = 0; i < taille; ++i){
		if(arr[i].x == position.x && arr[i].y == position.y){
			return 1;
		}
	}
	return 0;
}

int coordonneesDansTab(int x, int y, Position arr[], int taille){
	int i;
	for (i = 0; i < taille; ++i){
		if(arr[i].x == x && arr[i].y == y){
			return 1;
		}
	}
	return 0;
}