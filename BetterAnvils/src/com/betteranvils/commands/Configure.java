
package com.betteranvils.commands;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.betteranvils.config.Config;
import com.betteranvils.configs.gui.ConfigGUI;
import com.betteranvils.configs.gui.ConfigSpecific;
import com.betteranvils.configs.gui.ConfigsView;
import com.betteranvils.main.BetterAnvils;
import com.betteranvils.utils.MessageUtils;

public class Configure extends SubCommand {

    @Override
    public void onCommand(Player player, String[] args) {
    	
    	if(args.length == 1) {
        	ConfigsView gui = new ConfigsView(player, null, null);
        	gui.open();	
        	return;
    	}
    	
    	ArrayList<Config> config = BetterAnvils.getInstance().configManager.config;
    	
    	for(Config i : config) {
    		if(i.getName().equalsIgnoreCase(args[1])) {
    			player.closeInventory();
				ConfigGUI gui = new ConfigSpecific(player, i, null, null);
				gui.open();
				return;
    		}
    	}
    	
    	MessageUtils.sendRawMessage(player, BetterAnvils.getInstance().configManager.messages.invalid_configure_command.replace("%config%", args[1]));
    	
    }

    @Override

    public String name() {
        return "configure";
    }

    @Override
    public String info() {
        return "Modify configs in game!";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }

	@Override
	public String permission() {
		return  BetterAnvils.getInstance().configManager.permissions.configure;
	}

	@Override
	public AutoComplete autoComplete(CommandSender sender) {
		AutoComplete tabCompleter = new AutoComplete();
		
		for(Config i : BetterAnvils.getInstance().configManager.config) {
			tabCompleter.createEntry(i.getName());
		}
		
		return tabCompleter;
	}

	

}