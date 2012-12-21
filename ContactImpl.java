/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contactmanager;

/**
 *
 * @author tom
 */
public class ContactImpl implements Contact {
    private String id;
    private String name;
    private String notes;
    
    public void ContactImpl(String id, String name) {
        this.id = id;
        this.name = name;
        
    }

    @Override
    public int getId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getNotes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addNotes(String note) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
