/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Manager;
import videorentalman.NotPossibleException;

import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import search.SearchManager;
/**
 *
 * @author Khai
 */
public abstract class Manager implements ActionListener {
    private String title;
    private String titleText;
    private int width, height;
    private int x, y;
    public JFrame gui;
    private Dimension dim;
    private Point location;
    private JPanel p_middle;
    protected SearchManager SearchManager;
    public List<SearchManager> objectListeners;
    //Init with a basic GUI
    public Manager(String title, String titleText, int width, int height, int x, int y, SearchManager Search){
        this.title = title;
        this.titleText = titleText;
        this.dim = new Dimension(width, height);
        this.location = new Point(x, y);
        this.SearchManager = Search;
        objectListeners = new LinkedList();
        objectListeners.add(Search);
        createGUI();
    }
    //Create GUI
    public void createGUI(){
        initGUI();
        createFileMenu();
        createTopPanel();
        createMiddlePanel();
        createBottomPanel();
    }
    //Init Basic GUI components
    public void initGUI(){
        this.gui = new JFrame();
        this.gui.setTitle(this.title);
        this.gui.setSize(this.dim);
        this.gui.setLocation(this.location);
        this.gui.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }
    //Creat Menu FILE
    private void createFileMenu() {
        // TODO: complete this code
        JMenuBar menu_main = new JMenuBar();
        JMenu menu_file = new JMenu("File");
        JMenuItem menu_item_exit = new JMenuItem("Exit");
        menu_item_exit.addActionListener(this);
        menu_file.add(menu_item_exit);
        menu_main.add(menu_file);
        this.gui.setJMenuBar(menu_main);
    }
    
    private void createTopPanel() {
        JPanel panel_top = new JPanel();
        JLabel label_top = new JLabel(titleText);
        label_top.setFont(new Font("Caribi", Font.BOLD, 15));
        panel_top.add(label_top);
        this.gui.add(panel_top, BorderLayout.NORTH);
    }
    
    protected abstract void createMiddlePanel();
    
    private void createBottomPanel() {
        // TODO: complete this code    
        JPanel panel_bottom = new JPanel();
        JButton btn_ok, btn_cancel;
        btn_ok = new JButton("OK");
        btn_ok.addActionListener(this);
        btn_cancel = new JButton("Cancel");
        btn_cancel.addActionListener(this);
        panel_bottom.add(btn_ok);
        panel_bottom.add(btn_cancel);
        this.gui.getRootPane().setDefaultButton(btn_ok);//set default button for ENTER key
        this.gui.getContentPane().add(panel_bottom, BorderLayout.SOUTH);
    }
    
    /**
     * @requires <tt>gui != null</tt>
     * @effects show <tt>gui</tt>
     */
    public void display() {
        this.gui.setVisible(true);
    }

    /**
     * @effects return title
     */
    public String getTitle() {
        return title;
    }
    
    protected void displayMessage(String mesg, String title) {
        JOptionPane.showMessageDialog(gui, mesg, title,
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * @requires <tt>gui != null</tt>
     * @effects display on <tt>gui</tt> an error message dialog whose title is
     * <tt>title</tt>, and whose message is <tt>mesg</tt>
     */
    protected void displayErrorMessage(String mesg, String title) {
        JOptionPane.showMessageDialog(gui, mesg, title, JOptionPane.ERROR_MESSAGE);
    }
    public abstract void doTask() throws NotPossibleException;
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("Exit")) {
            exit();
        } else if (cmd.equals("OK")) {
            try {
                doTask();
            } catch (NotPossibleException s) {
                displayErrorMessage(s.getMessage(), this.getTitle());
            }
        } else if (cmd.equals("Cancel")) {
            // clear the GUI to enter again
            clearGUI();
        }// add others here...
    }
    
    protected void clearGUI(JPanel panel) {
        Component[] comps = panel.getComponents();
        Component comp;
        JTextField tf;

        for (int i = 0; i < comps.length; i++) {
            comp = comps[i];
            if (comp instanceof JTextField) {
                // found a text field
                tf = (JTextField) comp;

                // only clear the enabled text fields
                if (tf.isEnabled()) {
                    tf.setText("");
                }
            } else if (comp instanceof JPanel) {
                clearGUI((JPanel) comp);
            }
        }
    }
    //Clear GUI when click Cancel button
    public abstract void clearGUI();
    //Load data from file when start
    public abstract void startUp();
    //Save data to file when close
    public abstract void save();
    //Close form
    public void exit() {
        clearGUI();
        gui.setVisible(false);
    }
    //Shutdown form
    public void shutDown() {
        gui.dispose();
    }
}
