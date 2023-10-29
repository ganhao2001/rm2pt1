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

public class LoginServiceImpl implements LoginService, Serializable {
	
	
	public static Map<String, List<String>> opINVRelatedEntity = new HashMap<String, List<String>>();
	
	
	ThirdPartyServices services;
			
	public LoginServiceImpl() {
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
	public boolean inputUsername(String name) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get user
		User user = null;
		//no nested iterator --  iterator: any previous:any
		for (User ban : (List<User>)EntityManager.getAllInstancesOf("User"))
		{
			if (ban.getUsername().equals(name))
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
			this.setUsername(name);
			
			
			refresh();
			// post-condition checking
			if (!(this.getUsername() == name
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
		//string parameters: [name] 
		//all relevant vars : this
		//all relevant entities : 
	}  
	
	static {opINVRelatedEntity.put("inputUsername", Arrays.asList(""));}
	 
	@SuppressWarnings("unchecked")
	public boolean inputPassword(String password) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get user
		User user = null;
		//no nested iterator --  iterator: any previous:any
		for (User ban : (List<User>)EntityManager.getAllInstancesOf("User"))
		{
			if (ban.getUsername() == this.getUsername())
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
				this.setUser(user);
				
				refresh();
				// post-condition checking
				if (!((user.getPassword() == password ? this.getUser() == user
				 && 
				true : true))) {
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
			 	if (!((user.getPassword() == password ? this.getUser() == user
			 	 && 
			 	true : true))) {
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
		//string parameters: [password] 
		//all relevant vars : this
		//all relevant entities : 
	}  
	
	static {opINVRelatedEntity.put("inputPassword", Arrays.asList(""));}
	 
	
	
	
	/* temp property for controller */
	private String Username;
	private User User;
			
	/* all get and set functions for temp property*/
	public String getUsername() {
		return Username;
	}	
	
	public void setUsername(String username) {
		this.Username = username;
	}
	public User getUser() {
		return User;
	}	
	
	public void setUser(User user) {
		this.User = user;
	}
	
	/* invarints checking*/
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList());
			
}
