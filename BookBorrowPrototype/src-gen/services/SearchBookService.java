package services;

import entities.*;  
import java.util.List;
import java.time.LocalDate;


public interface SearchBookService {

	/* all system operations of the use case*/
	boolean login(String username, String password) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean inputtitle(String title) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean brows() throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	Book selectbook(int bookid) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	Book getBook();
	void setBook(Book book);
	
	/* all get and set functions for temp property*/
	
	/* invariant checking function */
}
