package videorentalman;

public class Vip extends Account{
	private String address;
	
	//constructor
	public Vip(String n, String e, String a){
		super(n,e);
		this.address = a;
	}
	
	public String getAddress(){
		return this.address;
	}
	
	public void setAddress(String a){
		this.address = a;
	}
	
	@Override
	public String toString(){
		return "Account Vip: <" +super.getID() +","+ super.getName() +","+ super.getEmail()
				+","+ getAddress() +">";
	}

}
