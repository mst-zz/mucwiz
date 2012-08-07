package models

case class Question(
		val spotifyUri: String,
		val alternatives: List[String],
		val rightAnswer: Int
		) 

case class Quiz(
		// created | started | finished
		val status: String,
		val players: List[String],
		val questions: List[Question],
		val answers: Map[String,List[(Int, Int)]],
		val updates: Map[String,List[(Int, Int)]]
		)
		
object Quiz {
  def makeQuiz(): Quiz = {
    Quiz("created", Nil, Nil, Map.empty, Map.empty)
    
  }
  def addQuestion(quiz: Quiz, spotifyUri: String, question: String, alternatives: List[String], rightAnswer: Int): Quiz = {
    Quiz(quiz.status,
    	 quiz.players, 
         Question(spotifyUri, alternatives, rightAnswer) :: quiz.questions, 
         quiz.answers, //TODO: remember to add -1 for persons
         quiz.updates)
  }
  
  def joinQuiz(quiz: Quiz, player: String): Quiz = {
    val cleanAnswers = List.range(0,quiz.questions.length).map(x=>(x,-1))
    val playerAnswers = quiz.answers + (player->cleanAnswers)
    Console.print(playerAnswers)
    Quiz(quiz.status, quiz.players ::: List(player), quiz.questions, playerAnswers, quiz.updates )
  }
  
  def emptyUpdates(quiz: Quiz): Quiz = {
    Quiz(quiz.status, quiz.players, quiz.questions, quiz.answers, Map.empty)
  }
  
  def updateQuiz(quiz: Quiz, player: String, questionIndex: Int, answer: Int) = {
    val playerAnswers = quiz.answers.get(player)
    
    playerAnswers match {
      case Some(playerAnswers) => 
      	val updatedAnswers = playerAnswers.updated(questionIndex, (questionIndex, answer))
      	Console.print(updatedAnswers)
      	val updates = quiz.updates.get(player)
      	updates match {
      	  case Some(updates) =>
      	    val playerUpdates = (questionIndex, answer) :: updates
      	    Quiz(quiz.status, quiz.players, quiz.questions, quiz.answers + (player->updatedAnswers), quiz.updates + (player->playerUpdates))
      	  case None => 
      	    Quiz(quiz.status, quiz.players, quiz.questions, quiz.answers + (player->updatedAnswers), quiz.updates + (player->List((questionIndex,answer))) )
      	}
      case None => quiz
      	
    }
    
    
    
  }
	
}