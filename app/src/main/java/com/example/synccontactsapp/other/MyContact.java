package com.example.synccontactsapp.other;

public class MyContact {
	
	public String name;
	public String email;
	public String lastName;
	public String phone;
	public String id;
	
	public MyContact(String id,String name,String phone,String email) {
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.email = email;
	}
}
