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

public class RegisterServiceImpl implements RegisterService, Serializable {
	
	
	public static Map<String, List<String>> opINVRelatedEntity = new HashMap<String, List<String>>();
	
	
	ThirdPartyServices services;
			
	public RegisterServiceImpl() {
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
	public boolean inputUser(String username, String password, String email) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
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
		if (StandardOPs.oclIsundefined(user) == true) 
		{ 
			/* Logic here */
			User ban = null;
			ban = (User) EntityManager.createObject("User");
			ban.setUserId(1214);
			ban.setUsername(username);
			ban.setPassword(password);
			ban.setMailbox(email);
			this.setUser(ban);
			this.setVcode("111");
			
			
			refresh();
			// post-condition checking
			if (!(true && 
			ban.getUserId() == 1214
			 && 
			ban.getUsername() == username
			 && 
			ban.getPassword() == password
			 && 
			ban.getMailbox() == email
			 && 
			this.getUser() == ban
			 && 
			this.getVcode().equals("111")
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
		//string parameters: [username, password, email] 
		//all relevant vars : this ban
		//all relevant entities :  User
	}  
	
	static {opINVRelatedEntity.put("inputUser", Arrays.asList("","User"));}
	 
	@SuppressWarnings("unchecked")
	public boolean verification(String code) throws PreconditionException, PostconditionException, ThirdPartyServiceException {
		
		
		/* Code generated for contract definition */
		//Get user
		User user = null;
		this.getUser();
		/* previous state in post-condition*/
 
		/* check precondition */
		if (true) 
		{ 
			/* Logic here */
			if ((this.getVcode().equals(code)))
			{
				EntityManager.addObject("User", user);
				
				refresh();
				// post-condition checking
				if (!(((this.getVcode() == code) ? StandardOPs.includes(((List<User>)EntityManager.getAllInstancesOf("User")), user)
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
			 	if (!(((this.getVcode() == code) ? StandardOPs.includes(((List<User>)EntityManager.getAllInstancesOf("User")), user)
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
		//string parameters: [code] 
		//all relevant vars : user
		//all relevant entities : User
	}  
	
	static {opINVRelatedEntity.put("verification", Arrays.asList("User"));}
	 
	
	
	
	/* temp property for controller */
	private String Vcode;
	private User User;
			
	/* all get and set functions for temp property*/
	public String getVcode() {
		return Vcode;
	}	
	
	public void setVcode(String vcode) {
		this.Vcode = vcode;
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
