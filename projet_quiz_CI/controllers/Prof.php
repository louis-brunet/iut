<?php
error_reporting(E_ALL & ~E_DEPRECATED);

defined('BASEPATH') OR exit('No direct script access allowed');

class Prof extends CI_Controller {

	// Suppression du quiz correspondant à l'argument.
	// Echoue si l'utilisateur connecté n'est pas le professeur qui a créé ce quiz.
	public function supprimer($id) {
		session_start();
		// Si l'utilisateur n'est pas un professeur connecté, rediriger vers la page d'accueil.
		if ( ! isset($_SESSION['login']))
			redirect('/');
		
		$this->load->model('model_quiz');
		$id = filter_var($id, FILTER_VALIDATE_INT);

		$this->load->view('templates/header');	
		// Essayer de supprimer le quiz, si celui-ci a été créé par l'utilisateur connecté.
		if ($this->model_quiz->delete_quiz($id, $_SESSION['login']) === TRUE)
		{
			// La suppression a marché.
			$this->load->view('prof/suppression_succes'); 
		}
		else
		{
			// La suppression a échoué.
			$this->load->view('prof/suppression_echec'); 
		}
		$this->load->view('templates/footer');
	}

	// Création d'un quiz par un professeur.
	public function creer() {
		session_start();
		// Si l'utilisateur n'est pas connecté en tant que professeur, 
		// rediriger vers la page d'accueil.
		if ( ! isset($_SESSION['login'])) 
			redirect('/');

		$this->load->model('model_quiz');
		$this->load->helpers('form');
		$this->load->library('form_validation');

		// Règles de validation:  éléments requis, trim les caractères d'espacement.
		$this->form_validation->set_rules('nom-quiz', 'NOM DU QUIZ', 'required|trim');
		$this->form_validation->set_rules('duree', 'DUREE', 'required');
		$this->form_validation->set_rules('expiration', 'DATE D\'EXPIRATION', 'required');
		$this->form_validation->set_rules('status', 'STATUS', 'required');

		if($this->form_validation->run() == FALSE)
		{
			// La validation des donnnées dans $_POST a échoué. Afficher le formulaire
			// de création d'un quiz.
			$this->load->view('templates/header');
			$this->load->view('prof/creation'); 
			$this->load->view('templates/footer');
		}
		else
		{
			// Formatter des données dans $_POST.
			$quizData = $this->_data_post_creer();
			// Essayer d'ajouter ces données formattées dans la BD.
			if ($this->model_quiz->add_quiz($quizData))
			{
				// Le quiz a été créé.
				$this->load->view('templates/header');
				$this->load->view('prof/ajout_quiz_succes'); // TODO créer cette vue
				$this->load->view('templates/footer');
			}
			else 
			{	
				// La création du quiz a échoué. Afficher un message d'erreur.
				$this->load->view('templates/header');
				$this->load->view('prof/ajout_quiz_echec'); // TODO créer cette vue
				$this->load->view('templates/footer');
			}
		}
	}

	// Modification des donnéees d'un quiz par le professeur qui l'a créé.
	public function modifier($id_quiz = '') {
		session_start();
		// Si l'utilisateur n'est pas connectéen tant que professeur,
		// rediriger vers la page d'accueil.
		if ( ! isset($_SESSION['login']) )
			redirect('/');
		
		$this->load->model('model_quiz');

		if ( $id_quiz === '' ) 
		{
			// Essayer de modifier le quiz correspondant à $id_quiz 
			// avec les données dans $_POST
			$this->load->library('form_validation');

			// Règles de validation: champs requis et trim les espaces.
			$this->form_validation->set_rules('id_quiz', 'ID DU QUIZ', 'required|trim');	
			$this->form_validation->set_rules('nom-quiz', 'NOM DU QUIZ', 'required|trim');
			$this->form_validation->set_rules('duree', 'DUREE', 'required');
			$this->form_validation->set_rules('expiration', 'DATE D\'EXPIRATION', 'required');	
			$this->form_validation->set_rules('status', 'STATUS', 'required');

			$id_quiz = filter_input(INPUT_POST, 'id_quiz', FILTER_VALIDATE_FLOAT);

			$this->load->view('templates/header');

			// Valider les données dans $_POST puis essayer de modifier le quiz dans la BD.
			if ($this->form_validation->run() === FALSE
				OR $this->model_quiz->edit_quiz($_SESSION['login'], $id_quiz, $this->_data_post_creer()) === FALSE)
			{
				// Modification échouée.
				$this->load->view('prof/edit_echec'); 
			} 
			else 
			{
				// Modification réussie.
				$this->load->view('prof/edit_succes');
			}
			$this->load->view('templates/footer');
		}
		else
		{
			$this->load->helpers('form');

			$data = $this->model_quiz->data_modifier($id_quiz, $_SESSION['login']);

			$this->load->view('templates/header');
			if ($data !== FALSE)
			{
				// Afficher le quiz existant et une interface de modification. 
				$this->load->view('prof/modification', $data);
			}
			else
			{
				// Le quiz à modifier n'a pas été trouvé.
				$this->load->view('quiz_not_found');
			}
			$this->load->view('templates/footer');
		}
	}

