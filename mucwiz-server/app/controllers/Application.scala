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
  
  
  // { "key":"dummyKey", "questions": [ {"spotifyUri":"akwekfkake","type": "artist", "alternatives": ["Mchek", "Something"], correctAnswer: 1}] }
  // returns: Json {"status":"ok", "key"->"dummyKey"}
  def create_quiz = Action (parse.json){ request =>

    val key = (request.body \ "key").as[String]
    val questions = (request.body \ "questions").as[List[JsObject]]
    val newQuiz = questions.foldLeft(Quiz.makeQuiz()){ (quiz,jsObj) => 
    						Quiz.addQuestion(quiz, 
    						(jsObj \ "spotifyUri").as[String],  
    						(jsObj \ "type").as[String], 
    						(jsObj \ "alternatives").as[List[String]],
    						(jsObj \ "correctAnswer").as[Int])	
    						}

    QuizHandler.setQuiz(key, newQuiz)
    val status = "ok"
    Ok(generate(Map("status"->"ok", "key"->key)))
     
  }
  // {"key":"dummyKey", "player":"PowErPlayer", }
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
					val jsonQuiz = Map("updated_answers"->generateAnswerMap(quiz.updates), "key"->key)
					QuizHandler.setQuiz(key, Quiz.emptyUpdates(quiz))
					Ok(generate(Map("status"->"ok", "quiz"->jsonQuiz)))
			case None => Ok(generate(Map("status"->"not found")))
		}
  }
  
  //{"key":"dummyKey"}
  // returns {"status":"ok","quiz":{"key":"dummyKey","questions":[{"spotifyUri":"akwekfkake","alternatives":["Mchek","Something"],"rightAnswer":1}],"players":["TheOtheyer"],"answers":[{"player":"TheOtheyer","answers":[{"question_index":0,"answer":1}]}]}
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
  
  //{"key":"dummyKey", "player":"SuperPlaey", "question_index":2, "answer":3}
  // returns Json {"status": "ok" | "not found", quiz-jsonized}
  def client_answer = Action (parse.json){ request =>
	val key = (request.body \ "key").as[String]
	val player = (request.body \ "player").as[String]
	val questionIndex = (request.body \ "question_index").as[Int]
	val answer = (request.body \ "answer").as[Int]
	val quiz = QuizHandler.getQuiz(key)

	quiz match {
		case Some(quiz) => 
			QuizHandler.setQuiz(key, Quiz.updateQuiz(quiz, player, questionIndex, answer))
			Ok(generate(Map("status"->"ok")))
		case None => Ok(generate(Map("status"->"not found")))
			
	}
  }
  
  def start_quiz = Action (parse.json){ request =>
	val key = (request.body \ "key").as[String]
	
	val quiz = QuizHandler.getQuiz(key)

	quiz match {
		case Some(quiz) => 
			QuizHandler.setQuiz(key, Quiz.setStatus(quiz, "started"))
			Ok(generate(Map("status"->"ok")))
		case None => Ok(generate(Map("status"->"not found")))
			
	}
  }
  
  //{ "key":"dummyKey", "questions": [ {"spotify_uri":"akwekfkake","type": "artist", "answers": ["Mchek", "Something"], correct_answer: 1}] }
  def generateJsonQuiz(quiz: Quiz, key: String) = {
	  val answerMap = generateAnswerMap(quiz.answers)
	  Map("key"->key, "questions"-> quiz.questions, "players"->quiz.players, "all_answers"->answerMap )
  }
  
  def generateAnswerMap(answers: Map[String, List[(Int,Int)]]) = {
	  answers.map(x => Map("player"->x._1, "answers"->x._2.map(y => Map("question_index"->y._1, "answer"->y._2 ))))
  }
  
}