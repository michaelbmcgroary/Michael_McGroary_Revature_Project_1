package com.revature.dao;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.dto.LoginDTO;
import com.revature.exception.BadPasswordException;
import com.revature.exception.DatabaseException;
import com.revature.exception.NoRecieptException;
import com.revature.exception.NotFinanceManagerException;
import com.revature.exception.PasswordHashException;
import com.revature.exception.UserNotFoundException;
import com.revature.model.Reimbursement;
import com.revature.model.ReimbursementStatus;
import com.revature.model.User;
import com.revature.util.ConnectionUtil;
import com.revature.util.SessionUtility;




public class ReimbursementRepository {

	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(ReimbursementRepository.class);
	private UserRepository userRepository;
	
	public ReimbursementRepository(UserRepository userRepository) throws DatabaseException {
		this.userRepository = userRepository;
	}
	
	
	public Reimbursement newReimbursementRequest(Reimbursement reimb) throws DatabaseException {
		//may replace user with a DTO that doesn't include the id
		try {
			Session session = SessionUtility.getSession();
			Transaction tx = session.beginTransaction();
			
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			reimb.setSubmittedTime(timestamp);
			tx.commit();
			//Time stamp may be set before but this is a more accurate time as it will be recorded at the time it enters the database
			int reimbID = (Integer) session.save(reimb);
			reimb = (Reimbursement) session.createQuery("from Reimbursement r WHERE r.reimbursementID = " + reimbID).getSingleResult();
			return reimb;
		} catch (javax.persistence.NoResultException e) {
			throw new DatabaseException("Reimbursement could not be added. Exception message is: " + e.getMessage());
		} catch (org.hibernate.exception.ConstraintViolationException e) {
			throw new DatabaseException("Reimbursement could not be added because unique value already existed. Constraint is: " + e.getConstraintName());
		}		
	}
	
	
	public Reimbursement getReimbursementByID(LoginDTO login, int reimbID) throws NotFinanceManagerException, DatabaseException {
		if(userRepository.isFinanceManager(login) == false) {
			throw new NotFinanceManagerException();
		}
		Reimbursement retReimb;
		try {
			Session session = SessionUtility.getSession();
			retReimb = session.createQuery("from Reimbursement r WHERE r.reimbursementID = " + reimbID, Reimbursement.class).getSingleResult();
		} catch (javax.persistence.NoResultException e) {
			throw new DatabaseException();
		}
		return retReimb;
	}
	
	
	public List<Reimbursement> getAllRequests(LoginDTO user) throws NotFinanceManagerException, DatabaseException{

		if(userRepository.isFinanceManager(user) == false) {
			throw new NotFinanceManagerException();
		}
		List<Reimbursement> reimbList = null;
		
		try {
			Session session = SessionUtility.getSession();
			reimbList = session.createQuery("from Reimbursement", Reimbursement.class).getResultList();
		} catch (javax.persistence.NoResultException e) {
			throw new DatabaseException();
		} catch (org.hibernate.ObjectNotFoundException e) {
			System.out.println(e.getEntityName());
		}
		
		return reimbList;
	}
	
	
	public List<Reimbursement> getAllRequestsFilterByStatus(LoginDTO user, ReimbursementStatus status) throws NotFinanceManagerException, DatabaseException{
		if(userRepository.isFinanceManager(user) == false) {
			throw new NotFinanceManagerException();
		}
		List<Reimbursement> reimbList;
		
		try {
			Session session = SessionUtility.getSession();
			reimbList = session.createQuery("from Reimbursement r WHERE r.status = " + status.getStatusID(), Reimbursement.class).getResultList();
		} catch (javax.persistence.NoResultException e) {
			throw new DatabaseException();
		}
		
		return reimbList;
	}
	
	
	public List<Reimbursement> getPreviousRequestsByEmployee(LoginDTO login) throws DatabaseException, PasswordHashException, BadPasswordException, UserNotFoundException{
		User user = userRepository.getUserByUsernameAndPassword(login);
		List<Reimbursement> reimbList = new ArrayList<Reimbursement>();
		
		try {
			Session session = SessionUtility.getSession();
			reimbList = session.createQuery("from Reimbursement WHERE author = " + user.getUserID(), Reimbursement.class).getResultList();
		} catch (javax.persistence.NoResultException e) {
			throw new DatabaseException();
		}
		
		return reimbList;
	}
	
	
	public Reimbursement approveReimbursementRequest(LoginDTO login, Reimbursement reimb) throws NotFinanceManagerException, DatabaseException, UserNotFoundException, BadPasswordException {
		if(userRepository.isFinanceManager(login) == false) {
			throw new NotFinanceManagerException();
		}
		
		Session session = SessionUtility.getSession();
		User user = userRepository.getUserByUsernameAndPassword(login);
		//We get the user so that it can be passed in as the resolver
		reimb.setStatus(1);
		reimb.setResolver(user);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		reimb.setResolvedTime(timestamp);
		try {
			Transaction tx = session.beginTransaction();
			session.update(reimb);
			tx.commit();
			Reimbursement retReimb = (Reimbursement) session.createQuery("from Reimbursement r WHERE r.reimbursementID = " + reimb.getReimbursementID()).getSingleResult();
			return retReimb;
		} catch (javax.persistence.NoResultException e) {
			throw new DatabaseException("Reimbursement could not be approved. Exception message is: " + e.getMessage());
		}
	}
	
	
	public Reimbursement denyReimbursementRequest(LoginDTO login, Reimbursement reimb) throws NotFinanceManagerException, DatabaseException, BadPasswordException, UserNotFoundException {
		if(userRepository.isFinanceManager(login) == false) {
			throw new NotFinanceManagerException();
		}
		
		Session session = SessionUtility.getSession();
		User user = userRepository.getUserByUsernameAndPassword(login);
		//We get the user so that it can be passed in as the resolver
		reimb.setStatus(2);
		reimb.setResolver(user);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		reimb.setResolvedTime(timestamp);
		try {
			Transaction tx = session.beginTransaction();
			session.update(reimb);
			tx.commit();
			Reimbursement retReimb = (Reimbursement) session.createQuery("from Reimbursement r WHERE r.reimbursementID = " + reimb.getReimbursementID()).getSingleResult();
			return retReimb;
		} catch (javax.persistence.NoResultException e) {
			throw new DatabaseException("Reimbursement could not be approved. Exception message is: " + e.getMessage());
		}
	}
	
	
	public Blob getRecieptByID(LoginDTO login, int reimbID) throws DatabaseException, NotFinanceManagerException, NoRecieptException {
		try(Connection connection = ConnectionUtil.getConnection()){
			String query = "SELECT reimb_reciept FROM ers_reimbursement WHERE reimb_id = ?";
			PreparedStatement prepStatement = connection.prepareStatement(query);
			prepStatement.setInt(1, reimbID);
			ResultSet results = prepStatement.executeQuery();			
			if (results.next()) {
				Blob blob = results.getBlob("reimb_reciept");
				if(blob.length() == 0) {
					blob = null;
				}
				return blob;
			} else {
				throw new NoRecieptException("The requested ID has no reciept");
			}
		} catch (SQLException e) {
			throw new DatabaseException("No Database Connection");
		} 
	}

}
