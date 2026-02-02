package com.ozcnyldz.todo_app.services.impl;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ozcnyldz.todo_app.config.CustomUserDetails;
import com.ozcnyldz.todo_app.config.JwtUtil;
import com.ozcnyldz.todo_app.dto.LoginRequestDTO;
import com.ozcnyldz.todo_app.dto.LoginResponseDTO;
import com.ozcnyldz.todo_app.dto.UserRequestDTO;
import com.ozcnyldz.todo_app.dto.UserResponseDTO;
import com.ozcnyldz.todo_app.entities.User;
import com.ozcnyldz.todo_app.repository.UserRepository;
import com.ozcnyldz.todo_app.services.IUserServices;

@Service
public class UserServicesImpl implements IUserServices {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final JwtUtil jwtUtil;

	public UserServicesImpl(UserRepository userRepository,
			PasswordEncoder passwordEncoder,
			JwtUtil jwtUtil) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
	}

	// Helper für ID über JWT
	public Long getCurrentUserId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails cud)) {
			throw new IllegalStateException("Kein eingeloggter Benutzer");
		}

		return cud.getUser().getId();
	}

	@Override
	public UserResponseDTO saveUser(UserRequestDTO dto) {

		User user = new User();
		user.setUserEmail(dto.getUserEmail());
		user.setUserName(dto.getUserName());
		user.setUserPassword(
				passwordEncoder.encode(dto.getUserPassword()));

		User savedUser = userRepository.save(user);

		return new UserResponseDTO(
				savedUser.getUserName(),
				savedUser.getUserEmail());
	}

	@Override
	public LoginResponseDTO loginUser(LoginRequestDTO dto) {
		// ... Validation ...
		String email = dto.getUserEmail().trim().toLowerCase();
		String password = dto.getUserPassword();

		User user = userRepository.findByUserEmail(email)
				.orElseThrow(() -> new IllegalArgumentException("Ungültige Email"));

		// Passwort-Check
		if (!passwordEncoder.matches(password, user.getUserPassword())) {
			throw new IllegalArgumentException("Ungültiges Passwort");
		}

		String token = jwtUtil.generateToken(user);

		return new LoginResponseDTO(
				token,
				user.getUserName(),
				user.getUserEmail(),
				"Bearer");
	}

	@Override
	public void deleteCurrentUser() {
		Long id = getCurrentUserId();
		User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User nicht gefunden"));
		user.setActive(false);
		userRepository.save(user);
	}

	@Override
	public User changeUserPassword(Long ID, String oldPassword, String newPassword, String newPasswordConfirm) {
		Optional<User> userOptForChangePassword = userRepository.findById(ID);

		newPassword = newPassword.trim();
		newPasswordConfirm = newPasswordConfirm.trim();

		if (userOptForChangePassword.isEmpty()) {
			throw new IllegalArgumentException("Benutzer wurde nicht gefunden. Bitte Support kontaktieren");
		}

		User user = userOptForChangePassword.get();

		if (!passwordEncoder.matches(oldPassword, user.getUserPassword())) {
			throw new IllegalArgumentException("Ungültige Password");
		}

		if (newPassword.equals(newPasswordConfirm)) {
			String newHashedPassword = passwordEncoder.encode(newPassword);
			user.setUserPassword(newHashedPassword);
		} else {
			throw new IllegalArgumentException("Passwörter stimmen nicht überein");
		}

		return userRepository.save(user);
	}

	@Override
	public User changePasswordForCurrentUser(
			String oldPassword,
			String newPassword,
			String newPasswordConfirm) {

		Long userId = getCurrentUserId();
		return changeUserPassword(userId, oldPassword, newPassword, newPasswordConfirm);
	}

	@Override
	public User updateUserEmail(Long ID, String oldUserEmail, String newUserEmail) {
		newUserEmail = newUserEmail.trim().toLowerCase();
		User user = userRepository.findById(ID)
				.orElseThrow(() -> new IllegalArgumentException("User nicht gefunden"));

		if (!user.getUserEmail().equalsIgnoreCase(oldUserEmail)) {
			throw new IllegalArgumentException("Alte Email stimmt nicht");
		}

		if (userRepository.existsByUserEmail(newUserEmail)) {
			throw new IllegalArgumentException("Email kann nicht geändert werden");
		}
		user.setUserEmail(newUserEmail);

		return userRepository.save(user);
	}

	@Override
	public User updateEmailForCurrentUser(String oldEmail, String newEmail) {
		Long id = getCurrentUserId();
		return updateUserEmail(id, oldEmail, newEmail);
	}
}
