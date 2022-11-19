<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

class Model_prof extends CI_Model {
	public function __construct(){
		$this->load->database();
	}

	// Renvoie les données nécessaires pour afficher le profil d'un professeur.
	public function profile_data($login) {
		return array(
			'login' 	=> $login,
			'liste'   => $this->get_liste_quiz($login)
		);
	}

	// Renvoie un tableau de tableaux qui contiennent chacun les données d'un
	// quiz à afficher dans le profil du professeur correspondant à $login.
	private function get_liste_quiz($login) {
		$this->db->select('Quiz.idQuiz AS id, Quiz.nom, status, clefPartage, COUNT(ReponseQuiz.clefEleve) AS nbReponses, expiration')
			->from('Quiz')
			->join('ReponseQuiz', 'Quiz.idQuiz = ReponseQuiz.idQuiz', 'left')
			->where('Quiz.loginProf', $login)
			->group_by('Quiz.idQuiz')
			->order_by('nom', 'ASC'); 
		$query = $this->db->get();

		$res = $query->result_array();
		foreach($res as $key => $quiz)
		{
			if (strtotime($quiz['expiration']) < time())
			{
				$res[$key]['status'] = 'Expiré';
			}
			else if ($quiz['status'] == 'actif') 
			{
				$res[$key]['status'] = 'Actif';
			}
			else
			{
				$res[$key]['status'] = 'En préparation';
			}
		}

		return $res;
	}

	// Renvoie TRUE si une professeur avec ce mdp et login existe dans la BD, 
	// FALSE sinon.
	public function prof_exists($login, $mdp = null) {
		$this->db->select('loginProf, mdp')
			->from('Professeur')
			->where('loginProf', $login)
			->limit(1);

		$row = $this->db->get()->row_array();
		if(isset($row))
		{
			return password_verify($mdp, $row['mdp']);
		} 

		return false;
	}

	// Renvoie TRUE si un professeur avec ce login existe dans la BD,
	// FALSE sinon.
	public function login_exists($login) {
		$this->db->select('*')
				->from('Professeur')
				->where('loginProf', $login);
		$query = $this->db->get();

		return $query->num_rows() > 0;

	}

	// Ajouter un professeur avec le login et mdp donnés en argument dans la BD.
	// Renvoie TRUE si l'ajout a réussi, FALSE sinon.
	public function add_prof ($login, $mdp) {
		$data = array(
			'loginProf' 	=> $this->db->escape_str($login),
			'mdp' 				=> password_hash($mdp, PASSWORD_DEFAULT)
		);
		return $this->db->insert('Professeur', $data);
	}

}
