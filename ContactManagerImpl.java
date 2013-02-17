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

    public ContactManagerImpl() {
        
    }
    
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
            //Calendar nextCal;
            List<PastMeeting> returnList = new ArrayList<>();
            while(meetingIterator.hasNext()) {
                nextMeeting = (Meeting) meetingIterator.next();
                //nextCal = nextMeeting.getDate();
                if(nextMeeting instanceof PastMeeting && nextMeeting.getContacts().contains(contact)) {
                    returnList.add((PastMeeting) nextMeeting);
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
            PastMeeting newMeeting = new PastMeetingImpl(generateMeetingId(), date, contacts, text);
            allMeetings.add(newMeeting);
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
        try {
            printToFile();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    
    private void printToFile() throws IOException {
        // Writes the current state of the ContactManager to a CSV File.
        BufferedWriter CSVFile = new BufferedWriter(new FileWriter(fileName));
        for(Contact eachContact : allContacts) {
            CSVFile.write("contact,"+eachContact.getId()+","+eachContact.getName()+","+eachContact.getNotes()+"\n");
        }
        for(Meeting eachMeeting : allMeetings) {
            if(eachMeeting instanceof PastMeeting) {
                PastMeeting eachPastMeeting = (PastMeeting) eachMeeting;
                CSVFile.write("pastmeeting,"+eachPastMeeting.getId()+","+serialDate(eachPastMeeting.getDate())+
                    ","+serialContactSet(eachPastMeeting.getContacts())+","+eachPastMeeting.getNotes()+"\n");
            } else {
                CSVFile.write("meeting,"+eachMeeting.getId()+","+serialDate(eachMeeting.getDate())+
                    ","+serialContactSet(eachMeeting.getContacts())+"\n");
            }
        }
        CSVFile.close();
    }
    
    private void initialise() throws IOException {
        // Reads the state of the ContactManager from a CSV File.
        BufferedReader CSVFile = new BufferedReader(new FileReader(fileName));
        String dataRow = CSVFile.readLine();
        // Reads each line until the end of the file.
        while (dataRow != null) {
            String[] stringArray = dataRow.split(",");
            // Checks to see if the line contains a contact or a meeting.
            switch (stringArray[0]) {
                case "contact":  
                    loadContact(Integer.getInteger(stringArray[1]), stringArray[2], stringArray[3]);
                    break;
                case "meeting":
                    //String[]
                    //loadFutureMeeting(Integer.getInteger(stringArray[1], Calendar date, Set<Contact> attendees)
                    break;
                case "pastmeeting":
                    addNewPastMeeting(allContacts, null, dataRow);
                    break;
            }
            dataRow = CSVFile.readLine();
        }
        CSVFile.close();
    }
    
    private void loadFutureMeeting(int id, Calendar date, Set<Contact> attendees) {
        Meeting newMeeting = new FutureMeetingImpl(id, date, attendees);
        allMeetings.add(newMeeting);
    }
    
    private void loadContact(int id, String name, String text) {
        Contact newContact = new ContactImpl(id, name);
        newContact.addNotes(text);
        allContacts.add(newContact);
    } 
    
    private String serialDate(Calendar date) {
        // Serialises Calendar objects to a readable string.
        return  date.get(Calendar.YEAR)+"/"+
                date.get(Calendar.MONTH)+"/"+
                date.get(Calendar.DAY_OF_MONTH)+"/"+
                date.get(Calendar.HOUR_OF_DAY)+"/"+
                date.get(Calendar.MINUTE);
    }
    
    private String serialContactSet(Set<Contact> contactSet) {
        // Serialises contact sets to a string of their ids.
        StringBuilder builder = new StringBuilder();
        String seperator = "";
        for(Contact each : contactSet) {
            builder.append(seperator).append(each.getId());
            seperator = "/";
        }
        return builder.toString();
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
    
    public void writeLine(String line) {
        try {
            BufferedWriter CSVFile = new BufferedWriter(new FileWriter(fileName));
            CSVFile.write(line);
            CSVFile.newLine();
            CSVFile.close();
        } catch (IOException ex) {
            //Logger.getLogger(ContactManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
      
}