	// Affichage des statistiques des réponses à un quiz.
	public function stats($id_quiz ='') {
		session_start();
		$this->load->model('model_quiz');

		// Si l'utilisateur n'est pas connecté en tant que professeur ou aucun 
		// argument n'est donné, rediriger vers la page d'accueil.
		if ( ! isset($_SESSION['login']) 
			OR $id_quiz === '' )
		{
			redirect('/');
		}

		// Verifier que le professeur connecté est celui qui a créé le quiz.
		$data = $this->model_quiz->data_stats($_SESSION['login'], $id_quiz);
		
		$this->load->view('templates/header');
		if ($data === FALSE)
		{
			// Le quiz n'a pas été trouvé.
			$this->load->view('quiz_not_found');
		}
		else
		{
			// Le quiz a été trouvé, afficher les statistiques correspondantes.
			$this->load->library('table');
			$this->load->view('prof/stats', $data);
		}

		$this->load->view('templates/footer');

	}

	// Formatter et renvoyer les données dans $_POST correspondant à la création 
	// ou la modification d'un quiz.
	public function _data_post_creer() {
		$max_questions = filter_input(INPUT_POST, 'max-questions', FILTER_VALIDATE_INT);
		$max_reponses = JSON_decode($this->input->post('max-reponses'));
		
		// Informations générales sur le quiz.
		$quizData = array(
			'loginProf'    => $_SESSION['login'],
			'nom'          => filter_input(INPUT_POST, 'nom-quiz', FILTER_SANITIZE_STRING), 
			'duree'        => filter_input(INPUT_POST, 'duree', FILTER_VALIDATE_INT),
			'status'       => filter_input(INPUT_POST, 'status', FILTER_SANITIZE_STRING),
			'expiration'   => filter_input(INPUT_POST, 'expiration', FILTER_SANITIZE_STRING), 
			'questions'    => array(),
			'supprimer'    => array(
				'choix'  => $this->_post_choix_supprimes(),
				'questions' => $this->_post_questions_supprimees()
			)
		);
		$id_quiz = filter_input(INPUT_POST, 'id_quiz', FILTER_VALIDATE_INT);
		$quizData['id_quiz'] = $id_quiz ? $id_quiz : '';

		// Récupérer les différentes questions et réponses à associer à ce quiz.
		$nbQuestions = 0;
		for ($i = 0; $i < $max_questions; $i++)
		{ 
			$question = array('text' => filter_input(INPUT_POST, "q-$i-text", FILTER_SANITIZE_STRING));
			if ($question['text'])
			{    
				$question['img'] = filter_input(INPUT_POST, "q-$i-img", FILTER_SANITIZE_STRING);
				$question['reponses'] = array();  
				$id_question = filter_input(INPUT_POST, "q-$i-id", FILTER_VALIDATE_INT);
				$question['id_question'] = $id_question ? $id_question : '';

				$nbReponses = 0;
				for ($j = 0; $j < $max_reponses->$i; $j++)        
				{        
					$repText = filter_input(INPUT_POST, "q-$i-r-$j-text", FILTER_SANITIZE_STRING);    
					if ($repText)    
					{         
						$repPoints = filter_input(INPUT_POST, "q-$i-r-$j-points", FILTER_VALIDATE_FLOAT); 
						$id_reponse = filter_input(INPUT_POST, "q-$i-r-$j-id", FILTER_VALIDATE_INT);

						$question['reponses'][$nbReponses] = array(   
							'text'       => $repText,   
							'points'     => $repPoints ,
							'id_reponse' => $id_reponse ? $id_reponse : ''
						);        
						$nbReponses++;
					}
				}
				$quizData['questions'][$nbQuestions] = $question;
				$nbQuestions++; 
			}
		}


		return $quizData;		
	}

	// Renvoie un tableau d'entiers correspondant aux id des choix à supprimer,
	// selon les données dans $_POST.
	public function _post_choix_supprimes() {
		$res = array();
		foreach($_POST as $key => $value)
		{
			if ( preg_match('/^supprimer-choix-/', $key) )
			{
				array_push($res, filter_var($value, FILTER_VALIDATE_INT));
			}
		}
		return $res;
	}

	// Renvoie un tableau d'entiers correspondant aux id des questions à supprimer,
	// selon les données dans $_POST.
	public function _post_questions_supprimees() {
		$res = array();
		foreach($_POST as $key => $value)
		{
			if (preg_match('/^supprimer-question-/', $key))
			{
				array_push($res, filter_var($value, FILTER_VALIDATE_INT));
			}
		}
		return $res;
	}

}
