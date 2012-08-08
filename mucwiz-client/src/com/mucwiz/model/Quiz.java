package com.mucwiz.model;

import java.util.List;

public class Quiz {
	private String status;
	private String key;
	private List<Question> questions;
	private List<String> players;
	
	private static Quiz instance;
	
	public static Quiz getInstance(){
		if (instance == null)
			instance = new Quiz();
		return instance;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public List<String> getPlayers() {
		return players;
	}

	public void setPlayers(List<String> players) {
		this.players = players;
	}

}