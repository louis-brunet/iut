<script type="text/javascript" src="<?=base_url('assets/js/creation.js')?>" ></script>
<link rel="stylesheet" type="text/css" href="<?=base_url('assets/css/creation.css')?>">
<h3>Création d'un quiz</h3>

<?php

echo form_open('prof/creer', array(
	'id'      =>'form-creation',
	'method'  =>'post',
	'onsubmit'=>'prepareForm();',
	'class'   => 'centered'
));
echo form_label('Nom:', 'nom-quiz');
echo form_input(array(
	'name'				=> 'nom-quiz',
	'id'   				=> 'nom-quiz',
	'maxlength'		=> '100',
	'size'  			=> '50',
	'value' 			=> set_value('nom-quiz'),
	'required' 		=> ''
));


echo '<br>' . form_label('Status:', 'status');
echo form_dropdown('status', array(
	'preparation' => 'En préparation',
	'actif'       => 'Actif'
), set_value('status'));

echo '<br>' . form_label('Durée:', 'duree');
?>
<input type="number" required id="duree" name="duree" maxlength="10" min="0" step="1" value="<?=set_value('duree')?>">
	<?=form_label('minute(s)', 'duree');?>
<br>
<?=form_label('Date d\'expiration:', 'expiration');?>

<input type="datetime-local" required id="expiration" name="expiration" value="<?=set_value('expiration')?>">
<div id="questions" class="questions">
	<h4>Questions</h4>
	<?= form_button('add-q', 'Ajouter une question', 'onClick="ajoutQuestion()"') ?> 
</div>

<br> 
<?=form_submit('', 'Créer');?>
<?= form_close()?>
