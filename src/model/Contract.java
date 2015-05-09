package model;

import model.Account;
import java.io.Serializable;

/**
 * 
 * A contract contains the information about the account of the customer;
 * the video, the date, the number of days, the fee and the status(opened or closed)
 *
 */
public class Contract implements Comparable<Contract>, Serializable, Document{
	private Account acc;
	private Video vid;
	private String date;
	private Integer days;
	private float fee;
	private boolean close;
	
	//constructor
	public Contract(Account a, Video v, String date, Integer days, boolean c){
		this.acc=a;
		this.vid=v;
		this.date=date;
		this.days=days;
		this.close=c;
	}
	
	//getter
	public Account getAccount() {return this.acc;}
	public Video getVideo() {return this.vid;}
	public String getDate() {return this.date;}
	public int getDays() {return this.days;}
	public float getFee() {return this.fee = days*vid.getPrice();}
	public boolean getStatus() {return this.close;}
	
	//setter
	public void setAccount(Account a) {this.acc = a;}
	public void setVideo(Video v) {this.vid = v;}
	public void setDate(String d) {this.date = d;}
	public void setDays(Integer d) {this.days = d;}
	public void setStatus(boolean c) {this.close = c;}
	
	@Override
	public String toString(){
		String result = "Contract <AccName: " +acc.getName()+ ", VideoName: " +vid.getName()+ ", RentDate: "
				+getDate()+ ", RentalDays: " +getDays()+ ", Fee: " +getFee();
		if(close) 
			result += ", Status: closed>";
		else
			result += ", Status: opened>";
		return result;
	}
	

	@Override
	public int compareTo(Contract c) throws NullPointerException,ClassCastException{
		if(c == null)
			throw new NullPointerException();
		else if(!(c instanceof Contract))
			throw new ClassCastException("Object is not a contract.");
		else
			return Float.compare(this.fee,c.fee);
	}

    @Override
    public String toHtmlDoc() {
        String ContractDoc;
        ContractDoc = "<html>";
        ContractDoc += "<head><title>Contract:" + getAccount().getName() + " - " + getVideo().getName() + "</title></head>";
        ContractDoc += "<body>";
        ContractDoc += getAccount().toString() + " " + getVideo().toString();
        ContractDoc += "</body></html>";
        return ContractDoc;
    }
}
