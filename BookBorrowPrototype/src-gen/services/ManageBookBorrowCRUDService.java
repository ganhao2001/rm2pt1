package services;

import entities.*;  
import java.util.List;
import java.time.LocalDate;


public interface ManageBookBorrowCRUDService {

	/* all system operations of the use case*/
	boolean createBookBorrow(int id, int userid, int bookid, LocalDate borrowtime, LocalDate returntime, BorrowStatus borrowstatus) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	BookBorrow queryBookBorrow(int id) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean modifyBookBorrow(int id, int userid, int bookid, LocalDate borrowtime, LocalDate returntime, BorrowStatus borrowstatus) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	boolean deleteBookBorrow(int id) throws PreconditionException, PostconditionException, ThirdPartyServiceException;
	
	/* all get and set functions for temp property*/
	
	/* all get and set functions for temp property*/
	
	/* invariant checking function */
}
