/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Manager;

import model.Vip;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import videorentalman2.NotPossibleException;

/**
 *
 * @author Khai
 */
public class VipAccountManager extends Manager{
    private JPanel panel_middle;
    private JTextField nameField, mailField, addField;
    private Vector<Vip> object_set;

    public VipAccountManager(String title, String titleText, int width, int height, int x, int y, search.SearchManager Search) {
        super(title, titleText, width, height, x, y, Search);
        object_set = new Vector<Vip>();
    }

    @Override
    protected void createMiddlePanel() {
        panel_middle = new JPanel();
        panel_middle.setLayout(new GridLayout(3, 2));
        //Add name field
        panel_middle.add(new JLabel("Name"));
        nameField = new JTextField();
        panel_middle.add(nameField);
        //Add mail field
        panel_middle.add(new JLabel("Email"));
        mailField = new JTextField();
        panel_middle.add(mailField);
        //Add address field
        panel_middle.add(new JLabel("Address"));
        addField = new JTextField();
        panel_middle.add(addField);
        
        this.gui.getContentPane().add(panel_middle);
    }

    @Override
    public void doTask() throws NotPossibleException {
        try {
            String name = (nameField.getText().length() > 0)
                    ? nameField.getText() : null;
            String mail = (mailField.getText().length() > 0)
                    ? mailField.getText() : null;
            String add = ( addField.getText().length() > 0 )
                    ? addField.getText() : null;
            if (name == null) {
                throw new NotPossibleException("Name must not be empty");
            } else {
                //Create new Vip account and add to list 
                object_set.add(new Vip(name, mail, add));
                System.out.printf("\nVip Account added successfully.");
                //Add vip account created to Search Engine
                this.SearchManager.objectCreated(new Vip(name, mail, add));
                this.objectListeners.add(SearchManager);
                this.displayMessage("Vip Account added successfully", this.getTitle());
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
        System.out.println("Load from Vip.dat -------------------------------------------------------");
        File f = new File("Vip.dat");
        try {
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Vip temp;
            try {
                while (true) {
                    temp = (Vip) ois.readObject();
                    Vip v = new Vip(temp.getName(), temp.getEmail(), temp.getAddress());
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
            System.out.println("Cannot find Vip.dat!");
            this.displayErrorMessage("Cannot find Vip.dat", this.getTitle());
        } catch (ClassNotFoundException e) {
            System.out.println("Problems with file input.");
        } catch (IOException e) {
            System.out.println("Problems with file input.");
        }
    }

    @Override
    public void save() {
        try {
            File f = new File("Vip.dat");
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            for (int i = 0; i < object_set.size(); i++) {
                Vip temp = (Vip) object_set.get(i);
                oos.writeObject(temp);
            }
            oos.close();
            this.displayMessage("File Vip.dat saved!", this.getTitle());
        } catch (IOException e) {
            e.printStackTrace();
            this.displayErrorMessage("Fail to save to file Vip.dat! Something wrong!", this.getTitle());
        }
    }
    public Vector<Vip> getList(){
        return this.object_set;
    }
    
}
