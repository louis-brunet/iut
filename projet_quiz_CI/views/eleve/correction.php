<link rel="stylesheet" href="<?=base_url('assets/css/correction.css')?>">
<button type="button" onclick="javascript:history.back()">Retour</button>
<h3>Réponse au quiz <i><?=$quiz['nom']?></i></h3>

<p>
	<b>Nom:</b> <?=$eleve['nom']?>
	<b>Prénom:</b> <?=$eleve['prenom']?>
</p>

<p>	<b>Score:</b> <?=$eleve['score_total']?> sur <?=$quiz['score_max']?> point(s) possible(s)</p>

<p>La réponse choisie  à chaque question est mise en gras</p>

<?php foreach($quiz['questions'] as $question): ?>

<div class="question card">
	<div class="q-texte"><?=$question['texte']?></div>

<?php foreach ($question['reponses'] as $reponse):
$choisie = in_array($reponse['id'], $eleve['reponses']);
?>

	<div class="reponse">
		<?php if ($choisie) echo '<b>';?>
		<span class="r-texte"><?=$reponse['texte']?></span>
		<?php if ($choisie) echo '</b>';?>
		<span class="r-points"><?=$reponse['points'] . ' point(s)'?></span>
	</div>
<?php endforeach; ?> <!--Réponses-->

	<div class="q-score">Score: <?=$eleve['scores'][$question['id']]?></div>
</div>

<?php endforeach; ?> <!--Questions-->
