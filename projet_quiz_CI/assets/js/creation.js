var indexQuestion = 0;
var indexReponse = {};

function createQuestionDiv(index, question = null) {
	let div = createElement('div', 'question card centered');
	div.id = 'Q-' + index;

	let delButton = createElement('button');
	delButton.setAttribute('onclick', 'supprimerQuestion('+index+ (question === null ? '' : ','+question.idQuestion) +')');
	delButton.innerHTML = 'Supprimer';
	delButton.className = 'delete';
	div.append(delButton);

	let labelText = createElement('label');
	labelText.setAttribute('for', 'q-' + index + '-text');
	labelText.innerHTML = 'Texte:';
	div.append(labelText);

	let inputText = createElement('input');
	inputText.setAttribute('type', 'text');
	inputText.setAttribute('maxlength', '255');
	inputText.setAttribute('size', '40');
	inputText.id = 'q-' + index + '-text';
	inputText.name = inputText.id;
	inputText.setAttribute('required', '');
	if(question !== null) inputText.value = question.texte;
	div.append(inputText);

	div.append(createElement('br'));

	let labelImg = createElement('label');
	labelImg.setAttribute('for', 'q-' + index + '-img');
	labelImg.innerHTML = 'Image optionnelle (URL):';
	div.append(labelImg);

	let inputImg = createElement('input');
	inputImg.id = 'q-' + index + '-img';
	inputImg.name = inputImg.id;
	inputImg.setAttribute('type', 'url');
	inputImg.setAttribute('maxlength', '255');
	inputImg.setAttribute('size', '40');
	if (question !== null) inputImg.value = question.srcImg;
	div.append(inputImg);

	let reponsesDiv = createReponsesDiv(index);
	div.append(reponsesDiv);
	
	if (question !== null)
	{
		let id = createElement('input');
		id.setAttribute('type', 'hidden');
		id.name = 'q-'+index+'-id';
		id.value = question.idQuestion;

		div.append(id);
	}

	return div;
}

function createReponsesDiv(indexQuestion) {
	let div = createElement('div', 'reponses');
	div.id = 'q-' + indexQuestion + '-reponses';

	let addButton = createElement('button', ''); // TODO ajouter classe
	addButton.setAttribute('onclick', 'ajoutReponse(' + indexQuestion  + ');');
	addButton.type = 'button';
	addButton.innerHTML = 'Ajouter une réponse';
	div.append(addButton);


	return div;
}

function createReponse(indexQuestion, indexReponse, reponse = null) {
	let reponseDiv =  createElement('div', 'reponse');
	reponseDiv.id = 'q-'+indexQuestion+'-reponse-'+indexReponse;
	
	let labelText = createElement('label');
	labelText.setAttribute('for', 'q-'+indexQuestion+'-r-'+indexReponse+'-text');
	labelText.innerHTML = 'Texte:';
	reponseDiv.append(labelText);
	
	let inputText = createElement('input');
	inputText.id = 'q-'+indexQuestion+'-r-'+indexReponse+'-text';
	inputText.name = inputText.id;
	inputText.type = 'text';
	inputText.maxLength = '128';
	inputText.size = '30';
	inputText.setAttribute('required', '');
	if (reponse !== null) inputText.value = reponse.texte;
	reponseDiv.append(inputText);

	let labelPoints = createElement('label');
	labelPoints.setAttribute('for', 'q-'+indexQuestion+'-r-'+indexReponse+'-points');
	labelPoints.innerHTML = 'Points:';
	reponseDiv.append(labelPoints);

	let inputPoints = createElement('input');
	inputPoints.type = 'number';
	inputPoints.id = 'q-'+indexQuestion+'-r-'+indexReponse+'-points';
	inputPoints.name = inputPoints.id;
	inputPoints.style = 'width: 5em;';
	if (reponse !== null)	inputPoints.value = reponse.points;
	inputPoints.setAttribute('step', '0.5');
	inputPoints.setAttribute('required', '');
	reponseDiv.append(inputPoints);

	let deleteBtn = createElement('button', ''); // TODO ajouter classe
	deleteBtn.setAttribute('onclick', 'supprimerReponse('+indexQuestion+','+indexReponse+ (reponse === null ? '' : ', '+ reponse.idChoix) +')');
	deleteBtn.type = 'button';
	deleteBtn.innerHTML = 'Supprimer';
	deleteBtn.className = 'delete';
	reponseDiv.append(deleteBtn);

	if (reponse !== null)
	{
		let id = createElement('input');
		id.setAttribute('type', 'hidden');
		id.name = 'q-'+indexQuestion+'-r-'+indexReponse+'-id';
		id.value = reponse.idChoix;
		reponseDiv.append(id);
	}
	return reponseDiv;
}

