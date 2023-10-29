package services;

import entities.*;  
import java.util.List;
import java.time.LocalDate;


public interface ManageBookCRUDService {

	/* all system operations of the use case*/
	boolean createBook(int bookid, String booktitle, String authors, BookStatus bookstatus) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	Book queryBook(int bookid) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean modifyBook(int bookid, String booktitle, String authors, BookStatus bookstatus) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean deleteBook(int bookid) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	
	/* all get and set functions for temp property*/
	
	/* invariant checking function */
}
