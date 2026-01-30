package com.ozcnyldz.todo_app.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ozcnyldz.todo_app.controller.IAuthController;
import com.ozcnyldz.todo_app.entities.User;
import com.ozcnyldz.todo_app.services.IUserServices;

@RestController
@RequestMapping("/auth")
public class AuthController implements IAuthController{
	
	@Autowired
	private IUserServices userServices;

	@PostMapping(path="/save")
	@Override
	public User saveUser(@RequestBody User user) {
		System.out.println("Empfangener User: " + user);
		return userServices.saveUser(user);
	}

	@PostMapping(path="/login")
	@Override
	public User loginUser(@RequestBody String userEmail, String userPassword) {
		return userServices.loginUser(userEmail, userPassword);

	}
	

}
