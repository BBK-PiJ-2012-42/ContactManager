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
        
        Calendar date = new GregorianCalendar();
        date.set(Calendar.YEAR, 2013);
        date.set(Calendar.MONTH, 3);
        date.set(Calendar.DAY_OF_MONTH, 23);
        
        manager.addFutureMeeting(contacts, date);
        manager.flush();
    }
}
