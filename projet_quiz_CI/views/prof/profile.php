<?php
defined('BASEPATH') OR exit('No direct script access allowed');
?>
	<link rel="stylesheet" href="<?=base_url('assets/css/profile.css')?>">
<script type="text/javascript" src="<?=base_url('assets/js/profile.js')?>"></script>

<?=form_open('/', array('method' => 'get'), array('logout' => 1));?>
<?=form_submit('', 'Déconnexion');?>
<?=form_close();?>
<?=anchor('prof/creer', '<div class="button">Créer un quiz</div>', 'title="Créer un nouveau quiz"')?>

<h3>Quiz créés par <?=$login?></h3>

<?php
$this->table->set_heading(array('Nom', 'Status', 'Réponses', ''));

$template = array('table_open' => '<table class="liste-quiz">');

$this->table->set_template($template);

//... populate table
// w/ data loaded in controller 
foreach($liste as $quiz)
{
	$links = anchor('prof/modifier/'.$quiz['id'], '<div class="button">Modifier</div>')
		.' '.anchor('prof/supprimer/'.$quiz['id'],'<div class="button delete">Supprimer</div>',array('onclick'=>'return confirm(\'Supprimer ce quiz et toutes les réponses associées ?\');'));
	$button = '<span class="button" onclick="afficherClefPartage(\''.$quiz['id'].'\',\''.$quiz['clefPartage'].'\');" id="bouton-partage-'.$quiz['id'].'">Partager</span>';
	$share = '<div class="clef-partage" id="partage-'.$quiz['id'].'"></div>';
		

	$this->table->add_row(
		$quiz['nom'],
		$quiz['status'],
		anchor('prof/stats/'.$quiz['id'], $quiz['nbReponses'], array('class' => 'button')),
		"$links\n $button\n $share" 
	);
}


echo $this->table->generate();
?>
