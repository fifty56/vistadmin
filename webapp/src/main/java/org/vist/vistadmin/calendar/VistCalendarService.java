package org.vist.vistadmin.calendar;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.web.CourseController;

import com.google.gdata.client.Query;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.calendar.CalendarEntry;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.data.calendar.CalendarFeed;
import com.google.gdata.data.calendar.WebContent;
import com.google.gdata.data.extensions.Recurrence;
import com.google.gdata.data.extensions.When;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

@Service
public class VistCalendarService {

	private static final Logger LOGGER =  LoggerFactory.getLogger(VistCalendarService.class);
	
	@Value("#{google_props['calendar.account.name']}")
	private String googleAccountName;
	
	@Value("#{google_props['calendar.account.password']}")
	private String googleAccountPassword;
	
	@Value("#{google_props['calendar.url.room1']}")
	private String calendarUrlRoom1;
	
	public static final String WARNING_COULDNT_UPDATE_GOOGLE_CAL = "warning_couldnt_update_google_calendar";

	public static final String WARNING_COULDNT_DELETE_GOOGLE_CAL = "warning_couldnt_delete_google_calendar";

	public static final String WARNING_COULDNT_CREATE_GOOGLE_CAL = "warning_couldnt_create_google_calendar";
	
	//private static final String CALENDAR_URL = "http://www.google.com/calendar/feeds/";
	
	//Type of calendars:
	//https://developers.google.com/google-apps/calendar/v2/reference#Feeds
	
	/*private static final String CALENDAR_TYPE_PRIVATE = "/private/full";
	
	private static final String CALENDAR_TYPE_ALL = "/allcalendars/full";
	
	private static final String CALENDAR_TYPE_OWN = "/owncalendars/full";
	*/
	/*TODO - get cal url from calendar UI (
    if you want the client to use your primary calendar. If you'd like to use another calendar, you can find 
	the calendar's address in the Google Calendar UI. Click the arrow next to the calendar's name in the left
	sidebar, and choose Calendar settings from the drop-down menu. Then copy and paste the "XML" URL for the
	Calendar Address seen on the "Calendar Details" tab. Please replace the suffix /public/basic 
	with /private/full in the URL, as the sample will be authenticating using the provided credentials.
	*/
	
	private String[] calendarUrls = null;
	
	private CalendarService getCalendarService() throws AuthenticationException {
		CalendarService cs1 = new CalendarService("vistadmin");			
		cs1.setUserCredentials(googleAccountName, googleAccountPassword);														
		if(calendarUrls == null) {
			calendarUrls = new String[] {"https://www.google.com/calendar/feeds/" + calendarUrlRoom1 + "/private/full"};	
		}
		return cs1;		
	}
	
	//TODO: warning msg-t kezelni a JSPken
	public void synchronizeCourse(Course course) throws ServiceException, IOException {
		/*Date courseStartDate = course.getStartDate();
		Date courseEndDate = course.getEndDate();
		String classScheduleStr = course.getTimeOfClasses();		 
		String[] classScheduleArray = classScheduleStr.split(";"); 
		LOGGER.trace("course id: " + course.getId() + ", courseId: " + course.getCourseId());
		LOGGER.trace("course startDate: " + courseStartDate + ", courseEndDate: " + courseEndDate);
		LOGGER.trace("course classScheduleStr: " + classScheduleStr);
		for (int i = 0; i < classScheduleArray.length; i++) {
			String classSchedule = classScheduleArray[i];
			int classDayIdx = Integer.parseInt(classSchedule.substring(0, 1));
			int courseDayIdx = courseStartDate.getDay();
			
			classDayIdx++;
			if(courseDayIdx == 0) {
				courseDayIdx = 7;
			}
			
			Date recurDataStartDate = null;
			if(classDayIdx == courseDayIdx) {
				recurDataStartDate = courseStartDate;
			} else if(classDayIdx > courseDayIdx) {
				long newStartDateTime = courseStartDate.getTime() + (24 * 60 * 60 * 1000 * (classDayIdx - courseDayIdx));
				recurDataStartDate = new Date(newStartDateTime);
			} else  {
				long newStartDateTime = courseStartDate.getTime() + (24 * 60 * 60 * 1000 * (courseDayIdx - classDayIdx));
				recurDataStartDate = new Date(newStartDateTime);				
			}
			
			String recurData = "DTSTART:" + getDateStr(recurDataStartDate) + "T" + classSchedule.substring(2, 4) + classSchedule.substring(5, 7) + "00\r\n"
			        + "DTEND:" + getDateStr(recurDataStartDate) + "T" + classSchedule.substring(8, 10) + classSchedule.substring(11, 13) + "00\r\n"
			        + "RRULE:FREQ=WEEKLY;UNTIL=" + getDateStr(courseEndDate) + "T235900Z\r\n";
			
			LOGGER.trace("recurData[" + i + "]: " + recurData);
		
			String eventContent = "[" + course.getCourseId() + "]" + "." + (i+1);
			String eventTitle = course.getCourseId() + "." + (i+1);
			CalendarEventEntry eventEntry = createEvent(eventTitle, eventContent, recurData, false, null, calendarUrls[0]);
		}*/
	}
		

