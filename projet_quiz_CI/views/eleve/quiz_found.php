<h3><?=$nom?></h3>
<div>Durée: <?=$duree . ' minute(s)'?></div>
<div><?=$nbQuestions?> question(s)</div>


<?php 
if ($status === 'actif')
{
	echo anchor('eleve/reponse/'.$clef, 'Démarrer');
}
else
{
	echo 'Ce quiz n\'est pas actif.';
}
?>
