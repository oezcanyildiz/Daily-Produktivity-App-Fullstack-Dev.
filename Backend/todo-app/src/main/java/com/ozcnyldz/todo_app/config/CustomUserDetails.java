package com.ozcnyldz.todo_app.config;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;

import com.ozcnyldz.todo_app.entities.User;

public class CustomUserDetails implements UserDetails {

	private final User user;


	public CustomUserDetails(User user) {
	this.user = user;
	}

	

	// LOGIN-IDENTITÄT (Email statt Username)
	@Override
	public String getUsername() {
	return user.getUserEmail();
	}


	// PASSWORT (HASH aus DB)
	@Override
	public String getPassword() {
	return user.getUserPassword();
	}


	// ROLLEN / RECHTE
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	return Collections.emptyList();
	}


	// ACCOUNT-STATUS
	@Override
	public boolean isAccountNonExpired() {
	return true;
	}


	@Override
	public boolean isAccountNonLocked() {
	return true;
	}


	@Override
	public boolean isCredentialsNonExpired() {
	return true;
	}


	// AKTIV / DEAKTIVIERT
	@Override
	public boolean isEnabled() {
	return user.isActive();
	}


	// BONUS: Zugriff auf echte Entity
	public User getUser() {
	return user;
	}
}