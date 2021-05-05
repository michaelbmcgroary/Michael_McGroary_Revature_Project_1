package com.revature.dao;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.exception.DatabaseException;
import com.revature.util.ConnectionUtil;

public class CreateAndAutoPopulate {

	private Logger logger = LoggerFactory.getLogger(CreateAndAutoPopulate.class);
	
	public CreateAndAutoPopulate() throws DatabaseException {
		super();
	}
	
	public void createTables() throws DatabaseException {
		try(Connection connection = ConnectionUtil.getConnection()){
			ScriptRunner sr = new ScriptRunner(connection);
			Reader reader = new BufferedReader(new FileReader(System.getenv("autoCreateTables")));
			sr.runScript(reader);
			logger.info("Created the ERS_USERS and ERS_REIMBURSEMENT Tables in the Database as well as the three smaller tables they rely on");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException("An error occured with the SQL syntax");
		} catch (FileNotFoundException e) {
			throw new DatabaseException("System environment variable \"autoCreateTabkes\" must point to script file");
		}
	}
	
	public void autoPopulateUsersWithData() throws DatabaseException {
		try(Connection connection = ConnectionUtil.getConnection()){
			ScriptRunner sr = new ScriptRunner(connection);
			Reader reader = new BufferedReader(new FileReader(System.getenv("autoPopulateUserData")));
			sr.runScript(reader);
			logger.info("Automatically populated the data in the ERS_USERS table with dummy data");
		} catch (SQLException e) {
			throw new DatabaseException("An error occured with the SQL syntax");
		} catch (FileNotFoundException e) {
			throw new DatabaseException("System environment variable \"autoPopulateUserData\" must point to script file");
		}
	}
	
	public void autoPopulateReimbursementsWithData() throws DatabaseException {
		try(Connection connection = ConnectionUtil.getConnection()){
			ScriptRunner sr = new ScriptRunner(connection);
			Reader reader = new BufferedReader(new FileReader(System.getenv("autoPopulateReimbursementData")));
			sr.runScript(reader);
			logger.info("Automatically populated the data in the ERS_REIMBURSEMENT table with dummy data");
		} catch (SQLException e) {
			throw new DatabaseException("An error occured with the SQL syntax");
		} catch (FileNotFoundException e) {
			throw new DatabaseException("System environment variable \"autoPopulateReimbursementData\" must point to script file");
		}
	}
	
}
