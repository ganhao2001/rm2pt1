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

public class ManageBookCRUDServiceImpl implements ManageBookCRUDService, Serializable {
	
	
	public static Map<String, List<String>> opINVRelatedEntity = new HashMap<String, List<String>>();
	
	
	ThirdPartyServices services;
			
	public ManageBookCRUDServiceImpl() {
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
	public boolean createBook(int bookid, String booktitle, String authors, BookStatus bookstatus) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
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
		if (StandardOPs.oclIsundefined(book) == true) 
		{ 
			/* Logic here */
			Book ban = null;
			ban = (Book) EntityManager.createObject("Book");
			ban.setBookId(bookid);
			ban.setBooktitle(booktitle);
			ban.setAuthors(authors);
			ban.setBookStatus(bookstatus);
			EntityManager.addObject("Book", ban);
			
			
			refresh();
			// post-condition checking
			if (!(true && 
			ban.getBookId() == bookid
			 && 
			ban.getBooktitle() == booktitle
			 && 
			ban.getAuthors() == authors
			 && 
			ban.getBookStatus() == bookstatus
			 && 
			StandardOPs.includes(((List<Book>)EntityManager.getAllInstancesOf("Book")), ban)
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
		//string parameters: [booktitle, authors] 
		//all relevant vars : ban
		//all relevant entities : Book
	}  
	
	static {opINVRelatedEntity.put("createBook", Arrays.asList("Book"));}
	 
	@SuppressWarnings("unchecked")
	public Book queryBook(int bookid) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
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
			
			
			refresh();
			// post-condition checking
			if (!(true)) {
				throw new PostconditionException();
			}
			
			refresh(); return book;
		}
		else
		{
			throw new PreconditionException();
		}
	}  
	
	 
	@SuppressWarnings("unchecked")
	public boolean modifyBook(int bookid, String booktitle, String authors, BookStatus bookstatus) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
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
			book.setBookId(bookid);
			book.setBooktitle(booktitle);
			book.setAuthors(authors);
			book.setBookStatus(bookstatus);
			
			
			refresh();
			// post-condition checking
			if (!(book.getBookId() == bookid
			 && 
			book.getBooktitle() == booktitle
			 && 
			book.getAuthors() == authors
			 && 
			book.getBookStatus() == bookstatus
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
		//string parameters: [booktitle, authors] 
		//all relevant vars : book
		//all relevant entities : Book
	}  
	
	static {opINVRelatedEntity.put("modifyBook", Arrays.asList("Book"));}
	 
	@SuppressWarnings("unchecked")
	public boolean deleteBook(int bookid) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
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
		if (StandardOPs.oclIsundefined(book) == false && StandardOPs.includes(((List<Book>)EntityManager.getAllInstancesOf("Book")), book)) 
		{ 
			/* Logic here */
			EntityManager.deleteObject("Book", book);
			
			
			refresh();
			// post-condition checking
			if (!(StandardOPs.excludes(((List<Book>)EntityManager.getAllInstancesOf("Book")), book)
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
		//all relevant vars : book
		//all relevant entities : Book
	}  
	
	static {opINVRelatedEntity.put("deleteBook", Arrays.asList("Book"));}
	 
	
	
	
	/* temp property for controller */
			
	/* all get and set functions for temp property*/
	
	/* invarints checking*/
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList());
			
}
