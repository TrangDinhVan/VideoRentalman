package search;

import java.util.Hashtable;
import java.util.Iterator;

import kengine.Doc;
import kengine.Engine;
import kengine.NotPossibleException;
import kengine.Query;
import model.Document;


/**
 * @overview A sub-type of Engine to represent an object-based keyword search
 * engine.
 *
 * @attributes objectTable Hashtable<Doc,Document>
 *
 * @abstract_properties  <pre>
 *  P_Engine /\
 *    optional(objectTable) = false /\
 *  (A):
 *    size(objectTable) > 0 ->
 *      (for each <d,o> in objectTable:
 *          Document(o) /\
 *          d = Doc<toHtmlDoc(o).title,toHtmlDoc(o).body> /\
 *          d in super)
 * </pre>
 *
 * i.e. (A): objectTable maps the Doc objects created from some Document objects
 * to the Document objects themselves /\ the Doc objects of objectTable are
 * contained in the keyword search engine
 *
 * @author dmle
 */
public class ObjectEngine extends Engine {

    private Hashtable<Doc, Document> objectTable;

    /**
     * @effects initialize this as an empty keyword engine and with an empty
     * object table
     */
    public ObjectEngine() {
        super();
        objectTable = new Hashtable();
    }

    /**
     * @requires obj != null
     * @effects create Doc d from obj.toHtmlDoc() add d and <d,obj> to this
     *
     * Throws NotPossibleException if obj.toHtmlDoc() is not a valid Html
     * document.
     */
    public Query addObject(Document obj) throws NotPossibleException {
        if (obj == null) {
            throw new NullPointerException("ObjectEngine.addObject: input document is null");
        }
        try {
            String objDoc = obj.toHtmlDoc();
            Doc d = new Doc(objDoc);
            this.objectTable.put(d, obj);
            return this.addDoc(d);
        } catch (NotPossibleException s) {
            throw new NotPossibleException("Invalid html syntax!");
        }
    }

    /**
     * @requires kwds != null
     *
     * @effects Search for keywords kwds in this and return an Iterator object
     * containing the matches.
     *
     *
     * Throws NotPossibleException if kwds do not form a valid keyword query.
     */
    public Iterator query(String[] words) throws NotPossibleException {
        Query q = new Query();//initialize a query object
        q = null;
        if (!(words.length > 0) || words == null) {//if there is no keyword
            System.out.println("No search keyword");
            throw new NotPossibleException("No key word!");
        }
        try {
            q = this.queryFirst(words[0]);
            if (words.length > 1) {
                for (int i = 1; i < words.length; ++i) {
                    q.addKey(words[i]);
                }
            }
        } catch (NotPossibleException e) {
            throw new NotPossibleException(e.getMessage());
        } catch (NullPointerException s) {
            return null;
        }
        return q.matchIterator();
    }

    /**
     * @requires d != null
     * @effects if d is a valid entry key in this.objectTable return the mapped
     * entry value else return null
     */
    public Object lookUp(Doc d) {
        if (objectTable.containsKey(d)) {
            return (Object) objectTable.get(d);
        }
        return null;
    }

    /**
     * @effects clear the resources associated to the attributes of this
     */
    public void shutDown() {
        this.objectTable.clear();
    }
}
