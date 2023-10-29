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

public class BookBorrowSystemImpl implements BookBorrowSystem, Serializable {
	
	
	public static Map<String, List<String>> opINVRelatedEntity = new HashMap<String, List<String>>();
	
	
	ThirdPartyServices services;
			
	public BookBorrowSystemImpl() {
		services = new ThirdPartyServicesImpl();
	}

	public void refresh() {
		RegisterService registerservice_service = (RegisterService) ServiceManager
				.getAllInstancesOf("RegisterService").get(0);
		LoginService loginservice_service = (LoginService) ServiceManager
				.getAllInstancesOf("LoginService").get(0);
		SearchBookService searchbookservice_service = (SearchBookService) ServiceManager
				.getAllInstancesOf("SearchBookService").get(0);
		BorrowBookService borrowbookservice_service = (BorrowBookService) ServiceManager
				.getAllInstancesOf("BorrowBookService").get(0);
		ReturnBookService returnbookservice_service = (ReturnBookService) ServiceManager
				.getAllInstancesOf("ReturnBookService").get(0);
		ManageUserCRUDService manageusercrudservice_service = (ManageUserCRUDService) ServiceManager
				.getAllInstancesOf("ManageUserCRUDService").get(0);
		ManageBookCRUDService managebookcrudservice_service = (ManageBookCRUDService) ServiceManager
				.getAllInstancesOf("ManageBookCRUDService").get(0);
		ManageBookBorrowCRUDService managebookborrowcrudservice_service = (ManageBookBorrowCRUDService) ServiceManager
				.getAllInstancesOf("ManageBookBorrowCRUDService").get(0);
	}			
	
	/* Generate buiness logic according to functional requirement */
	
	
	
	/* temp property for controller */
			
	/* all get and set functions for temp property*/
	
	/* invarints checking*/
	public final static ArrayList<String> allInvariantCheckingFunction = new ArrayList<String>(Arrays.asList());
			
}
