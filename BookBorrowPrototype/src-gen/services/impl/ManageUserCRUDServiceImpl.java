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

public class ManageUserCRUDServiceImpl implements ManageUserCRUDService, Serializable {
	
	
	public static Map<String, List<String>> opINVRelatedEntity = new HashMap<String, List<String>>();
	
	
	ThirdPartyServices services;
			
	public ManageUserCRUDServiceImpl() {
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
	public boolean createUser(int userid, String username, String password, String mailbox, UserStatus userstatus) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get user
		User user = null;
		//no nested iterator --  iterator: any previous:any
		for (User ban : (List<User>)EntityManager.getAllInstancesOf("User"))
		{
			if (ban.getUserId() == userid)
			{
				user = ban;
				break;
			}
				
			
		}
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(user) == true) 
		{ 
			/* Logic here */
			User ban = null;
			ban = (User) EntityManager.createObject("User");
			ban.setUserId(userid);
			ban.setUsername(username);
			ban.setPassword(password);
			ban.setMailbox(mailbox);
			ban.setUserStatus(userstatus);
			EntityManager.addObject("User", ban);
			
			
			refresh();
			// post-condition checking
			if (!(true && 
			ban.getUserId() == userid
			 && 
			ban.getUsername() == username
			 && 
			ban.getPassword() == password
			 && 
			ban.getMailbox() == mailbox
			 && 
			ban.getUserStatus() == userstatus
			 && 
			StandardOPs.includes(((List<User>)EntityManager.getAllInstancesOf("User")), ban)
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
		//string parameters: [username, password, mailbox] 
		//all relevant vars : ban
		//all relevant entities : User
	}  
	
	static {opINVRelatedEntity.put("createUser", Arrays.asList("User"));}
	 
	@SuppressWarnings("unchecked")
	public User queryUser(int userid) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get user
		User user = null;
		//no nested iterator --  iterator: any previous:any
		for (User ban : (List<User>)EntityManager.getAllInstancesOf("User"))
		{
			if (ban.getUserId() == userid)
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
			
			
			refresh();
			// post-condition checking
			if (!(true)) {
				throw new PostconditionException();
			}
			
			refresh(); return user;
		}
		else
		{
			throw new PreconditionException();
		}
	}  
	
	 
	@SuppressWarnings("unchecked")
	public boolean modifyUser(int userid, String username, String password, String mailbox, UserStatus userstatus) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get user
		User user = null;
		//no nested iterator --  iterator: any previous:any
		for (User ban : (List<User>)EntityManager.getAllInstancesOf("User"))
		{
			if (ban.getUserId() == userid)
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
			user.setUserId(userid);
			user.setUsername(username);
			user.setPassword(password);
			user.setMailbox(mailbox);
			user.setUserStatus(userstatus);
			
			
			refresh();
			// post-condition checking
			if (!(user.getUserId() == userid
			 && 
			user.getUsername() == username
			 && 
			user.getPassword() == password
			 && 
			user.getMailbox() == mailbox
			 && 
			user.getUserStatus() == userstatus
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
		//string parameters: [username, password, mailbox] 
		//all relevant vars : user
		//all relevant entities : User
	}  
	
	static {opINVRelatedEntity.put("modifyUser", Arrays.asList("User"));}
	 
	@SuppressWarnings("unchecked")
	public boolean deleteUser(int userid) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get user
		User user = null;
		//no nested iterator --  iterator: any previous:any
		for (User ban : (List<User>)EntityManager.getAllInstancesOf("User"))
		{
			if (ban.getUserId() == userid)
			{
				user = ban;
				break;
			}
				
			
		}
		/* previous state in post-condition*/
 
		/* check precondition */
		if (StandardOPs.oclIsundefined(user) == false && StandardOPs.includes(((List<User>)EntityManager.getAllInstancesOf("User")), user)) 
		{ 
			/* Logic here */
			EntityManager.deleteObject("User", user);
			
			
			refresh();
			// post-condition checking
			if (!(StandardOPs.excludes(((List<User>)EntityManager.getAllInstancesOf("User")), user)
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
		//all relevant vars : user
		//all relevant entities : User
	}  
	
	static {opINVRelatedEntity.put("deleteUser", Arrays.asList("User"));}
	 
	
	
	
	/* temp property for controller */
			
	/* all get and set functions for temp property*/
	
	/* invarints checking*/
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList());
			
}
