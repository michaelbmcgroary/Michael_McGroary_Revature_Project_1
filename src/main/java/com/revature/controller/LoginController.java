package com.revature.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.dao.UserRepository;
import com.revature.dto.LoginDTO;
import com.revature.dto.MessageDTO;
import com.revature.dto.PostUserDTO;
import com.revature.exception.DatabaseException;
import com.revature.exception.LoginException;
import com.revature.model.User;
import com.revature.service.LoginService;

import io.javalin.Javalin;
import io.javalin.http.Handler;


public class LoginController implements Controller {

	private Logger logger = LoggerFactory.getLogger(ProjExceptionHandler.class);
	private LoginService loginService;
	
	public LoginController() throws DatabaseException {
		this.loginService = new LoginService();
	}
	
	private Handler loginHandler = (ctx) -> {
		try {
			LoginDTO loginDTO = ctx.bodyAsClass(LoginDTO.class);
			User user = null;
			user =loginService.login(loginDTO);
			if(user == null) {
				MessageDTO messageDTO = new MessageDTO();
				messageDTO.setMessage("User could not be logged in");
				ctx.json(messageDTO);
				ctx.status(400);
			}
			ctx.sessionAttribute("currentlyLoggedInUser", user);
			ctx.sessionAttribute("currentLogin", loginDTO);
			
			System.out.println(user);
			ctx.json(user);
			ctx.status(200);
		} catch(LoginException e) {
			System.out.println(e.getMessage());
			ctx.status(400);
			logger.error("The user could not be found and therefore could not be logged in");
		}
	};
	
	private Handler currentUserHandler = (ctx) -> {
		User user = (User) ctx.sessionAttribute("currentlyLoggedInUser");
		System.out.println(user);
		if(user == null) {
			MessageDTO messageDTO = new MessageDTO();
			messageDTO.setMessage("User is not currently logged in");
			ctx.json(messageDTO);
			ctx.status(400);
		} else {
			ctx.json(user);
			ctx.status(200);
		}
	};
	
	
	private Handler logoutHandler = (ctx) -> {
		logger.info("User, " + ((User) ctx.sessionAttribute("currentlyLoggedInUser")).getUsername()+ " has logged out");
		ctx.req.getSession().invalidate();
	};
	
	
	private Handler newUserHandler = (ctx) -> {
		PostUserDTO userDTO = ctx.bodyAsClass(PostUserDTO.class);
		User user = loginService.createNewUser(userDTO);
		if(user == null) {
			System.out.println("user is null");
			MessageDTO messageDTO = new MessageDTO();
			messageDTO.setMessage("User could not be logged in");
			ctx.json(messageDTO);
			ctx.status(400);
		}
		ctx.json(user);
		ctx.status(200);
	};
	
	

	
	@Override
	public void mapEndpoints(Javalin app) {
		app.post("/login", loginHandler);
		app.get("/current_user", currentUserHandler);
		app.post("/logout", logoutHandler);
		app.post("/newUser", newUserHandler);
	}
	
	public UserRepository getUserRepository() {
		return loginService.getUserRepository();
	}
}
