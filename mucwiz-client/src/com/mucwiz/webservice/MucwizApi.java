package com.mucwiz.webservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.schema.JsonSchema;

import com.mucwiz.model.Question;
import com.mucwiz.model.Quiz;
import com.mucwiz.webservice.RestClient.RequestMethod;

public class MucwizApi {
	
	public static final String API_URL = "http://192.168.2.105:9000"; //"http://172.21.113.190:9000";
	
	public static void createQuiz(Quiz quiz) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		
		String jsonData = mapper.writeValueAsString(quiz);
		RestClient client = new RestClient(API_URL + "/mucwiz/create_quiz");
		client.setContent(jsonData);
		client.Execute(RequestMethod.POST);
	}
	
	public static Quiz getQuiz(String key) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, String> stuff = new HashMap<String, String>();
		stuff.put("key", key);
		String content = mapper.writeValueAsString(stuff);
		
		RestClient client = new RestClient(API_URL + "/mucwiz/get_quiz");
		client.setContent(content);
		client.Execute(RequestMethod.POST);
		String quizString = client.getResponse();
		System.out.println("getting quiz: (key: "+ key +") " + quizString);
		
		Map data = mapper.readValue(quizString, Map.class);
		
		Quiz q = mapper.readValue(mapper.writeValueAsString(data.get("quiz")), Quiz.class);
		
		
		return q;
	}
	
	public static void joinQuiz(String key, String player) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, String> stuff = new HashMap<String, String>();
		stuff.put("key", key);
		stuff.put("player", player);
		String content = mapper.writeValueAsString(stuff);
		RestClient client = new RestClient(API_URL + "/mucwiz/join_quiz");
		client.setContent(content);
		client.Execute(RequestMethod.POST);		
	}
	
	public static void startQuiz(String key) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, String> stuff = new HashMap<String, String>();
		stuff.put("key", key);
		String content = mapper.writeValueAsString(stuff);
		RestClient client = new RestClient(API_URL + "/mucwiz/start_quiz");
		client.setContent(content);
		client.Execute(RequestMethod.POST);
	}
	
	public static void sendAnswer(String key, String player, Integer qIndex, Integer answer) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, Object> stuff = new HashMap<String, Object>();
		stuff.put("key", key);
		stuff.put("player", player);
		stuff.put("question_index", qIndex);
		stuff.put("answer", answer);
		String content = mapper.writeValueAsString(stuff);
		System.out.println(content);
		RestClient client = new RestClient(API_URL + "/mucwiz/client_answer");
		client.setContent(content);
		client.Execute(RequestMethod.POST);
	}
	
	public static void testCreateQuiz() {
		try {
        	Quiz quiz = Quiz.getInstance();
        	quiz.setKey("testquiz");
        	
        	List<Question> questions = new ArrayList<Question>();
        	
        	List<String> alternatives = new ArrayList<String>();
        	alternatives.add("bry");
        	alternatives.add("asd");
        	Question q = new Question("sptofiy:://asdasdasd", "artist", "test", alternatives, 0);
        	
        	q.setCorrectAnswer(0);
        	questions.add(q);
        	quiz.setQuestions(questions);
			MucwizApi.createQuiz(Quiz.getInstance());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testGetQuiz() {
		try {
			System.out.println(getQuiz("testquiz").toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testJoinQuiz() {
		try {
		joinQuiz("testquiz","Abbedabbe");
		} catch ( Exception e) {
			e.printStackTrace();
		}
	}
}
