<?php date_default_timezone_set('Europe/Paris') ?>
<link rel="stylesheet" href="<?=base_url('assets/css/reponse.css')?>">
<script src="<?=base_url('assets/js/reponse.js')?>"></script>

<h3>Reponse</h3>
<h3><?=$nom?></h3>

<div class="date-fin">
	Vous avez jusqu'à <?=date('h:i:sa (Y-m-d)', $date_fin)?> pour envoyer votre réponse.
</div>

<?=form_open(
	'eleve/soumission', 
	array(
		'method'   => 'post', 
		'onsubmit' => "return validate('$date_fin');",
		'class'    => 'card centered',
	),
	array('id_quiz'=> $id_quiz)
)?>

<?=form_label('Prénom: ', 'prenom_eleve')?>
<?=form_input(array(
	'name'   => 'prenom_eleve',
	'id'     => 'prenom_eleve',
	'maxlength' => '60',
	'size'      => '30',
	'required'  => ''
))?>
<br>
<?=form_label('Nom: ', 'nom_eleve')?>
<?=form_input(array(  
	'name'   => 'nom_eleve',
	'id'     => 'nom_eleve',
	'maxlength' => '60',
	'size'      => '30',
	'required'  => ''
))?>

<?php foreach($questions as $num => $question): ?>
	<hr>

	<div class="question">
<?php if($question['src_img'] !== ''): ?>
	<img class="q-img" src="<?=$question['src_img']?>">
<?php endif; ?>

		<div class="q-texte">
			<?=($num + 1) . '. ' . $question['texte'] ?>
		</div>
		
		<div class="q-liste-choix">
			<?php foreach($question['reponses'] as $choix): ?>
				<div class="q-choix">
				<input type="checkbox" id="choix-<?=$choix['id']?>" name="choix-<?=$choix['id']?>" value="<?=$choix['id']?>">
					<label for="choix-<?=$choix['id']?>">
						<?=$choix['texte']?>
					</label>
				</div>
			<?php endforeach; ?>
		</div>
	</div>
<?php endforeach; ?>

<?=form_submit('repondre', 'Envoyer la réponse'
	//, array('onclick'=>"return submit($date_fin);")
);?>
<?=form_close()?>
