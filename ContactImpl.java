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
    private int id;
    private String name;
    private String notes = "";
    
    public ContactImpl(int id, String name) {
        this.id = id;
        this.name = name;
        
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getNotes() {
        return notes;
    }

    @Override
    public void addNotes(String note) {
        notes += note;
    }
    
}
