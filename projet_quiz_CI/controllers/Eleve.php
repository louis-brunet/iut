<?php
error_reporting(E_ALL & ~E_DEPRECATED);

defined('BASEPATH') OR exit('No direct script access allowed');

class Eleve extends CI_Controller {

	// Recherche d'un quiz par un élève
	// Si le quiz n'est pas trouvé, afficher un message d'erreur.
	// Si le quiz est expiré, prévenir l'utilisateur, 
	// et proposer de chercher une correction.
	// Sinon, afficher des informations générales sur le quiz et proposer d'y répondre.
	public function recherche($clef = '') {
		// Si la clef de partage du quiz n'est pas donné e en argument,
		// la chercher dans $_GET.
		if ($clef === '')
		{
			$clef = filter_input(INPUT_GET, 'clef', FILTER_SANITIZE_STRING);
		}
		$this->load->model('model_quiz');
		$data = $this->model_quiz->data_recherche($clef);

		$this->load->view('templates/header');
		if ($data)
		{
			// Le quiz existe.
			if ( strtotime($data['expiration']) < time() )
			{
				// Le quiz est expiré. Proposer de trouver une correction.
				$this->load->helpers('form');
				$this->load->view('eleve/quiz_expire', $data);  
			}
			else 
			{
				// Le quiz n'est pas expiré. Proposer d'y répondre.
				$this->load->view('eleve/quiz_found', $data);
			}
		}
		else
		{
			// Le quiz n'a pas pu être trouvé. 
			$this->load->view('quiz_not_found'); 
		}
		$this->load->view('templates/footer');
	}

	// Réponse à un quiz par un élève.
	public function reponse($clef = '') {
		// Si aucune clef n'est donnée en argument, rediriger vers la page d'accueil.
		if ($clef === '')
		{
			redirect('/');
		}

		// Si l'élève rafraichit la page, le renvoyer à la page qui propose 
		// de démarrer le quiz.
		$refreshed = isset($_SERVER['HTTP_CACHE_CONTROL']) && ($_SERVER['HTTP_CACHE_CONTROL'] === 'max-age=0' || $_SERVER['HTTP_CACHE_CONTROL'] === 'no-cache' );
		if ($refreshed)
		{
			redirect("eleve/recherche/$clef");
		}
		
		$this->load->helpers('form');
		$this->load->model('model_quiz');

		$data = $this->model_quiz->get_quiz($clef);

		$this->load->view('templates/header');
		if ($data)
		{
			// Le quiz est trouvé.
			if ( strtotime($data['expiration']) < time() )
			{
				// Le quiz est expiré. Proposer de chercher une correction.
				$this->load->view('eleve/quiz_expire', $data); 
			}
		 	else if ( $data['status'] === 'preparation' )
			{
				// Le quiz est en préparation. Rediriger vers la page de recherched'un quiz.
				redirect("eleve/recherche/$clef");
			}
			else
			{
				// Le quiz est actif, afficher le formulaire de réponse.
				$this->load->view('eleve/reponse', $data);
			}
		}
		else
		{
			// Le quiz n'a pas été trouvé.
			$this->load->view('quiz_not_found'); 
		}
		$this->load->view('templates/footer');
		
	} 

	// Correction personnalisée d'un réponse à un quiz.
	public function correction ($clef = '') {
		// Si la clef personnelle n'est pas donnée en argument, la chercher dans $_GET.
		if ($clef === '')
		{
			$clef = filter_input(INPUT_GET, 'clef_e', FILTER_SANITIZE_STRING);
		}

		// Si aucune clef n'a été fournie, rediriger vers la page d'accueil.
		if ( $clef === FALSE OR $clef === NULL )
		{
			redirect('/');
		}

		$this->load->model('model_quiz');
		
		$data = $this->model_quiz->data_correction($clef);

		$this->load->view('templates/header');
		if ($data === FALSE)
		{
			// La correction n'a pas été trouvée, proposer de chercher de nouveau.
			$this->load->helpers('form');

			$this->load->view('eleve/correction_not_found');
			$this->load->view('eleve/recherche_correction');
		}
		else
		{
			// La correction a été trouvée.
			// Afficher les choix de l'élève et les réponses attendues.
			$this->load->view('eleve/correction', $data);
		}
		$this->load->view('templates/footer');
	}

	// Soumission d'un réponse à un quiz par un élève.
	public function soumission() {
		$id_quiz = filter_input(INPUT_POST, 'id_quiz', FILTER_VALIDATE_INT);
		if ( ! $id_quiz)
			redirect('/');

		$this->load->model('model_quiz');
		$this->load ->library('form_validation');
		
		$this->form_validation->set_rules('id_quiz', 'QUIZ ID', 'required');
		$this->form_validation->set_rules('prenom_eleve', 'PRENOM DE L\'ELEVE', 'required|trim');
		$this->form_validation->set_rules('nom_eleve', 'NOM DE L\'ELEVE', 'required|trim');

		$this->load->view('templates/header');
		if ($this->form_validation->run() === FALSE
			OR ($clef_perso = $this->model_quiz->add_reponse($id_quiz, $this->_data_post_soumission())) === FALSE)
		{
			$this->load->view('eleve/soumission_invalide'); // TODO creer cette vue
		}
		else
		{
			$this->load->view('eleve/soumission_valide', array('clef_perso'=>$clef_perso));
		}
		$this->load->view('templates/footer');
	}

	// Formatte et renvoie les données  contenues dans $_POST relatives à la soumission
	// d'une réponse à un quiz.
	public function _data_post_soumission() {
		$data = array(
			'nom' => filter_input(INPUT_POST, 'nom_eleve', FILTER_SANITIZE_STRING),
			'prenom' => filter_input(INPUT_POST, 'prenom_eleve', FILTER_SANITIZE_STRING),
			'reponses' => array()
		);
		// remplir le tableau `reponses` avec une liste de de valeurs correspondant 
		// aux idChoix des réponses choisies.
		foreach($_POST as $key => $value)
		{
			// Prendre les valeurs dans $_POST dont l'indice commence par 'choix-';
			if ( preg_match('/^choix-/', $key) )
			{
				array_push($data['reponses'], filter_var($value, FILTER_VALIDATE_INT));
			}
		}	
		return $data;
	}

}
