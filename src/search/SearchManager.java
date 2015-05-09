package search;

import Manager.Manager;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import kengine.Doc;
import kengine.DocCnt;
import javax.swing.table.TableColumn;
import model.Document;
import videorentalman2.NotPossibleException;

/**
 * @overview A sub-type of AppController that is responsible for handling object
 * search function. It is also a sub-type of ObjectListener to listen to object
 * manipulation events.
 *
 * @attributes engine ObjectEngine
 *
 * @abstract_properties  <pre>
 *    P_AppController /\
 *    optional(engine) = false /\
 * (A):
 *    gui.contentPane.components[1].components = [ kwdPanel:panel<>, resultPane:scrollpane<>> ] /\
 *    kwdPanel.components = [ kwds:textfield<> ] /\
 *    resultPane.components = [ result:easytable<["No", "Objects", "Rank"] ] /\
 *    size(engine.objectTable) > 0 ->
 *      (for each <d,o> in engine.objectTable:
 *        Student(o) \/ Module(o) \/ Enrolment(o)) /\
 *    size(result) > 0 ->
 *      length(kwds.text) > 0 /\
 *      (for each row(r) in result.
 *        i = rowindex(r) /\ <d,c> = matchIterator(query(engine,kwds.text)).next /\ <d,o> in engine.objectTable /\
 *        r = <i,o,c>)
 * </pre>
 *
 * i.e. (A): gui's middle panel contains two components: a panel with a text
 * field named kwds, and a scroll pane named resultPane containing a table named
 * result that has 3 columns: "No", "Objects", "Rank" /\ there are entries
 * &lt;d,o&gt; in engine.objectTable -> o is one of the domain objects of the
 * application: Student, Module, or Enrolment /\ there are rows in table result
 * -> kwds is not empty /\ result.rows lists the objects whose, HTML documents
 * match with kwds, and their document count ranks
 *
 * @author dmle
 */
public class SearchManager extends Manager implements ObjectListener {

    private ObjectEngine engine;
    protected JPanel panel_middle;
    protected EasyTable table;
    protected JTextField keyword;

    public SearchManager(String title, String titleText, int width, int height, int x, int y, SearchManager Search) {
        super(title, titleText, width, height, x, y, Search);
        this.engine = new ObjectEngine();
    }

    /**
     * @requires title != null /\ titleText != null /\ width > 0 /\ height > 0
     * /\ x > 0 /\ y > 0
     *
     * @effects initialize this as
     * SearchManager<title,titleText,width,height,x,y,eng>
     * where eng is initialized to be an empty ObjectEngine
     */


    /**
     * @requires gui != null
     * @modifies this
     * @effects initialize gui's middle panel to contain two components: a panel
     * with a text field, and a scroll pane containing a table that has 3
     * columns: "No", "Objects", "Rank"
     */
    @Override
    protected void createMiddlePanel() {
        panel_middle = new JPanel();
        JPanel panel1 = new JPanel();
        JLabel promote = new JLabel("Enter keywords (seperated by spaces): ");
        panel1.add(promote);
        keyword = new JTextField(15);
        promote.setLabelFor(keyword);
        panel1.add(keyword);
        panel_middle.add(panel1);
        panel_middle.add(new JLabel("Result: "));
        table = new EasyTable(new String[]{"NO.", "OBJECT", "RANK"});
        //set width for columns
        TableColumn column0 = table.getColumnModel().getColumn(0);
        TableColumn column1 = table.getColumnModel().getColumn(1);
        TableColumn column2 = table.getColumnModel().getColumn(2);
        column0.setPreferredWidth(5);
        column1.setPreferredWidth(100);
        column2.setPreferredWidth(10);

        panel_middle.add(new JScrollPane(table));
        this.gui.add(panel_middle);
    }

    /**
     * @effects set kwds = keywords in the middle panel of gui
     * {@link #clearSearchResult()}: clear the result table in the middle panel
     * of gui search for objects whose Html documents match with kwds      <pre>if there are matches
     *        {@link #displayResult(EasyTable, Iterator)}: display them in the result
     * table of middle panel of gui else
     * {@link #displayErrorMessage(String, String)}: display error message "No
     * objects found matching keywords"
     *
     * if an exception was thrown {@link #displayErrorMessage(String, String)}:
     * display an error message showing the exception message
     * </pre>
     */
    @Override
    public void doTask() throws NotPossibleException {
        clearSearchResult();//clear old search result
        System.out.println("Display the result of searching here!--------------------------------");
        Iterator matches = null;
        try {
            String sentence = this.keyword.getText();//name
            sentence = sentence.replaceAll("[!?,^&*@#:;'\"][=+--]", "");
            String[] keyword = sentence.split("\\s+");
            try {
                matches = this.engine.query(keyword);
            } catch (kengine.NotPossibleException e) {
                throw new NotPossibleException("No object matching that keywords found!");
            }
        } catch (NotPossibleException s) {
            throw new NotPossibleException(s.getMessage());
        }
        try {
            displayResult(this.table, matches);
        } catch (Exception m) {
            throw new NotPossibleException("No object matching that keywords found!");
        }
    }

    /**
     * @requires table != null /\ matches != null /\ Iterator<DocCnt>(matches)
     * /\ engine.resultTable contains objects mapped to documents in matches
     * @effects      <pre>
     *  for each match <d,c> in matches
     *    look up <d,o> in engine.resultTable
     *    add row r = <i,o,c> to table, where i = row index
     * </pre>
     */
    private void displayResult(EasyTable table, Iterator matches) {
        int newRowIndex;
        while (matches.hasNext()) {
            DocCnt match = (DocCnt) matches.next();
            newRowIndex = table.addRow();
            table.setValueAt(newRowIndex + 1, newRowIndex, 0); // 1st column
            table.setValueAt(match.getDoc().toString(), newRowIndex, 1); // 2nd column
            table.setValueAt(match.getCount(), newRowIndex, 2); // 3rd column
            System.out.println(match.getDoc().toString());
        }
    }

    /**
     * @requires gui != null
     *
     * @effects clear the text of all editable text fields of middle panel of
     * gui clear the result table of middle panel of gui
     */
    @Override
    public void clearGUI() {
        this.clearGUI(this.panel_middle);
        clearSearchResult();
    }

    /**
     * @requires gui != null
     * @effects clear the result table of the middle panel of gui
     */
    private void clearSearchResult() {
        this.table.clear();
    }

    @Override
    public void startUp() {
        // do nothing
    }

    @Override
    public void shutDown() {
        // clear resources and shutdown
        super.shutDown();
        engine.shutDown();
        engine = null;
    }

    /**
     * @requires obj != null
     * @effects      <pre>
     *  if Document(obj)
     *    add obj to engine
     *  else
     *    throw NotPossibleException
     * </pre>
     */
    @Override
    public void objectCreated(Object obj) throws NotPossibleException {
        if (obj == null) {
            throw new NotPossibleException("SearchManager.objectCreated: null object found!");
        } else {
            String htmlDoc = ((Document) obj).toHtmlDoc();
            Doc d = new Doc(htmlDoc);
            this.engine.addObject((Document) obj);
        }
    }

    @Override
    public void save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
