package com.atlaspuplabs.lumberjack;

public enum EventType {
	INFO(Event.ANSI_BLUE),
	DEBUG(Event.ANSI_PURPLE),
	WARNING(Event.ANSI_YELLOW),
	CRITICAL(Event.ANSI_RED);
	
	public String color;
	
	private EventType(String color) {
		this.color = color;
	}
}
