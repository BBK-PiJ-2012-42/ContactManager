/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contactmanager;

import java.util.Calendar;
import java.util.Set;

/**
 *
 * @author tom
 */
public class MeetingImpl implements Meeting {
    private int id;
    private Calendar date;
    private Set<Contact> attendees;
    
    MeetingImpl(int id, Calendar date, Set<Contact> attendees) {
        this.id = id;
        this.date = date;
        this.attendees = attendees;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Calendar getDate() {
        return date;
    }

    @Override
    public Set<Contact> getContacts() {
        return attendees;
    }
    
}
