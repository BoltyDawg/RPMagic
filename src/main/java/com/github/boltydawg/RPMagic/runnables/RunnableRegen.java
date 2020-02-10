package com.github.boltydawg.RPMagic.runnables;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.boltydawg.RPMagic.RPMagic;

public class RunnableRegen extends BukkitRunnable{
	private Player player;
	private int clazz;
	
	public static HashMap<UUID,RunnableRegen> regenerates = new HashMap<UUID,RunnableRegen>();
	
	public RunnableRegen(Player p) {
		player=p;
		clazz = RPMagic.scoreboard.getObjective("class").getScore(player.getName()).getScore();
	}
	
	public void run() {
		if(!RPMagic.bars.containsKey(player.getUniqueId())) {
			cancel();
			return;
		}
		//Mages
		if(clazz==2) {
			int sc = RPMagic.scoreboard.getObjective("Magicka").getScore(player.getName()).getScore();
			if(sc+1>=RPMagic.attributes.getOrDefault(player,0)+RPMagic.BASE_MAG) {
				RPMagic.scoreboard.getObjective("Magicka").getScore(player.getName()).setScore(RPMagic.attributes.getOrDefault(player,0)+RPMagic.BASE_MAG);
				regenerates.remove(player.getUniqueId());
				RPMagic.bars.get(player.getUniqueId()).setProgress(1.0);
				new BukkitRunnable(){
			        @Override
			        public void run(){
			        	if(RPMagic.bars.containsKey(player.getUniqueId()) && RPMagic.bars.get(player.getUniqueId()).getProgress()==1.0)
			        		RPMagic.bars.get(player.getUniqueId()).setVisible(false);
			        }
			   }.runTaskLater(RPMagic.instance, 70);
				cancel();
				return;
			}
			else {
				RPMagic.scoreboard.getObjective("Magicka").getScore(player.getName()).setScore(sc+1);
				RPMagic.bars.get(player.getUniqueId()).setProgress(((double)sc+1)/(RPMagic.BASE_MAG+RPMagic.attributes.getOrDefault(player,0)));
			}
				
		}
		//Fighters and Rangers----------------------------------------------
		else {
			int sc = RPMagic.scoreboard.getObjective("Stamina").getScore(player.getName()).getScore();
			if(sc+1>=RPMagic.attributes.getOrDefault(player,0)+RPMagic.BASE_STAM) {
				RPMagic.scoreboard.getObjective("Stamina").getScore(player.getName()).setScore(RPMagic.attributes.getOrDefault(player,0)+RPMagic.BASE_STAM);
				regenerates.remove(player.getUniqueId());
				RPMagic.bars.get(player.getUniqueId()).setProgress(1.0);
				new BukkitRunnable(){
			        @Override
			        public void run(){
			        	if(RPMagic.bars.containsKey(player.getUniqueId()) && RPMagic.bars.get(player.getUniqueId()).getProgress()==1.0)
			        		RPMagic.bars.get(player.getUniqueId()).setVisible(false);
			        }
			   }.runTaskLater(RPMagic.instance, 70);
				cancel();
				return;
			}
			else {
				RPMagic.scoreboard.getObjective("Stamina").getScore(player.getName()).setScore(sc+1);
				RPMagic.bars.get(player.getUniqueId()).setProgress(((double)sc+1)/(RPMagic.BASE_STAM+RPMagic.attributes.getOrDefault(player,0)));
			}
				
		}
	}
	//11.5 seconds to regen 100 Stamina
	public static void fighterRegen(Player player) {
		cancelRegen(player);
		
		RunnableRegen runner = new RunnableRegen(player);
		regenerates.put(player.getUniqueId(), runner);
		
		RPMagic.bars.get(player.getUniqueId()).setProgress(((double)RPMagic.scoreboard.getObjective("Stamina").getScore(player.getName()).getScore())/(RPMagic.BASE_STAM+RPMagic.attributes.getOrDefault(player,0)));
		
		runner.runTaskTimer(RPMagic.instance, 30L, 2L);
	}
	//12.2 seconds to regen 100 Stamina
	public static void rangerRegen(Player player) {
		cancelRegen(player);
		
		RunnableRegen runner = new RunnableRegen(player);
		regenerates.put(player.getUniqueId(), runner);
		
		RPMagic.bars.get(player.getUniqueId()).setProgress(((double)RPMagic.scoreboard.getObjective("Stamina").getScore(player.getName()).getScore())/(RPMagic.BASE_STAM+RPMagic.attributes.getOrDefault(player,0)));
		
		runner.runTaskTimer(RPMagic.instance, 1L, 3L);
	}
	public static void sprintingRegen(Player player) {
		cancelRegen(player);
		
		RunnableRegen runner = new RunnableRegen(player);
		regenerates.put(player.getUniqueId(), runner);
		
		RPMagic.bars.get(player.getUniqueId()).setProgress(((double)RPMagic.scoreboard.getObjective("Stamina").getScore(player.getName()).getScore())/(RPMagic.BASE_STAM+RPMagic.attributes.getOrDefault(player,0)));
		
		runner.runTaskTimer(RPMagic.instance, 40L, 5L);
	}
	//takes 35 seconds to regen 100 Magicka
	public static void mageRegen(Player player) {
		cancelRegen(player);
		
		RunnableRegen runner = new RunnableRegen(player);
		regenerates.put(player.getUniqueId(), runner);
		
		RPMagic.bars.get(player.getUniqueId()).setProgress(((double)RPMagic.scoreboard.getObjective("Magicka").getScore(player.getName()).getScore())/(RPMagic.BASE_MAG+RPMagic.attributes.getOrDefault(player,0)));
		
		runner.runTaskTimer(RPMagic.instance, 100L, 6L);
	}
	
	public static void cancelRegen(Player player) {
		if(!RPMagic.bars.containsKey(player.getUniqueId())) {
			return;
		}
		if(!RPMagic.bars.get(player.getUniqueId()).isVisible())
			RPMagic.bars.get(player.getUniqueId()).setVisible(true);
		try {
			RunnableRegen runner = regenerates.get(player.getUniqueId());
			if(runner!=null && !runner.isCancelled())
				runner.cancel();
		}
		catch(IllegalStateException e){}
		regenerates.remove(player.getUniqueId());
	}
}