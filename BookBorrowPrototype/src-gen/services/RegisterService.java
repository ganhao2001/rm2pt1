package services;

import entities.*;  
import java.util.List;
import java.time.LocalDate;


public interface RegisterService {

	/* all system operations of the use case*/
	boolean inputUser(String username, String password, String email) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean verification(String code) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	String getVcode();
	void setVcode(String vcode);
	User getUser();
	void setUser(User user);
	
	/* all get and set functions for temp property*/
	
	/* invariant checking function */
}
