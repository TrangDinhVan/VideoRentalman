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
public class VideoManager extends Manager{
    private JPanel panel_middle;
    private JTextField nameField, noField, feeField;
    private Vector<Video> video_set;
    public VideoManager(String title, String titleText, int width, int height, int x, int y) {
        super(title, titleText, width, height, x, y);
        video_set = new Vector<Video>();
    }

    @Override
    protected void createMiddlePanel() {
        panel_middle = new JPanel();
        panel_middle.setLayout(new GridLayout(3, 2));
        //Add field name
        JLabel lb_name = new JLabel("Video Name");
        nameField = new JTextField();
        panel_middle.add(lb_name);
        panel_middle.add(nameField);
        //Add field no.
        panel_middle.add(new JLabel("No. of Disk"));
        noField = new JTextField();
        panel_middle.add(noField);
        //Add field rental fee
        panel_middle.add(new JLabel("Rental Fee"));
        feeField = new JTextField();
        panel_middle.add(feeField);
        
        panel_middle.setOpaque(true);
        this.gui.getContentPane().add(panel_middle);
    }

    @Override
    public void doTask() throws NotPossibleException {
        try {
            String name = ( nameField.getText().length() > 0 ) 
                    ? nameField.getText() 
                    : null;//name
            Integer no = ( noField.getText().length() > 0 )
                    ? Integer.parseInt(noField.getText())
                    : null;//no
            Float fee = ( feeField.getText().length() > 0 )
                    ? Float.parseFloat(feeField.getText())
                    : null;//fee
            if( name == null || no == null || fee == null ){
                throw new NotPossibleException("All field must not be empty\nOr No. Disk and Fee must be a number!");
            }else{
                video_set.add(new Video(name, no, fee));
                System.out.printf("\nVideo added successfully.\nNumber of videos: %d", video_set.size());
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
        System.out.println("Load from Video.dat -------------------------------------------------------");
        File f = new File("Video.dat");
        try {
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Video temp;
            try {
                while (true) {
                    temp = (Video) ois.readObject();
                    Video v = new Video(temp.getName(),temp.getDisk(),temp.getPrice());
                    this.video_set.add(v);
                    System.out.println(v);
                }
            } catch (EOFException e) {
            }
            ois.close();
        } catch (FileNotFoundException e) {
            System.out.println("Cannot find Video.dat!");
            this.displayErrorMessage("Cannot find Video.dat", this.getTitle());
        } catch (ClassNotFoundException e) {
            System.out.println("Problems with file input.");
        } catch (IOException e) {
            System.out.println("Problems with file input.");
        }
    }

    @Override
    public void save() {
        try {
            File f = new File("Video.dat");
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            for (int i = 0; i < video_set.size(); i++) {
                Video temp = (Video) video_set.get(i);
                oos.writeObject(temp);
            }
            oos.close();
            this.displayMessage("File Video.dat saved!", this.getTitle());
        } catch (IOException e) {
            e.printStackTrace();
            this.displayErrorMessage("Fail to save to file Vdieo.dat! Something wrong!", this.getTitle());
        }
    }
    
}
