# Creation d'une Tiled Map (Obsolète)  
  
Pré-requis :  
 - Tiled Map Editor : *https://www.mapeditor.org/*  
 - Types d'objets : fichier [objecttypes.xml](./objecttypes.xml) situé dans ce repertoire  
  
## Calques de Tuiles  
  
Les calques de tuiles ne sont utilisés que pour l'affichage, vous pouvez en créer autant que vous le voulez.  
  
## Calques d'Objets  
  
Les calques d'objets sont les plus importants; ils désignent les collisions, les points d'apparition et tout autres objets important sur la cartes.  
  
### Installation des types d'objets :  
  
Dans Tiled, dans la barre d'outils en haut, ouvrez le menu Vue et cochez la case Editeur de Types d'Objets, ensuite cliquez sur Importer et selectionnez le fichier indiqué dans les pré-requis.  
  
### Les Collisions  
  
Il existe 2 types de collisions :  
 - Les collisions Solides : Elle sont présentent sur tous les côtés des rectangles  
 - Les collisions Jump-Through : Elle sont présentent seulement sur la face north/haute des rectangles  
  
Pour les collisions solides, créez un calque d'objet nommé "SolidCollisions" et n'ajoutez y que des rectangles.  
Si vous voulez une coloration, changez le type de ces rectangles en SolidCollision.  
  
Pour les collisions Jump-Through, créez un calque d'objet nommé "JumpThroughCollisions" et n'ajoutez y que des rectangles.  
Si vous voulez une coloration, changez le type de ces rectangles en JumpThroughCollision.  
  
### Les Points d'Apparition  
  
Il existe 2 types de points d'apparition :  
 - Le point d'apparition du joueur : celui-ci désigne où le joueur devrait apparaître  
 - Les points d'apparition des ennemies : ceux-ci désignent les emplacement où les ennemies devraient apparaître  
  
Pour placer ces points, créez un calque d'objets nommé "SpawnPoints" et n'ajoutez y que des points.  
Le point du joueur doit être nommé "PlayerSpawn" et peut avoir le type PlayerSpawn pour y ajouter la coloration.  
Les points des ennemies doivent être nommés "EnemySpawn" et peuvent avoir le type EnemySpawn pour y ajouter la coloration.  
  
### Les plateformes mouvantes  
  
Celles-ci sont plus complexes, ce sont des plateformes non-fixes, qui se déplaceront selon les points que vous placerez.  
  
Tout d'abord, créez un calque d'objet nommé "MovingPlatforms" et n'ajoutez y que des points.  
**Tous les points sur ce calque devront avoir le type MovingPlatformPath, celui-ci ajoutera pleins de variables dans votre point**  
Placez votre premier point et donner lui un nom, ce nom sera le nom de la plateforme dans le code du jeu et devra être utilisé pour tous les autres points de la trajectoire.  
Dans ce point, choisissez les propriétés de la plateformes :  
 - orderID : la position du point dans la trajectoire de la platforme  
 - bothWays :  
   - Si coché : la trajectoire sera 0, 1, 2, ..., n-1, n, n-1, ..., 2, 1, 0...  
   - Sinon : la trajectoire sera 0, 1, 2, ..., n-1, n, 0...  
 - width : largeur (en pixels) de la plateforme  
 - height : hauteur (en pixels) de la plateforme  
 - imagePath : le lien local vers le fichier (dans le répertoire assets)  
 - speed : vitesse (en pixels par seconde) de la plateforme  
 - waitTime : temps d'attente/de pause (en secondes) lors de l'arrivé à un point  
  
**L'orderID doit être 0 pour le premier point**  
Ensuite, placez vos autres points pour la plateforme et donnez leur le même nom que le premier point ainsi que le type MovingPlatformPath.  
Ensuite, pour chacun de ses points, changez l'orderID pour changez la position du point dans la trajectoire de la plateforme.  
Par exemple : vous avez 3 points pour la plateformes, les orderID devront être 0, 1 et 2.  
*Inutile de changez les autres valeurs des variables dans ces points là, car seul le point d'orderID 0 changera les propriétés de la plateforme.*  
  
Si vous voulez créer une autre plateforme, faite comme ci-dessus mais avec un autre nom de plateforme.  
  
### Les lumières

Celles-ci sont très simple :
 - Créez un calque d'objets nommé "***Lights***" (si vous en avez déjà un, utilisez le)
 - Placez un point
 - Donnez lui le type **Light**
 - Remplissez les paramètres :
   - color : La couleur
     - r : rouge
     - g : vert
     - b : bleu
     - alpha : la transparence (plus c'est faible, moins la lumière est visible)
   - radius : rayon de la lumière en nobmre de cases
  
  
## Exemple de Tiled Map :  
![Map de test du jeu](https://cdn.discordapp.com/attachments/760772592000303114/762683159774625822/unknown.png)