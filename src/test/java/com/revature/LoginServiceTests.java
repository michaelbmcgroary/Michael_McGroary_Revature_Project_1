package com.revature;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.MockedStatic;

import org.hibernate.Session;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.dao.UserRepository;
import com.revature.dto.LoginDTO;
import com.revature.dto.PostUserDTO;
import com.revature.exception.*;
import com.revature.model.User;
import com.revature.model.UserRole;
import com.revature.service.LoginService;
import com.revature.util.SessionUtility;

public class LoginServiceTests {

	private static UserRepository mockUserRepository;
	private static Session mockSession;
	
	private LoginService loginService;
	
	@BeforeClass
	public static void setUp() throws DatabaseException, PasswordHashException, UserNotFoundException, BadPasswordException {
		mockUserRepository = mock(UserRepository.class);
		mockSession = mock(Session.class);
		
		when(mockUserRepository.getUserByUsernameAndPassword(eq(new LoginDTO("Username", "Password"))))
			.thenReturn(new User(1, "Username", "Password", "George", "Lucas", null, null));
		
		when(mockUserRepository.getUserByUsernameAndPassword(eq(new LoginDTO("Username-2", "Password"))))
			.thenReturn(new User(-2, "Username", "Password", "George", "Lucas", null, null));
		
		when(mockUserRepository.getUserByUsernameAndPassword(eq(new LoginDTO("Username-1", "Password"))))
			.thenReturn(new User(-1, "Username", "Password", "George", "Lucas", null, null));
		
		when(mockUserRepository.getUserByUsernameAndPassword(eq(new LoginDTO("Username2", "Password"))))
			.thenThrow(new UserNotFoundException("User could not be found."));
		
		when(mockUserRepository.getUserByUsernameAndPassword(eq(new LoginDTO("Username", "Password2"))))
			.thenThrow(new BadPasswordException("Password does not match username"));
		
		when(mockUserRepository.getUserByUsernameAndPassword(eq(new LoginDTO("Username2", "Password2"))))
			.thenReturn(null);
		
		when(mockUserRepository.addUser(eq(new User(new PostUserDTO("Username", "Password", "George", "Lucas", "email")))))
			.thenReturn(new User(1, "Username", "Password", "George", "Lucas", "email", new UserRole(2)));
		
		User addUser = new User(new PostUserDTO("Username2", "Password2", "George", "Lucas", "email"));
		when(mockUserRepository.addUser(eq(addUser)))
			.thenThrow(new DatabaseException());
		
		
	
		
	}
	
	
	@Before
	public void beforeTest() {
		try {
			loginService = new LoginService(mockUserRepository);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void test_login_NoIssues() throws PasswordHashException, BadParameterException, LoginException  {
		try(MockedStatic<SessionUtility> mockedSessionUtil = mockStatic(SessionUtility.class)) {
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			User expected = new User(1, "Username", "Password", "George", "Lucas", null, null);
			LoginDTO login = new LoginDTO("Username", "Password");
			User actual = loginService.login(login);
			assertEquals(expected, actual);
		}
	}
	
	@Test
	public void test_login_BlankUsername() throws PasswordHashException, BadParameterException, LoginException  {
		try(MockedStatic<SessionUtility> mockedSessionUtil = mockStatic(SessionUtility.class)) {
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			try {
				loginService.login(new LoginDTO("", "Password"));
				fail("EmptyParameterException was not thrown");
			} catch (BadParameterException e) {
				assertEquals(e.getMessage(), "Cannot have blank username or password");
			}
		}
	}
	
	@Test
	public void test_login_BlankPassword() throws PasswordHashException, BadParameterException, LoginException  {
		try(MockedStatic<SessionUtility> mockedSessionUtil = mockStatic(SessionUtility.class)) {
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			try {
				loginService.login(new LoginDTO("Username", ""));
				fail("EmptyParameterException was not thrown");
			} catch (LoginException e) {
				assertEquals(e.getMessage(), "No user was able to be returned");
			}
		}
	}
	
	@Test
	public void test_login_UserNotFound() throws BadParameterException, PasswordHashException  {
		try(MockedStatic<SessionUtility> mockedSessionUtil = mockStatic(SessionUtility.class)) {
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			try {
				loginService.login(new LoginDTO("Username2", "Password"));
				fail("EmptyParameterException was not thrown");
			} catch (LoginException e) {
				assertEquals(e.getMessage(), "User with given username was not found");
			}
		}
	}
	
	@Test
	public void test_login_BadPassword() throws BadParameterException, PasswordHashException  {
		try(MockedStatic<SessionUtility> mockedSessionUtil = mockStatic(SessionUtility.class)) {
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			try {
				loginService.login(new LoginDTO("Username", "Password2"));
				fail("EmptyParameterException was not thrown");
			} catch (LoginException e) {
				assertEquals(e.getMessage(), "Given password does not match the given username and therefore couldn't be logged in");
			}
		}
	}
	
	@Test
	public void test_login_BackupUserNotFound() throws BadParameterException, PasswordHashException  {
		try(MockedStatic<SessionUtility> mockedSessionUtil = mockStatic(SessionUtility.class)) {
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			try {
				loginService.login(new LoginDTO("Username2", "Password2"));
				fail("LoginException was not thrown");
			} catch (LoginException e) {
				assertEquals(e.getMessage(), "No user was able to be returned");
			}
		}
	}
	
	@Test
	public void test_login_BackupUserNotFound2() throws BadParameterException, PasswordHashException  {
		try(MockedStatic<SessionUtility> mockedSessionUtil = mockStatic(SessionUtility.class)) {
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			try {
				loginService.login(new LoginDTO("Username-1", "Password"));
				fail("LoginException was not thrown");
			} catch (LoginException e) {
				assertEquals(e.getMessage(), "Username was not found");
			}
		}
	}
	
	@Test
	public void test_login_BackupBadPassword() throws BadParameterException, PasswordHashException  {
		try(MockedStatic<SessionUtility> mockedSessionUtil = mockStatic(SessionUtility.class)) {
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			try {
				loginService.login(new LoginDTO("Username-2", "Password"));
				fail("LoginException was not thrown");
			} catch (LoginException e) {
				assertEquals(e.getMessage(), "Password provided does not match username");
			}
		}
	}
	
	@Test
	public void test_getUserRepository_NoIssues()  {
		try(MockedStatic<SessionUtility> mockedSessionUtil = mockStatic(SessionUtility.class)) {
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			assertEquals(mockUserRepository, loginService.getUserRepository());
		}
	}
	
	@Test
	public void test_createNewUser_NoIssues() throws PasswordHashException, UserAddException  {
		try(MockedStatic<SessionUtility> mockedSessionUtil = mockStatic(SessionUtility.class)) {
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			User expected = new User(1, "Username", "Password", "George", "Lucas", "email", new UserRole(2));
			User actual = loginService.createNewUser(new PostUserDTO("Username", "Password", "George", "Lucas", "email"));
			assertEquals(expected, actual);
		}
	}
	
	@Test
	public void test_createNewUser_DatabaseSyntaxIssue() throws BadParameterException, PasswordHashException, UserAddException  {
		try(MockedStatic<SessionUtility> mockedSessionUtil = mockStatic(SessionUtility.class)) {
			mockedSessionUtil.when(SessionUtility::getSession).thenReturn(mockSession);
			try {
				loginService.createNewUser(new PostUserDTO("Username2", "Password2", "George", "Lucas", "email"));
				fail("LoginException was not thrown");
			} catch (UserAddException e) {
				assertEquals(e.getMessage(), "User could not be added to the database");
			}
		}
	}
	
	

}
