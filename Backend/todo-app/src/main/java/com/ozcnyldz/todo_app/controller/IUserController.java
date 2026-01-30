package com.ozcnyldz.todo_app.controller;


import com.ozcnyldz.todo_app.entities.User;

public interface IUserController {
	
	public void deleteMe();
	
	public User changeUserPassword( String oldPassword, String newPassword, String newPasswordConfirm);
	
	public User updateUserEmail(String oldUserEmail, String newUserEmail);

}
