As a Customer, I want to regist, so that I can login
{
	Basic Flow {
		(User) 1. the Customer shall input information of user.
		(System) 2. when end of input, the BookBorrowSystem shall send verification code.//function
		(User) 3. the Customer shall input verification code.
		(System) 4. when end of input, the BookBorrowSystem shall verify.//function
		(System) 5. when end of verification, the BookBorrowSystem shall records information of user and send it into database.//function
	}
}

As a Customer, I want to log in, so that I can use BookBorrowSystem
{
	Basic Flow {
		(User) 1. the Customer shall enter a starting page.
		(User) 2. the Customer shall log in using username and psaaword.
		(System) 3. when Customer log in, the BookBorrowSystem shall verify username and password.//function
		(System) 4. when end of verification completed successfully, the BookBorrowSystem shall display search homepage.//function
	}
	Alternative Flow {
		A. At any time, System verification fails : 
		  1.  BookBorrowSystem pops up a window and explains reasons for verification failure.
		  2.  BookBorrowSystem return to a starting page.
	}
}

As a Customer, I want to search books, so that I can know  information of books
{
	Basic Flow {
		(User) 1. the Customer shall click search bar after log in.
		(User) 2. the Customer shall input title of book.
		(System) 3. when end of input, the BookBorrowSystem shall search for related books by title of book.//function
		(System) 4. when end of search books, the BookBorrowSystem shall return a list of related books with some information.//function
		(User) 5. the Customer shall view all search results and select a desired book.
		(System) 6. when end of select, the BookBorrowSystem shall search for all content of this book.//function
		(System) 7. when end of search content, the BookBorrowSystem shall a return a page to present content.//function
	}
}

As a Customer, I want to borrow a book, so that I can read it
{
	Basic Flow {
		(User) 1. the Customer shall click on borrowing button.
		(System) 2. when end of click on borrowing button, the BookBorrowSystem shall display a page to collect user information.//function
		(User) 3. the Customer shall select a time period for borrowing.
		(System) 4. when end of select,  the BookBorrowSystem shall handle borrowing.//function
		(System) 5. when completed borrowing, the BookBorrowSystem shall renturn a page to show information of borrowing.//function
		(User) 6. the Customer shall confirm borrowing information.
		(System) 7. when end of confirm, the BookBorrowSystem shall records relevant information and inputs it into database.//function
		(System) 8. when end of record, the BookBorrowSystem shall return to search homepage.//function
	}
}
As a Customer, I want to return book, so that I can end borrowing process
{
	Basic Flow {
		(User) 1. the Customer shall arrives at front desk of library with book.
		(User) 2. the librarian shall start a return process.
		(System) 3. when starts a return process, the BookBorrowSystem shall presents a return page.//function
		(User) 4. the Customer shall tell all information about book and their information.
		(User) 5. the  librarian shall input book name and username of Customer.
		(System) 6. when end of input, the BookBorrowSystem shall verify relevant information.//function
		(System) 7. when end of verification, the BookBorrowSystem shall present a information page for returning books.//function
		(User) 8. the Customer shall return book.
		(User) 9. the librarian shall confirm book and completed a return step in BookBorrowSystem.
		(System) 10. when end of return step, the BookBorrowSystem shall records relevant information and inputs it into database.//function
	}
}

