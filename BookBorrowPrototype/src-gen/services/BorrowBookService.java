package services;

import entities.*;  
import java.util.List;
import java.time.LocalDate;


public interface BorrowBookService {

	/* all system operations of the use case*/
	boolean choosebook(int bookid) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean selecttime(LocalDate time) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean confirm() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	
	/* all get and set functions for temp property*/
	
	/* invariant checking function */
}
