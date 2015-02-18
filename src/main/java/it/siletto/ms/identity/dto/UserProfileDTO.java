package it.siletto.ms.identity.dto;

import java.util.Set;

public class UserProfileDTO {

	private String username;
	private Set<String> roles;
	private Set<String> permissions;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Set<String> getRoles() {
		return roles;
	}
	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
	public Set<String> getPermissions() {
		return permissions;
	}
	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}
	
}
