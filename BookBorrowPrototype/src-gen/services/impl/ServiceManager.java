package services.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import services.*;
	
public class ServiceManager {
	
	private static Map<String, List> AllServiceInstance = new HashMap<String, List>();
	
	private static List<BookBorrowSystem> BookBorrowSystemInstances = new LinkedList<BookBorrowSystem>();
	private static List<ThirdPartyServices> ThirdPartyServicesInstances = new LinkedList<ThirdPartyServices>();
	private static List<RegisterService> RegisterServiceInstances = new LinkedList<RegisterService>();
	private static List<LoginService> LoginServiceInstances = new LinkedList<LoginService>();
	private static List<SearchBookService> SearchBookServiceInstances = new LinkedList<SearchBookService>();
	private static List<BorrowBookService> BorrowBookServiceInstances = new LinkedList<BorrowBookService>();
	private static List<ReturnBookService> ReturnBookServiceInstances = new LinkedList<ReturnBookService>();
	private static List<ManageUserCRUDService> ManageUserCRUDServiceInstances = new LinkedList<ManageUserCRUDService>();
	private static List<ManageBookCRUDService> ManageBookCRUDServiceInstances = new LinkedList<ManageBookCRUDService>();
	private static List<ManageBookBorrowCRUDService> ManageBookBorrowCRUDServiceInstances = new LinkedList<ManageBookBorrowCRUDService>();
	
	static {
		AllServiceInstance.put("BookBorrowSystem", BookBorrowSystemInstances);
		AllServiceInstance.put("ThirdPartyServices", ThirdPartyServicesInstances);
		AllServiceInstance.put("RegisterService", RegisterServiceInstances);
		AllServiceInstance.put("LoginService", LoginServiceInstances);
		AllServiceInstance.put("SearchBookService", SearchBookServiceInstances);
		AllServiceInstance.put("BorrowBookService", BorrowBookServiceInstances);
		AllServiceInstance.put("ReturnBookService", ReturnBookServiceInstances);
		AllServiceInstance.put("ManageUserCRUDService", ManageUserCRUDServiceInstances);
		AllServiceInstance.put("ManageBookCRUDService", ManageBookCRUDServiceInstances);
		AllServiceInstance.put("ManageBookBorrowCRUDService", ManageBookBorrowCRUDServiceInstances);
	} 
	
	public static List getAllInstancesOf(String ClassName) {
			 return AllServiceInstance.get(ClassName);
	}	
	
	public static BookBorrowSystem createBookBorrowSystem() {
		BookBorrowSystem s = new BookBorrowSystemImpl();
		BookBorrowSystemInstances.add(s);
		return s;
	}
	public static ThirdPartyServices createThirdPartyServices() {
		ThirdPartyServices s = new ThirdPartyServicesImpl();
		ThirdPartyServicesInstances.add(s);
		return s;
	}
	public static RegisterService createRegisterService() {
		RegisterService s = new RegisterServiceImpl();
		RegisterServiceInstances.add(s);
		return s;
	}
	public static LoginService createLoginService() {
		LoginService s = new LoginServiceImpl();
		LoginServiceInstances.add(s);
		return s;
	}
	public static SearchBookService createSearchBookService() {
		SearchBookService s = new SearchBookServiceImpl();
		SearchBookServiceInstances.add(s);
		return s;
	}
	public static BorrowBookService createBorrowBookService() {
		BorrowBookService s = new BorrowBookServiceImpl();
		BorrowBookServiceInstances.add(s);
		return s;
	}
	public static ReturnBookService createReturnBookService() {
		ReturnBookService s = new ReturnBookServiceImpl();
		ReturnBookServiceInstances.add(s);
		return s;
	}
	public static ManageUserCRUDService createManageUserCRUDService() {
		ManageUserCRUDService s = new ManageUserCRUDServiceImpl();
		ManageUserCRUDServiceInstances.add(s);
		return s;
	}
	public static ManageBookCRUDService createManageBookCRUDService() {
		ManageBookCRUDService s = new ManageBookCRUDServiceImpl();
		ManageBookCRUDServiceInstances.add(s);
		return s;
	}
	public static ManageBookBorrowCRUDService createManageBookBorrowCRUDService() {
		ManageBookBorrowCRUDService s = new ManageBookBorrowCRUDServiceImpl();
		ManageBookBorrowCRUDServiceInstances.add(s);
		return s;
	}
}	
