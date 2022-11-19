<h3>Réponses au quiz "<?=$nom?>"</h3>

<p>
Une réponse est considérée bonne si aucun des choix selectionnés pour cette question vaut un nombre nul ou négatif de points. 
<!--Une réponse qui n'a pas tous les points possibles sur une question est comptée partiellement bonne.-->
</p>

<p>
Ce quiz comporte <b><?=$nb_questions?></b> questions. Le score maximal possible est <b><?=$score_max?></b>.
</p>

<?php
$this->table->set_heading(array('Nom', 'Prénom', 'Date de soumission', 'Score', 'Questions ratées','Réponses')); // Dans 'Questions ratées', afficher nb et pourcentage

$template = array('table_open' => '<table class="liste-reponses">');

$this->table->set_template($template);

foreach($reponses as $reponse)
{
	
	//...
	//
	$this->table->add_row(//..
		$reponse['nom'],
		$reponse['prenom'],
		$reponse['soumission'],
		$reponse['score'],
		$reponse['q_ratees_nb'] .' ('.$reponse['q_ratees_pourcent'].'%)',
		anchor('eleve/correction/' . $reponse['clef_eleve'],'Détail')
	);
}

echo $this->table->generate();
?>
