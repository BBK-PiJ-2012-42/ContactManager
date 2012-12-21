/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contactmanager;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

/**
 *
 * @author tom
 */
public class ContactManagerImpl implements ContactManager {
    
    @Override
    public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PastMeeting getPastMeeting(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FutureMeeting getFutureMeeting(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Meeting getMeeting(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Meeting> getFutureMeetingList(Contact contact) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Meeting> getFutureMeetingList(Calendar date) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<PastMeeting> getPastMeetingList(Contact contact) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addMeetingNotes(int id, String text) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addNewContact(String name, String notes) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<Contact> getContacts(int... ids) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<Contact> getContacts(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void flush() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
