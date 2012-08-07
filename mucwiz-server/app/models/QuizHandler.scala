package models

import play.api.cache.Cache
import play.api.Play.current

object QuizHandler {
	def createQuiz(key: String, quiz: Quiz) = {
		setQuiz(key, quiz)
	}
  
	def setQuiz(key: String, quiz: Quiz) = {
		Cache.set(key, quiz, 50*60)
	  
	}
	def getQuiz(key: String): Option[Quiz] = {
		Cache.getAs[Quiz](key)
	  
	}
}