package com.ozcnyldz.todo_app.services.impl;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ozcnyldz.todo_app.entities.User;
import com.ozcnyldz.todo_app.repository.UserRepository;
import com.ozcnyldz.todo_app.services.IUserServices;

@Service
public class UserServicesImpl implements IUserServices{
	
	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;

	public UserServicesImpl(UserRepository userRepository,
	                        PasswordEncoder passwordEncoder) {
	    this.userRepository = userRepository;
	    this.passwordEncoder = passwordEncoder;
	}

	@Override
	public User saveUser(User user) {

	    if(user.getUserName() == null || user.getUserName().isBlank()) {
	        throw new IllegalArgumentException("UserName darf nicht leer sein");
	    }
	    if(user.getUserEmail() == null || user.getUserEmail().isBlank()) {
	        throw new IllegalArgumentException("Email darf nicht leer sein");
	    }
	    if(user.getUserPassword() == null || user.getUserPassword().isBlank()) {
	        throw new IllegalArgumentException("Passwort darf nicht leer sein");
	    }

	    if(userRepository.existsByUserEmail(user.getUserEmail())) {
	        throw new IllegalArgumentException("Email existiert bereits");
	    }

	    //user.setUserPassword(hashPassword(user.getUserPassword()));
	    String hashedPassword = passwordEncoder.encode(user.getUserPassword());
	    user.setUserPassword(hashedPassword);
	    
	    String trimEmail=user.getUserEmail().trim().toLowerCase();
	    user.setUserEmail(trimEmail);
	    
	    return userRepository.save(user);
	    
	}
	
	
	@Override
	public User loginUser(String userEmail, String userPassword) {
		Optional<User> userOpt = userRepository.findByUserEmail(userEmail);
		if(userOpt.isEmpty()) {
			throw new IllegalArgumentException("Ungültige Zugangsdaten");
		}
		
		User user = userOpt.get();


		if (!passwordEncoder.matches(userPassword, user.getUserPassword())) {
		throw new IllegalArgumentException("Ungültige Zugangsdaten");
		}


		if (!user.isActive()) {
		throw new IllegalArgumentException("Benutzer ist deaktiviert");
		}


		return user;
	}
	


	@Override
	public void deactivateUser(Long ID) {
		User user = userRepository.findById(ID).orElseThrow(() -> new IllegalArgumentException("User nicht gefunden"));
		user.setActive(false);
		userRepository.save(user);		
	}

	@Override
	public User changeUserPassword(Long ID, String oldPassword, String newPassword, String newPasswordConfirm) {
		Optional<User> userOptForChangePassword=userRepository.findById(ID);
		
		newPassword = newPassword.trim();
		newPasswordConfirm = newPasswordConfirm.trim();
		
		if(userOptForChangePassword.isEmpty()) {
			throw new IllegalArgumentException("Benutzer wurde nicht gefunden. Bitte Support kontaktieren");
		}
		
		User user = userOptForChangePassword.get();

		if (!passwordEncoder.matches(oldPassword, user.getUserPassword())) {
		throw new IllegalArgumentException("Ungültige Password");
		}
		

		if(newPassword.equals(newPasswordConfirm)) {
		    String newHashedPassword = passwordEncoder.encode(newPassword);
		    user.setUserPassword(newHashedPassword);
		} else {
		    throw new IllegalArgumentException("Passwörter stimmen nicht überein");
		}

		return userRepository.save(user);
	}

	@Override
	public User updateUserEmail(Long ID,String oldUserEmail,String newUserEmail) {
		newUserEmail=newUserEmail.trim().toLowerCase();
		User user = userRepository.findById(ID)
		        .orElseThrow(() -> new IllegalArgumentException("User nicht gefunden"));
		
		if (!user.getUserEmail().equalsIgnoreCase(oldUserEmail)) {
		    throw new IllegalArgumentException("Alte Email stimmt nicht");
		}
		
	    if(userRepository.existsByUserEmail(newUserEmail)) {
	        throw new IllegalArgumentException("Email kann nicht geändert werden");
	    }
		user.setUserEmail(newUserEmail);
		
		return userRepository.save(user);
	}
}
