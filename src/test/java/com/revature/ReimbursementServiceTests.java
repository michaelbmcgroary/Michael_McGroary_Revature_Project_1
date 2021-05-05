package com.revature;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.MockedStatic;

import static org.junit.Assert.*;

import java.sql.Blob;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedList;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
import com.revature.model.ReimbursementType;
import com.revature.model.User;
import com.revature.model.UserRole;
import com.revature.service.ReimbursementService;
import com.revature.util.SessionUtility;
import com.revature.dao.ReimbursementRepository;

public class ReimbursementServiceTests {

	private static UserRepository mockUserRepository;
	private static ReimbursementRepository mockReimbRepo;
	private static Session mockSession;
	
	private ReimbursementService reimbService;
	
	@BeforeClass
	public static void setUp() throws DatabaseException, PasswordHashException, NotFinanceManagerException, BadPasswordException, UserNotFoundException, NoRecieptException {
		mockUserRepository = mock(UserRepository.class);
		mockReimbRepo = mock(ReimbursementRepository.class);
		mockSession = mock(Session.class);
		
		LoginDTO login = new LoginDTO("Username", "Password");
		LoginDTO login2 = new LoginDTO("Username2", "Password");
		User author = new User(1, "Username", "Password", "First", "Last", "email", new UserRole(2));
		User resolver = new User(2, "Username2", "Password2", "First2", "Last2", "email2", new UserRole(1));
		PostReimbursementDTO reimbDTO = new PostReimbursementDTO("100", "Test", 1, null);
		Reimbursement sendReimb = new Reimbursement(reimbDTO);
		sendReimb.setAmount(100.00);
		Reimbursement goodReturnUnresolved = new Reimbursement(1, 100.00, "Test", null, author, null, new ReimbursementStatus(3), new ReimbursementType(1), null, null);
		ArrayList<Reimbursement> reimbList = new ArrayList<Reimbursement>();
		reimbList.add(goodReturnUnresolved);
		reimbList.add(sendReimb);
		Reimbursement goodReturnApproved = new Reimbursement(1, 100.00, "Test", null, author, resolver, new ReimbursementStatus(1), new ReimbursementType(1), null, null);
		Reimbursement goodReturnDenied = new Reimbursement(1, 100.00, "Test", null, author, resolver, new ReimbursementStatus(2), new ReimbursementType(1), null, null);
		Blob blob = null;
		
		when(mockReimbRepo.newReimbursementRequest(sendReimb))
			.thenReturn(goodReturnUnresolved);
		
		when(mockReimbRepo.getReimbursementByID(login, 1))
			.thenReturn(goodReturnUnresolved);
		
		when(mockReimbRepo.getReimbursementByID(new LoginDTO("BadUsername", "Password"), 1))
			.thenThrow(new DatabaseException());
		
		when(mockReimbRepo.getReimbursementByID(new LoginDTO("Username", "BadPassword"), 1))
			.thenThrow(new DatabaseException());
		
		when(mockReimbRepo.getReimbursementByID(login, 2))
			.thenThrow(new NotFinanceManagerException());
		
		when(mockReimbRepo.getAllRequests(login))
			.thenReturn(reimbList);
		
		when(mockReimbRepo.getAllRequestsFilterByStatus(login, new ReimbursementStatus(1)))
			.thenReturn(reimbList);
		
		when(mockReimbRepo.getAllRequestsFilterByStatus(login, new ReimbursementStatus(3)))
			.thenReturn(reimbList);
		
		when(mockReimbRepo.getPreviousRequestsByEmployee(login))
			.thenReturn(reimbList);
		
		when(mockReimbRepo.approveReimbursementRequest(login, goodReturnUnresolved))
			.thenReturn(goodReturnApproved);
		
		when(mockReimbRepo.approveReimbursementRequest(login2, goodReturnUnresolved))
			.thenReturn(goodReturnDenied);
		
		when(mockReimbRepo.getRecieptByID(eq(login), eq(1)))
			.thenReturn(blob);
		
		when(mockReimbRepo.getRecieptByID(eq(login), eq(0)))
			.thenThrow(new NoRecieptException());
		
	}
	
	
	@Before
	public void beforeTest() {
		reimbService = new ReimbursementService(mockUserRepository, mockReimbRepo); 
	}
	
	
	@Test
	public void test_newReimbursementRequest_NoIssue() throws ReimbursementAddException, DatabaseException, BadParameterException {
		try(MockedStatic<SessionUtility> mockedSessionUtil = mockStatic(SessionUtility.class)) {
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			PostReimbursementDTO reimbDTO = new PostReimbursementDTO("100", "Test", 1, null);
			reimbService.newReimbursementRequest(reimbDTO);
		}
	}
	
