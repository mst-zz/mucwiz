package models

import play.api.cache.Cache
import play.api.Play.current

class QuizHandler {
	def setQuiz(key: String, quiz: Quiz) = {
		Cache.set(key, quiz, 5*60)
	  
	}

}