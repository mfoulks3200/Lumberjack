package com.atlaspuplabs.lumberjack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventLog {
	
private static final long serialVersionUID = 1L;
	
	/**
	 * Collection of stored {@link Event}s
	 */
	private ArrayList<Event> EventLog = new ArrayList<>();
	
	
	/**
	 * Lumberjack version
	 */
	public static final String version = "1.0.1.22.1.10";
	
	
	/**
	 * Keeps track of whether the log path has been shown
	 */
	private static boolean logLocationShown = false;

	
	/**
	 * Disable dumping log to file before program exit 
	 */
	private static boolean writeToFile = true;
	
	
	/**
	 * Global copy of log
	 */
	public static EventLog log;
	
	
	/**
	 * The timestamp format string, using the {@link SimpleDateFormat} format
	 */
	public static String timestampFormat = "MM/dd/yyy HH:mm:ss";
	
	
	/**
	 * Enable/Disable displaying event types
	 */
	public static boolean hideEventType = false;
	
	
	/**
	 * Enable/Disable displaying event sources
	 */
	public static boolean hideEventSource = false;
	
	
	/**
	 * Show debug events in the console
	 */
	public static boolean showDebug = true;
	

	
	/**
	 * Create a new, blank {@link EventLog}. This method is private, to instantiate an event log, use {@link EventLog#initLog()} which checks for orphaned {@link EventLog} objects
	 */
	public EventLog() {
		log = this;
		showStartMessage();
		if(writeToFile) {
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					new Event(EventType.WARNING, "Process killed, dumping log to file...", Event.ANSI_PURPLE+"Lumberjack");
					String logFile = "Timestamp, Event Type, Calling Class, Calling Method, Message\r\n";
					for(int i = 0; i < log.EventLog.size(); i++) {
						Event e = log.EventLog.get(i);
						logFile += new SimpleDateFormat("MM/dd/yyy HH:mm:ss").format(e.Timestamp)+","+e.EventType.toString()+","+e.CallingClass+","+e.CallingMethod+",\""+e.EventText+"\"\r\n";
					}
					try {
						File directory = new File("debug");
					    if (! directory.exists()){
					        directory.mkdir();
					        // If you require it to make the entire directory path including parents,
					        // use directory.mkdirs(); here instead.
					    }
					    File logFileObj = new File("debug/CrashLog "+new SimpleDateFormat("MM.dd.yyy HH.mm.ss").format(new Date())+".csv");   
					    FileWriter myWriter = new FileWriter(logFileObj);
					    myWriter.write(logFile);
					    myWriter.close();
						new Event("Log file written to "+logFileObj.getAbsolutePath(), Event.ANSI_PURPLE+"Lumberjack");
				    } catch (IOException e) {
						new Event("Error writing log file", Event.ANSI_PURPLE+"Lumberjack");
				      e.printStackTrace();
				    }
	
				}
			});
		}
	}

	
	
	/**
	 * Create a new, blank {@link EventLog}. This method is private, to instantiate an event log, use {@link EventLog#initLog()} which checks for orphaned {@link EventLog} objects
	 * @param writeLogToDisk Enable/Disable writing full event log to disk on program exit
	 */
	public EventLog(boolean writeLogToDisk) {
		writeToFile = writeLogToDisk;
		new EventLog();
	}
	
	
	
	/**
	 * Get the specified {@link Event}
	 * @param index Events index counting from the end
	 * @return The specified {@link Event}
	 */
	public Event getEvent(int index) {
		return EventLog.get(EventLog.size() - (index+1));
	}
	
	
	
	/**
	 * Add the specified event to the event log
	 * @param event {@link Event} Specified event
	 */
	public void Push(Event event) {
		this.EventLog.add(event);
		if(showDebug || event.EventType != EventType.DEBUG)
			System.out.println(event);
	}
	
	
	
	/**
	 * Serializes the event log to the filesystem, creates a warm backup of log entries
	 */
	public void Serialize() {
		try {
			Files.createDirectories(Paths.get("tmp"));
			File cacheFile = new File("tmp/EventLog.dat");
			FileOutputStream fileOut = new FileOutputStream(cacheFile);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this);
			out.close();
			fileOut.close();
		} catch (Exception i) {
			new Event(EventType.CRITICAL, "Event Log Concurrent Write Error");
		}
	}
	
	
	
	/**
	 * Initializes the log, importing any entries orphaned on the file system
	 * @return The initialized log object
	 */
	public static EventLog initLog() {
		File logFile = new File("tmp/EventLog.dat");
		if(!logLocationShown) {
			logLocationShown = true;
		}
		try
        {
			if(logFile.exists()) {
	            FileInputStream fis = new FileInputStream("tmp/EventLog.dat");
	            ObjectInputStream ois = new ObjectInputStream(fis);
	            EventLog l = (EventLog)ois.readObject();
	            ois.close();
	            return l;
			}else {
				return new EventLog();
			}
        }
		catch (Exception e)
        {
			return new EventLog();
        }
	}
	
	
	
	/**
	 * Returns log as serialized JSON object
	 * @param exclude {@link EventType}s to exclude from the serialization
	 * @return The serialized JSON object
	 */
	public static String getJsonLog(EventType... exclude) {
		String json = "{\"events\":[";
		for(int x = 0; x < log.EventLog.size(); x++) {
			json += "{\"timestamp\":\""+log.EventLog.get(x).Timestamp+"\",";
			json += "\"callingClass\":\""+log.EventLog.get(x).CallingClass+"\",";
			json += "\"callingMethod\":\""+log.EventLog.get(x).CallingMethod+"\",";
			json += "\"eventType\":\""+log.EventLog.get(x).EventType.toString()+"\",";
			json += "\"eventText\":\""+log.EventLog.get(x).EventText+"\"},";
		}
		json = json.substring(0, json.length()-1).replaceAll("[^A-Za-z0-9-+|/:&,. \\{\\}\\[\\]\"\\']", "")+"]}";
		return json;
	}
	
	
	
	private static boolean hasElement(Object[] array, Object search) {
		for (Object item : array) {
		    if (item.equals(search)) {
		        return true;
		    }
		}
		return false;
	}
	
	public static void showStartMessage() {
		new Event(EventType.INFO, "Starting Lumberjack v"+version+"...", Event.ANSI_PURPLE+"Lumberjack");
	}
}
