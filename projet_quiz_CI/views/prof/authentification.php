<?php
defined('BASEPATH') OR exit('No direct script access allowed');
?>

<h3>Professeurs</h3>
<?=form_open('accueil/inscription', array(
	'method' => 'get',
	'class'  => 'card centered'
))
. form_submit('', 'S\'inscrire')
. form_close()
. validation_errors('<div class="error">', '</div>')
. form_open('accueil', array(
	'method' => 'post',
	'class'  => 'card centered'
))
. form_label('Login :', 'login')
. form_input(array(
	'name'			=> 'login',
	'id'  			=> 'login',
	'maxlength'	=> '60',
	'value'			=> set_value('login'),
	'size'			=> '30',
	'required'   => ''
))?>

<br>

<?= form_label('Mot de passe :', 'mdp')
. form_password(array(
	'name'      => 'mdp',
	'id'        => 'mdp',
	'maxlength' => '60',
	'value'			=> set_value('mdp'),
	'size'      => '30',
	'required'  => ''
))?>

<br>

<?=form_submit('', 'S\'authentifier')
. form_close()
?>