	public void removeCourse(Course course) {
		
	}

	
	private static String getDateStr(Date date) {			  
		return (date.getYear() + 1900) + "" + String.format("%02d", (date.getMonth() + 1)) + "" + String.format("%02d", (date.getDate()));
	}

		  
	
	/**
	 * Helper method to create either single-instance or recurring events. For
	 * simplicity, some values that might normally be passed as parameters (such
	 * as author name, email, etc.) are hard-coded.
	 * 
	 * @param service
	 *            An authenticated CalendarService object.
	 * @param eventTitle
	 *            Title of the event to create.
	 * @param eventContent
	 *            Text content of the event to create.
	 * @param recurData
	 *            Recurrence value for the event, or null for single-instance
	 *            events.
	 * @param isQuickAdd
	 *            True if eventContent should be interpreted as the text of a
	 *            quick add event.
	 * @param wc
	 *            A WebContent object, or null if this is not a web content
	 *            event.
	 * @return The newly-created CalendarEventEntry.
	 * @throws ServiceException
	 *             If the service is unable to handle the request.
	 * @throws IOException
	 *             Error communicating with the server.
	 */
	private CalendarEventEntry createEvent(String eventTitle,
			String eventContent, String recurData, boolean isQuickAdd,
			WebContent wc, String clendarUrl) throws ServiceException,
			IOException {
		CalendarEventEntry myEntry = new CalendarEventEntry();

		myEntry.setTitle(new PlainTextConstruct(eventTitle));
		myEntry.setContent(new PlainTextConstruct(eventContent));
		myEntry.setQuickAdd(isQuickAdd);
		myEntry.setWebContent(wc);

		// If a recurrence was requested, add it. Otherwise, set the
		// time (the current date and time) and duration (30 minutes)
		// of the event.
		if (recurData == null) {
			Calendar calendar = new GregorianCalendar();
			DateTime startTime = new DateTime(calendar.getTime(),
					TimeZone.getDefault());

			calendar.add(Calendar.MINUTE, 30);
			DateTime endTime = new DateTime(calendar.getTime(),
					TimeZone.getDefault());

			When eventTimes = new When();
			eventTimes.setStartTime(startTime);
			eventTimes.setEndTime(endTime);
			myEntry.addTime(eventTimes);
		} else {
			Recurrence recur = new Recurrence();
			recur.setValue(recurData);
			myEntry.setRecurrence(recur);
		}

		// Send the request and receive the response:
		URL eventFeedUrl = new URL(clendarUrl);
		CalendarService calser = getCalendarService(); 
		return calser.insert(eventFeedUrl, myEntry);
	}	
	
	
	
