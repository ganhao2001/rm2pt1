package services.impl;

import services.*;
import entities.*;
import java.util.List;
import java.util.LinkedList;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.Arrays;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;
import org.apache.commons.lang3.SerializationUtils;
import java.util.Iterator;

public class ManageBookBorrowCRUDServiceImpl implements ManageBookBorrowCRUDService, Serializable {
	
	
	public static Map<String, List<String>> opINVRelatedEntity = new HashMap<String, List<String>>();
	
	
	ThirdPartyServices services;
			
	public ManageBookBorrowCRUDServiceImpl() {
		services = new ThirdPartyServicesImpl();
	}

	
	//Shared variable from system services
	
	/* Shared variable from system services and get()/set() methods */
			
	/* all get and set functions for temp property*/
				
	
	
	/* Generate inject for sharing temp variables between use cases in system service */
	public void refresh() {
		BookBorrowSystem bookborrowsystem_service = (BookBorrowSystem) ServiceManager.getAllInstancesOf("BookBorrowSystem").get(0);
	}
	
	/* Generate buiness logic according to functional requirement */
	@SuppressWarnings("unchecked")
	public boolean createBookBorrow(int id, int userid, int bookid, LocalDate borrowtime, LocalDate returntime, BorrowStatus borrowstatus) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get bookborrow
		BookBorrow bookborrow = null;
		//no nested iterator --  iterator: any previous:any
		for (BookBorrow ban : (List<BookBorrow>)EntityManager.getAllInstancesOf("BookBorrow"))
		{
			if (ban.getId() == id)
			{
				bookborrow = ban;
				break;
			}
				
			
		}
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(bookborrow) == true) 
		{ 
			/* Logic here */
			BookBorrow ban = null;
			ban = (BookBorrow) EntityManager.createObject("BookBorrow");
			ban.setId(id);
			ban.setUserid(userid);
			ban.setBookid(bookid);
			ban.setBorrowTime(borrowtime);
			ban.setReturnTime(returntime);
			ban.setBorrowStatus(borrowstatus);
			EntityManager.addObject("BookBorrow", ban);
			
			
			refresh();
			// post-condition checking
			if (!(true && 
			ban.getId() == id
			 && 
			ban.getUserid() == userid
			 && 
			ban.getBookid() == bookid
			 && 
			ban.getBorrowTime() == borrowtime
			 && 
			ban.getReturnTime() == returntime
			 && 
			ban.getBorrowStatus() == borrowstatus
			 && 
			StandardOPs.includes(((List<BookBorrow>)EntityManager.getAllInstancesOf("BookBorrow")), ban)
			 && 
			true)) {
				throw new PostconditionException();
			}
			
		
			//return primitive type
			refresh();				
			return true;
		}
		else
		{
			throw new PreconditionException();
		}
		//all relevant vars : ban
		//all relevant entities : BookBorrow
	}  
	
	static {opINVRelatedEntity.put("createBookBorrow", Arrays.asList("BookBorrow"));}
	 
	@SuppressWarnings("unchecked")
	public BookBorrow queryBookBorrow(int id) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get bookborrow
		BookBorrow bookborrow = null;
		//no nested iterator --  iterator: any previous:any
		for (BookBorrow ban : (List<BookBorrow>)EntityManager.getAllInstancesOf("BookBorrow"))
		{
			if (ban.getId() == id)
			{
				bookborrow = ban;
				break;
			}
				
			
		}
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(bookborrow) == false) 
		{ 
			/* Logic here */
			
			
			refresh();
			// post-condition checking
			if (!(true)) {
				throw new PostconditionException();
			}
			
			refresh(); return bookborrow;
		}
		else
		{
			throw new PreconditionException();
		}
	}  
	
	 
	@SuppressWarnings("unchecked")
	public boolean modifyBookBorrow(int id, int userid, int bookid, LocalDate borrowtime, LocalDate returntime, BorrowStatus borrowstatus) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get bookborrow
		BookBorrow bookborrow = null;
		//no nested iterator --  iterator: any previous:any
		for (BookBorrow ban : (List<BookBorrow>)EntityManager.getAllInstancesOf("BookBorrow"))
		{
			if (ban.getId() == id)
			{
				bookborrow = ban;
				break;
			}
				
			
		}
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(bookborrow) == false) 
		{ 
			/* Logic here */
			bookborrow.setId(id);
			bookborrow.setUserid(userid);
			bookborrow.setBookid(bookid);
			bookborrow.setBorrowTime(borrowtime);
			bookborrow.setReturnTime(returntime);
			bookborrow.setBorrowStatus(borrowstatus);
			
			
			refresh();
			// post-condition checking
			if (!(bookborrow.getId() == id
			 && 
			bookborrow.getUserid() == userid
			 && 
			bookborrow.getBookid() == bookid
			 && 
			bookborrow.getBorrowTime() == borrowtime
			 && 
			bookborrow.getReturnTime() == returntime
			 && 
			bookborrow.getBorrowStatus() == borrowstatus
			 && 
			true)) {
				throw new PostconditionException();
			}
			
		
			//return primitive type
			refresh();				
			return true;
		}
		else
		{
			throw new PreconditionException();
		}
		//all relevant vars : bookborrow
		//all relevant entities : BookBorrow
	}  
	
	static {opINVRelatedEntity.put("modifyBookBorrow", Arrays.asList("BookBorrow"));}
	 
	@SuppressWarnings("unchecked")
	public boolean deleteBookBorrow(int id) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get bookborrow
		BookBorrow bookborrow = null;
		//no nested iterator --  iterator: any previous:any
		for (BookBorrow ban : (List<BookBorrow>)EntityManager.getAllInstancesOf("BookBorrow"))
		{
			if (ban.getId() == id)
			{
				bookborrow = ban;
				break;
			}
				
			
		}
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(bookborrow) == false && StandardOPs.includes(((List<BookBorrow>)EntityManager.getAllInstancesOf("BookBorrow")), bookborrow)) 
		{ 
			/* Logic here */
			EntityManager.deleteObject("BookBorrow", bookborrow);
			
			
			refresh();
			// post-condition checking
			if (!(StandardOPs.excludes(((List<BookBorrow>)EntityManager.getAllInstancesOf("BookBorrow")), bookborrow)
			 && 
			true)) {
				throw new PostconditionException();
			}
			
		
			//return primitive type
			refresh();				
			return true;
		}
		else
		{
			throw new PreconditionException();
		}
		//all relevant vars : bookborrow
		//all relevant entities : BookBorrow
	}  
	
	static {opINVRelatedEntity.put("deleteBookBorrow", Arrays.asList("BookBorrow"));}
	 
	
	
	
	/* temp property for controller */
			
	/* all get and set functions for temp property*/
	
	/* invarints checking*/
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList());
			
}
