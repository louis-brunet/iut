<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Model_quiz extends CI_Model {
	public function __construct(){
		$this->load->database();
	}
	
	// Ajouter un quiz dans la BD avec les données fournies en argument.
	// Renvoie TRUE si l'ajout a réussi, FALSE sinon.
	public function add_quiz($data) {
		$quizData = array(
			'loginProf'   => $data['loginProf'],
			'nom'         => $data['nom'],
			'status'      => $data['status'],
			'expiration'  => $data['expiration'],
			'dureeMinutes'=> $data['duree'],
			'clefPartage' => uniqid('', TRUE)
		);

		if ($this->db->insert('Quiz', $quizData))
		{
			$idQuiz = $this->db->insert_id();
			$nbQuestions = count($data['questions']);

			for ($i = 0; $i < $nbQuestions; $i++)
			{
				$questionData = array(
					'idQuiz'  => $idQuiz,
					'texte'   => $data['questions'][$i]['text'],
					'srcImg'  => $data['questions'][$i]['img']
				);
				if ($this->db->insert('Question', $questionData))
				{
					$idQuestion = $this->db->insert_id();
					$nbChoix = count($data['questions'][$i]['reponses']);
					for ($j = 0; $j < $nbChoix; $j++)
					{
						$choixData = array(
							'idQuestion'  => $idQuestion,
							'texte'       => $data['questions'][$i]['reponses'][$j]['text'],
							'points'      => $data['questions'][$i]['reponses'][$j]['points']
						);

						if(!$this->db->insert('Choix', $choixData))
							return FALSE;
					}
				} 
				else return FALSE; 
			}
			return TRUE;
		} 
		return FALSE;
	}

	// Modifier un quiz dans la BD avec les données fournies en argument.
	// Renvoie TRUE si la modification a réussi, FALSE sinon.
	public function edit_quiz($login, $id_quiz, $data) {
		$query = $this->db
			->select('*')
			->from('Quiz')
			->where('idQuiz', $id_quiz)
			->where('loginProf', $login)
			->get();

		if ($query->num_rows() > 0)
		{
			$quiz = array(
				'nom'         => $data['nom'],
				'status'      => $data['status'],
				'expiration'  => $data['expiration'],
				'dureeMinutes'=> $data['duree']
			);
			$updated = $this->db
				->where('idQuiz', $id_quiz)
				->update('Quiz', $quiz);

			if ($updated === FALSE) 
			{
				echo'Quiz update failed';
				return FALSE;
			}
			
			foreach($data['supprimer']['choix'] as $id)
			{
				if ($this->delete_choix($id) === FALSE) 
					return FALSE;
			}

			foreach($data['supprimer']['questions'] as $id)
			{
				if ($this->delete_question($id) === FALSE) 
					return FALSE;
			}

			foreach($data['questions'] as $question)
			{
				if ($this->update_question($id_quiz, $question) === FALSE)
					return FALSE;
			}
			return TRUE;
		}

		return FALSE;
	}

	// Modifier une question dans la BD avec les données fournies en argument.
	// Renvoie TRUE si la modification a ré ussi, FALSE sinon.
	public function update_question($id_quiz, $question) {
		$data_question = array( 
			'idQuiz' => $id_quiz,
			'texte'  => $question['text'],
			'srcImg' => $question['img']
		);
		$updated_question = FALSE;
		if ($question['id_question'] !== '')
		{
			$updated_question = $this->db
				->where('idQuiz', $id_quiz)
				->where('idQuestion', $question['id_question'])
				->update('Question', $data_question);

			if ($updated_question === FALSE) {
				return FALSE; 
			}
		}
		else if ($this->db->insert('Question', $data_question) === FALSE)
		{
			return FALSE;
		}
		$id_question = $updated_question ? $question['id_question'] : $this->db->insert_id();

		foreach($question['reponses'] as $reponse) 
		{
			$data_reponse = array(
				'texte' => $reponse['text'],
				'points'=> $reponse['points'],
				'idQuestion' => $id_question
			);

			if($reponse['id_reponse'] === '')
			{
				if ($this->db->insert('Choix', $data_reponse) === FALSE)
				{
					return FALSE;
				}
			}
			else 
			{
				$updated_choix = $this->db 
					->where('idChoix', $reponse['id_reponse'])
					->where('idQuestion', $data_reponse['idQuestion'])
					->update('Choix', $data_reponse); 

				if( $updated_choix === FALSE)
				{
					return FALSE;
				}
			}
		}

		return TRUE;
	}
	
	// Supprimer le Choix dans la BD correspondant à l'id donné.
	// Renvoie TRUE si la suppression a réussi, FALSE sinon.
	public function delete_choix($id) {
		return $this->db
			->where('idChoix', $id)
			->delete('Choix');
	}

	// Supprimer la Question dans la BD correspondant à l'id donné.
	// Renvoie TRUE si la suppression a réussi, FALSE sinon.
	public function delete_question($id) {
		return $this->db
			->where('idQuestion', $id)
			->delete('Question');
	}

	// Supprimer le Quiz dans la BD.
	// Renvoie TRUE si la suppression a réussi, FALSE sinon.
	public function delete_quiz($id, $login) {
		$query = $this->db
			->select('loginProf')
			->from('Quiz')
			->where('idQuiz', $id)
			->get();

		if ($query->num_rows() > 0 AND $query->row_array()['loginProf'] == $login)
		{
			$this->db->delete('ReponseQuestion', "idQuiz = $id");
			$this->db->delete('ReponseQuiz', "idQuiz = $id");
			$this->db->delete('Quiz', "idQuiz = $id");

			return TRUE;
		}
	
		return FALSE;
	}

	// Ajouter une réponse à un quiz par un élève dans la BD.
	// Renvoie un identifiant unique permettant de retrouver la correction de cette réponse
	// une fois le quiz expiré.
	public function add_reponse($id_quiz, $data) {		
		$reponse_quiz = array(
			'clefEleve'  => uniqid('', TRUE),
			'idQuiz'     => $id_quiz,
			'nom'        => $data['nom'],
			'prenom'     => $data['prenom']
		);

		if ($this->db->insert('ReponseQuiz', $reponse_quiz))
		{
			foreach ($data['reponses'] as $id_choix)
			{
				$reponse_question = array(
					'idQuiz'   => $id_quiz,
					'clefEleve'=> $reponse_quiz['clefEleve'],
					'idChoix'  => $id_choix
				);
				if ( ! $this->db->insert('ReponseQuestion', $reponse_question) )
				{
					return FALSE;
				}
			}

			return $reponse_quiz['clefEleve'];
		}
		return FALSE;
	}

	// Renvoie les données nécessaires à l'affichage de la recherche du quiz qui correspond à la 
	// clef de partage donnée en argument,
	// ou FALSE si le quiz n'est pas trouvé.
	public function data_recherche($clef = '') {
		$this->db
			->select('clefPartage AS clef, Quiz.idQuiz AS id, nom, status, dureeMinutes AS duree, expiration, COUNT(idQuestion) AS nbQuestions')
			->from('Quiz')
			->join('Question', 'Quiz.idQuiz = Question.idQuiz', 'left')
			->where('clefPartage', $clef)
			->group_by('Quiz.idQuiz');
		$query = $this->db->get();
		if($query->num_rows() > 0) 
		{
			$data = $query->result_array();

			return $data[0];
		}
		return FALSE;
	}

	// Formate et renvoie toutes les données nécessaires à l'affichage du quiz identifié
	// par la clef de partage donnée en argument,
	// ou FALSE si le quiz n'est pas trouvé.
	public function get_quiz($clef = '') {
		// FORMAT: 
		//array(
		//	'nom'=>'',
		//	'id_quiz'=>'',
		//	'date_fin'=>'',
		//	'expiration' => '', 
		//	'score_max' => '',
		//	'questions'=>array(
		//		array('id'=>'','texte'=>'','src_img'=>'', 'reponses'=>array(
		//			array('id'=>'','texte'=>'','points'=>''), ...)),...)
		//);
		
		$queryQuiz = $this->db
			->select('nom, idQuiz, dureeMinutes, expiration')
			->from('Quiz')
			->where('clefPartage', $clef)
			->get();
		if ($queryQuiz->num_rows() > 0) 
		{
			$quiz = $queryQuiz->row_array();
			$data = array(
				'clefPartage'=> $clef,
				'nom'        => $quiz['nom'],
				'id_quiz'    => $quiz['idQuiz'],
				'date_fin'   => time() + ($quiz['dureeMinutes'] * 60),
				'expiration' => $quiz['expiration'],
				'score_max'  => $this->get_score_max($quiz['idQuiz']),
				'questions'  => array()
			);

			$queryQuestion = $this->db
				->select('idQuestion, texte, srcImg')
				->from('Question')
				->where('idQuiz', $quiz['idQuiz'])
				->get();

			foreach($queryQuestion->result_array() as $question)
			{
				$dataQuestion = array(
					'id'      => $question['idQuestion'],
					'texte'   => $question['texte'],
					'src_img' => $question['srcImg'],
					'reponses'=> array()
				);
				
				$queryChoix = $this->db
					->select('idChoix, texte, points')
					->from('Choix')
					->where('idQuestion', $question['idQuestion'])
					->get();

				foreach($queryChoix->result_array() as $choix) 
				{
					$dataReponse = array(
						'id'    => $choix['idChoix'],
						'texte' => $choix['texte'],
						'points'=> $choix['points']
					);
					array_push($dataQuestion['reponses'], $dataReponse);
				}

				array_push($data['questions'], $dataQuestion);
			}

			return $data;
		}
		
		return FALSE;
	}

	// Renvoie les données nécessaire à l'affichage du formulaire de modification d'un quiz.
	public function data_modifier($id_quiz, $login) {
		$query_quiz = $this->db
			->select('idQuiz AS id_quiz, nom, dureeMinutes AS duree, status, loginProf, expiration')
			->from('Quiz')
			->where('idQuiz', $id_quiz) 
			->where('loginProf', $login)
			->limit(1)
			->get();
		
		if ($query_quiz->num_rows() > 0)
		{
			$quiz = $query_quiz->row_array();
			$quiz['questions'] = array();
			$quiz['expiration'] = str_replace(' ', 'T', $quiz['expiration']);

			$query_question = $this->db
				->select('texte, idQuestion, srcImg')
				->from('Question')
				->where('idQuiz', $id_quiz)
				->get();
			
			foreach($query_question->result_array() as $question)
			{
				$question['reponses'] = array();
				
				$query_choix = $this->db
					->select('idChoix, texte, points')
					->from('Choix')
					->where('idQuestion', $question['idQuestion'])
					->get();
				foreach($query_choix->result_array() as $choix)
				{
					array_push($question['reponses'], $choix);
				}

				array_push($quiz['questions'], $question);
			}
			return $quiz;
		}

		return FALSE;
	}

	// Renvoie les données nécessaire  à l'affichage de la correction individuelle
	// d'un réponse à un quiz, identifiée par la clef personnelle donnée en argument. 
	public function data_correction($clef_eleve) {
		$quiz = $this->get_quiz($this->_clef_eleve_to_clef_quiz($clef_eleve));
		
		if ( $quiz !== FALSE
			AND strtotime($quiz['expiration']) < time() )
		{
			$eleve = $this->get_reponse_quiz($quiz['id_quiz'], $clef_eleve);

			if ($eleve !== FALSE)
			{
				$data = array(
					'quiz' => $quiz,
					'eleve'=> $eleve
				// infos eleve + reponses choisies ('eleve'=>array(...))
				);
		
				return $data;
			}

			return FALSE;
		}

		return FALSE;
	}

	// Prend en argument une clef personnelle d'un élève et renvoie la clef de partage
	// du quiz auquel cet élève a répondu.
	private function _clef_eleve_to_clef_quiz($clef_eleve) {
		$query = $this->db
			->select('clefPartage')
			->from('ReponseQuiz')
			->join('Quiz', 'Quiz.idQuiz = ReponseQuiz.idQuiz')
			->where('clefEleve', $clef_eleve)
			->limit(1)
			->get();

		if ($query->num_rows() > 0)
		{
			return $query->row()->clefPartage;
		}

		return '';
	}

	// Renvoie les données relatives à une réponse à un quiz, identifié par son idQuiz, 
	// par un élève, identifié par sa clef personnelle.
	public function get_reponse_quiz($id_quiz, $clef_eleve) {
		$query = $this->db
			->select('nom, prenom')
			->from('ReponseQuiz')
			->where('idQuiz', $id_quiz)
			->where('clefEleve', $clef_eleve)
			->limit(1)
			->get();

		if ( $query->num_rows() > 0 )
		{
			$row = $query->row_array();

			$eleve = array(
				'nom'         => $row['nom'],
				'prenom'      => $row['prenom'],
				'score_total' => $this->get_score_total($clef_eleve, $id_quiz),
				'reponses'    => $this->get_reponses($clef_eleve, $id_quiz),
				'scores'      => $this->get_scores($clef_eleve, $id_quiz)
			); /// 'reponses' => { idChoix }

			return $eleve;
		}

		return FALSE;
	}

	// Renvoie un tableau contenant les idChoix des réponses choisies par l'élève donné au quiz donné.
	public function get_reponses($clef_eleve, $id_quiz) {
		$query = $this->db
			->select('idChoix')
			->from('ReponseQuestion')
			->where('idQuiz', $id_quiz)
			->where('clefEleve', $clef_eleve)
			->get();

		$reponses = array();
		foreach($query->result_array() as $choix)
		{
			array_push($reponses, $choix['idChoix']);
		}

		return $reponses;
	}

	// Renvoie un tableau associant les idQuestion des questions qui 
	// appartiennent au quiz identifié par $id_quiz au score obtenu 
	// pour chaque question par l'élève identifié par $clef_eleve.
	public function get_scores($clef_eleve, $id_quiz) {
		$query = $this->db
			->select('idQuestion AS id')
			->from('Question')
			->where('idQuiz', $id_quiz)
			->get();
		$scores = array();

		foreach($query->result_array() as $question) 
		{
			$scores[$question['id']] = $this->get_score_question($clef_eleve, $id_quiz, $question['id']);
		}
		return $scores;
	}

	// Renvoie les doonnées nécessaires à l'affichage des statistiques 
	// globales d'un quiz,
	// ou FALSE si ce quiz n'existe pas ou si le login donné ne 
	// correspond pas au login du professeur qui a créé ce quiz
	public function data_stats($login, $id_quiz) {
		$query = $this->db
			->select('nom, COUNT(idQuestion) AS nb_questions')
			->from('Quiz')
			->join('Question', 'Quiz.idQuiz = Question.idQuiz','left')
			->where('Quiz.idQuiz', $id_quiz)
			->where('loginProf', $login)
			->group_by('Quiz.idQuiz')
			->get();

		if ($query->num_rows() > 0)
		{
			$data = $query->row_array();
			
			$data['score_max'] = $this->get_score_max($id_quiz);
			$data['reponses'] = $this->reponses_summary($id_quiz, $data['nb_questions']);

			return $data;
		}
		return FALSE;
	}

	// Renvoie le score maximal possible obtenable sur le quiz correspondant à $id_quiz. 
	public function get_score_max($id_quiz) {
		$res = 0;

		$query = $this->db
			->select('SUM(points) AS total')
			->from('Choix')
			->join('Question', 'Question.idQuestion = Choix.idQuestion')
			->where('idQuiz', $id_quiz)
			->where('points >=', 0)
			->get();
		
		if ($query->num_rows() > 0)
		{
			$res += $query->row()->total;
		}
	
		return $res;	
	}

	// Renvoie un tableau de tableaux qui contiennent des statistiques générales sur la
	// réponse à un quiz par un élève.
	public function reponses_summary($id_quiz, $nb_questions) {
		$res = array();
		
		$query = $this->db
			->select('nom, prenom, clefEleve AS clef_eleve, soumission')
			->from('ReponseQuiz')
			->where('idQuiz', $id_quiz)
			->order_by('soumission', 'DESC')
			->get();

		foreach($query->result_array() as $reponse)
		{
			$reponse['score'] = $this->get_score_total($reponse['clef_eleve'], $id_quiz);
			$reponse['q_ratees_nb'] = $this->nb_questions_ratees($reponse['clef_eleve'], $id_quiz);
			$reponse['q_ratees_pourcent'] = ($reponse['q_ratees_nb'] / $nb_questions) * 100;			
			
			array_push($res, $reponse);
		}

		return $res;
	}

	// Renvoie le score total obtenu par un élève donné pour un quiz donné.
	public function get_score_total($clef_eleve, $id_quiz) {
		$score = 0;

		$query =$this->db
			->select('idQuestion AS id')
			->distinct()
			->from('ReponseQuestion')
			->join('Choix', 'ReponseQuestion.idChoix = Choix.idChoix')
			->where('clefEleve', $clef_eleve)
			->where('idQuiz', $id_quiz)
			->get();

		foreach($query->result_array() as $question)
		{
			$score_question = $this->get_score_question($clef_eleve, $id_quiz, $question['id']);
			$score += $score_question;
		}
		return $score;
	}

	// Renvoie le score obtenu par un élève à une question dans un quiz donné.
	public function get_score_question($clef_eleve, $id_quiz, $id_question) {
		$res = 0;

		$query = $this->db
			->select('points')
			->from('ReponseQuestion')
			->join('Choix', 'ReponseQuestion.idChoix = Choix.idChoix')
			->where('idQuiz', $id_quiz)
			->where('clefEleve', $clef_eleve)
			->where('idQuestion', $id_question)
			->get();
		
		foreach($query->result_array() as $choix)
		{
			if ($choix['points'] <= 0)
			{
				return 0;
			}
			
			$res += $choix['points'];
		}

		return $res;
	}

	// Renvoie le nombre de question d'un quiz pour lesquelles l'élève donné a eu 0 ou moins de points.
	public function nb_questions_ratees($clef_eleve, $id_quiz) {
		$count = 0;

		$query = $this->db
			->select('idQuestion')
			->from('Question')
			->where('idQuiz', $id_quiz)
			->get();

		foreach($query->result_array() as $res)
		{
			$score = $this->get_score_question($clef_eleve, $id_quiz, $res['idQuestion']);

			if ($score <= 0)
			{
				$count++;
			}
		}

		return $count;
	}

}
