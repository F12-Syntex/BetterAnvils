
package com.betteranvils.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.betteranvils.main.BetterAnvils;
import com.betteranvils.utils.MessageUtils;

public class Reload extends SubCommand {

    @Override
    public void onCommand(Player player, String[] args) {
    
    	if(args.length == 1) {
        	BetterAnvils.getInstance().reload();
        	player.sendMessage(BetterAnvils.getInstance().configManager.messages.prefix + " " + MessageUtils.translateAlternateColorCodes("&6reloaded!"));
    		return;
    	}
    
    }

    @Override

    public String name() {
        return "reload";
    }

    @Override
    public String info() {
        return "reloads the plugin.";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }

	@Override
	public String permission() {
		return  BetterAnvils.getInstance().configManager.permissions.reload;	
	}
	
	@Override
	public AutoComplete autoComplete(CommandSender sender) {
		AutoComplete tabCompleter = new AutoComplete();
		return tabCompleter;
	}
	

}