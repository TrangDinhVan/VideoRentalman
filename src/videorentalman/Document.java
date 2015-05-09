/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videorentalman;

/**
 *
 * @author Khai
 */
public interface Document {
    /**
     * @effects return an HTML document whose title and body are derived from
     * this
     */
    public String toHtmlDoc();
}
