<?php
defined('BASEPATH') OR exit('No direct script access allowed');

echo '<h3>Inscription</h3>';

echo validation_errors('<div class="error">', '</div>');

echo form_open('accueil/inscription', array('method' => 'post'));
echo form_label('Login :', 'login');
echo form_input(array(
	'name' 		=> 'login',
	'id'			=> 'login',
	'maxlength'=> '60',
	'size'		=> '30',
	'value'		=> set_value('login'),
	'required'=> ''
));

echo '<br>' . form_label('Mot de passe :',  'mdp');
echo form_password(array(
	'name'		=> 'mdp',
	'id' 			=> 'mdp',
	'maxlength'=> '60',
	'size'		=> '30',
	'value'		=> set_value('mdp'),
	'required'=> ''
));

echo '<br>' . form_label('Confirmation mot de passe :',  'mdp-conf');
echo form_password(array(
	'name'    => 'mdp-conf',
	'id'      => 'mdp-conf',
	'maxlength'=> '60',
	'size'    => '30',
	'required'=> ''
));

echo '<br>' . form_submit('', 'Inscription');
echo form_close();

?>
