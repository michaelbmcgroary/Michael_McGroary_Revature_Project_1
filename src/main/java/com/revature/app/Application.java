package com.revature.app;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.controller.Controller;
import com.revature.controller.LoginController;
import com.revature.controller.ProjExceptionHandler;
import com.revature.controller.ReimbursementController;
import com.revature.dao.CreateAndAutoPopulate;
import com.revature.dao.UserRepository;
import com.revature.dto.LoginDTO;
import com.revature.exception.DatabaseException;
import com.revature.exception.NotFinanceManagerException;
import com.revature.exception.PasswordHashException;

import io.javalin.Javalin;


public class Application {

	private static Javalin app;
	private static Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) throws SQLException, DatabaseException, PasswordHashException, NotFinanceManagerException {
		
		//resetDatabase();
		startJavalin();

		
	}

	public static void startJavalin() throws DatabaseException {
		//Change what's being configured
		app = Javalin.create((config) ->
			config.enableCorsForAllOrigins()
				  .addStaticFiles("frontend")
				  .addStaticFiles("SQLScripts")
		);
		
		//app = Javalin.create();
		
		app.before(ctx -> {
			String URI = ctx.req.getRequestURI();
			String httpMethod = ctx.req.getMethod();
			logger.info(httpMethod + "request to endpoint " + URI + " recieved");
		});
		
		LoginController loginControl = new LoginController();
		UserRepository userRepo = loginControl.getUserRepository();
		
		mapControllers(new ProjExceptionHandler(), loginControl, new ReimbursementController(userRepo));

		app.start(7000);
	}
	
	public static void mapControllers( Controller... controllers) {
		for (int i = 0; i < controllers.length; i++) {
			controllers[i].mapEndpoints(app);
		}
	}
	
	
	public static void getHashedPasswords(String password) throws PasswordHashException {
		//Only call to show off the fact that the passwords do hash before they are stored
		LoginDTO loginDTO = new LoginDTO("", password);
		System.out.println("Original: " + password);
		System.out.println("Hashed: " + loginDTO.getPassword());
	}
	
	
	public static void resetDatabase() {
		try {
			CreateAndAutoPopulate caap = new CreateAndAutoPopulate();
			caap.createTables();
			try {
				caap.autoPopulateUsersWithData();
				caap.autoPopulateReimbursementsWithData();
			} catch (DatabaseException e) {
				System.out.println("Something went wrong while auto-populating the data");
			}
		} catch(DatabaseException e) {
			e.printStackTrace();
			System.out.println("Something went wrong while connecting");
		}
	}
	
}
