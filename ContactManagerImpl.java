package contactmanager;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.security.pkcs11.wrapper.CK_SSL3_MASTER_KEY_DERIVE_PARAMS;

/**
 *
 * @author tom
 */
public class ContactManagerImpl implements ContactManager {
    String fileName = "./contacts.txt";
    Set<Contact> allContacts = new HashSet();
    Set<Meeting> allMeetings = new HashSet();
    private static Comparator<Meeting> dateSorter = new Comparator<Meeting>() {
        @Override
        public int compare(Meeting o1, Meeting o2) {
            return o1.getDate().compareTo(o2.getDate());
        }
    };
    
    @Override
    public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
        if(date.before(timeNow()) || !contactsExist(contacts)) {
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
            // the specification but seems appropriate given that the same
            // exception is given in the getPastMeeting method.
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
        // Sorts list using the Comparator from Java Collections.
        Collections.sort(returnList, dateSorter);
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
            // Sorts list using the Comparator from Java Collections.
            Collections.sort(returnList, dateSorter);
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
        if(name != null && notes != null) {
            Contact newContact = new ContactImpl(generateContactId(), name);
            allContacts.add(newContact);            
        } else {
            throw new NullPointerException();
        }
    }

    @Override
    public Set<Contact> getContacts(int... ids) {
        Set<Contact> contacts = new HashSet<>();
        Contact retrievedContact;
        for(int eachID : ids) {
            retrievedContact = getContactFromID(eachID);
            if(retrievedContact != null) {
                contacts.add(retrievedContact);
            } else {
                throw new IllegalArgumentException();
            }
        }
        return contacts;
    }

    @Override
    public Set<Contact> getContacts(String name) {
        if(name != null) {
            Set<Contact> contacts = new HashSet<>();
            Iterator contactIterator = allContacts.iterator();
            Contact nextContact;
            while(contactIterator.hasNext()) {
                nextContact = (Contact) contactIterator.next();
                if(nextContact.getName().contains(name)) {
                    contacts.add(nextContact);
                }
            }
            return contacts;
        } else {
            throw new NullPointerException();
        }   
    }

    @Override
    public void flush() {
        writeLine("::CONTACTS::");
        for(Contact eachContact : allContacts) {
            writeLine(eachContact.getId()+","+eachContact.getName()+","+eachContact.getNotes());
        }
        writeLine("::MEETINGS::");
        for(Meeting eachMeeting : allMeetings) {
            writeLine(eachMeeting.getId()+","+eachMeeting.getDate());
        }
    }
    
    private Calendar timeNow() {
        return Calendar.getInstance();
    }
    
    private boolean contactExists(Contact contact) {
        if(allContacts.contains(contact)) {
            return true;
        } else {
            return false;
        }
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
        // Generates a random 6 digit integer as the ID number.
        int newId = (int)(Math.random()*999999);
        // Then checks to see if this number is already in use recursively.
        if(getMeeting(newId) != null) {
            return generateMeetingId(); 
        } else {
            return newId;
        }
    }
    
    private Meeting getMeetingFromID(int id) {
        Iterator meetingIterator = allMeetings.iterator();
        Meeting nextMeeting;
        while(meetingIterator.hasNext()) {
            nextMeeting = (Meeting) meetingIterator.next();
            if(nextMeeting.getId() == id) {
                return nextMeeting;
            }
        }
        return null;
    }
   
    private int generateContactId() {
        // Generates a random 6 digit integer as the ID number.
        int newId = (int)(Math.random()*999999);
        // Then checks to see if this number is already in use recursively.
        if(getContactFromID(newId) != null) {
            return generateContactId(); 
        } else {
            return newId;
        }
    }
    
    private Contact getContactFromID(int id) {
        Iterator contactIterator = allContacts.iterator();
        Contact nextContact;
        while(contactIterator.hasNext()) {
            nextContact = (Contact) contactIterator.next();
            if(nextContact.getId() == id) {
                return nextContact;
            }
        }
        return null;
    }
    
    private void writeLine(String line) {
        try {
            BufferedWriter CSVFile = new BufferedWriter(new FileWriter(fileName));
            CSVFile.write(line);
            CSVFile.newLine();
        } catch (IOException ex) {
            //Logger.getLogger(ContactManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
      
}
