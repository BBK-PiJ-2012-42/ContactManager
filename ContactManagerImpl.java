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
            int id = generateMeetingId();
            allMeetings.add(new FutureMeetingImpl(id, date, contacts));
            return id;
        } 
    }

    @Override
    public PastMeeting getPastMeeting(int id) {
        Meeting returnMeeting = getMeeting(id);
        if(returnMeeting != null) {
            if(returnMeeting.getDate().after(timeNow())) {
                throw new IllegalArgumentException();
            } else {
                return (PastMeetingImpl) returnMeeting;
            } 
        } else {
            return null;
        }  
    }

    @Override
    public FutureMeeting getFutureMeeting(int id) {
        Meeting returnMeeting = getMeeting(id);
        if(returnMeeting != null) {
            // Added a check to see if the meeting is now in the past
            // and throws and exception if this is the case. This is not in
            // the specification.
            if(returnMeeting.getDate().before(timeNow())) {
                throw new IllegalArgumentException();
            } else {
                return (FutureMeetingImpl) returnMeeting;
            } 
        } else {
            return null;
        }  
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
        // not yet sorted
        Iterator meetingIterator = allMeetings.iterator();
        Meeting nextMeeting;
        Calendar nextCal;
        List<Meeting> returnList = new ArrayList<>();
        while(meetingIterator.hasNext()) {
            nextMeeting = (Meeting) meetingIterator.next();
            nextCal = nextMeeting.getDate();
            if(nextCal.after(timeNow()) && nextMeeting.getContacts().contains(contact)) {
                returnList.add(nextMeeting);
            }
        }
        return returnList;
    }

    @Override
    public List<Meeting> getFutureMeetingList(Calendar date) {
        Iterator meetingIterator = allMeetings.iterator();
        Meeting nextMeeting;
        Calendar nextCal;
        List<Meeting> returnList = new ArrayList<>();
        while(meetingIterator.hasNext()) {
            nextMeeting = (Meeting) meetingIterator.next();
            nextCal = nextMeeting.getDate();
            if(nextCal.equals(date)) {
                returnList.add(nextMeeting);
            }
        }
        return returnList;
    }

    @Override
    public List<PastMeeting> getPastMeetingList(Contact contact) {
        if(contactExists(contact)) {
            Iterator meetingIterator = allMeetings.iterator();
            Meeting nextMeeting;
            Calendar nextCal;
            List<PastMeeting> returnList = new ArrayList<>();
            while(meetingIterator.hasNext()) {
                nextMeeting = (Meeting) meetingIterator.next();
                nextCal = nextMeeting.getDate();
                if(nextCal.after(timeNow()) && nextMeeting.getContacts().contains(contact)) {
                    returnList.add((PastMeetingImpl) nextMeeting);
                }
            }
            return returnList;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
        if(contacts.isEmpty() || !contactsExist(contacts)) {
            throw new IllegalArgumentException();
        } else if(contacts != null && date != null && text != null) {
            PastMeetingImpl newMeeting = new PastMeetingImpl(generateMeetingId(), date, contacts);
            newMeeting.setNotes(text);
        } else {
            throw new NullPointerException();
        }
    }

    @Override
    public void addMeetingNotes(int id, String text) {
        if(text != null) {
            Meeting newMeeting = getMeeting(id);
            if(newMeeting != null) {
                if(newMeeting.getDate().before(timeNow())) {
                    throw new IllegalStateException();
                } else {
                    PastMeetingImpl newPastMeeting = (PastMeetingImpl) newMeeting;
                    newPastMeeting.setNotes(text);
                }
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            throw new NullPointerException();
        }
//        Iterator meetingIterator = allMeetings.iterator();
//        Meeting nextMeeting;
//        Calendar nextCal;
//        PastMeetingImpl newPastMeeting = null;
//        while(meetingIterator.hasNext()) {
//            nextMeeting = (Meeting) meetingIterator.next();
//            nextCal = nextMeeting.getDate();
//            if(nextCal.equals(date) && nextMeeting.getContacts().equals(contacts)) {
//                if(!(nextMeeting instanceof PastMeetingImpl))
//                    newPastMeeting = (PastMeetingImpl) nextMeeting;
//                    newPastMeeting.setNotes(text);
//                    allMeetings.add(newPastMeeting);
//                    allMeetings.remove(nextMeeting);
//                } else {
//                    ((PastMeetingImpl) nextMeeting).setNotes(text);
//            }
//            break;
//        }
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
        while(contactsIterator.hasNext()) {
            if(!contactExists((Contact) contactsIterator.next())) {
                return false;
            }
        }
        return true;
    }
    
    private int generateMeetingId() {
        int newId = (int)(Math.random()*999999);
        if(getMeeting(newId) != null) {
            return generateMeetingId(); 
        } else {
            return newId;
        }
    }
   
    private int generateContactId() {
        int newId = (int)(Math.random()*999999);
        if(getContacts(newId) != null) {
            return generateContactId(); 
        } else {
            return newId;
        }
    }
    
    private List<Meeting> getFutureMeetings() {
        Iterator meetingIterator = allMeetings.iterator();
        Meeting nextMeeting;
        Calendar nextCal;
        List<Meeting> returnList = new ArrayList<>();
        while(meetingIterator.hasNext()) {
            nextMeeting = (Meeting) meetingIterator.next();
            nextCal = nextMeeting.getDate();
            if(nextCal.after(timeNow())) {
                returnList.add(nextMeeting);
            }
        }
        return returnList;
    }
}
