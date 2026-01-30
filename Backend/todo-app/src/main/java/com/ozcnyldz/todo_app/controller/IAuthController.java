package com.ozcnyldz.todo_app.controller;

import com.ozcnyldz.todo_app.entities.User;

public interface IAuthController {
	
	public User saveUser(User user);
	
	public User loginUser(String userEmail, String userPassword);

}
