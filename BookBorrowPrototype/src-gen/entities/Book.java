package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class Book implements Serializable {
	
	/* all primary attributes */
	private int BookId;
	private String Booktitle;
	private String Authors;
	private BookStatus BookStatus;
	
	/* all references */
	private BookBorrow BooktoBookBorrow; 
	
	/* all get and set functions */
	public int getBookId() {
		return BookId;
	}	
	
	public void setBookId(int bookid) {
		this.BookId = bookid;
	}
	public String getBooktitle() {
		return Booktitle;
	}	
	
	public void setBooktitle(String booktitle) {
		this.Booktitle = booktitle;
	}
	public String getAuthors() {
		return Authors;
	}	
	
	public void setAuthors(String authors) {
		this.Authors = authors;
	}
	public BookStatus getBookStatus() {
		return BookStatus;
	}	
	
	public void setBookStatus(BookStatus bookstatus) {
		this.BookStatus = bookstatus;
	}
	
	/* all functions for reference*/
	public BookBorrow getBooktoBookBorrow() {
		return BooktoBookBorrow;
	}	
	
	public void setBooktoBookBorrow(BookBorrow bookborrow) {
		this.BooktoBookBorrow = bookborrow;
	}			
	


}
