package services;

import entities.*;  
import java.util.List;
import java.time.LocalDate;


public interface LoginService {

	/* all system operations of the use case*/
	boolean inputUsername(String name) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean inputPassword(String password) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	String getUsername();
	void setUsername(String username);
	User getUser();
	void setUser(User user);
	
	/* all get and set functions for temp property*/
	
	/* invariant checking function */
}
