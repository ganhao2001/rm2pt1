package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class BookBorrow implements Serializable {
	
	/* all primary attributes */
	private int Id;
	private int Userid;
	private int Bookid;
	private LocalDate BorrowTime;
	private LocalDate ReturnTime;
	private BorrowStatus BorrowStatus;
	
	/* all references */
	private User BookBorrowtoUser; 
	private Book BookBorrowtoBook; 
	private Librarian BookBorrowtoLibrarian; 
	
	/* all get and set functions */
	public int getId() {
		return Id;
	}	
	
	public void setId(int id) {
		this.Id = id;
	}
	public int getUserid() {
		return Userid;
	}	
	
	public void setUserid(int userid) {
		this.Userid = userid;
	}
	public int getBookid() {
		return Bookid;
	}	
	
	public void setBookid(int bookid) {
		this.Bookid = bookid;
	}
	public LocalDate getBorrowTime() {
		return BorrowTime;
	}	
	
	public void setBorrowTime(LocalDate borrowtime) {
		this.BorrowTime = borrowtime;
	}
	public LocalDate getReturnTime() {
		return ReturnTime;
	}	
	
	public void setReturnTime(LocalDate returntime) {
		this.ReturnTime = returntime;
	}
	public BorrowStatus getBorrowStatus() {
		return BorrowStatus;
	}	
	
	public void setBorrowStatus(BorrowStatus borrowstatus) {
		this.BorrowStatus = borrowstatus;
	}
	
	/* all functions for reference*/
	public User getBookBorrowtoUser() {
		return BookBorrowtoUser;
	}	
	
	public void setBookBorrowtoUser(User user) {
		this.BookBorrowtoUser = user;
	}			
	public Book getBookBorrowtoBook() {
		return BookBorrowtoBook;
	}	
	
	public void setBookBorrowtoBook(Book book) {
		this.BookBorrowtoBook = book;
	}			
	public Librarian getBookBorrowtoLibrarian() {
		return BookBorrowtoLibrarian;
	}	
	
	public void setBookBorrowtoLibrarian(Librarian librarian) {
		this.BookBorrowtoLibrarian = librarian;
	}			
	


}
