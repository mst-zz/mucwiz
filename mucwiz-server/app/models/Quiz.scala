package models

case class Question(
		val spotifyUri: String,
		val answers: List[String],
		val rightAnswer: Int
		)

case class Quiz(
		val key: String,
		val players: List[String],
		val questions: List[Question]

		)

		object Quiz { 
	def test() = {
		Quiz("testgame",List("a","b"),List(Question("uri",List("fel","ratt"),1)))
	}
}