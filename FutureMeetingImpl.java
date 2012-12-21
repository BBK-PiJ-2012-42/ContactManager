package contactmanager;

import java.util.Calendar;
import java.util.Set;

/**
 *
 * @author tom
 */
public class FutureMeetingImpl extends MeetingImpl implements FutureMeeting {

    public FutureMeetingImpl(int id, Calendar date, Set<Contact> attendees) {
        super(id, date, attendees);
    }
    
    
    
}
