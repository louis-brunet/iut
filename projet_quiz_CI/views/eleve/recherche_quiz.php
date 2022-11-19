<?php
defined('BASEPATH') OR exit('No direct script access allowed');
?>
<hr><h3>Elèves</h3>

<?=form_open('eleve/recherche', array(
	'method' => 'get',
	'class' => 'card centered'
))
. form_label('Clef du quiz :', 'clef')
. form_input(array(
	'name'			=> 'clef',
	'id'  			=> 'clef',
	'maxlength'	=> '80',
	'size'			=> '40',
	'required'  => ''
))
. form_submit('', 'Trouver quiz')
. form_close()
?>
