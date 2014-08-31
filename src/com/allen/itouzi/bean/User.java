package com.allen.itouzi.bean;

import java.io.Serializable;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5649789993950701305L;
	String user_id;
	String username;
	String token;

	public User() {
		// TODO Auto-generated constructor stub
	}

	public User(String user_id, String username, String token) {
		super();
		this.user_id = user_id;
		this.username = username;
		this.token = token;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "User [user_id=" + user_id + ", username=" + username
				+ ", token=" + token + "]";
	}

}
