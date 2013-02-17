/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contactmanager;

import java.io.IOException;
import java.util.*;
import javax.xml.crypto.Data;


/**
 *
 * @author tom
 */
public class Launch {
    public static void main(String[] args) throws IOException {
        Launch.restart();
    }
    
    public static void start() throws IOException {
        ContactManager manager = new ContactManagerImpl(0);
        manager.addNewContact("Bill Smith", "");
        manager.addNewContact("Jim Smith", "");
        manager.addNewContact("Sally Smith", "");
        manager.addNewContact("Sue Smith", "");
        
        Set<Contact> contacts = manager.getContacts("Smith");
        for(Contact each : contacts) {
            System.out.println(each.getId()+" "+each.getName());
        }
        
        Calendar date = new GregorianCalendar();
        date.set(2013, 7, 23, 14, 0);
        Calendar date2 = new GregorianCalendar();
        date2.set(2013, 5, 10, 14, 0);
        Calendar date3 = new GregorianCalendar();
        date3.set(2013, 5, 10, 14, 0);
        Calendar date4 = new GregorianCalendar();
        date4.set(2013, 9, 10, 14, 0);
        
        Calendar oldDate = new GregorianCalendar();
        oldDate.set(2013, 01, 23, 14, 0);
        
        manager.addFutureMeeting(contacts, date);
        manager.addFutureMeeting(contacts, date2);
        manager.addNewPastMeeting(contacts, oldDate, "We all agreed that was a great meeting.");
        
        manager.flush();           
    }
    
    public static void restart() throws IOException {
        ContactManager loadedManager = new ContactManagerImpl();
        ((ContactManagerImpl) loadedManager).printToConsole();
        
        Set<Contact> loadedContacts = loadedManager.getContacts("Smith");
        
        ((ContactManagerImpl) loadedManager).getContactFromID(id)

        loadedManager.getFutureMeetingList(null)
    }
    

}
