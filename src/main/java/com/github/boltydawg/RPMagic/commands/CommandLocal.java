package com.github.boltydawg.RPMagic.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.github.boltydawg.RPMagic.RPMagic;

public class CommandLocal implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length==0) {
			if(RPMagic.local) {
				RPMagic.local=false;
				RPMagic.instance.getServer().broadcastMessage(ChatColor.YELLOW+"Chat is no longer localized");
			}
			else {
				RPMagic.local=true;
				RPMagic.instance.getServer().broadcastMessage(ChatColor.YELLOW+"Chat is now localized");
			}
			return true;
		}
		else
			return false;
	}

}
