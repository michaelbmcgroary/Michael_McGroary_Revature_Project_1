package com.revature.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.dto.LoginDTO;
import com.revature.exception.BadPasswordException;
import com.revature.exception.DatabaseException;
import com.revature.exception.UserNotFoundException;
import com.revature.model.User;
import com.revature.model.UserRole;
import com.revature.util.ConnectionUtil;
import com.revature.util.SessionUtility;

public class UserRepository {

	private Logger logger = LoggerFactory.getLogger(UserRepository.class);
	
	//add in fake data for now and replace with SQL later
	List<User> users = new ArrayList<User>();
	
	
	
	public UserRepository() throws DatabaseException {
		
	}
	
	
	public User getUserByUsernameAndPassword(LoginDTO loginDTO) throws UserNotFoundException, BadPasswordException {
		//if user is not found, return a user with an ID of -1 and null values for the rest
		//if the password is not correct, return a user with an ID of -2 and null values for the rest
		
		try {
			Session session = SessionUtility.getSession();
			@SuppressWarnings("rawtypes")
			Query query = session.createQuery("from User u WHERE u.username = :un AND u.password = :pw");
			query.setParameter("un", loginDTO.getUsername());
			query.setParameter("pw", loginDTO.getPassword());
			User retUser = (User) query.getSingleResult();
			return retUser;
			
		} catch (javax.persistence.NoResultException e) {
			try {
				Session session = SessionUtility.getSession();
				@SuppressWarnings("rawtypes")
				Query query = session.createQuery("from User u WHERE u.username = :un");
				query.setParameter("un", loginDTO.getUsername());
				throw new BadPasswordException("Password does not match username");
			} catch (javax.persistence.NoResultException e1) {
				throw new UserNotFoundException("User could not be found.");
			} 
		} 
		
	}
	
	
	public User addUser(User user) throws DatabaseException {
		//may replace user with a DTO that doesn't include the id
		try {
			Session session = SessionUtility.getSession();
			Transaction tx = session.beginTransaction();
			tx.commit();
			user.setUserRole(new UserRole(2));
			int userID = (Integer) session.save(user);			
			User retUser = (User) session.createQuery("from User u WHERE u.userID = " + userID).getSingleResult();
			return retUser;
		} catch (javax.persistence.NoResultException e) {
			throw new DatabaseException("User could not be added. Exception message is: " + e.getMessage());
		} catch (org.hibernate.exception.ConstraintViolationException e) {
			throw new DatabaseException("User could not be added because unique value already existed. Constraint is: " + e.getConstraintName());
			//may add in some checking to see what constraint it is and send back a specific Exception to display to the user
			//like "username/email already exists"
		}	
	}
	
	public User promoteEmployeeToFinanceManager(LoginDTO login, int UserToPromoteID) {
		return null;
	}
	
	
	public boolean isFinanceManager(LoginDTO loginDTO) throws DatabaseException {
		try {
			Session session = SessionUtility.getSession();
			@SuppressWarnings("rawtypes")
			Query query = session.createQuery("select userRole from User u WHERE u.username = :un AND u.password = :pw");
			query.setParameter("un", loginDTO.getUsername());
			query.setParameter("pw", loginDTO.getPassword());
			UserRole role = (UserRole) query.getSingleResult();
			if(role.getRoleID() == 1) {
				return true;
			} else {
				return false;
			}
		} catch (javax.persistence.NoResultException e) {
			throw new DatabaseException("When checking if user is a finance manager, user could not be found.");
		} 
		
	}


	public List<User> getAllUsers() {
		Session session = SessionUtility.getSession();
		List<User> userList = session.createQuery("from User", User.class).getResultList();
		return userList;
	}
	
}
