package controllers

import play.api._
import play.api.mvc._
import models.QuizHandler
import models.Quiz
import play.api.libs.json._

import com.codahale.jerkson.Json._
import models.Question

object Application extends Controller {
  
  def index = Action {
    Ok("test")
  }
  
  
  // { "key":"dummyKey", "questions": [ {"spotify_uri":"akwekfkake","type": "artist", "alternatives": ["Mchek", "Something"], correct_answer: 1}] }
  // returns: Json {"status":"ok", "key"->"dummyKey"}
  def create_quiz = Action (parse.json){ request =>

    val key = (request.body \ "key").as[String]

    val questions = (request.body \ "questions").as[List[JsObject]]
    val quiz = Quiz.makeQuiz()	
    val newQuiz = questions.foldLeft(quiz){ (quizs,jsObj) => 
    						Quiz.addQuestion(quizs, 
    						(jsObj \ "spotify_uri").as[String],  
    						(jsObj \ "type").as[String], 
    						(jsObj \ "alternatives").as[List[String]],
    						(jsObj \ "correct_answer").as[Int])	
    						}

    QuizHandler.setQuiz(key, newQuiz)
    val status = "ok"
    Ok(generate(Map("status"->"ok", "key"->key)))
     
  }
  // {"key":"dummyKey", "player":"PowErPlayer"}
  // returns Json {"status": "ok" | "not found"}
  def join_quiz = Action (parse.json){ request =>

  	val key = (request.body \ "key").as[String]
  	val player = (request.body \ "player").as[String]
  	val quiz = QuizHandler.getQuiz(key)
  	quiz match {
  		case Some(quiz) => 
  				val newQuiz = Quiz.joinQuiz(quiz, player)
  				QuizHandler.setQuiz(key, newQuiz)
  				Ok(generate(Map("status"->"ok")))
  		case None => Ok(generate(Map("status"->"not found")))
  		}

  }
  //{"status": "ok" | "not found", updates (Map(playername -> List(indexofquestion, choise))}
  def get_updated_quiz = Action (parse.json){ request =>
	val key = (request.body \ "key").as[String]
	val quiz = QuizHandler.getQuiz(key)
	quiz match {
			case Some(quiz) =>
					val updates = quiz.updates
					val jsonQuiz = Map("updated_answers"->updates, "key"->key)
					QuizHandler.setQuiz(key, Quiz.emptyUpdates(quiz))
					Ok(generate(Map("status"->"ok", "quiz"->jsonQuiz)))
			case None => Ok(generate(Map("status"->"not found")))
		}
  }
  
  //{"key":"dummyKey"}
  // returns Json {"status": "ok" | "not found", quiz-jsonized}
  def get_quiz = Action (parse.json){ request =>
	val key = (request.body \ "key").as[String]
	val quiz = QuizHandler.getQuiz(key)
	
	quiz match {
		case Some(quiz) => 
				val jsonQuiz = generateJsonQuiz(quiz, key)
				Ok(generate(Map("status"->"ok", "quiz"->jsonQuiz)))
		case None => Ok(generate(Map("status"->"not found")))
	}
  }
  
  //{ "key":"dummyKey", "questions": [ {"spotify_uri":"akwekfkake","type": "artist", "answers": ["Mchek", "Something"], correct_answer: 1}] }
  def generateJsonQuiz(quiz: Quiz, key: String) = {
	  //val questionMap = quiz.questions.foldLeft(Map.empty[String])
	  Map("key"->key, "questions"-> quiz.questions, "players"->quiz.players )
  }
  
}