	@Test
	public void test_newReimbursementRequest_BadAmount() throws ReimbursementAddException, DatabaseException {
		try(MockedStatic<SessionUtility> mockedSessionUtil = mockStatic(SessionUtility.class)) {
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			try {
				reimbService.newReimbursementRequest(new PostReimbursementDTO("Test", "Test", 1, null));
				fail("EmptyParameterException was not thrown");
			} catch (BadParameterException e) {
				assertEquals(e.getMessage(), "Amount must be a double. User provided Test");
			}
		}
	}
	
	@Test
	public void test_getReimbursementByID_NoIssue() throws PasswordHashException, ReimbursementNotFoundException, BadParameterException, EmptyParameterException {
		try(MockedStatic<SessionUtility> mockedSessionUtil = mockStatic(SessionUtility.class)) {
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			LoginDTO login = new LoginDTO("Username", "Password");
			String id = "1";
			User author = new User(1, "Username", "Password", "First", "Last", "email", new UserRole(2));
			Reimbursement expected = new Reimbursement(1, 100.00, "Test", null, author, null, new ReimbursementStatus(3), new ReimbursementType(1), null, null);
			Reimbursement actual = reimbService.getReimbursementByID(login, id);
			assertEquals(expected, actual);
		}
	}
	
	@Test
	public void test_getReimbursementByID_BlankID() throws ReimbursementNotFoundException, EmptyParameterException, PasswordHashException, BadParameterException {
		try(MockedStatic<SessionUtility> mockedSessionUtil = mockStatic(SessionUtility.class)) {
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			try {
				LoginDTO login = new LoginDTO("Username", "Password");
				reimbService.getReimbursementByID(login, " ");
				fail("EmptyParameterException was not thrown");
			} catch (EmptyParameterException e) {
				assertEquals(e.getMessage(), "The Reimbursement ID was left blank");
			}
		}
	}
	
	@Test
	public void test_getReimbursementByID_BadID() throws ReimbursementNotFoundException, EmptyParameterException, PasswordHashException {
		try(MockedStatic<SessionUtility> mockedSessionUtil = mockStatic(SessionUtility.class)) {
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			try {
				LoginDTO login = new LoginDTO("Username", "Password");
				reimbService.getReimbursementByID(login, "Test");
				fail("EmptyParameterException was not thrown");
			} catch (BadParameterException e) {
				assertEquals(e.getMessage(), "The Reimbursement ID provided must be of type int");
			}
		}
	}
	
	@Test
	public void test_getReimbursementByID_EmptyUsername() throws ReimbursementNotFoundException, BadParameterException, PasswordHashException {
		try(MockedStatic<SessionUtility> mockedSessionUtil = mockStatic(SessionUtility.class)) {
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			try {
				LoginDTO login = new LoginDTO("", "Password");
				reimbService.getReimbursementByID(login, "1");
				fail("EmptyParameterException was not thrown");
			} catch (EmptyParameterException e) {
				assertEquals(e.getMessage(), "The username of the logged in user was not found");
			}
		}
	}
	
	@Test
	public void test_getReimbursementByID_BadUsername() throws PasswordHashException, ReimbursementNotFoundException, EmptyParameterException, BadParameterException {
		try(MockedStatic<SessionUtility> mockedSessionUtil = mockStatic(SessionUtility.class)) {
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			try {
				LoginDTO login = new LoginDTO("BadUsername", "Password");
				reimbService.getReimbursementByID(login, "1");
				fail("EmptyParameterException was not thrown");
			} catch (ReimbursementNotFoundException e) {
				assertEquals(e.getMessage(), "The reimbursement with id 1 could not be found");
			}
		}
	}
	
	@Test
	public void test_getReimbursementByID_BadPassword() throws PasswordHashException, BadParameterException, EmptyParameterException {
		try(MockedStatic<SessionUtility> mockedSessionUtil = mockStatic(SessionUtility.class)) {
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			try {
				LoginDTO login = new LoginDTO("Username", "BadPassword");
				reimbService.getReimbursementByID(login, "1");
				fail("EmptyParameterException was not thrown");
			} catch (ReimbursementNotFoundException e) {
				assertEquals(e.getMessage(), "The reimbursement with id 1 could not be found");
			}
		}
	}
	
