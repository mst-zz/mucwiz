package com.mucwiz.model;

import java.util.List;

public class Question {
	private String spotifyUri;
	private String type;
	private List<String> alternatives;
	private int correctAnswer;
	
	public Question(String spotifyUri, String type, List<String> alternatives, int correctAnswer) {
		super();
		this.spotifyUri = spotifyUri;
		this.type = type;
		this.alternatives = alternatives;
		this.correctAnswer = correctAnswer;
	}
	public String getSpotifyUri() {
		return spotifyUri;
	}
	public void setSpotifyUri(String spotifyUri) {
		this.spotifyUri = spotifyUri;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
