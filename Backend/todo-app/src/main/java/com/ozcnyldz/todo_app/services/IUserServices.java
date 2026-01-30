package com.ozcnyldz.todo_app.services;

import com.ozcnyldz.todo_app.entities.User;

public interface IUserServices {
	
	public User saveUser(User user);
	
	public User loginUser(String userEmail, String userPassword);
	
	public void deleteCurrentUser();
	
	public User changeUserPassword(Long ID, String oldPassword, String newPassword, String newPasswordConfirm);
	
	public User updateUserEmail(Long ID,String oldUserEmail,String newUserEmail);
	
	public User changePasswordForCurrentUser(
	        String oldPassword,
	        String newPassword,
	        String newPasswordConfirm);
	
	public User updateEmailForCurrentUser(String oldEmail, String newEmail);
	

}
