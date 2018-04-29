package com.example.ciacho.aishengdemo.bean;

public class User {
	private String userid;
	private String username;
	private int sex;
	private int age;
	
	public User(String userid, String username, int sex, int age) {
		super();
		this.userid = userid;
		this.username = username;
		this.sex = sex;
		this.age = age;
	}
	
	public User() {
		super();
	}

	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	
	
}
