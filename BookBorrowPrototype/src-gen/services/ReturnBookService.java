package services;

import entities.*;  
import java.util.List;
import java.time.LocalDate;


public interface ReturnBookService {

	/* all system operations of the use case*/
	boolean inputuser(String username) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean inputbook(String bookname) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean confirminformation() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	
	/* all get and set functions for temp property*/
	
	/* invariant checking function */
}
