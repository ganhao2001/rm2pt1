package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class Librarian implements Serializable {
	
	/* all primary attributes */
	private int Id;
	private String Username;
	private String Password;
	
	/* all references */
	private List<BookBorrow> LibrariantoBookBorrow = new LinkedList<BookBorrow>(); 
	
	/* all get and set functions */
	public int getId() {
		return Id;
	}	
	
	public void setId(int id) {
		this.Id = id;
	}
	public String getUsername() {
		return Username;
	}	
	
	public void setUsername(String username) {
		this.Username = username;
	}
	public String getPassword() {
		return Password;
	}	
	
	public void setPassword(String password) {
		this.Password = password;
	}
	
	/* all functions for reference*/
	public List<BookBorrow> getLibrariantoBookBorrow() {
		return LibrariantoBookBorrow;
	}	
	
	public void addLibrariantoBookBorrow(BookBorrow bookborrow) {
		this.LibrariantoBookBorrow.add(bookborrow);
	}
	
	public void deleteLibrariantoBookBorrow(BookBorrow bookborrow) {
		this.LibrariantoBookBorrow.remove(bookborrow);
	}
	


}
