package com.mucwiz.model;

import Spotify.Session;

public class User {
	
	private String username;
	private String password;
	private Session session;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setSession(Session session){
		this.session = session;
	}
	
	public Session getSession(){
		return session;
	}

	private static User instance;
	
	public static User getInstance(){
		if (instance == null){
			instance = new User();
		}
		return instance;
	}

}
