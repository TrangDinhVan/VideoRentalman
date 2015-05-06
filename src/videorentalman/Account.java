package videorentalman;

/**
 * 
 * An account contain the information about the customer: id, name and email
 * 
 * @attributes
 * 			id      String
 *          name    String
 *          email   String
 *
 *@object  A typical object:  Account a <id,name,email>
 */
public class Account {
	private String id;
	private static int count = 5;
	private String name;
	private String email;
	
	//constructor
	public Account(String n, String e) throws NotPossibleException{
		if(validateString(n) && validateString(e)){
			this.name = n;
			this.email = e;
			count++;
			this.id = "S201" + Integer.toString(count);
		}
		else
			throw new NotPossibleException("Customer: invalid arguments");				
	}
	
	//return the ID
	public String getID(){ return this.id;}
	
	//return the account name
	public String getName(){ return this.name;}
	
	//return the account email
	public String getEmail(){ return this.email;}
	
	//Mutator
	public void setName(String n) throws NotPossibleException{ 
		if(validateString(n))
			this.name = n;
		else
			throw new NotPossibleException("Invalid Name!");
	}
	
	public void setEmail(String e){ 
		if(validateString(e))
			this.email = e;
		else
			throw new NotPossibleException("Invalid Email!");
	}
	
	//validate name
	private boolean validateString(String n){
		if(n.length() < 1 || n == null)
			return false;
		else return true;
	}
	
	private boolean repOK(){
		if(validateString(this.name) && validateString(this.email))
			return true;
		else return false;
	}
	
}
