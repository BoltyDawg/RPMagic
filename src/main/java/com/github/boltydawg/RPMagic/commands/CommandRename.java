package com.github.boltydawg.RPMagic.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandRename implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player) || args.length==0) return false;
		Player player = (Player)sender;
		ItemStack item = player.getInventory().getItemInMainHand();
		ItemMeta meta = item.getItemMeta();
		String rename = "";
		for(String str:args) {
			rename+=str+" ";
		}
		rename = rename.trim();
		if(rename.length()>35)
			return false;
		meta.setDisplayName(rename);
		item.setItemMeta(meta);
		return true;
	}

}
