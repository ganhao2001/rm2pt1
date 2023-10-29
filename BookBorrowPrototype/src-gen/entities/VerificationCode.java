package entities;

import services.impl.StandardOPs;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.io.Serializable;
import java.lang.reflect.Method;

public class VerificationCode implements Serializable {
	
	/* all primary attributes */
	private String Mailbox;
	private String Code;
	
	/* all references */
	
	/* all get and set functions */
	public String getMailbox() {
		return Mailbox;
	}	
	
	public void setMailbox(String mailbox) {
		this.Mailbox = mailbox;
	}
	public String getCode() {
		return Code;
	}	
	
	public void setCode(String code) {
		this.Code = code;
	}
	
	/* all functions for reference*/
	


}
