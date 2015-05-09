package videorentalman2;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.TableColumn;
import search.EasyTable;
import model.Account;
import model.Contract;
import videorentalman2.DuplicateException;
import model.Video;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VideoRentalManager {
    private List<Contract> conlist= new ArrayList<Contract>();
    private static int count = 0;

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
    /**
     * @effect display the report of sorted RAW enrollment list
     */
    public void reportTable() {
        sort();
        JFrame ReportWindow = new JFrame();
        ReportWindow.setSize(500, 450);
        ReportWindow.setLocation(450, 250);
        ReportWindow.setTitle("All Contracts Report");
        ReportWindow.setLayout(new BorderLayout());
        ReportWindow.addWindowListener(new WindowEventHandler());

        String[] headers = {"No", "Account", "Video", "Date", "Days","Fee","Status"};
        List<List> data = new LinkedList();
        for (int i = 0; i < this.conlist.size(); i++) {
            List listObject = new LinkedList();
            Contract temp = (Contract) this.conlist.get(i);
            listObject.add(count);
            listObject.add(temp.getAccount().getName());
            listObject.add(temp.getVideo().getName());
            listObject.add(temp.getDate());
            listObject.add(temp.getDays());
            listObject.add(temp.getFee());
            listObject.add(temp.getStatus());
            data.add(listObject);
        }
        EasyTable report = new EasyTable(data, headers);
        //set width for the column of NO. so that it is smaller
        TableColumn column0 = report.getColumnModel().getColumn(0);
        column0.setPreferredWidth(5);

        JLabel report_title = new JLabel("All Contracts");
        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());
        main.add(report_title, BorderLayout.NORTH);
        main.add(new JScrollPane(report), BorderLayout.CENTER);
        ReportWindow.add(main, BorderLayout.CENTER);
        ReportWindow.setVisible(true);
    }

    class WindowEventHandler extends WindowAdapter {

        public void windowClosing(WindowEvent evt) {
            count = 0;
        }
    }
}
