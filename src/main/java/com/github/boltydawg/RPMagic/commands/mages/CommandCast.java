package com.github.boltydawg.RPMagic.commands.mages;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.boltydawg.RPMagic.Spells;

/**
 * This command class is what is run when a player does /rpmcast
 * or right clicks with a spell book in their left hand.
 * I split up the code here because there's a lot of it, 
 * and it would be very messy to put all in one class
 * @author BoltyDawg
 */
public class CommandCast implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player) || args == null || args.length != 1) return false;
		else if(Spells.isValid(args[0])) {
			Spells.cast((Player)sender, args[0]);
			return true;
		}
		else {sender.sendMessage("Invalid spell name"); return true;}
	}
}
