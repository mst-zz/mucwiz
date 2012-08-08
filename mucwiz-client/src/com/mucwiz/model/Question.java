package com.mucwiz.model;

import java.util.List;

public class Question {
	private String spotifyUri;
	private String qType;
	private List<String> alternatives;
	private int correctAnswer;
	
	public Question(String spotifyUri, String type, List<String> alternatives, int correctAnswer) {
		super();
		this.spotifyUri = spotifyUri;
		this.qType = type;
		this.alternatives = alternatives;
		this.correctAnswer = correctAnswer;
	}
	
	public Question() {
		
	}
	
	public String getSpotifyUri() {
		return spotifyUri;
	}
	public void setSpotifyUri(String spotifyUri) {
		this.spotifyUri = spotifyUri;
	}
	public String getQType() {
		return qType;
	}
	public void setQType(String type) {
		this.qType = type;
	}
	public List<String> getAlternatives() {
		return alternatives;
	}
	public void setAlternatives(List<String> alternatives) {
		this.alternatives = alternatives;
	}
	public int getCorrectAnswer() {
		return correctAnswer;
	}
	public void setCorrectAnswer(int correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
	
}
