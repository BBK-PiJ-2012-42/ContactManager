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
public class PastMeetingImpl extends MeetingImpl implements PastMeeting {
    
    private String notes = "";

    public PastMeetingImpl(int id, Calendar date, Set<Contact> attendees) {
        super(id, date, attendees);
    }
    
    @Override
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String text) {
        this.notes = text;
    }
    
}
