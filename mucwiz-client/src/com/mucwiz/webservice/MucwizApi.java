package com.mucwiz.webservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.schema.JsonSchema;

import com.mucwiz.model.Question;
import com.mucwiz.model.Quiz;
import com.mucwiz.webservice.RestClient.RequestMethod;

public class MucwizApi {
	
	public static final String API_URL = "http://ec2-176-34-85-171.eu-west-1.compute.amazonaws.com"; //"http://172.21.113.190:9000";
	
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
		Quiz q = mapper.readValue(quizString, Quiz.class);
		
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
	
	public static void testCreateQuiz() {
		try {
        	Quiz quiz = Quiz.getInstance();
        	quiz.setKey("testquiz");
        	
        	List<Question> questions = new ArrayList<Question>();
        	Question q = new Question();
        	List<String> alternatives = new ArrayList<String>();
        	alternatives.add("bry");
        	alternatives.add("asd");
        	q.setAlternatives(alternatives);
        	q.setCorrectAnswer(0);
        	questions.add(q);
        	q.setType("artist");
        	q.setSpotifyUri("sptofiy:://asdasdasd");
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
