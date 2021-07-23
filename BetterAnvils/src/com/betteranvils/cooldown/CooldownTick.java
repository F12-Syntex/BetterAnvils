package com.betteranvils.cooldown;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.scheduler.BukkitScheduler;

import com.betteranvils.main.BetterAnvils;

public class CooldownTick {
	
	private BukkitScheduler scheduler;

	public CooldownTick() {
		this.scheduler = BetterAnvils.getInstance().getServer().getScheduler();
	}

	@SuppressWarnings("deprecation")
	public void schedule() {

		new Thread(() -> {

	        scheduler.scheduleSyncRepeatingTask(BetterAnvils.getInstance(), new Runnable() {
	            @Override
	            public void run() {
	            	List<CooldownRunnable> remove = new ArrayList<CooldownRunnable>();
	            	
	            	
	            	for(CooldownRunnable i : BetterAnvils.getInstance().cooldownManager.getRunnables()) {
	            		
	            		i.setTimer((i.getTimer()-1));
	            		
	            		if(i.getTimer() == 0) {
	            			i.onComplete();
	            			remove.add(i);
	            		}else {
	            			i.onTick();
	            		}
	            		
	            	}
	            	
	            	for(CooldownRunnable i : remove) {
	            		BetterAnvils.getInstance().cooldownManager.getRunnables().remove(i);
	            	}
	            }  	
	            	
	        }, 0L, 20L);
	        
	        scheduler.scheduleAsyncRepeatingTask(BetterAnvils.getInstance(), new Runnable() {
	            @Override
	            public void run() {
	            	for(CooldownUser i : BetterAnvils.getInstance().cooldownManager.getUsers()) {
	            		i.tick();
	            	}
	            }  	
	            	
	        }, 0L, 20L);
			
		}, "Schedular").start();

	}
	
	public void stop() {
		this.scheduler.cancelTasks(BetterAnvils.getInstance());
	}
	
}
