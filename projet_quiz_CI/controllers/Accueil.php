<?php
error_reporting(E_ALL & ~E_DEPRECATED);

defined('BASEPATH') OR exit('No direct script access allowed');

class Accueil extends CI_Controller {

	// Page d'accueil du site.
	// Si un professeur est connecté ou des données d'authentification 
	// dans $_GET sont présentes et valides, alors afficher le profile du professeur.
	// Sinon, proposer de s'inscrire, s'authentifier, rechercher un quiz ou rechercher
	// un correction à un quiz.
	public function index() {
		session_start();

		$this->load->model('model_prof');
		$this->load->helpers('form');
		$this->load->library('form_validation');

		// Gérer la déconnexion
		$deconnexion = filter_input(INPUT_GET, 'logout', FILTER_SANITIZE_STRING);
		if ($deconnexion == 1)
		{
			unset($_SESSION['login']);
		}

		// Règles de validation: login et mdp requis, le professeur existe dans la BD.
		$this->form_validation->set_rules('login', 'LOGIN', 'required|callback__prof_exists', array('_prof_exists' => 'Login ou mot de passe incorrect'));
		$this->form_validation->set_rules('mdp', 'MOT DE PASSE', 'required');
		$this->form_validation->set_message('required',  'Le champs {field} doit être rempli.');

		if ( !isset($_SESSION['login']) &&
			$this->form_validation->run() == false)
		{
			// L'utilisateur n'est pas connecté, afficher la page d'accueil.
			$this->load->view('templates/header');
			$this->load->view('prof/authentification');
			$this->load->view('eleve/recherche_quiz');
			$this->load->view('eleve/recherche_correction');
			$this->load->view('templates/footer');
		}
		else 
		{
			if(!isset($_SESSION['login']))
			{
				// Si un professeur vient de se connecter, enregistrer son login dans 
				// une variable de session
				$_SESSION['login'] = filter_input(INPUT_POST, 'login', FILTER_SANITIZE_STRING);
			}
			$this->load->library('table');
			
			$data = $this->model_prof->profile_data($_SESSION['login']);

			// Le professeur est connecté, afficher son profil contenant ses quiz créés.
			$this->load->view('templates/header');
			$this->load->view('prof/profile', $data);
			$this->load->view('templates/footer');
		}
	}

	// Formulaire d'inscription.
	// Si l'insciption échoue, avertir l'utilisateur.
	// Sinon, le rediriger vers son profil.
	public function inscription() {
		session_start();

		$this->load->model('model_prof');
		$this->load->helpers('form');
		$this->load->library('form_validation');

		// Règles de validation: 
		// login, mdp et mdp-conf requis, 
		// mdp-conf est identique à mdp,
		// le login n'existe pas dans la BD.
		$this->form_validation->set_rules('login', 'LOGIN', 'required|callback__login_not_in_db', array('_login_not_in_db' => 'Ce LOGIN est déjà pris, veuillez en choisir un autre.'));
		$this->form_validation->set_rules('mdp', 'MOT DE PASSE', 'required');
		$this->form_validation->set_rules('mdp-conf', 'CONFIRMATION MOT DE PASSE', 'required|matches[mdp]', array('matches' => 'Le champs de confirmation du mot de passe ne correspond pas au mot de passe entré.'));

		if($this->form_validation->run() == false) 
		{
			// Les données POST ne sont pas validées, le formulaire est affiché.
			$this->load->view('templates/header');
			$this->load->view('prof/inscription');
			$this->load->view('templates/footer');
		}
		else 
		{
			$login = filter_input(INPUT_POST, 'login', FILTER_SANITIZE_STRING);
			$mdp = filter_input(INPUT_POST, 'mdp', FILTER_SANITIZE_STRING);

			if ($this->model_prof->add_prof($login, $mdp) )
			{
				// Le professeur a été ajouté à la BD, il est redirigé vers son profil.
				$_SESSION['login'] = $login;
				redirect('/');
			}
			else
			{
				// Le professeur n'a pas pu être ajouté à la BD, un message d'erreur est affcihé.
				$this->load->view('templates/header');
				$this->load->view('prof/echec_inscription'); // TODO créer cette vue
				$this->load->view('templates/footer');
			}
		}
	}

	// Callback utilisé pour la validation des données d'authentification.
	// Renvoie TRUE si le professeur existe dans la BD, FALSE sinon.
	public function _prof_exists($login) {
		$mdp = filter_input(INPUT_POST, 'mdp', FILTER_SANITIZE_STRING);
		if($mdp)
		{
			return $this->model_prof->prof_exists($login, $mdp);
		}
		return FALSE;
	}

	// Callback utilisé  pour la validation des donné es d'inscription.
	// Renvoie FALSE si le login donné existe dans la BD, TRUE sinon. 
	public function _login_not_in_db($login) {
		return  ! $this->model_prof->login_exists($login);
	}
}
