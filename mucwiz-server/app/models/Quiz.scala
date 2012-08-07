package models

case class Question(
		val spotifyUri: String,
		val answers: List[String],
		val rightAnswer: Int
		)

case class Quiz(
    val admin: String,
		val players: List[String],
		val questions: List[Question]
		
		)
		
object Quiz {
  def makeQuiz(admin:String): Quiz = {
    Quiz(admin, Nil, Nil)
    
  }
  def addQuestion(quiz: Quiz, spotifyUri: String,question: String, answers: List[String], rightAnswer: Int): Quiz = {
    Quiz(quiz.admin, quiz.players, Question(spotifyUri, answers, rightAnswer) :: quiz.questions)
  }
	def test() = {
		Quiz("testadmin",List("a","b"),List(Question("uri",List("fel","ratt"),1)))
	}
}