	@Test
	public void test_getReimbursementByID_NotFinanceManager() throws BadParameterException, EmptyParameterException, PasswordHashException {
		try(MockedStatic<SessionUtility> mockedSessionUtil = mockStatic(SessionUtility.class)) {
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			try {
				LoginDTO login = new LoginDTO("Username", "Password");
				reimbService.getReimbursementByID(login, "2");
				fail("BadParameterException was not thrown");
			} catch (ReimbursementNotFoundException e) {
				assertEquals(e.getMessage(), "The login credentials are not for a finance manager");
			}
		}
	}
	
	@Test
	public void test_getAllReimbursements_NoIssue() throws PasswordHashException, DatabaseException, ReimbursementNotFoundException, EmptyParameterException {
		try(MockedStatic<SessionUtility> mockedSessionUtil = mockStatic(SessionUtility.class)) {
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			User author = new User(1, "Username", "Password", "First", "Last", "email", new UserRole(2));
			Reimbursement reimb1 = new Reimbursement(1, 100.00, "Test", null, author, null, new ReimbursementStatus(3), new ReimbursementType(1), null, null);
			PostReimbursementDTO reimbDTO = new PostReimbursementDTO("100", "Test", 1, null);
			Reimbursement reimb2 = new Reimbursement(reimbDTO);
			reimb2.setAmount(100.00);
			ArrayList<Reimbursement> expected = new ArrayList<Reimbursement>();
			expected.add(reimb1);
			expected.add(reimb2);
			LoginDTO login = new LoginDTO("Username", "Password");
			ArrayList<Reimbursement> actual = (ArrayList<Reimbursement>) reimbService.getAllReimbursements(login);
			assertEquals(expected, actual);
			
		}
	}
	
	
	@Test
	public void test_getReimbursementsByStatus_NoIssue() throws PasswordHashException, DatabaseException, ReimbursementNotFoundException, EmptyParameterException {
		try(MockedStatic<SessionUtility> mockedSessionUtil = mockStatic(SessionUtility.class)) {
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			User author = new User(1, "Username", "Password", "First", "Last", "email", new UserRole(2));
			Reimbursement reimb1 = new Reimbursement(1, 100.00, "Test", null, author, null, new ReimbursementStatus(3), new ReimbursementType(1), null, null);
			PostReimbursementDTO reimbDTO = new PostReimbursementDTO("100", "Test", 1, null);
			Reimbursement reimb2 = new Reimbursement(reimbDTO);
			reimb2.setAmount(100.00);
			ArrayList<Reimbursement> expected = new ArrayList<Reimbursement>();
			expected.add(reimb1);
			expected.add(reimb2);
			LoginDTO login = new LoginDTO("Username", "Password");
			ArrayList<Reimbursement> actual = (ArrayList<Reimbursement>) reimbService.getReimbursementsByStatus(login, "Approved");
			assertEquals(expected, actual);
		}
	}
	
	@Test
	public void test_getReimbursementsByStatus_BlankStatus() throws PasswordHashException, ReimbursementNotFoundException {
		try(MockedStatic<SessionUtility> mockedSessionUtil = mockStatic(SessionUtility.class)) {
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			try {
				LoginDTO login = new LoginDTO("Username", "Password");
				reimbService.getReimbursementsByStatus(login, "");
				fail("EmptyParameterException was not thrown");
			} catch (EmptyParameterException e) {
				assertEquals(e.getMessage(), "The status to filter by was left blank");
			}
		}
	}
	
