/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Manager;

import model.Account;
import model.Video;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;
import search.EasyTable;
import model.Contract;
import videorentalman2.NotPossibleException;

/**
 *
 * @author Khai
 */
public class ContractManager extends Manager{
    private JPanel panel_middle;
    private JTextField dateField, daysField, addField;
    private JComboBox accountBox, videoBox, statusBox;
    private Vector<Contract> object_set;
    private List<Contract> conlist= new ArrayList<Contract>();
    private Vector<Account> account_set = new Vector<Account>();
    private Vector<Video> video_set;
    private static int count = 0;

    public ContractManager(String title, String titleText, int width, int height, int x, int y, 
            Vector<Account> v_account, Vector<Video> v_video, search.SearchManager Search) {
        super(title, titleText, width, height, x, y, Search);
        object_set = new Vector<Contract>();
        account_set = v_account;
        video_set = v_video;
    }


    @Override
    protected void createMiddlePanel() {
        panel_middle = new JPanel();
        panel_middle.setLayout(new GridLayout(6, 5));
        //Add Account Box
        panel_middle.add(new JLabel("Choose Account"));
        accountBox = new JComboBox();
        panel_middle.add(accountBox);
        //Add Video Box
        panel_middle.add(new JLabel("Choose Video"));
        videoBox = new JComboBox();
        panel_middle.add(this.videoBox);
        //Add date field
        panel_middle.add(new JLabel("Date"));
        dateField = new JTextField();
        panel_middle.add(dateField);
        //Add days field
        panel_middle.add(new JLabel("Days"));
        daysField = new JTextField();
        panel_middle.add(daysField);
        //Add status Box
        Vector<Boolean> status = new Vector<Boolean>();
        status.add(Boolean.TRUE);
        status.add(Boolean.FALSE);
        panel_middle.add(new JLabel("Closed"));
        statusBox = new JComboBox(status);
        panel_middle.add(statusBox);
        this.gui.getContentPane().add(panel_middle);
        
    }

    @Override
    public void doTask() throws NotPossibleException {
        try {
            Object a = accountBox.getSelectedItem();
            Object v = videoBox.getSelectedItem();
            Object status = statusBox.getSelectedItem();
            if( dateField.getText().length() < 1 
                    || daysField.getText().length() < 1 ){
                throw new NotPossibleException("Don't leave Date and Days empty!");
            }else{
                //Create contract and add to the list
                String date = dateField.getText();
                int days = Integer.parseInt(daysField.getText());
                object_set.add(new Contract((Account) a, (Video) v, date, days, (Boolean) status));
                System.out.println("Contact added successfully.");
                //Add to Search Engine
                this.SearchManager.objectCreated(new Contract((Account) a, (Video) v, date, days, (Boolean) status));
                this.objectListeners.add(SearchManager);
                this.displayMessage("Contact added successfully.", this.getTitle());
                this.gui.setVisible(false);
            }
        } catch (Exception e) {
            throw new NotPossibleException(e.getMessage());
        }
    }

    @Override
    public void clearGUI() {
        this.clearGUI(this.panel_middle);
    }

    @Override
    public void startUp() {
        System.out.println("Load from Contract.dat -------------------------------------------------------");
        File f = new File("Contract.dat");
        try {
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Contract temp;
            try {
                while (true) {
                    temp = (Contract) ois.readObject();
                    Contract v = new Contract(temp.getAccount(), temp.getVideo(), temp.getDate(), temp.getDays(), temp.getStatus());
                    this.object_set.add(v);
                    //Add to Search Engine
                    this.SearchManager.objectCreated(v);
                    this.objectListeners.add(SearchManager);
                    System.out.println(v);
                }
            } catch (EOFException e) {
            }
            ois.close();
        } catch (FileNotFoundException e) {
            System.out.println("Cannot find Contract.dat!");
            this.displayErrorMessage("Cannot find Contract.dat", this.getTitle());
        } catch (ClassNotFoundException e) {
            System.out.println("Problems with file input.");
        } catch (IOException e) {
            System.out.println("Problems with file input.");
        }
    }

