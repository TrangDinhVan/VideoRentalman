package videorentalman2;
//Import domain classes
import Manager.ContractManager;
import Manager.StandartAccountManager;
import Manager.VideoManager;
import Manager.VipAccountManager;
import model.Account;
import model.Standard;
import model.Vip;
//Import neccesary interface libraries
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import java.awt.BorderLayout;
//Inport Extra libraries
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import search.SearchManager;

public class VideoRentalDemo implements ActionListener {
    private JFrame mainGUI;
    private static VideoManager m_video;
    private static StandartAccountManager m_standard;
    private static VipAccountManager m_vip;
    private static ContractManager m_contract;
    private static SearchManager m_search;
    private static final String IMG_PATH_01 = "src/images/01.jpg";
    private static final String IMG_PATH_02 = "src/images/02.jpg";
    public static void main(String [] args) throws IOException{
        VideoRentalDemo app = new VideoRentalDemo();
        //Search Module
        m_search = new SearchManager("Search Functions", "Search by keywords",
                500, 600, 600, 10, null);
        //Start Module Video Manager
        m_video = new VideoManager("Video Manager", "Enter Video Detail", 
                400, 280, 600, 140, m_search);
        m_video.startUp();
        //Start Module Stardard Account Manager
        m_standard = new StandartAccountManager("Standard Account Manager", "Enter Standard Account Detail", 
                400, 280, 600, 140, m_search);
        m_standard.startUp();
        //Staart Module Vip Account Manager
        m_vip = new VipAccountManager("Vip Account Manager", "Enter Vip Account Detail", 
                400, 280, 600, 140, m_search);
        m_vip.startUp();
        //Start Module Rental Contract Manager
        Vector<Account> account_set = new Vector<Account>();
        for( Standard s : m_standard.getList() ){
            account_set.add(s);
        }
        for( Vip v : m_vip.getList() ){
            account_set.add(v);
        }
        m_contract = new ContractManager("Rental Contract Manager", "Enter Contract Detail", 
                600, 380, 600, 40, account_set, m_video.getList(), m_search);
        m_contract.renderBox(account_set,m_video.getList());
        m_contract.startUp();
        //Start App
        app.createGUI();
        app.display();
		
    }
    protected void createGUI() throws IOException{
        initGUI();
        createMenu();
        createMiddle();
    }
    //Initiate main gui for the application
    public void initGUI(){
        mainGUI = new JFrame();
        mainGUI.setSize(400, 220);
        mainGUI.setLocation(400, 200);
        mainGUI.setTitle("Video Rental Management");
        mainGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    //create menu bar
    public void createMenu(){
        //Initiating the comopents of mainGUI
        //main menubar and its components
        JMenuBar menu_main = new JMenuBar();
        //menu Report and its items
        JMenu menu_report = new JMenu("Report");
        JMenuItem item_report1 = new JMenuItem("All Contracts");
        JMenuItem item_report2 = new JMenuItem("Open Contracts");
        JMenuItem item_report3 = new JMenuItem("Sorted Open Contracts");
        //menu Toos and its items
        JMenu menu_tool = new JMenu("Tools");
        JMenuItem m_item_mana_Video = new JMenuItem("Manage Video");
        JMenuItem m_item_mana_Standard = new JMenuItem("Manage Standard Account");
        JMenuItem m_item_mana_Vip = new JMenuItem("Manage Vip Account");
        JMenuItem m_item_mana_Con = new JMenuItem("Manage Rental Contract");
        JMenuItem m_item_mana_Search = new JMenuItem("Search for Object");
        //menu File and its items
        JMenu menu_file = new JMenu("File");
        JMenuItem m_item_save = new JMenuItem("Save");
        JMenuItem m_item_exit = new JMenuItem("Exit");
        //Create Main MENU
        //Menu File
        m_item_exit.addActionListener(this);
        m_item_exit.setMnemonic('E');
        m_item_save.addActionListener(this);
        m_item_save.setMnemonic('S');
        menu_file.add(m_item_save);
        menu_file.add(m_item_exit);
        menu_file.setMnemonic('F');//set shortcut (ATL+F)
        //Menu Tools
        m_item_mana_Video.addActionListener(this);
        m_item_mana_Video.setMnemonic('u');
        m_item_mana_Standard.addActionListener(this);
        m_item_mana_Standard.setMnemonic('C');
        m_item_mana_Vip.addActionListener(this);
        m_item_mana_Vip.setMnemonic('E');
        m_item_mana_Con.addActionListener(this);
        m_item_mana_Con.setMnemonic('C');
        m_item_mana_Search.addActionListener(this);
        m_item_mana_Search.setMnemonic('S');
        menu_tool.add(m_item_mana_Video);
        menu_tool.add(m_item_mana_Standard);
        menu_tool.add(m_item_mana_Vip);
        menu_tool.add(m_item_mana_Con);
        menu_tool.add(m_item_mana_Search);
        menu_tool.setMnemonic('T');
        //Menu Report
        item_report1.addActionListener(this);
        item_report1.setMnemonic('A');
        item_report2.addActionListener(this);
        item_report2.setMnemonic('O');
        item_report3.addActionListener(this);
        item_report3.setMnemonic('S');
        menu_report.add(item_report1);
        menu_report.add(item_report2);
        menu_report.add(item_report3);
        menu_report.setMnemonic('R');
        //add menus to menubar
        menu_main.add(menu_file);
        menu_main.add(menu_tool);
        menu_main.add(menu_report);
        //createLogoPanel
        //add main_menu to mainGUI
        this.mainGUI.add(menu_main, BorderLayout.NORTH);
    }
    //display students' pictures
    public void createMiddle() throws IOException{
        JPanel middle = new JPanel();
        BufferedImage img_01 = ImageIO.read(new File(IMG_PATH_01));
        BufferedImage img_02 = ImageIO.read(new File(IMG_PATH_02));
        middle.add(new JLabel(new ImageIcon(img_01)), new BorderLayout().LINE_START);
        middle.add(new JLabel(new ImageIcon(img_02)), new BorderLayout().LINE_END);
        mainGUI.getContentPane().add(middle, new BorderLayout().CENTER);
    }
    //display the application
    public void display(){
        mainGUI.setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        switch (cmd) {
            case "Save":
                m_video.save();
                m_standard.save();
                m_vip.save();
                m_contract.save();
                break;
            case "Exit":
                shutDown();
                break;
            case "Manage Video":
                m_video.display();
                break;
            case "Manage Standard Account":
                m_standard.display();
                break;
            case "Manage Vip Account": {
                m_vip.display();
                break;
            }
            case "Manage Rental Contract":
                //Start Module Rental Contract Manager
                Vector<Account> account_set = new Vector<Account>();
                for( Standard s : m_standard.getList() ){
                    account_set.add(s);
                }
                for( Vip v : m_vip.getList() ){
                    account_set.add(v);
                }
               m_contract.renderBox(account_set,m_video.getList());
               m_contract.display();
                break;
            case "Search for Object":
                m_search.display();
                break;
            case "All Contracts":
                m_contract.reportAll();
                break;
            case "Open Contracts":
                m_contract.reportOpen();
                break;
            case "Sorted Open Contracts":
                m_contract.reportSortedOpen();
                break;
            default:
                System.out.println("No command found!");
        }
    }
    //Save data and exit app
    private void shutDown() {
        m_video.shutDown();
        m_standard.shutDown();
        m_vip.shutDown();
        m_contract.shutDown();
        mainGUI.dispose();
        System.out.println("Bui Quang Khai - Pham Thi Huong");
        System.exit(0);
    }
}
