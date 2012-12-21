/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contactmanager;

import java.util.*;

/**
 *
 * @author tom
 */
public class ContactManagerImpl implements ContactManager {
    Set<Contact> allContacts = new HashSet();
    Set<Meeting> allMeetings = new HashSet();
    
    @Override
    public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
        if(date.after(timeNow()) || !contactsExist(contacts)) {
            throw new IllegalArgumentException();
        } else {
            
        } 
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
        Iterator meetingIterator = allMeetings.iterator();
        Meeting nextMeeting;
        int nextId;
        while(meetingIterator.hasNext()) {
            nextMeeting = (Meeting) meetingIterator.next();
            nextId = nextMeeting.getId();
            if(nextId == id) {
                return nextMeeting;
            }
        }
        return null;
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
    
    private Calendar timeNow() {
        return Calendar.getInstance();
    }
    
    private boolean contactExists(Contact contact) {
        return false;
    }
    
    private boolean contactsExist(Set<Contact> contacts) {
        Iterator contactsIterator = contacts.iterator();
        boolean result = true;
        while(contactsIterator.hasNext()) {
            if(!contactExists((Contact) contactsIterator.next())) {
                return false;
            }
        }
        return true;
    }
    
    private int generateMeetingID() {
        int newId = (int)(Math.random()*999999);
        if(this.getMeeting(newId) == void) {
        
        }
        return newId;
    }
}
