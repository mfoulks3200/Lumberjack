package com.atlaspuplabs.lumberjack;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Event {
	
private static final long serialVersionUID = 1L;
	
	
	/**
	 * ANSI Color Code constants
	 */
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	
	public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

	
	/**
	 * The {@link EventType} for this event, similar to a catagory
	 */
	public EventType EventType;
	
	
	/**
	 * (Optional) Any text associated with this event
	 */
	public String EventText;
	
	
	/**
	 * The timestamp of this event
	 */
	public Date Timestamp;
	
	
	/**
	 * Extra tags to add
	 */
	public String[] Attributes;
	
	
	/**
	 * Info on events origin
	 */
	public String CallingClass;
	public String CallingMethod;
	
	
	
	/**
	 * Publish log event to the {@link EventLog}, and immediately serialize to the file system for redundancy
	 * @param EventType {@link EventType} The type of event to be created
	 * @param Text {@link java.lang.String} Additional text to be associated with this event
	 * @param Attributes... Add additional boxes to any event
	 */
	public Event(EventType EventType, String Text, String... Attributes) {
		this.EventType = EventType;
		this.Timestamp = new Date();
		this.EventText = Text;
		this.Attributes = Attributes;
		
		int stackDepth = Thread.currentThread().getStackTrace().length > 3 ? 3 : 2;
		
		String[] classComponents = Thread.currentThread().getStackTrace()[stackDepth].getClassName().split("[.]");
		this.CallingClass = classComponents[classComponents.length - 1];
		this.CallingMethod = Thread.currentThread().getStackTrace()[stackDepth].getMethodName();
		
		EventLog.log.Push(this);
	}
	
	public Event(EventType EventType) {
		Event e = new Event (EventType, "", new String[] {""});
	}
	
	public Event(EventType system, String Text) {
		Event e = new Event (system, Text, new String[] {""});
	}
	
	public Event(String Text) {
		Event e = new Event (EventType.INFO, Text, new String[] {""});
	}
	
	public Event(String Text, String... Attributes) {
		Event e = new Event (EventType.INFO, Text, Attributes);
	}

	
	
	/**
	 * @return A human readable string representing this event
	 */
	@Override
	public String toString() {
		String output =  formattedLogBox(new SimpleDateFormat(EventLog.timestampFormat).format(this.Timestamp), ANSI_GREEN);
		
		if(!EventLog.hideEventType)
			output += formattedLogBox(this.EventType.toString(), this.EventType.color);
		
		if(Attributes.length >= 1 && Attributes[0].length() != 0) {
			for(int i = 0; i < Attributes.length; i++) {
				output += formattedLogBox(Attributes[i], ANSI_RESET);
			}
		}
		
		if(!EventLog.hideEventSource)
			output += formattedLogBox(this.CallingClass+"."+this.CallingMethod+"()",ANSI_YELLOW);
		
		output += " "+EventText;
		return output;
	}
	
	
	
	/**
	 * Wrap input text in square brackets, and optionally color it
	 * @param inputText Text to wrap
	 * @param color ANSI color code to change the color of enclosed text, must be an ANSI color code as defined in {@link Event}
	 */
	private String formattedLogBox(String inputText, String color) {
		return "["+color+inputText+ANSI_RESET+"]";
	}
}
