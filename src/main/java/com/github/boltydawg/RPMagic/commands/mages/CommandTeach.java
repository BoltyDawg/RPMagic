package com.github.boltydawg.RPMagic.commands.mages;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;

import com.github.boltydawg.RPMagic.RPMagic;
import com.github.boltydawg.RPMagic.Spells;

/**
 * This class is responsible for storing new spells
 * that a mage knows
 * @author BoltyDawg
 */
public class CommandTeach implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args == null || args.length!=2) return false;
		Player player = Bukkit.getPlayer(args[0]);
		if (player == null) {sender.sendMessage("Invalid player name"); return true;}
		else if(RPMagic.scoreboard.getObjective("class").getScore(player.getName()).getScore()!=2) {sender.sendMessage("Only Mages can learn spells!"); return true;}
		else if(!Spells.isValid(args[1])) {sender.sendMessage("Invalid spell"); return true;}
		else if(RPMagic.mages.get(player.getUniqueId()).contains(args[1])) {player.sendMessage(ChatColor.GRAY+"You already know this spell"); return true;}
		else {
			if(RPMagic.mages.get(player.getUniqueId()).size()>=7) {
				player.sendMessage(ChatColor.DARK_RED+"Your head is full of too much knowledge, you must first forget one of your current spells to learn this one!"+ChatColor.GRAY+"\n(use /forget)");
				return true;
			}
			else {
				RPMagic.mages.get(player.getUniqueId()).add(args[1]);
				player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1F, 1F);
				player.sendMessage(ChatColor.BLUE+"You learned "+Spells.getColor(args[1])+(args[1]+"!"));
				return true;
			}
		}
	}
}
