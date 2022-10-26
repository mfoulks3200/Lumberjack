# Lumberjack

A dead simple logging solution for simple Java projects.

[![Maven Package](https://github.com/mfoulks3200/Lumberjack/actions/workflows/maven-publish.yml/badge.svg)](https://github.com/mfoulks3200/Lumberjack/actions/workflows/maven-publish.yml)

## Features
- Timestamps
- Event Types
- Reflective Event Sources
- Custom Attributes
- CSV Crash Reports
- JSON Log Exports

# Examples
## Basic Events
Trigger a basic event

    //Initialize EventLog
	new EventLog();
	
	//Create a basic event
	new Event("Basic event");

## Event Types
Trigger an event with an EventType

    new Event(EventType.WARNING, "Warning! Something has happened!");

## Debug Events
Debug events will only show up in crash logs by default

    new Event(EventType.DEBUG, "I'll only show up in the log file!");
		
	//Start showing debug events in the console
	EventLog.showDebug = true;
		
	new Event(EventType.DEBUG, "I'll show up in the log file AND the console!");

## Modify Default Attributes

    //We can disable reflective event source attributes
	EventLog.hideEventSource = true;
		
	//Let's change the timestamp format to only show the time
	EventLog.timestampFormat = "HH:mm:ss";
		
	new Event(EventType.INFO, "Let's clean up the console a bit by removing event source attributes, and shortening the timestamp");

## Custom Attributes
You can add custom attributes to each event message by passing additional String parameters into the constructor. There are a number of color constants in the Event class to allow coloring these attributes as desired.

    new Event("I have some special attributes!", "Example Attribute");
	new Event("My attributes have color!", Event.ANSI_RED+"Red Attribute");
