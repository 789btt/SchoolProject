package edu.spsu.swe2313.group7.library.model;

import edu.spsu.swe2313.group7.library.dao.UserMapper;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import edu.spsu.swe2313.group7.library.util.PasswordHash;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.apache.log4j.Logger;

/**
 *
 * @author Robert Whitaker
 */
@Entity
@Table(name="USER")
public class User {
	private static final Logger logger = Logger.getLogger(User.class);
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private String lastName;
	private String firstName;
	private Date dateOfBirth;
	private int bookCheckoutLimit;
	private int bookCheckedOutCount;
	private int lateFees;
	private boolean allowedCheckout;
	private UserLevel userLevel;
	private String userName;
	private String passwordHash;

	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="BOOK_ID", referencedColumnName="ID")
	private List<Book> BooksCheckedOut;
	
	public UserLevel getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(UserLevel userLevel) {
		this.userLevel = userLevel;
	}

	public String getUserName() {
		return userName;
	}

	public List<Book> getBooksCheckedOut() {
		return BooksCheckedOut;
	}

	public void setBooksCheckedOut(List<Book> BooksCheckedOut) {
		this.BooksCheckedOut = BooksCheckedOut;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public boolean setPassword(String password) {
		PasswordHash hashFunction = new PasswordHash();
		String hash = hashFunction.passwordToHash(password);
		if (hash != null) {
			this.passwordHash = hash;
			return true;
		}
		return false;
		
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public int getBookCheckoutLimit() {
		return bookCheckoutLimit;
	}

	public void setBookCheckoutLimit(int bookCheckoutLimit) {
		this.bookCheckoutLimit = bookCheckoutLimit;
	}

	public int getBookCheckedOutCount() {
		return bookCheckedOutCount;
	}

	public void setBookCheckedOutCount(int bookCheckedOutCount) {
		this.bookCheckedOutCount = bookCheckedOutCount;
	}

	public int getLateFees() {
		return lateFees;
	}

	public void setLateFees(int lateFees) {
		this.lateFees = lateFees;
	}

	public boolean isAllowedCheckout() {
		return allowedCheckout;
	}

	public void setAllowedCheckout(boolean allowedCheckout) {
		this.allowedCheckout = allowedCheckout;
	}
	
	
	public boolean checkCheckoutStatus() {
		if (!isAllowedCheckout()) {
			logger.debug("User isn't allowed to checkout");
			return false;
		}
		if  (getLateFees() > 0 ) {
			//User owes fees, cannot check out more books
			logger.debug("User has late fees greater than 0");
			return false;
		}
		if (getBookCheckedOutCount() >= this.getBookCheckoutLimit()) {
			//User has checked out too many books
			logger.debug("User has too many books checked out");
			return false;
		}
		//Everything looks good, allow checkout
		logger.debug("User is allowed checkout");
		return true;
		
	}
}
