function validate (endTime) {
	if ( (Date.now() / 1000) > endTime)
	{
		alert('La durée impartie à ce quiz est terminée.');
		return false;
	}
	else return confirm('Soumettre cette réponse ?');
}
