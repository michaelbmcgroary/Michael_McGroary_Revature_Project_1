package com.revature.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.dao.UserRepository;
import com.revature.dto.DisplayReimbursementDTO;
import com.revature.dto.LoginDTO;
import com.revature.dto.PostReimbursementDTO;
import com.revature.exception.DatabaseException;
import com.revature.exception.NotLoggedInException;
import com.revature.model.Reimbursement;
import com.revature.model.ReimbursementType;
import com.revature.model.User;
import com.revature.service.ReimbursementService;

import io.javalin.Javalin;
import io.javalin.http.Handler;
import io.javalin.http.UploadedFile;

public class ReimbursementController implements Controller {

	private Logger logger = LoggerFactory.getLogger(ReimbursementController.class);
	private ReimbursementService reimbursementService;
	
	public ReimbursementController(UserRepository userRepo) throws DatabaseException {
		reimbursementService = new ReimbursementService(userRepo);
	}
	
	//For Mockito and JUnit Testing
	public ReimbursementController(ReimbursementService reimbursementService) {
		this.reimbursementService = reimbursementService;
	}
	
	

	
	private Handler newReimbursementRequest = (ctx) -> {
		LoginDTO login = ctx.sessionAttribute("currentLogin");
		if(login != null) {
			UploadedFile upFile = null;
			if(ctx.uploadedFiles().size() !=0) {
				upFile = ctx.uploadedFiles().get(0);
			}
			PostReimbursementDTO reimbDTO = new PostReimbursementDTO();
			reimbDTO.setAmount(ctx.formParam("amount"));
			reimbDTO.setAuthor(ctx.sessionAttribute("currentlyLoggedInUser"));
			if(upFile != null) {
				reimbDTO.setBlob((ByteArrayInputStream) upFile.component1());
			}
			reimbDTO.setType(new ReimbursementType(ctx.formParam("type")));
			reimbDTO.setDescription(ctx.formParam("description"));
			Reimbursement reimb = reimbursementService.newReimbursementRequest(reimbDTO);
			logger.info("User posted a new reimbursement request"); 
			DisplayReimbursementDTO dispReimb = new DisplayReimbursementDTO(reimb);
			ctx.json(dispReimb);
			ctx.status(200); 
			
		} else {
			throw new NotLoggedInException("User tried to post a new Reimbursement while not being logged in");
		}
	};
	
	private Handler getReimbursementByID = (ctx) -> {
		String id = ctx.pathParam("id");
		LoginDTO login = ctx.sessionAttribute("currentLogin");
		if(login != null) {
			Reimbursement reimb = reimbursementService.getReimbursementByID(login, id);
			logger.info("User requested information about Reimbursement with ID " + id);
			DisplayReimbursementDTO dispReimb = new DisplayReimbursementDTO(reimb);
			ctx.json(dispReimb); 
			ctx.status(200); 
		} else {
			throw new NotLoggedInException("User tried to get reimbursement information while not being logged in");
		}
	};
	
	private Handler getAllReimbursements = (ctx) -> {
		LoginDTO login = ctx.sessionAttribute("currentLogin");
		User user = ctx.sessionAttribute("currentlyLoggedInUser");
		if(login != null) {
			if(user.getUserRole().getRoleID() == 1) {
				//1 indicates a Manager
				ArrayList<Reimbursement> reimbList = (ArrayList<Reimbursement>) reimbursementService.getAllReimbursements(login);
				logger.info("User, " +login.getUsername() + ", requested information about all Reimbursements");
				ArrayList<DisplayReimbursementDTO> dispReimbList = new ArrayList<DisplayReimbursementDTO>();
				for(int i=reimbList.size()-1; i>=0; i--) {
					dispReimbList.add(new DisplayReimbursementDTO(reimbList.get(i)));
				}
				ctx.json(dispReimbList);
				ctx.status(200); 
			} else {
				//If the roleID wasn't 1, then they are an employee and it should only post their reimbursement history
				ArrayList<Reimbursement> reimbList = (ArrayList<Reimbursement>) reimbursementService.getReimbursementsByUser(login);
				logger.info("User, " + login.getUsername() + ", requested information about their Reimbursement history");
				ArrayList<DisplayReimbursementDTO> dispReimbList = new ArrayList<DisplayReimbursementDTO>();
				for(int i=reimbList.size()-1; i>=0; i--) {
					dispReimbList.add(new DisplayReimbursementDTO(reimbList.get(i)));
				}
				ctx.json(dispReimbList); 
				ctx.status(200); 
			}
		} else {
			throw new NotLoggedInException("User tried to get reimbursement information while not being logged in");
		}
	};
	
