package videorentalman;

import java.util.*;
import videorentalman.Account;
import videorentalman.Contract;
import videorentalman.DuplicateException;
import videorentalman.Video;

public class VideoRentalManager {
    private List<Contract> conlist= new ArrayList<Contract>();

    public VideoRentalManager(){ 
        //do nothing
    }

    //add a new contract
    public void addContract(Contract c) throws DuplicateException{		
        //check duplicate object before adding
        Contract con;
        Iterator iter = conlist.iterator();
        while(iter.hasNext()){
            con = (Contract) iter.next();
            if(c.getAccount().equals(con.getAccount()) && c.getVideo().equals(con.getVideo()))
                throw new DuplicateException("VideoRentalManager.addContract: Contract is already added");
        }
        conlist.add(c);
    }

    //get the contract with specific account and video
    public Contract getContract(Account a, Video v){
        Contract c;
        Iterator iter = conlist.iterator();
        while(iter.hasNext()){
            c = (Contract) iter.next();
            if(c.getAccount().equals(a) && c.getVideo().equals(v)){
                return c;
            }		
        }

        return null;
    }

    //set the contract info
    public void setContractInfo(Account a, Video v, String date, Integer days){
        boolean flag = false;
        Contract c;              
        Iterator iter = conlist.iterator();
        while(iter.hasNext()){
            c = (Contract) iter.next();
            if(c.getAccount().equals(a) && c.getVideo().equals(v)){
                c.setDate(date);
                c.setDays(days);
                flag = true;
                break;
            }
        }

        if(flag) System.out.println("Set date and days successfully!");
        else System.out.println("No contract matching the account and video!");
    }

    //set the status of the contract to closed
    public void closeContract(Contract c){
        c.setStatus(true);
    }

    //return all the current contract
    public String reportAll(){
        String result="=====================ALL CONTRACTS===================== \n";
        Iterator iter = conlist.iterator();
        while(iter.hasNext()){
            Contract c = (Contract) iter.next();
            result += c.toString() + "\n";
        }
        return result +="======================================================";		
    }

    //return all the opened contract
    public String reportOpenContract(){
        String result="=====================OPENED CONTRACT======================= \n";
        Iterator iter = conlist.iterator();
        while(iter.hasNext()){
            Contract c = (Contract) iter.next();
            if(!c.getStatus())
            result += c.toString() + "\n";
        }
        return result +="==========================================================";		
    }

    //sort the contracts as the fee in descending order
    public void sort(){
        Collections.sort(conlist, Collections.reverseOrder());
    }
}
