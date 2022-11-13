package com.atlaspuplabs.lumberjack.example;

import com.atlaspuplabs.lumberjack.Event;
import com.atlaspuplabs.lumberjack.EventLog;
import com.atlaspuplabs.lumberjack.EventType;

public class Main {

	public static void main(String[] args) {

		//Initialize EventLog
		new EventLog(true);
		
		//Optional: Initialize EventLog without dumping to file upon exit
		//new EventLog(false);
		
		//Create a basic event
		new Event("Basic event");
		
		//Create an event with an EventType
		new Event(EventType.WARNING, "Warning! Something has happened!");
		
		
		
		//Create a debug event (we won't see this one in the console quite yet, but it will appear in the log file)
		new Event(EventType.DEBUG, "I'll only show up in the log file!");
		
		//Start showing debug events in the console
		EventLog.showDebug = true;
		
		new Event(EventType.DEBUG, "I'll show up in the log file AND the console!");
		
		
		
		//We can disable reflective event source attributes as well
		EventLog.hideEventSource = true;
		
		//Let's change the timestamp format to only show the time
		EventLog.timestampFormat = "HH:mm:ss";
		
		new Event(EventType.INFO, "Let's clean up the console a bit by removing event source attributes, and shortening the timestamp");
		
		
		
		//We can also add additional attributes to each event
		new Event("I have some special attributes!", "Example Attribute");
		new Event("My attributes have color!", Event.ANSI_RED+"Red Attribute");
		
	}

}
