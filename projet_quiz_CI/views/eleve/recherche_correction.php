<?php
defined('BASEPATH') OR exit('No direct script access allowed');
?>

<?= form_open('eleve/correction', array(
	'method' => 'get',
	'class' => 'card centered'
))
. form_label('Clef personnelle :', 'clef_e')
. form_input(array(
	'name'			=> 'clef_e',
	'id'  			=> 'clef_e',
	'maxlength'	=> '80',
	'size'			=> '40',
	'required'  => ''
))
. form_submit('', 'Trouver correction')
. form_close()

?>