    @Override
    public void save() {
        try {
            File f = new File("Contract.dat");
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            for (int i = 0; i < object_set.size(); i++) {
                Contract temp = (Contract) object_set.get(i);
                oos.writeObject(temp);
            }
            oos.close();
            this.displayMessage("File Contract.dat saved!", this.getTitle());
        } catch (IOException e) {
            e.printStackTrace();
            this.displayErrorMessage("Fail to save to file Contract.dat! Something wrong!", this.getTitle());
        }
    }
    //Add data of video and account to the combobox when create a new contract
    public void renderBox(Vector<Account> v1, Vector<Video> v2){
        //Reset
        accountBox.removeAllItems();
        videoBox.removeAllItems();
        for( Account a : v1 ){
            accountBox.addItem(a);
        }
        for( Video v : v2 ){
            videoBox.addItem(v);
        }
    }
    //Conter vector to arraylist of contracts
    protected void convert(){
        //Clear the list first
        conlist.clear();
        for( Contract c : object_set ){
            conlist.add(c);
        }
    }
    
    public void sort() {
        convert();
        Collections.sort(conlist, Collections.reverseOrder());
    }
    //Display the report of All contract
    public void reportAll() {
        convert();
        JFrame ReportWindow = new JFrame();
        ReportWindow.setSize(600, 350);
        ReportWindow.setLocation(450, 50);
        ReportWindow.setTitle("All Contracts Report");
        ReportWindow.setLayout(new BorderLayout());
        ReportWindow.addWindowListener(new ContractManager.WindowEventHandler());

        String[] headers = {"No", "Account", "Video", "Date", "Days", "Fee", "Status"};
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

    public void reportOpen() {
        convert();
        JFrame ReportWindow = new JFrame();
        ReportWindow.setSize(600, 350);
        ReportWindow.setLocation(450, 50);
        ReportWindow.setTitle("Open Contracts Report");
        ReportWindow.setLayout(new BorderLayout());
        ReportWindow.addWindowListener(new ContractManager.WindowEventHandler());

        String[] headers = {"No", "Account", "Video", "Date", "Days", "Fee", "Status"};
        List<List> data = new LinkedList();
        for (int i = 0; i < this.conlist.size(); i++) {
            List listObject = new LinkedList();
            Contract temp = (Contract) this.conlist.get(i);
            if( temp.getStatus() == false ){
                listObject.add(count);
                listObject.add(temp.getAccount().getName());
                listObject.add(temp.getVideo().getName());
                listObject.add(temp.getDate());
                listObject.add(temp.getDays());
                listObject.add(temp.getFee());
                listObject.add(temp.getStatus());
                data.add(listObject);
            }
        }
        EasyTable report = new EasyTable(data, headers);
        //set width for the column of NO. so that it is smaller
        TableColumn column0 = report.getColumnModel().getColumn(0);
        column0.setPreferredWidth(5);

        JLabel report_title = new JLabel("Open Contracts");
        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());
        main.add(report_title, BorderLayout.NORTH);
        main.add(new JScrollPane(report), BorderLayout.CENTER);
        ReportWindow.add(main, BorderLayout.CENTER);
        ReportWindow.setVisible(true);
    }

    public void reportSortedOpen() {
        sort();
        JFrame ReportWindow = new JFrame();
        ReportWindow.setSize(600, 350);
        ReportWindow.setLocation(450, 50);
        ReportWindow.setTitle("Open Sorted Contracts Report");
        ReportWindow.setLayout(new BorderLayout());
        ReportWindow.addWindowListener(new ContractManager.WindowEventHandler());

        String[] headers = {"No", "Account", "Video", "Date", "Days", "Fee", "Status"};
        List<List> data = new LinkedList();
        for (int i = 0; i < this.conlist.size(); i++) {
            List listObject = new LinkedList();
            Contract temp = (Contract) this.conlist.get(i);
            if (temp.getStatus() == false) {
                listObject.add(count);
                listObject.add(temp.getAccount().getName());
                listObject.add(temp.getVideo().getName());
                listObject.add(temp.getDate());
                listObject.add(temp.getDays());
                listObject.add(temp.getFee());
                listObject.add(temp.getStatus());
                data.add(listObject);
            }
        }
        EasyTable report = new EasyTable(data, headers);
        //set width for the column of NO. so that it is smaller
        TableColumn column0 = report.getColumnModel().getColumn(0);
        column0.setPreferredWidth(5);

        JLabel report_title = new JLabel("Open Sorted Contracts");
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
