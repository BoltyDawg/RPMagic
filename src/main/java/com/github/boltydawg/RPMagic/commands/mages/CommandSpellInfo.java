package com.github.boltydawg.RPMagic.commands.mages;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.boltydawg.RPMagic.RPMagic;

public class CommandSpellInfo implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) return false;
		Player p = ((Player)sender);
		if(RPMagic.scoreboard.getObjective("class").getScore(p.getName()).getScore()!=2) {p.sendMessage("only mages can use this command"); return true;}
		ArrayList<String> spells = RPMagic.mages.getOrDefault(p.getUniqueId(), new ArrayList<String>());
		if(spells.size()==0) {p.sendMessage(ChatColor.BLUE+"You don't know any spells!"); return true;}
		if(args.length!=1)return false;
		
		p.sendMessage(details(args[0]));
		return true;
	}
	private String details(String spell) {
		switch(spell) {
		//Common tier
		case "beam": return ChatColor.RED+"Beam:"+ChatColor.GRAY+"<10 Magicka>\n"+ChatColor.YELLOW+"Shoots 2 lasers that damage enemies. Does same damage as Iron Sword, and you can still deal crits with it by jumping.";
		case "fireball": return ChatColor.RED+"Fireball:"+ChatColor.GRAY+"<15 Magicka>\n"+ChatColor.YELLOW+"Launches a fireball that explodes and sets fire to the area.";
		case "flashbang": return ChatColor.RED+"Flashbang:"+ChatColor.GRAY+"<15 Magicka>\n"+ChatColor.YELLOW+"A projectile that blinds anybody within its radius. Works well for PvP.";
		case "ghast": return ChatColor.RED+"Ghast:"+ChatColor.GRAY+"<20 Magicka>\n"+ChatColor.YELLOW+"Summon a ghast to fight with you. Doesn't work well for PvE.";
		case "soothe": return ChatColor.RED+"Soothe:"+ChatColor.GRAY+"<33 Magicka>\n"+ChatColor.YELLOW+"Give your self regeneration for 10 seconds";
		case "homing": return ChatColor.RED+"Homing:"+ChatColor.GRAY+"<15 Magicka>\n"+ChatColor.YELLOW+"Locks onto the target you'e looking at";
		case "platform": return ChatColor.RED+"Platform:"+ChatColor.GRAY+"<5 Magicka>\n"+ChatColor.YELLOW+"Places a temporty platform where you're looking.";
		
		//Mid tier
		case "shock": return ChatColor.GREEN+"Shock:"+ChatColor.GRAY+"<20 Magicka>\n"+ChatColor.YELLOW+"Shoots a bolt of lightning that jumps from target to target";
		case "fury": return ChatColor.GREEN+"Fury:"+ChatColor.GRAY+"<20 Magicka>\n"+ChatColor.YELLOW+"Launches a projectile straight up in the air that rains down onto multiple targets.";
		case "grenade": return ChatColor.GREEN+"Grenade:"+ChatColor.GRAY+"<25 Magicka>\n"+ChatColor.YELLOW+"Frag out!";
		case "tornado": return ChatColor.GREEN+"Tornado:"+ChatColor.GRAY+"<33 Magicka>\n"+ChatColor.YELLOW+"Spawns a tornado that sweeps up blocks and creatures";
		case "lift": return ChatColor.GREEN+"Lift:"+ChatColor.GRAY+"<15 Magicka>\n"+ChatColor.YELLOW+"Makes the target you're looking at float in the air";
		case "wave": return ChatColor.GREEN+"Wave:"+ChatColor.GRAY+"<25 Magicka>\n"+ChatColor.YELLOW+"Sends a shockwave through the ground, damaging the ground and hurting enemies in its path";
		case "bomb": return ChatColor.GREEN+"Bomb:"+ChatColor.GRAY+"<60 Magicka>\n"+ChatColor.YELLOW+"Summons TNT that rains from the sky";
		
		//High tier
		case "time": return ChatColor.BLUE+"Time Warp:"+ChatColor.GRAY+"<100 Magicka>\n"+ChatColor.YELLOW+"Changes the time of day from night to day, or vice versa.";
		case "earthquake": return ChatColor.BLUE+"Earthquake:"+ChatColor.GRAY+"<33 Magicka>\n"+ChatColor.YELLOW+"Shakes the earth, damaging anything in its radius";
		case "entangle": return ChatColor.BLUE+"Entangle:"+ChatColor.GRAY+"<33 Magicka>\n"+ChatColor.YELLOW+"Traps your target for 5 seconds";
		case "heal": return ChatColor.BLUE+"Heal:"+ChatColor.GRAY+"<40 Magicka>\n"+ChatColor.YELLOW+"Heals either the target your looking at, or if there is none, yourself.\nGets interupted by casting another spell or taking damage";
		case "meteor": return ChatColor.BLUE+"Meteor:"+ChatColor.GRAY+"<40 Magicka>\n"+ChatColor.YELLOW+"Rains meteors down from the sky that damage the environment, but unfortunately don't deal much damage";
		case"shower": return ChatColor.BLUE+"Shower:"+ChatColor.GRAY+"<75 Magicka>\n"+ChatColor.YELLOW+"A meteor shower that lasts for 30 seconds";
		
		//Exotic
		case "blessing": return ChatColor.LIGHT_PURPLE+"Blessing:"+ChatColor.GRAY+"<90 Magicka>\n"+ChatColor.YELLOW+"Fully heals anyone within its radius, and damages mobs";
		case "nuke": return ChatColor.LIGHT_PURPLE+"Nuke:"+ChatColor.GRAY+"<100 Magicka>\n"+ChatColor.YELLOW+"It's literally a nuke.";
		case "singularity": return ChatColor.LIGHT_PURPLE+"Singularity:"+ChatColor.GRAY+"<75 Magicka>\n"+ChatColor.YELLOW+"Spawns a blackhole that sucks up creatures.";
		case "skyfall": return ChatColor.LIGHT_PURPLE+"Skyfall:"+ChatColor.GRAY+"<120 Magicka>\n"+"Makes the sky fall upon your enemies";
		
		case "vallone": return ChatColor.GOLD+"Vallone?\n"+ChatColor.YELLOW.toString()+ChatColor.MAGIC+"N"+"          "+ChatColor.MAGIC+" e";
		case "test": return ChatColor.GOLD+"yeah it works";
		default: return ChatColor.GRAY+"Couldn't find any info on that spell...\n(All spell names are in lowercase)";
	}
	}
}
