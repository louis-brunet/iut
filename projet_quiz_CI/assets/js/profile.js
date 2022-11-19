function afficherClefPartage(idQuiz, clefPartage) {
	let button = document.getElementById('bouton-partage-'+idQuiz);
	button.style.display = 'none';
	let partage = document.getElementById('partage-'+idQuiz);
	partage.innerHTML = '<b>Clef de partage</b>&nbsp;:&nbsp;' + clefPartage;
}


