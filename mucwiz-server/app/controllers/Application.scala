package controllers

import play.api._
import play.api.mvc._
import models.QuizHandler
import models.Quiz


object Application extends Controller {
  
  def index = Action {
    Ok("test")
  }
  
  def create_quiz = Action(parse.json) { request =>
     val key = (request.body \ "key").as[String]
     val gameName = (request.body \ "game_name").asOpt[String]
     val admin = (request.body \ "admin").as[String]
     val quiz = Quiz.makeQuiz(admin)
     Ok("rast")
     //QuizHandler.createQuiz(key, quiz)
  }
 
  
}