function createElement(type, className = '') {
	let el = document.createElement(type);
	if(className != '')
	{
		el.className = className;
	}
	return el;
}


function ajoutQuestion(question = null) {
	let container = document.getElementById('questions');

	if(question === null)
	{
		let child = createQuestionDiv(indexQuestion);
		container.append(child);
	
		ajoutReponse(indexQuestion);
		ajoutReponse(indexQuestion);
	}
	else 
	{
		let questionDiv = createQuestionDiv(indexQuestion, question);
		container.append(questionDiv);
		question.reponses.forEach( reponse => ajoutReponse(indexQuestion, reponse));
	}

	indexQuestion++;
}

function supprimerQuestion(index, idQuestion = '') {
	let aSupprimer = document.getElementById('Q-' + index);
	
	if(aSupprimer) aSupprimer.remove();

	if (idQuestion !== '')
	{
		let hidden = createElement('input');
		hidden.setAttribute('type', 'hidden');
		hidden.name = 'supprimer-question-'+idQuestion;
		hidden.value = idQuestion;

		document.getElementById('form-creation').append(hidden);
	}
}

function supprimerReponse(indexQuestion, indexReponse, idChoix = '') {
	let aSupprimer = document.getElementById('q-'+indexQuestion+'-reponse-'+indexReponse);

	if (aSupprimer) aSupprimer.remove();

	if (idChoix !== '')
	{
		let hidden = createElement('input');
		hidden.setAttribute('type', 'hidden');
		hidden.name = 'supprimer-choix-'+idChoix;
		hidden.value = idChoix;

		document.getElementById('form-creation').append(hidden);
	}
}

// TODO tester les nb de reponses déjà créées
// Limiter à 4 réponses
function ajoutReponse(indexQuestion, reponse = null) {
	let nbReponses = numReponses(indexQuestion);
	if (nbReponses >= 4) 
	{
		alert('Une question peut avoir au maximum 4 réponses possibles.');
		return;
	}

	let container = document.getElementById('q-'+ indexQuestion +'-reponses');
	let index = indexReponse[indexQuestion];
	
	if (index === undefined)
	{
		index = 0;
	}

	let reponseDiv;
	if (reponse === null)
	{
		reponseDiv = createReponse(indexQuestion, index);
	}
	else
	{
		reponseDiv = createReponse(indexQuestion, index, reponse);
	}

	container.append(reponseDiv);
	indexReponse[indexQuestion] = index + 1;
}

//function ajoutReponses(indexQuestion, reponses) {
//	let container = document.getElementById('q-'+ indexQuestion +'-reponses');
//	let index = indexReponse[indexQuestion];
//	if (index === undefined)
//	{
//		index = 0;
//	}
//	reponses.forEach( reponse => {
//}

function numReponses(indexQuestion) {
	let reponses = document.querySelectorAll('[id^="q-'+indexQuestion+'-reponse-"]');
	return reponses.length;
}

function prepareForm() {
	let form = document.getElementById('form-creation');
	
	let hiddenNbQuestions = createElement('input');
	hiddenNbQuestions.setAttribute('type', 'hidden');
	hiddenNbQuestions.value = indexQuestion;
	hiddenNbQuestions.name = 'max-questions';
	form.append(hiddenNbQuestions);

	let hiddenIndexReponse = createElement('input');
	hiddenIndexReponse.setAttribute('type', 'hidden');
	hiddenIndexReponse.value = JSON.stringify(indexReponse);
	hiddenIndexReponse.name = 'max-reponses';
	form.append(hiddenIndexReponse);


	//alert('preparing form data. found form ['+form.id+'] & appended hidden stuff');
}

function afficherQuestions(json) {
	let questions = JSON.parse(json);

	questions.forEach(ajoutQuestion);
}
