### Exercice
On souhaite écrire un programme qui permet de créer un ```bourreau``` et ses 3 prisonniers. Le bourreau aura comme tâches de taper les prisonniers à la demande. Les prisonniers meurent après avoir reçu trois coups. 

On vous propose d’implémenter, en C sous UNIX à l’aide des processus, des tubes et des signaux, les 3 programmes suivant:

- [ ] ```tape``` dont le rôle est de créer un processus ```bourreau``` et un tube de communication uni-directionnel avec lui. Ensuite il propose une interface permettant de commander le bourreau : on doit pouvoir choisir le numéro d’un prisonnier ou bien 0. Cet entier est envoyé dans le tube au processus ```bourreau```
- [ ] ```bourreau``` qui au début crée 3 fils (prisonniers) et qui attend un entier sur le tube qui le relie à ```tape```. Ce processus va envoyer un signal à un de ses prisonniers dont le numéro correspond à l’entier lu. Si c’est 0 alors il tire au hasard un entier compris entre 1 et 3 et donc choisit le processus à qui il va envoyer le signal. Si ```bourreau``` n’a plus de fils il s’arrête.
- [ ] Un prisonnier qui attend un signal venant de son père ```bourreau```. Au bout de trois signaux reçus il meurt.

Bonus: on peut améliorer ce programme en permettant de paramétrer le nombre de prisonniers (paramètre de la commande ```tape```). Dans ce cas là le premier entier écrit dans le tube sera le nombre de fils que le bourreau crée.