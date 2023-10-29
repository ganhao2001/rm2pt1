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

public class SearchBookServiceImpl implements SearchBookService, Serializable {
	
	
	public static Map<String, List<String>> opINVRelatedEntity = new HashMap<String, List<String>>();
	
	
	ThirdPartyServices services;
			
	public SearchBookServiceImpl() {
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
	public boolean login(String username, String password) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get user
		User user = null;
		//no nested iterator --  iterator: any previous:any
		for (User ban : (List<User>)EntityManager.getAllInstancesOf("User"))
		{
			if (ban.getUsername().equals(username))
			{
				user = ban;
				break;
			}
				
			
		}
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(user) == false) 
		{ 
			/* Logic here */
			if (user.getPassword().equals(password))
			{
				
				refresh();
				// post-condition checking
				if (!((user.getPassword() == password ? true : true))) {
					throw new PostconditionException();
				}
				
				//return code
				refresh();
				return true;
			}
			else
			{
			 	
			 	refresh();
			 	// post-condition checking
			 	if (!((user.getPassword() == password ? true : true))) {
			 		throw new PostconditionException();
			 	}
			 	
			 	//return code
			 	refresh();
			 	return false;
			}
			
			
			
		
		}
		else
		{
			throw new PreconditionException();
		}
		//string parameters: [username, password] 
	}  
	
	 
	@SuppressWarnings("unchecked")
	public boolean inputtitle(String title) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get book
		Book book = null;
		//no nested iterator --  iterator: any previous:any
		for (Book ban : (List<Book>)EntityManager.getAllInstancesOf("Book"))
		{
			if (ban.getBooktitle().equals(title))
			{
				book = ban;
				break;
			}
				
			
		}
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(book) == false) 
		{ 
			/* Logic here */
			this.setBook(book);
			
			
			refresh();
			// post-condition checking
			if (!(this.getBook() == book
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
		//string parameters: [title] 
		//all relevant vars : this
		//all relevant entities : 
	}  
	
	static {opINVRelatedEntity.put("inputtitle", Arrays.asList(""));}
	 
	@SuppressWarnings("unchecked")
	public boolean brows() throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* previous state in post-condition*/
 
		/* check precondition */
		if (true) 
		{ 
			/* Logic here */
			
			
			refresh();
			// post-condition checking
			if (!(true)) {
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
	}  
	
	 
	@SuppressWarnings("unchecked")
	public Book selectbook(int bookid) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get book
		Book book = null;
		//no nested iterator --  iterator: any previous:any
		for (Book ban : (List<Book>)EntityManager.getAllInstancesOf("Book"))
		{
			if (ban.getBookId() == bookid)
			{
				book = ban;
				break;
			}
				
			
		}
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(book) == false) 
		{ 
			/* Logic here */
			if (bookid == this.getBook().getBookId())
			{
				
				refresh();
				// post-condition checking
				if (!((bookid == this.getBook().getBookId() ? true : true))) {
					throw new PostconditionException();
				}
				
				//return code
				refresh();
				return this.getBook();
			}
			else
			{
			 	
			 	refresh();
			 	// post-condition checking
			 	if (!((bookid == this.getBook().getBookId() ? true : true))) {
			 		throw new PostconditionException();
			 	}
			 	
			 	//return code
			 	refresh();
			 	return null;
			}
			
			
			
		}
		else
		{
			throw new PreconditionException();
		}
		//all relevant vars : this
		//all relevant entities : 
	}  
	
	static {opINVRelatedEntity.put("selectbook", Arrays.asList(""));}
	 
	
	
	
	/* temp property for controller */
	private Book Book;
			
	/* all get and set functions for temp property*/
	public Book getBook() {
		return Book;
	}	
	
	public void setBook(Book book) {
		this.Book = book;
	}
	
	/* invarints checking*/
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList());
			
}
