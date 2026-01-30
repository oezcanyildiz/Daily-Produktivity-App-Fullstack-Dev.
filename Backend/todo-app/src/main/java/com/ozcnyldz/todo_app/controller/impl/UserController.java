package com.ozcnyldz.todo_app.controller.impl;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ozcnyldz.todo_app.controller.IUserController;
import com.ozcnyldz.todo_app.entities.User;
import com.ozcnyldz.todo_app.services.IUserServices;

@RestController
@RequestMapping(path="/useropt")
public class UserController implements IUserController{
	
	private IUserServices userServices;
	
	public UserController(IUserServices userServices) {
		this.userServices = userServices;
		}

	@DeleteMapping("/delete/me")
	public void deleteMe() {
	    userServices.deleteCurrentUser();
	}

	@PutMapping("/password")
	@Override
	public User changeUserPassword(
	@RequestParam String oldPassword,
	@RequestParam String newPassword,
	@RequestParam String newPasswordConfirm) {


	return userServices.changePasswordForCurrentUser(oldPassword, newPassword, newPasswordConfirm);
	}

	@Override
	@PutMapping("/email/{oldEmail}/{newEmail}")
	public User updateUserEmail(
	    @PathVariable String oldEmail,
	    @PathVariable String newEmail
	) {
	    return userServices.updateEmailForCurrentUser(oldEmail, newEmail);
	}

}
