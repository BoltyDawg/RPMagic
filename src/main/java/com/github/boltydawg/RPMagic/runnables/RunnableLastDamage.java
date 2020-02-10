package com.github.boltydawg.RPMagic.runnables;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;

import com.github.boltydawg.RPMagic.RPMagic;

public class RunnableLastDamage extends BukkitRunnable{
	private Player player;
	private Objective time;
	private static final int MAX = 200;
	
	public static HashMap<Player,RunnableLastDamage> damaged = new HashMap<Player,RunnableLastDamage>();
	
	public RunnableLastDamage(Player p) {
		player=p;
		time = RPMagic.scoreboard.getObjective("damageTime");
	}
	
	public void run() {
		int sc = time.getScore(player.getName()).getScore();
		if(sc>=MAX) {
			cancel();
			damaged.remove(player);
			return;
		}
		time.getScore(player.getName()).setScore(sc+1);
	}
	
	public static void startCounter(Player player) {
		cancelCounter(player);
		RunnableLastDamage runner = new RunnableLastDamage(player);
		damaged.put(player, runner);
		
		runner.runTaskTimer(RPMagic.instance, 1L, 1L);
		runner.time.getScore(player.getName()).setScore(0);
	}
	private static void cancelCounter(Player player) {
		RunnableLastDamage runner = damaged.get(player);
		if(runner!=null && !runner.isCancelled()) {
			runner.cancel();
		}
		damaged.remove(player);
	}
	public static int timeSinceLastDamage(Player player) {
		if(damaged.get(player)==null)
			return MAX/20;
		else {
			return damaged.get(player).time.getScore(player.getName()).getScore()/20;
		}
	}
}