	@Test
	public void test_getReimbursementsByStatus_BadStatus() throws PasswordHashException, ReimbursementNotFoundException, EmptyParameterException, DatabaseException {
		try(MockedStatic<SessionUtility> mockedSessionUtil = mockStatic(SessionUtility.class)) {
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			User author = new User(1, "Username", "Password", "First", "Last", "email", new UserRole(2));
			Reimbursement reimb1 = new Reimbursement(1, 100.00, "Test", null, author, null, new ReimbursementStatus(3), new ReimbursementType(1), null, null);
			PostReimbursementDTO reimbDTO = new PostReimbursementDTO("100", "Test", 1, null);
			Reimbursement reimb2 = new Reimbursement(reimbDTO);
			reimb2.setAmount(100.00);
			ArrayList<Reimbursement> expected = new ArrayList<Reimbursement>();
			expected.add(reimb1);
			expected.add(reimb2);
			LoginDTO login = new LoginDTO("Username", "Password");
			ArrayList<Reimbursement> actual = (ArrayList<Reimbursement>) reimbService.getReimbursementsByStatus(login, "Pending");
			assertEquals(expected, actual);
		}
	}
	
	
	@Test
	public void test_getReimbursementsByUser_NoIssue() throws PasswordHashException, DatabaseException, ReimbursementNotFoundException, EmptyParameterException, BadPasswordException, UserNotFoundException {
		try(MockedStatic<SessionUtility> mockedSessionUtil = mockStatic(SessionUtility.class)) {
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			User author = new User(1, "Username", "Password", "First", "Last", "email", new UserRole(2));
			Reimbursement reimb1 = new Reimbursement(1, 100.00, "Test", null, author, null, new ReimbursementStatus(3), new ReimbursementType(1), null, null);
			PostReimbursementDTO reimbDTO = new PostReimbursementDTO("100", "Test", 1, null);
			Reimbursement reimb2 = new Reimbursement(reimbDTO);
			reimb2.setAmount(100.00);
			ArrayList<Reimbursement> expected = new ArrayList<Reimbursement>();
			expected.add(reimb1);
			expected.add(reimb2);
			LoginDTO login = new LoginDTO("Username", "Password");
			ArrayList<Reimbursement> actual = (ArrayList<Reimbursement>) reimbService.getReimbursementsByUser(login);
			assertEquals(expected, actual);
		}
	}
	
	
	@Test
	public void test_approveReimbursement_NoIssue() throws PasswordHashException, ReimbursementApproveException, EmptyParameterException, BadPasswordException, UserNotFoundException {
		try(MockedStatic<SessionUtility> mockedSessionUtil = mockStatic(SessionUtility.class)) {
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			LoginDTO login = new LoginDTO("Username", "Password");
			User author = new User(1, "Username", "Password", "First", "Last", "email", new UserRole(2));
			User resolver = new User(2, "Username2", "Password2", "First2", "Last2", "email2", new UserRole(1));
			Reimbursement sendReimb = new Reimbursement(1, 100.00, "Test", null, author, null, new ReimbursementStatus(3), new ReimbursementType(1), null, null);
			Reimbursement expected = new Reimbursement(1, 100.00, "Test", null, author, resolver, new ReimbursementStatus(1), new ReimbursementType(1), null, null);
			Reimbursement actual = reimbService.approveReimbursement(login, sendReimb);
			assertEquals(expected, actual);
		}
	}
	
	
	@Test
	public void test_denyReimbursement_NoIssue() throws PasswordHashException, EmptyParameterException, ReimbursementDenyException, BadPasswordException, UserNotFoundException, ReimbursementApproveException {
		try(MockedStatic<SessionUtility> mockedSessionUtil = mockStatic(SessionUtility.class)) {
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			LoginDTO login = new LoginDTO("Username", "Password");
			User author = new User(1, "Username", "Password", "First", "Last", "email", new UserRole(2));
			User resolver = new User(2, "Username2", "Password2", "First2", "Last2", "email2", new UserRole(1));
			Reimbursement sendReimb = new Reimbursement(1, 100.00, "Test", null, author, null, new ReimbursementStatus(3), new ReimbursementType(1), null, null);
			Reimbursement expected = new Reimbursement(1, 100.00, "Test", null, author, resolver, new ReimbursementStatus(1), new ReimbursementType(1), null, null);
			Reimbursement actual = reimbService.approveReimbursement(login, sendReimb);
			assertEquals(expected, actual);
		}
	}
	
	
	@Test
	public void test_getRecieptByID_NoIssue() throws PasswordHashException, EmptyParameterException, BadParameterException, DatabaseException, NotFinanceManagerException, NoRecieptException {
		try(MockedStatic<SessionUtility> mockedSessionUtil = mockStatic(SessionUtility.class)) {
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			LoginDTO login = new LoginDTO("Username", "Password");
			Blob expected = null;
			Blob actual = reimbService.getRecieptByID(login, "1");
			assertEquals(expected, actual);
		}
	}
	
	@Test
	public void test_getRecieptByID_NoRecieptForID() throws PasswordHashException, EmptyParameterException, BadParameterException, DatabaseException, NotFinanceManagerException, NoRecieptException {
		try(MockedStatic<SessionUtility> mockedSessionUtil = mockStatic(SessionUtility.class)) {
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			LoginDTO login = new LoginDTO("Username", "Password");
			Blob expected = null;
			Blob actual = reimbService.getRecieptByID(login, "0");
			assertEquals(expected, actual);
		}
	}
	

}
