package com.revature.service;

import java.sql.Blob;
import java.util.List;

import com.revature.dao.ReimbursementRepository;
import com.revature.dao.UserRepository;
import com.revature.dto.LoginDTO;
import com.revature.dto.PostReimbursementDTO;
import com.revature.exception.BadParameterException;
import com.revature.exception.BadPasswordException;
import com.revature.exception.DatabaseException;
import com.revature.exception.EmptyParameterException;
import com.revature.exception.NoRecieptException;
import com.revature.exception.NotFinanceManagerException;
import com.revature.exception.PasswordHashException;
import com.revature.exception.ReimbursementAddException;
import com.revature.exception.ReimbursementApproveException;
import com.revature.exception.ReimbursementDenyException;
import com.revature.exception.ReimbursementNotFoundException;
import com.revature.exception.UserNotFoundException;
import com.revature.model.Reimbursement;
import com.revature.model.ReimbursementStatus;

public class ReimbursementService {

	private ReimbursementRepository reimbRepo;
	
	public ReimbursementService(UserRepository userRepo) throws DatabaseException {
		reimbRepo = new ReimbursementRepository(userRepo);
	}
	
	//Normally, we don't need this and would use the other one, but for testing, we need this to "inject" the mock object into this service
	public ReimbursementService(UserRepository userRepo, ReimbursementRepository reimbursementRepository) {
		this.reimbRepo = reimbursementRepository;
	}
	
	public Reimbursement newReimbursementRequest(PostReimbursementDTO reimbDTO) throws ReimbursementAddException, DatabaseException, BadParameterException {
		try {
			Reimbursement reimb = new Reimbursement(reimbDTO);
			reimb.setAmount(Double.parseDouble(reimbDTO.getAmount()));
			reimb = reimbRepo.newReimbursementRequest(reimb);
			return reimb;
		} catch (DatabaseException e) {
			throw new ReimbursementAddException(e.getMessage());
		} catch (NumberFormatException e) {
			throw new BadParameterException("Amount must be a double. User provided " + reimbDTO.getAmount());
		}
	}
	
	public Reimbursement getReimbursementByID(LoginDTO login, String id) throws ReimbursementNotFoundException, BadParameterException, EmptyParameterException {
		Reimbursement reimb = null;
		try {
			if(login.getUsername().isBlank()) {
				throw new EmptyParameterException("The username of the logged in user was not found");
			}
			if(login.getPassword().isBlank()) {
				throw new EmptyParameterException("The password of the logged in user was not found");
			}
			if(id.isBlank()) {
				throw new EmptyParameterException("The Reimbursement ID was left blank");
			}
			int reimbID = Integer.parseInt(id);
			reimb = reimbRepo.getReimbursementByID(login, reimbID);
			return reimb;
		} catch (DatabaseException e) {
			throw new ReimbursementNotFoundException("The reimbursement with id " + id + " could not be found");
		} catch (NotFinanceManagerException e) {
			throw new ReimbursementNotFoundException("The login credentials are not for a finance manager");
		} catch (NumberFormatException e) {
			throw new BadParameterException("The Reimbursement ID provided must be of type int");
		}
	}
	
	public List<Reimbursement> getAllReimbursements(LoginDTO login) throws ReimbursementNotFoundException, EmptyParameterException{
		List<Reimbursement> reimbList = null;
		try {
			if(login.getUsername().isBlank()) {
				throw new EmptyParameterException("The username of the logged in user was not found");
			}
			if(login.getPassword().isBlank()) {
				throw new EmptyParameterException("The password of the logged in user was not found");
			}
			reimbList = reimbRepo.getAllRequests(login);
			return reimbList;
		} catch (DatabaseException e) {
			throw new ReimbursementNotFoundException("No Reimbursements could be found");
		} catch (NotFinanceManagerException e) {
			throw new ReimbursementNotFoundException("The login credentials are not for a finance manager");
		}
	}
	
