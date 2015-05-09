/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Manager;

import java.awt.GridLayout;
import java.awt.Panel;
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
import videorentalman.NotPossibleException;
import videorentalman.*;
/**
 *
 * @author Khai
 */
public class StandartAccountManager extends Manager{
    private Vector<Standard> object_set;
    private JPanel panel_middle;
    private JTextField nameField, mailField;

    public StandartAccountManager(String title, String titleText, int width, int height, int x, int y) {
        super(title, titleText, width, height, x, y);
        object_set = new Vector<Standard>();
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
        this.gui.getContentPane().add(panel_middle);
    }

    @Override
    public void doTask() throws NotPossibleException {
        try {
            String name = ( nameField.getText().length() > 0 )
                    ? nameField.getText() : null;
            String mail = ( mailField.getText().length() > 0 )
                    ? mailField.getText() : null;
            if( name == null ){
                throw new NotPossibleException("Name must not be empty");
            }else{
                object_set.add(new Standard(name, mail));
                System.out.printf("\nStandard Account added successfully.");
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
        System.out.println("Load from Standard.dat -------------------------------------------------------");
        File f = new File("Standard.dat");
        try {
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Standard temp;
            try {
                while (true) {
                    temp = (Standard) ois.readObject();
                    Standard v = new Standard(temp.getName(),temp.getEmail());
                    this.object_set.add(v);
                    System.out.println(v);
                }
            } catch (EOFException e) {
            }
            ois.close();
        } catch (FileNotFoundException e) {
            System.out.println("Cannot find Standard.dat!");
            this.displayErrorMessage("Cannot find Standard.dat", this.getTitle());
        } catch (ClassNotFoundException e) {
            System.out.println("Problems with file input.");
        } catch (IOException e) {
            System.out.println("Problems with file input.");
        }
    }

    @Override
    public void save() {
        try {
            File f = new File("Standard.dat");
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            for (int i = 0; i < object_set.size(); i++) {
                Standard temp = (Standard) object_set.get(i);
                oos.writeObject(temp);
            }
            oos.close();
            this.displayMessage("File Standard.dat saved!", this.getTitle());
        } catch (IOException e) {
            e.printStackTrace();
            this.displayErrorMessage("Fail to save to file Standard.dat! Something wrong!", this.getTitle());
        }
    }
}