	private Handler getReimbursementsByStatus = (ctx) -> {
		String status = ctx.pathParam("status"); 
		LoginDTO login = ctx.sessionAttribute("currentLogin");
		if(login != null) {
			ArrayList<Reimbursement> reimbList = (ArrayList<Reimbursement>) reimbursementService.getReimbursementsByStatus(login, status);
			logger.info("User, " + login.getUsername() + ", requested information about Reimbursements of Status " + status);
			ArrayList<DisplayReimbursementDTO> dispReimbList = new ArrayList<DisplayReimbursementDTO>();
			for(int i=reimbList.size()-1; i>=0; i--) {
				dispReimbList.add(new DisplayReimbursementDTO(reimbList.get(i)));
			}
			ctx.json(dispReimbList); 
			ctx.status(200); 
		} else {
			throw new NotLoggedInException("User tried to get reimbursement information while not being logged in");
		}
	};
	
	private Handler getReimbursementsByUser = (ctx) -> {
		LoginDTO login = ctx.sessionAttribute("currentLogin");
		if(login != null) {
			ArrayList<Reimbursement> reimbList = (ArrayList<Reimbursement>) reimbursementService.getReimbursementsByUser(login);
			logger.info("User, " + login.getUsername() + ", requested information about their Reimbursements history");
			ArrayList<DisplayReimbursementDTO> dispReimbList = new ArrayList<DisplayReimbursementDTO>();
			for(int i=0; i<reimbList.size(); i++) {
				dispReimbList.add(new DisplayReimbursementDTO(reimbList.get(i)));
			}
			ctx.json(dispReimbList); 
			ctx.status(200); 
		} else {
			throw new NotLoggedInException("User tried to get reimbursement information while not being logged in");
		}
	};
	
	private Handler approveReimbursement = (ctx) -> {
		LoginDTO login = ctx.sessionAttribute("currentLogin");
		if(login != null) {
			String id = ctx.pathParam("id");
			Reimbursement reimb = reimbursementService.getReimbursementByID(login, id);
			reimb = reimbursementService.approveReimbursement(login, reimb);
			logger.info("User, " + login.getUsername() + ", requested information about their Reimbursements history");
			DisplayReimbursementDTO dispReimb = new DisplayReimbursementDTO(reimb);
			ctx.json(dispReimb); 
			ctx.status(200); 
		} else {
			throw new NotLoggedInException("User tried to approve reimbursement while not being logged in");
		}
	};
	
	private Handler denyReimbursement = (ctx) -> {
		LoginDTO login = ctx.sessionAttribute("currentLogin");
		if(login != null) {
			String id = ctx.pathParam("id");
			Reimbursement reimb = reimbursementService.getReimbursementByID(login, id);
			reimb = reimbursementService.denyReimbursement(login, reimb);
			logger.info("User, " + login.getUsername() + ", requested information about their Reimbursements history");
			DisplayReimbursementDTO dispReimb = new DisplayReimbursementDTO(reimb);
			ctx.json(dispReimb); 
			ctx.status(200); 
		} else {
			throw new NotLoggedInException("User tried to approve reimbursement while not being logged in");
		}
	};
	
	
	private Handler getRecieptByID = (ctx) -> {
		LoginDTO login = ctx.sessionAttribute("currentLogin");
		if(login != null) {
			String reimbID = ctx.pathParam("id");
			Blob blob = reimbursementService.getRecieptByID(login, reimbID);
			InputStream in = blob.getBinaryStream();
			ctx.result(in);
			ctx.contentType("image/jpeg");
			ctx.status(200);
		} else {
			throw new NotLoggedInException("User tried to get a reimbursement reciept while not being logged in");
		}
		
	};
	
	
	@Override
	public void mapEndpoints(Javalin app) {
		app.post("/reimbursement", newReimbursementRequest);
		app.get("/reimbursement", getAllReimbursements);
		app.get("/reimbursement/reciept/:id", getRecieptByID);
		app.get("/reimbursement/id/:id", getReimbursementByID);
		app.get("/reimbursement/status/:status", getReimbursementsByStatus);
		app.get("/reimbursement/user", getReimbursementsByUser);
		app.put("/reimbursement/:id/approve", approveReimbursement);
		app.put("/reimbursement/:id/deny", denyReimbursement);
	}
}
