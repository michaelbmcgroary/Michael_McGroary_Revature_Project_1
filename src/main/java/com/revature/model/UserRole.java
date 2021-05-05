package com.revature.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="ers_user_roles")
public class UserRole {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ers_user_role_id")
	private int roleID;
	
	@Column(name = "user_role", length=10)
	private String role;
	
	public UserRole() {
		super();
	}

	public UserRole(int roleID) {
		super();
		this.roleID = roleID;
		switch(roleID) {
			case 1: role = "Manager";
				break;
			case 2: role = "Employee";
				break;
			default: role = "Employee";
				break;
		}
	}

	public int getRoleID() {
		return roleID;
	}

	public void setRoleID(int roleID) {
		this.roleID = roleID;
		setRole(this.roleID);
	}

	public String getRole() {
		return role;
	}

	public void setRole(int roleID) {
		switch(roleID) {
			case 1: role = "Manager";
				break;
			case 2: role = "Employee";
				break;
			default: role = "Employee";
				break;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + roleID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserRole other = (UserRole) obj;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		if (roleID != other.roleID)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserRole [roleID=" + roleID + ", role=" + role + "]";
	}
	
	

}
