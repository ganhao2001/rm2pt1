package services;

import entities.*;  
import java.util.List;
import java.time.LocalDate;


public interface ManageUserCRUDService {

	/* all system operations of the use case*/
	boolean createUser(int userid, String username, String password, String mailbox, UserStatus userstatus) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	User queryUser(int userid) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean modifyUser(int userid, String username, String password, String mailbox, UserStatus userstatus) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean deleteUser(int userid) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	
	/* all get and set functions for temp property*/
	
	/* invariant checking function */
}
