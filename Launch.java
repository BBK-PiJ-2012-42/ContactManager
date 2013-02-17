/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package contactmanager;

import java.util.*;
import javax.xml.crypto.Data;

/**
 *
 * @author tom
 */
public class Launch {
    public static void main(String[] args) {
        Launch.start();
    }
    
    public static void start() {
        ContactManager manager = new ContactManagerImpl();
        manager.addNewContact("Bill Smith", "");
        manager.addNewContact("Jim Smith", "");
        manager.addNewContact("Sally Smith", "");
        manager.addNewContact("Sue Smith", "");
        
        Set<Contact> contacts = manager.getContacts("Smith");
        for(Contact each : contacts) {
            System.out.println(each.getId()+" "+each.getName());
        }
        
        Calendar date = new GregorianCalendar();
        date.set(2013, 03, 23, 14, 0);
        System.out.println(date.toString());
        
        Calendar oldDate = new GregorianCalendar();
        date.set(2013, 01, 23, 14, 0);
        
        manager.addFutureMeeting(contacts, date);
        
        manager.addNewPastMeeting(contacts, oldDate, "We all agreed that was a great meeting.");
        
        manager.flush();
        
        Set<Contact> newcontacts = manager.getContacts("Bill");
        for(Contact each : newcontacts) {
            List<PastMeeting> meets = manager.getPastMeetingList(each);
            System.out.println(meets.get(0));
        }
        
        
        
        
    }
    
    public static void restart() {
        
    }
}
