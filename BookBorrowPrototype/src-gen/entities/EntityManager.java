package entities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.lang.reflect.Method;
import java.util.Map;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.File;

public class EntityManager {

	private static Map<String, List> AllInstance = new HashMap<String, List>();
	
	private static List<User> UserInstances = new LinkedList<User>();
	private static List<Book> BookInstances = new LinkedList<Book>();
	private static List<BookBorrow> BookBorrowInstances = new LinkedList<BookBorrow>();
	private static List<Librarian> LibrarianInstances = new LinkedList<Librarian>();
	private static List<VerificationCode> VerificationCodeInstances = new LinkedList<VerificationCode>();

	
	/* Put instances list into Map */
	static {
		AllInstance.put("User", UserInstances);
		AllInstance.put("Book", BookInstances);
		AllInstance.put("BookBorrow", BookBorrowInstances);
		AllInstance.put("Librarian", LibrarianInstances);
		AllInstance.put("VerificationCode", VerificationCodeInstances);
	} 
		
	/* Save State */
	public static void save(File file) {
		
		try {
			
			ObjectOutputStream stateSave = new ObjectOutputStream(new FileOutputStream(file));
			
			stateSave.writeObject(UserInstances);
			stateSave.writeObject(BookInstances);
			stateSave.writeObject(BookBorrowInstances);
			stateSave.writeObject(LibrarianInstances);
			stateSave.writeObject(VerificationCodeInstances);
			
			stateSave.close();
					
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/* Load State */
	public static void load(File file) {
		
		try {
			
			ObjectInputStream stateLoad = new ObjectInputStream(new FileInputStream(file));
			
			try {
				
				UserInstances =  (List<User>) stateLoad.readObject();
				AllInstance.put("User", UserInstances);
				BookInstances =  (List<Book>) stateLoad.readObject();
				AllInstance.put("Book", BookInstances);
				BookBorrowInstances =  (List<BookBorrow>) stateLoad.readObject();
				AllInstance.put("BookBorrow", BookBorrowInstances);
				LibrarianInstances =  (List<Librarian>) stateLoad.readObject();
				AllInstance.put("Librarian", LibrarianInstances);
				VerificationCodeInstances =  (List<VerificationCode>) stateLoad.readObject();
				AllInstance.put("VerificationCode", VerificationCodeInstances);
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
	/* create object */  
	public static Object createObject(String Classifer) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method createObjectMethod = c.getDeclaredMethod("create" + Classifer + "Object");
			return createObjectMethod.invoke(c);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/* add object */  
	public static Object addObject(String Classifer, Object ob) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method addObjectMethod = c.getDeclaredMethod("add" + Classifer + "Object", Class.forName("entities." + Classifer));
			return  (boolean) addObjectMethod.invoke(c, ob);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}	
	
	/* add objects */  
	public static Object addObjects(String Classifer, List obs) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method addObjectsMethod = c.getDeclaredMethod("add" + Classifer + "Objects", Class.forName("java.util.List"));
			return  (boolean) addObjectsMethod.invoke(c, obs);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/* Release object */
	public static boolean deleteObject(String Classifer, Object ob) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method deleteObjectMethod = c.getDeclaredMethod("delete" + Classifer + "Object", Class.forName("entities." + Classifer));
			return  (boolean) deleteObjectMethod.invoke(c, ob);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/* Release objects */
	public static boolean deleteObjects(String Classifer, List obs) {
		try
		{
			Class c = Class.forName("entities.EntityManager");
			Method deleteObjectMethod = c.getDeclaredMethod("delete" + Classifer + "Objects", Class.forName("java.util.List"));
			return  (boolean) deleteObjectMethod.invoke(c, obs);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}		 	
	
	 /* Get all objects belongs to same class */
	public static List getAllInstancesOf(String ClassName) {
			 return AllInstance.get(ClassName);
	}	

   /* Sub-create object */
	public static User createUserObject() {
		User o = new User();
		return o;
	}
	
	public static boolean addUserObject(User o) {
		return UserInstances.add(o);
	}
	
	public static boolean addUserObjects(List<User> os) {
		return UserInstances.addAll(os);
	}
	
	public static boolean deleteUserObject(User o) {
		return UserInstances.remove(o);
	}
	
	public static boolean deleteUserObjects(List<User> os) {
		return UserInstances.removeAll(os);
	}
	public static Book createBookObject() {
		Book o = new Book();
		return o;
	}
	
	public static boolean addBookObject(Book o) {
		return BookInstances.add(o);
	}
	
	public static boolean addBookObjects(List<Book> os) {
		return BookInstances.addAll(os);
	}
	
	public static boolean deleteBookObject(Book o) {
		return BookInstances.remove(o);
	}
	
	public static boolean deleteBookObjects(List<Book> os) {
		return BookInstances.removeAll(os);
	}
	public static BookBorrow createBookBorrowObject() {
		BookBorrow o = new BookBorrow();
		return o;
	}
	
	public static boolean addBookBorrowObject(BookBorrow o) {
		return BookBorrowInstances.add(o);
	}
	
	public static boolean addBookBorrowObjects(List<BookBorrow> os) {
		return BookBorrowInstances.addAll(os);
	}
	
	public static boolean deleteBookBorrowObject(BookBorrow o) {
		return BookBorrowInstances.remove(o);
	}
	
	public static boolean deleteBookBorrowObjects(List<BookBorrow> os) {
		return BookBorrowInstances.removeAll(os);
	}
	public static Librarian createLibrarianObject() {
		Librarian o = new Librarian();
		return o;
	}
	
	public static boolean addLibrarianObject(Librarian o) {
		return LibrarianInstances.add(o);
	}
	
	public static boolean addLibrarianObjects(List<Librarian> os) {
		return LibrarianInstances.addAll(os);
	}
	
	public static boolean deleteLibrarianObject(Librarian o) {
		return LibrarianInstances.remove(o);
	}
	
	public static boolean deleteLibrarianObjects(List<Librarian> os) {
		return LibrarianInstances.removeAll(os);
	}
	public static VerificationCode createVerificationCodeObject() {
		VerificationCode o = new VerificationCode();
		return o;
	}
	
	public static boolean addVerificationCodeObject(VerificationCode o) {
		return VerificationCodeInstances.add(o);
	}
	
	public static boolean addVerificationCodeObjects(List<VerificationCode> os) {
		return VerificationCodeInstances.addAll(os);
	}
	
	public static boolean deleteVerificationCodeObject(VerificationCode o) {
		return VerificationCodeInstances.remove(o);
	}
	
	public static boolean deleteVerificationCodeObjects(List<VerificationCode> os) {
		return VerificationCodeInstances.removeAll(os);
	}
  
}