	public List<Reimbursement> getReimbursementsByStatus(LoginDTO login, String status) throws EmptyParameterException, ReimbursementNotFoundException{
		List<Reimbursement> reimbList = null;
		try {
			if(login.getUsername().isBlank()) {
				throw new EmptyParameterException("The username of the logged in user was not found");
			}
			if(login.getPassword().isBlank()) {
				throw new EmptyParameterException("The password of the logged in user was not found");
			}
			if(status.isBlank()) {
				throw new EmptyParameterException("The status to filter by was left blank");
			}
			ReimbursementStatus stat = new ReimbursementStatus();
			stat.setStatus(status);
			reimbList = reimbRepo.getAllRequestsFilterByStatus(login, stat);
			return reimbList;
		} catch (DatabaseException e) {
			throw new ReimbursementNotFoundException("No Reimbursements could be found");
		} catch (NotFinanceManagerException e) {
			throw new ReimbursementNotFoundException("The login credentials are not for a finance manager");
		}
	}
	
	public List<Reimbursement> getReimbursementsByUser(LoginDTO login) throws ReimbursementNotFoundException, EmptyParameterException, PasswordHashException, BadPasswordException, UserNotFoundException{
		List<Reimbursement> reimbList = null;
		try {
			if(login.getUsername().isBlank()) {
				throw new EmptyParameterException("The username of the logged in user was not found");
			}
			if(login.getPassword().isBlank()) {
				throw new EmptyParameterException("The password of the logged in user was not found");
			}
			reimbList = reimbRepo.getPreviousRequestsByEmployee(login);
			return reimbList;
		} catch (DatabaseException e) {
			throw new ReimbursementNotFoundException("No Reimbursements could be found");
		}
	}
	
	public Reimbursement approveReimbursement(LoginDTO login, Reimbursement reimb) throws ReimbursementApproveException, EmptyParameterException, BadPasswordException, UserNotFoundException {
		try {
			if(login.getUsername().isBlank()) {
				throw new EmptyParameterException("The username of the logged in user was not found");
			}
			if(login.getPassword().isBlank()) {
				throw new EmptyParameterException("The password of the logged in user was not found");
			}
			reimb = reimbRepo.approveReimbursementRequest(login, reimb);
			return reimb;
		} catch (DatabaseException e) {
			throw new ReimbursementApproveException(e.getMessage());
		} catch (NotFinanceManagerException e) {
			throw new ReimbursementApproveException("User provided is not a finance manager, and thus could not approve the request");
		}
	}
	
	public Reimbursement denyReimbursement(LoginDTO login, Reimbursement reimb) throws EmptyParameterException, ReimbursementDenyException, BadPasswordException, UserNotFoundException {
		try {
			if(login.getUsername().isBlank()) {
				throw new EmptyParameterException("The username of the logged in user was not found");
			}
			if(login.getPassword().isBlank()) {
				throw new EmptyParameterException("The password of the logged in user was not found");
			}
			reimb = reimbRepo.denyReimbursementRequest(login, reimb);
			return reimb;
		} catch (DatabaseException e) {
			throw new ReimbursementDenyException(e.getMessage());
		} catch (NotFinanceManagerException e) {
			throw new ReimbursementDenyException("User provided is not a finance manager, and thus could not deny the request");
		}
	}
	
	public Blob getRecieptByID(LoginDTO login, String id) throws EmptyParameterException, BadParameterException, DatabaseException, NotFinanceManagerException, NoRecieptException {
		try {
			if(login.getUsername().isBlank()) {
				throw new EmptyParameterException("The username of the logged in user was not found");
			}
			if(login.getPassword().isBlank()) {
				throw new EmptyParameterException("The password of the logged in user was not found");
			}
			if(id.isBlank()) {
				throw new EmptyParameterException("The Reimbursement ID was left blank when getting the reciept");
			}
			int reimbID;
			reimbID = Integer.parseInt(id);
			Blob blob = reimbRepo.getRecieptByID(login, reimbID);
			return blob;
		} catch (DatabaseException e) {
			throw new DatabaseException(e.getMessage());
		} catch (NotFinanceManagerException e) {
			throw new NotFinanceManagerException(e.getMessage());
		} catch (NoRecieptException e) {
			return null;
		}catch (NumberFormatException e) {
			throw new BadParameterException("The Reimbursement ID provided must be of type int");
		}
	}
}
