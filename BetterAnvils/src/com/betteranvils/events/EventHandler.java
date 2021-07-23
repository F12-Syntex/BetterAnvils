package com.betteranvils.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.Plugin;

import com.betteranvils.main.BetterAnvils;

public class EventHandler {

    public List<SubEvent> events = new ArrayList<SubEvent>();
	
    private Plugin plugin = BetterAnvils.instance;
    
	public void setup() {
		this.events.add(new InputHandler());
		this.events.add(new AnvilEventsHandler());
		this.events.forEach(i -> plugin.getServer().getPluginManager().registerEvents(i, plugin));
	}
	
}
