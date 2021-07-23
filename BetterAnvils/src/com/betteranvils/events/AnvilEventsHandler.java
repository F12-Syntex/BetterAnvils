package com.betteranvils.events;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftInventoryAnvil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

import com.betteranvils.main.BetterAnvils;

import net.minecraft.network.protocol.game.PacketPlayOutGameStateChange;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.inventory.ContainerAnvil;

public class AnvilEventsHandler extends SubEvent{

	private Map<Player, GameMode> modes = new HashMap<Player, GameMode>();
	
	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Anvil Events.";
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "Maintains all the anvil related events.";
	}
	
	@EventHandler
    public void anvilCost(PrepareAnvilEvent e){
		
            Player p = (Player) e.getViewers().get(0);
            AnvilInventory inv = e.getInventory();
			
			
			ItemStack item = e.getView().getItem(0);
			ItemStack second = e.getView().getItem(0);
			
			if(item == null) return;
			if(item.getType() == Material.AIR) return;
			
			
			if(item.hasItemMeta()) {
				
			    ItemMeta iMeta = item.getItemMeta();
			    
			    if(iMeta instanceof Repairable) {
			      Repairable repairable = (Repairable)iMeta;
			      repairable.setRepairCost(0);
			      item.setItemMeta(iMeta);
			    } 
			    
			}
			
			if(second.hasItemMeta()) {
				
			    ItemMeta iMeta = second.getItemMeta();
			    
			    if(iMeta instanceof Repairable) {
			      Repairable repairable = (Repairable)iMeta;
			      repairable.setRepairCost(0);
			      second.setItemMeta(iMeta);
			    } 
			    
			}
			
			Bukkit.getScheduler().runTask(BetterAnvils.getInstance(), ()-> {
			
			boolean isEnchanting = checkIfEnchanting(e, inv);
			boolean isRepairing = checkIfRepairing(e, inv);
			
			if(isEnchanting && isRepairing) {
				this.changeXPCost(inv, p, BetterAnvils.getInstance().configManager.settings.enchantRepair, e);
			}else if(isEnchanting) {
				this.changeXPCost(inv, p, BetterAnvils.getInstance().configManager.settings.level, e);
			}else if(isRepairing) {
				this.changeXPCost(inv, p, BetterAnvils.getInstance().configManager.settings.rename, e);
			}

            
			});
            
    }
	
	public boolean checkIfRepairing(PrepareAnvilEvent e, AnvilInventory inv) {
		
        Player p = (Player) e.getViewers().get(0);
		
        
        
        	if(inv.getItem(0) != null) {
        		if(inv.getItem(2) != null) {
            		
        			ItemStack item = e.getView().getItem(0);
        			ItemStack result = e.getView().getItem(2);
        			
        			if(!item.getItemMeta().getDisplayName().equals(result.getItemMeta().getDisplayName())) {
        				
        				GameMode mode = p.getGameMode();
        				
        				if(!modes.containsKey(p)) {
        					modes.put(p, mode);
        				}

        				return true;
        			}
        			
        	}
        }
        
		return false;
	}
	
	
	
	public boolean checkIfEnchanting(PrepareAnvilEvent e, AnvilInventory inv) {
		
        Player p = (Player) e.getViewers().get(0);
		
        if(inv.getItem(1) != null) {
        	if(inv.getItem(0) != null) {
        		if(inv.getItem(2) != null) {
            		
        			ItemStack enchantmentBook = e.getView().getItem(1);
        			
        			if(enchantmentBook.getType() == Material.ENCHANTED_BOOK) {
        			
        				GameMode mode = p.getGameMode();
        				
        				if(!modes.containsKey(p)) {
        					modes.put(p, mode);
        				}

        				return true;
        			
        			}
        			
        		}	
        	}
        }
        
		return false;
	}
	
	@EventHandler
    public void InventoryOpen(InventoryOpenEvent e){
		if(e.getInventory() instanceof AnvilInventory) {
		
			AnvilInventory inv = (AnvilInventory) e.getInventory();

	        CraftInventoryAnvil anvil = CraftInventoryAnvil.class.cast(inv);
			
			try {
	        	
	            Field container = CraftInventoryAnvil.class.getDeclaredField("container");
	            container.setAccessible(true);

	            Object containerAnvil = container.get(anvil);
	            Field maximumRepairCost = ContainerAnvil.class.getField("maximumRepairCost");
	            
	        	maximumRepairCost.set(containerAnvil, Integer.MAX_VALUE);
	            
	        } catch (NoSuchFieldException | IllegalAccessException ex) {
	            ex.printStackTrace();
	        }
			
		}
	}
	
	@EventHandler
    public void clickEvent(InventoryClickEvent e){
		if(e.getInventory() instanceof AnvilInventory) {
			AnvilInventory inv = (AnvilInventory) e.getInventory();
            if(inv.getItem(1) == null) {
            	if(inv.getItem(0) != null) {
            		if(inv.getItem(2) != null) {
            			
            			ItemStack item = e.getView().getItem(0);
            			ItemStack result = e.getView().getItem(2);
            			
            			if(!item.getItemMeta().getDisplayName().equals(result.getItemMeta().getDisplayName())) {
            				
            				Player player = (Player)e.getWhoClicked();
        					
            				if(!this.modes.containsKey(player)) {
            					return;
            				}
            				
            				if(e.getSlot() == 2 || e.getSlot() == 0) {
            					e.getWhoClicked().setGameMode(this.modes.get(player));
            					this.modes.remove(player);
            				}
            				
            				return;
            			}
            		}
            	}
            }
		}
	}
	
	public GameMode changeXPCost(AnvilInventory inv, Player p, int amount, PrepareAnvilEvent e) {
        
		EntityPlayer handle = ((CraftPlayer) p).getHandle();

        CraftInventoryAnvil anvil = CraftInventoryAnvil.class.cast(inv);
		
		GameMode mode = p.getGameMode();
		
		try {
        	
            Field container = CraftInventoryAnvil.class.getDeclaredField("container");
            container.setAccessible(true);

            Object containerAnvil = container.get(anvil);
            Field maximumRepairCost = ContainerAnvil.class.getField("maximumRepairCost");
            
        	maximumRepairCost.set(containerAnvil, Integer.MAX_VALUE);
        	
        	Bukkit.getScheduler().runTask(BetterAnvils.getInstance(), ()-> {

        		inv.setRepairCost(amount);
        		
        		if(amount > 39) {
        			e.getView().setProperty(Property.REPAIR_COST, amount);
            	}else {
        			e.getView().setProperty(Property.REPAIR_COST, amount);
            	}
        		
        		int repairCost = amount;
        		
        		if(p.getLevel() >= repairCost) {}

        		if(repairCost > 39) {
        			//p.setGameMode(GameMode.CREATIVE);
        		
        			PacketPlayOutGameStateChange packet = new PacketPlayOutGameStateChange(new PacketPlayOutGameStateChange.a(3), 0);
        			handle.b.sendPacket(packet);
        		}
        		
        		handle.updateAbilities();
        		
        	});
            
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
        
		return mode;
        
	}
			
		
	}


