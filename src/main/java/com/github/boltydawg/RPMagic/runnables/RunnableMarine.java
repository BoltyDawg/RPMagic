package com.github.boltydawg.RPMagic.runnables;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.boltydawg.RPMagic.RPMagic;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.HashMap;
import java.util.UUID;

public class RunnableMarine extends BukkitRunnable{
	private static HashMap<UUID,RunnableMarine> marines = new HashMap<UUID,RunnableMarine>();
	private Player marine;
	private int time;
	public RunnableMarine(Player p) {
		marine = p;
		time = 45;
		marines.put(p.getUniqueId(), this);
	}
	@Override
	public void run() {
		if(time <= 0) {
			TextComponent msg = new TextComponent();
			msg.setText("Call To Arms Recharged!");
			msg.setColor(ChatColor.BLUE);
			marine.spigot().sendMessage(ChatMessageType.ACTION_BAR,msg);
			marines.remove(marine.getUniqueId());
			cancel();
			return;
		}
		else
			time-=1;
	}
	
	public static void callToArms(Player player) {
		if(marines.containsKey(player.getUniqueId())) {
			TextComponent msg = new TextComponent();
			msg.setText("You must wait "+marines.get(player.getUniqueId()).time+" more seconds");
			msg.setColor(ChatColor.DARK_BLUE);
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR,msg);
		}
		else {
			for(Entity e: player.getNearbyEntities(25, 20, 25)) {
				if(e instanceof Player) {
					Player target = (Player)e;
					target.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,200,2));
					target.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING,200,2));
				}
			}
			player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,200,2));
			player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING,200,2));
			player.getWorld().playSound(player.getLocation(), Sound.BLOCK_END_PORTAL_SPAWN, 1.8F, 1F);
			
			new RunnableMarine(player).runTaskTimer(RPMagic.instance, 2L, 20L);
		}
	}
}