	  private void fullTextQuery(String query, String calUrl)
		      throws ServiceException, IOException {
		    Query myQuery = new Query(new URL(calUrl));
		    myQuery.setFullTextQuery(query);

		    CalendarEventFeed resultFeed = getCalendarService().query(myQuery, CalendarEventFeed.class);

		    System.out.println("Events matching " + query + ":");
		    System.out.println();
		    for (int i = 0; i < resultFeed.getEntries().size(); i++) {
		      CalendarEventEntry entry = resultFeed.getEntries().get(i);
		      //updateTitle(entry, "kaka");
		      System.out.println("\t" + entry.getTitle().getPlainText());
		    }
		    System.out.println();
		  }


		
	/*private CalendarFeed getAllCalendars() throws Exception  {
		URL feedUrl = //new URL(CALENDAR_URL + googleAccountName + CALENDAR_TYPE_OWN); 
				new URL(CALENDAR_URL[0]);
		CalendarFeed resultFeed = cs.getFeed(feedUrl, CalendarFeed.class);
		return resultFeed;
	}

	private void createSampleEvent() throws ServiceException, IOException {
		CalendarEventEntry singleEvent = createSingleEvent("Tennis with Mike", "Meet for a quick lesson.");
		System.out.println("Successfully created event "  + singleEvent.getTitle().getPlainText());
	}

	private void createSampleWebEvent() throws ServiceException, IOException {
		CalendarEventEntry webContentEvent = createWebContentEvent(
				"World Cup", "image/gif",
				"http://www.google.com/logos/worldcup06.gif",
				"http://www.google.com/calendar/images/google-holiday.gif", "276",
		"120");
		System.out.println("Successfully created web content event " + webContentEvent.getTitle().getPlainText());
	}

	private void createSampleRecurringEvent() throws ServiceException, IOException {
		CalendarEventEntry recurringEvent = createRecurringEvent(
	          "Tennis with Dan", "Weekly tennis lesson.");
	      System.out.println("Successfully created recurring event "
	          + recurringEvent.getTitle().getPlainText());
	}
	
	public static void main(String args[]) throws Exception {
		VistCalendarService vc = new VistCalendarService();
		vc.setGoogleAccountName("vilagnyelvek@gmail.com");
		vc.setGoogleAccountPassword("KANkalin80");
		vc.initService();		
		printCalendarFeed(vc.getAllCalendars());

		vc.createSampleEvent();
		//vc.createSampleWebEvent();
		//vc.createSampleRecurringEvent();
	}

	private void initService() throws AuthenticationException {
		cs = new CalendarService("vistadmin");
		cs.setUserCredentials(googleAccountName, googleAccountPassword);
	}
	

	private static void printCalendarFeed(CalendarFeed resultFeed) {
		for (int i = 0; i < resultFeed.getEntries().size(); i++) {
		      CalendarEntry entry = resultFeed.getEntries().get(i);
		      System.out.println("\t" + entry.getTitle().getPlainText());
		    }
	}
	
	private void setGoogleAccountName(String googleAccountName) {
		this.googleAccountName = googleAccountName;
	}

	private void setGoogleAccountPassword(String googleAccountPassword) {
		this.googleAccountPassword = googleAccountPassword;
	}

	  
	      
	    private  CalendarEventEntry createSingleEvent(
	    	      String eventTitle, String eventContent) throws ServiceException,
	    	      IOException {
	        return createEvent(eventTitle, eventContent, null, false, null);
	    }	      
*/
//	  /**
//	   * Creates a web content event.
//	   * 
//	   * @param service An authenticated CalendarService object.
//	   * @param title The title of the web content event.
//	   * @param type The MIME type of the web content event, e.g. "image/gif"
//	   * @param url The URL of the content to display in the web content window.
//	   * @param icon The icon to display in the main Calendar user interface.
//	   * @param width The width of the web content window.
//	   * @param height The height of the web content window.
//	   * @return The newly-created CalendarEventEntry.
//	   * @throws ServiceException If the service is unable to handle the request.
//	   * @throws IOException Error communicating with the server.
//	   */
//	  private CalendarEventEntry createWebContentEvent(
//	      String title, String type, String url,
//	      String icon, String width, String height) throws ServiceException,
//	      IOException {
//	    WebContent wc = new WebContent();
//
//	    wc.setHeight(height);
//	    wc.setWidth(width);
//	    wc.setTitle(title);
//	    wc.setType(type);
//	    wc.setUrl(url);
//	    wc.setIcon(icon);
//
//	    return createEvent(title, null, null, false, wc);
//	  }
//
//
//	  /**
//	   * Creates a new recurring event.
//	   * 
//	   * @param service An authenticated CalendarService object.
//	   * @param eventTitle Title of the event to create.
//	   * @param eventContent Text content of the event to create.
//	   * @return The newly-created CalendarEventEntry.
//	   * @throws ServiceException If the service is unable to handle the request.
//	   * @throws IOException Error communicating with the server.
//	   */
//	  private CalendarEventEntry createRecurringEvent(
//	      String eventTitle, String eventContent)
//	      throws ServiceException, IOException {
//	    // Specify a recurring event that occurs every Tuesday from May 1,
//	    // 2007 through September 4, 2007. Note that we are using iCal (RFC 2445)
//	    // syntax; see http://www.ietf.org/rfc/rfc2445.txt for more information.
//
//            
//	    String recurData = "DTSTART;VALUE=DATE:20120801\r\n"
//	        + "DTEND;VALUE=DATE:20120802\r\n"
//	        + "RRULE:FREQ=WEEKLY;BYDAY=Tu;UNTIL=20120901\r\n";
//
//	    return createEvent(eventTitle, eventContent, recurData, false,
//	        null);
//	  }
//	  
}
