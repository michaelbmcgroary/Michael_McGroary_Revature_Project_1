package com.revature.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.dto.MessageDTO;
import com.revature.exception.*;

import io.javalin.Javalin;
import io.javalin.http.ExceptionHandler;

public class ProjExceptionHandler implements Controller {

	private Logger logger = LoggerFactory.getLogger(ProjExceptionHandler.class);
	
	private ExceptionHandler<DatabaseException> databaseExceptionHandler = (e, ctx) -> {
		logger.warn("An issue occured with the Database.\nException message is: " + e.getMessage());
		ctx.status(500);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	private ExceptionHandler<PasswordHashException> passwordHashExceptionHandler = (e, ctx) -> {
		logger.warn("An issue occured while Hashing a password.\nException message is: " + e.getMessage());
		ctx.status(500);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	private ExceptionHandler<BadParameterException> badParameterExceptionHandler = (e, ctx) -> {
		logger.warn("An unexpected parameter was given.\nException message is: " + e.getMessage());
		ctx.status(400);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	private ExceptionHandler<EmptyParameterException> emptyParameterExceptionHandler = (e, ctx) -> {
		logger.warn("A parameter was left empty when it shouldn't have been.\nException message is: " + e.getMessage());
		ctx.status(400);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	private ExceptionHandler<GetReimbursementException> getReimbursementExceptionHandler = (e, ctx) -> {
		logger.warn("An issue occured when Retrieving Reimbursements.\nException message is: " + e.getMessage());
		ctx.status(400);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	private ExceptionHandler<NotFinanceManagerException> notFinanceManagerExceptionHandler = (e, ctx) -> {
		logger.warn("A User who is not a finance manager tried to access information specific to finance managers.\nException message is: " + e.getMessage());
		ctx.status(400);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	private ExceptionHandler<UserNotFoundException> userNotFoundExceptionHandler = (e, ctx) -> {
		logger.warn("A user was not found in the database when looking for one.\nException message is: " + e.getMessage());
		ctx.status(400);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	private ExceptionHandler<ReimbursementNotFoundException> reimbursementNotFoundExceptionHandler = (e, ctx) -> {
		logger.warn("A reimbursement was not found in the database when looking for one.\nException message is: " + e.getMessage());
		ctx.status(400);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	private ExceptionHandler<ReimbursementAddException> reimbursementAddExceptionHandler = (e, ctx) -> {
		logger.warn("An issue occured when adding a Reimbursement to the database.\nException message is: " + e.getMessage());
		ctx.status(400);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	private ExceptionHandler<LoginException> loginExceptionHandler = (e, ctx) -> {
		logger.warn("An issue occured when trying to login.\nException message is: " + e.getMessage());
		ctx.status(400);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	private ExceptionHandler<ReimbursementApproveException> reimbursementApproveExceptionHandler = (e, ctx) -> {
		logger.warn("An issue occured when trying to approve a reimbursement request.\nException message is: " + e.getMessage());
		ctx.status(400);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	private ExceptionHandler<ReimbursementDenyException> reimbursementDenyExceptionHandler = (e, ctx) -> {
		logger.warn("An issue occured when trying to deny a reimbursement request.\nException message is: " + e.getMessage());
		ctx.status(400);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	private ExceptionHandler<UserAlreadyExistsException> userAlreadyExistsExceptionHandler = (e, ctx) -> {
		logger.warn("A user tried to make a user with already taken credentials.\nException message is: " + e.getMessage());
		ctx.status(400);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	private ExceptionHandler<NotLoggedInException> notLoggedInExceptionHandler = (e, ctx) -> {
		logger.warn("Something happened where a user should have been logged in, but they weren't.\nException message is: " + e.getMessage());
		ctx.status(400);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	private ExceptionHandler<BadPasswordException> badPasswordExceptionHandler = (e, ctx) -> {
		logger.warn("The given password did not match what was expected.\nException message is: " + e.getMessage());
		ctx.status(400);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	private ExceptionHandler<UserAddException> userAddExceptionHandler = (e, ctx) -> {
		logger.warn("An error occured while trying to add a user.\nException message is: " + e.getMessage());
		ctx.status(500);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	private ExceptionHandler<NoRecieptException> noRecieptExceptionHandler = (e, ctx) -> {
		logger.warn("An error occured while trying to get a Reimbursement Reciept.\nException message is: " + e.getMessage());
		ctx.status(400);
		ctx.json(new MessageDTO(e.getMessage()));
	};
	
	
	public void mapEndpoints(Javalin app) {
		app.exception(DatabaseException.class, databaseExceptionHandler);
		app.exception(PasswordHashException.class, passwordHashExceptionHandler);
		app.exception(BadParameterException.class, badParameterExceptionHandler);
		app.exception(LoginException.class, loginExceptionHandler);
		app.exception(EmptyParameterException.class, emptyParameterExceptionHandler);
		app.exception(GetReimbursementException.class, getReimbursementExceptionHandler);
		app.exception(NotFinanceManagerException.class, notFinanceManagerExceptionHandler);
		app.exception(UserNotFoundException.class, userNotFoundExceptionHandler);
		app.exception(ReimbursementAddException.class, reimbursementAddExceptionHandler);
		app.exception(ReimbursementApproveException.class, reimbursementApproveExceptionHandler);
		app.exception(ReimbursementDenyException.class, reimbursementDenyExceptionHandler);
		app.exception(UserAlreadyExistsException.class, userAlreadyExistsExceptionHandler);
		app.exception(NotLoggedInException.class, notLoggedInExceptionHandler);
		app.exception(ReimbursementNotFoundException.class, reimbursementNotFoundExceptionHandler);
		app.exception(BadPasswordException.class, badPasswordExceptionHandler);
		app.exception(UserAddException.class, userAddExceptionHandler);
		app.exception(NoRecieptException.class, noRecieptExceptionHandler);
	}
}
