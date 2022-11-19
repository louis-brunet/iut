<script type="text/javascript" src="<?=base_url('assets/js/creation.js')?>" ></script>
<link rel="stylesheet" type="text/css" href="<?=base_url('assets/css/creation.css')?>">
<h3>Modification d'un quiz</h3>

<?= form_open('prof/modifier', array(
	'id'      =>'form-creation',
	'method'  =>'post',	
	'onsubmit'=>'prepareForm();',
	'class'   => 'centered'
), array('id_quiz' => $id_quiz))
. form_label('Nom:', 'nom-quiz')
. form_input(array(
	'name'				=> 'nom-quiz',
	'id'   				=> 'nom-quiz',
	'maxlength'		=> '100',
	'size'  			=> '50',
	'value' 			=> $nom,
	'required' 		=> ''
));?>
<br>
<?=form_label('Status:', 'status')
. form_dropdown('status', array(
	'preparation' => 'En préparation',
	'actif'       => 'Actif'
), $status)?>
<br>

<?=form_label('Durée:', 'duree');?>
<input type="number" required id="duree" name="duree" maxlength="10" min="0" step="1" value="<?=$duree?>">
<?=form_label('minute(s)', 'duree');?>
<br>

<?=form_label('Date d\'expiration:', 'expiration');?>
<input type="datetime-local" required id="expiration" name="expiration" value="<?=$expiration?>">

<div id="questions" class="questions centered">
	<h4>Questions</h4>
	<?= form_button('add-q', 'Ajouter une question', 'onClick="ajoutQuestion()"') ?> 
</div>
 
<?=form_submit('', 'Modifier');?>
<?=form_close();?>

<script type="application/javascript">
	afficherQuestions('<?=json_encode($questions)?>');
</script>
