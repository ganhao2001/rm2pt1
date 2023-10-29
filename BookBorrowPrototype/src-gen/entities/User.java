package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class User implements Serializable {
	
	/* all primary attributes */
	private int UserId;
	private String Username;
	private String Password;
	private String Mailbox;
	private UserStatus UserStatus;
	
	/* all references */
	private List<BookBorrow> UsertoBookBorrow = new LinkedList<BookBorrow>(); 
	private VerificationCode UsertoVerificationCode; 
	
	/* all get and set functions */
	public int getUserId() {
		return UserId;
	}	
	
	public void setUserId(int userid) {
		this.UserId = userid;
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
	public String getMailbox() {
		return Mailbox;
	}	
	
	public void setMailbox(String mailbox) {
		this.Mailbox = mailbox;
	}
	public UserStatus getUserStatus() {
		return UserStatus;
	}	
	
	public void setUserStatus(UserStatus userstatus) {
		this.UserStatus = userstatus;
	}
	
	/* all functions for reference*/
	public List<BookBorrow> getUsertoBookBorrow() {
		return UsertoBookBorrow;
	}	
	
	public void addUsertoBookBorrow(BookBorrow bookborrow) {
		this.UsertoBookBorrow.add(bookborrow);
	}
	
	public void deleteUsertoBookBorrow(BookBorrow bookborrow) {
		this.UsertoBookBorrow.remove(bookborrow);
	}
	public VerificationCode getUsertoVerificationCode() {
		return UsertoVerificationCode;
	}	
	
	public void setUsertoVerificationCode(VerificationCode verificationcode) {
		this.UsertoVerificationCode = verificationcode;
	}			
	


}
