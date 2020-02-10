package com.github.boltydawg.RPMagic.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import com.github.boltydawg.RPMagic.RPMagic;

import net.md_5.bungee.api.ChatColor;

public class CommandDungeons implements CommandExecutor {
	private static ArrayList<UUID> pillars = new ArrayList<UUID>();
	private static ArrayList<UUID> zombie = new ArrayList<UUID>();
	private static ArrayList<UUID> water = new ArrayList<UUID>();
	
	private static HashMap<UUID,PlayerInventory> inventories = new HashMap<UUID,PlayerInventory>();
	private static HashMap<UUID,ArrayList<String>> spells = new HashMap<UUID,ArrayList<String>>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof BlockCommandSender)) {
			sender.sendMessage("only command blocks can use this command");
			return true;
		}
		BlockCommandSender cmdblock = (BlockCommandSender)sender;
		if(args[0].equalsIgnoreCase("start")) {
			 ArrayList<Player> players = new ArrayList<Player>();
			 for(Player player : Bukkit.getOnlinePlayers()) {
				if(player.getLocation().distance(cmdblock.getBlock().getLocation()) <=40) {
					players.add(player);
				}
			}
			return startDungeon(players,args[1]);
		}
		else if(args[0].equalsIgnoreCase("end")) {
			return endDungeon(args[1]);
		}
		else if(args[0].equalsIgnoreCase("reset")) {
			for(int i=0; i<Bukkit.getOfflinePlayers().length; i++) {
				RPMagic.scoreboard.getObjective("pillars").getScore(Bukkit.getOfflinePlayers()[i].getName()).setScore(0);
				RPMagic.scoreboard.getObjective("zombie").getScore(Bukkit.getOfflinePlayers()[i].getName()).setScore(0);
				RPMagic.scoreboard.getObjective("water").getScore(Bukkit.getOfflinePlayers()[i].getName()).setScore(0);
			}
			Bukkit.broadcastMessage(ChatColor.DARK_AQUA+"Dungeons have been reset!");
			return true;
		}
		else
			return false;
	}
	public boolean startDungeon(ArrayList<Player> players, String dungeon) {
		if(dungeon.equalsIgnoreCase("pillars")) {
			if(pillars.size()>0) {
				for(Player player : players) {
					player.sendMessage(ChatColor.YELLOW+"This dungeon is in progress!");
				}
				return false;
			}
			else {
				for(Player player : players){
					if(RPMagic.scoreboard.getObjective("pillars").getScore(player.getName()).getScore()>0) {
						player.sendMessage(ChatColor.YELLOW+"You've already completed this dungeon!");
						players.remove(player);
					}
				}
				if(players.size()<2) {
					players.get(0).sendMessage(ChatColor.YELLOW+"You can't do dungeons alone, you need at least 1 other person!");
					return false;
				}
				else if(players.size()>6) {
					for(Player player : players) {
						player.sendMessage(ChatColor.YELLOW+"You can't do dungeons with more than 6 people!");
					}
					return false;
				}
				else {
					for(Player player : players) {
						player.setGameMode(GameMode.ADVENTURE);
						RPMagic.scoreboard.getObjective("pillars").getScore(player.getName()).setScore(1);
						pillars.add(player.getUniqueId());
						player.sendMessage(ChatColor.DARK_PURPLE+"Entering the dungeon...");
					}
					return true;
				}
			}
		}
		
		else if(dungeon.equalsIgnoreCase("zombie")) {
			if(zombie.size()>0) {
				for(Player player : players) {
					player.sendMessage(ChatColor.YELLOW+"This dungeon is in progress!");
				}
				return false;
			}
			else {
				for(Player player : players){
					if(RPMagic.scoreboard.getObjective("zombie").getScore(player.getName()).getScore()>0) {
						player.sendMessage(ChatColor.YELLOW+"You've already completed this dungeon!");
						players.remove(player);
					}
				}
				if(players.size()<2) {
					players.get(0).sendMessage(ChatColor.YELLOW+"You can't do dungeons alone, you need at least 1 other person!");
					return false;
				}
				else if(players.size()>6) {
					for(Player player : players) {
						player.sendMessage(ChatColor.YELLOW+"You can't do dungeons with more than 6 people!");
					}
					return false;
				}
				else {
					for(Player player : players) {
						player.setGameMode(GameMode.ADVENTURE);
						RPMagic.scoreboard.getObjective("zombie").getScore(player.getName()).setScore(1);
						zombie.add(player.getUniqueId());
						
						if(RPMagic.scoreboard.getObjective("class").getScore(player.getName()).getScore()==2) {
							spells.put(player.getUniqueId(), RPMagic.mages.get(player.getUniqueId()));
							RPMagic.mages.get(player.getUniqueId()).clear();
						}
						inventories.put(player.getUniqueId(), player.getInventory());
						player.getInventory().clear();
						
						player.sendMessage(ChatColor.DARK_GREEN+"Entering the dungeon...");
						//TODO Make sure that if the server stops while they're doing the dungeon, they don't their lose shit?
					}
					return true;
				}
			}
		}
		
		else if(dungeon.equalsIgnoreCase("water")) {
			if(water.size()>0) {
				for(Player player : players) {
					player.sendMessage(ChatColor.YELLOW+"This dungeon is in progress!");
				}
				return false;
			}
			else {
				for(Player player : players){
					if(RPMagic.scoreboard.getObjective("water").getScore(player.getName()).getScore()>0) {
						player.sendMessage(ChatColor.YELLOW+"You've already completed this dungeon!");
						players.remove(player);
					}
				}
				if(players.size()<2) {
					players.get(0).sendMessage(ChatColor.YELLOW+"You can't do dungeons alone, you need at least 1 other person!");
					return false;
				}
				else if(players.size()>6) {
					for(Player player : players) {
						player.sendMessage(ChatColor.YELLOW+"You can't do dungeons with more than 6 people!");
					}
					return false;
				}
				else {
					for(Player player : players) {
						player.setGameMode(GameMode.ADVENTURE);
						RPMagic.scoreboard.getObjective("water").getScore(player.getName()).setScore(1);
						water.add(player.getUniqueId());
						player.sendMessage(ChatColor.BLUE+"Entering the dungeon...");
					}
					
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"fill -21685 63 15542 -21684 65 15542 air replace barrier");
					
					return true;
				}
			}
		}
		else
			return false;
	}
	public boolean endDungeon(String dungeon) {
		if(dungeon.equalsIgnoreCase("pillars")) {
			for(UUID u : pillars) {
				Player player = Bukkit.getPlayer(u);
				RPMagic.scoreboard.getObjective("pillars").getScore(player.getName()).setScore(2);
				pillars.remove(u);
			}
			//TODO dispatch command to run mcfunction
			return true;
		}
		else if(dungeon.equalsIgnoreCase("zombie")) {
			for(UUID u : zombie) {
				Player player = Bukkit.getPlayer(u);
				RPMagic.scoreboard.getObjective("zombie").getScore(player.getName()).setScore(2);
				
				if(RPMagic.scoreboard.getObjective("class").getScore(player.getName()).getScore()==2) {
					RPMagic.mages.put(u, spells.get(u));
					spells.remove(u);
				}
				player.getInventory().setContents(inventories.get(u).getContents());
				inventories.remove(u);
				
				zombie.remove(u);
			}
			//TODO dispatch command to run mcfunction
			return true;
		}
		else if(dungeon.equalsIgnoreCase("water")) {
			for(UUID u : water) {
				Player player = Bukkit.getPlayer(u);
				RPMagic.scoreboard.getObjective("water").getScore(player.getName()).setScore(2);
				water.remove(u);
			}
			
			Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"fill -21685 63 15542 -21684 65 15542 barrier replace air");
			Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"setblock -21811 39 15460 air");
			
			return true;
		}
		else
			return false;
	}
}
