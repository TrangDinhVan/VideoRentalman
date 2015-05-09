/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Manager;

import java.awt.GridLayout;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import videorentalman.Contract;
import videorentalman.NotPossibleException;
import videorentalman.*;

/**
 *
 * @author Khai
 */
public class RentalContractManager extends Manager{
    private JPanel panel_middle;
    private JTextField dateField, daysField, addField;
    private JComboBox accountBox, videoBox, statusBox;
    private Vector<Contract> object_set;
    private Vector<Account> account_set = new Vector<Account>();
    private Vector<Video> video_set;

    public RentalContractManager(String title, String titleText, int width, int height, int x, int y, 
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
    
    public void renderBox(){
        //Add Account Box
        panel_middle.add(new JLabel("Choose Account"));
        accountBox = new JComboBox(account_set);
        panel_middle.add(accountBox);
        //Add Video Box
        panel_middle.add(new JLabel("Choose Video"));
        videoBox = new JComboBox(video_set);
        panel_middle.add(this.videoBox);
    }
    
}
