package com.betteranvils.config;

public class Settings extends Config{

	public int level = 5;
	public int rename = 5;
	public int enchantRepair = 10;
	
	
	public Settings(String name, double version) {
		super(name, version);
		
		this.items.add(new ConfigItem("Settings.enchantment.enchanting", level));
		this.items.add(new ConfigItem("Settings.enchantment.rename", rename));
		this.items.add(new ConfigItem("Settings.enchantment.enchantingWithRename", enchantRepair));
		
	}

	@Override
	public Configuration configuration() {
		// TODO Auto-generated method stub
		return Configuration.ANVILS;
	}
	
	@Override
	public void initialize() {
		this.level = this.getConfiguration().getInt("Settings.enchantment.enchanting");
		this.rename = this.getConfiguration().getInt("Settings.enchantment.rename");
		this.enchantRepair = this.getConfiguration().getInt("Settings.enchantment.enchantingWithRename");
	}


	
}
