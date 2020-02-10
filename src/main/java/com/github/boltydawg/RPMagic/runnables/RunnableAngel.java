package com.github.boltydawg.RPMagic.runnables;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.boltydawg.RPMagic.RPMagic;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class RunnableAngel extends BukkitRunnable{
	public static ArrayList<Player> angels = new ArrayList<Player>();
	
	public RunnableAngel() {
	}
	@Override
	public void run() {
		if(angels.size()==0) {
			cancel();
			return;
		}
		for(Player player : angels) {
			if(player.getInventory().getItemInMainHand().getType()==Material.ELYTRA) {
				int dmg = RunnableLastDamage.timeSinceLastDamage(player);
				if(dmg<=4){
					if(player.hasPotionEffect(PotionEffectType.LEVITATION)) {
						player.sendMessage(ChatColor.RED+ChatColor.ITALIC.toString()+"You've been shot out of the sky!");
						player.removePotionEffect(PotionEffectType.LEVITATION);
					}
					else {
						TextComponent msg = new TextComponent();
						msg.setText("You have recently taken damage and must wait another "+(4-dmg)+" seconds");
						msg.setColor(ChatColor.RED);
						player.spigot().sendMessage(ChatMessageType.ACTION_BAR,msg);
					}
				}
				else {
					player.removePotionEffect(PotionEffectType.LEVITATION);
					player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,20,0));
				}
			}
		}
	}
	public static void activate(Player player) {
		if(angels.size()==0) 
			new RunnableAngel().runTaskTimer(RPMagic.instance, 10L, 15L);
		angels.add(player);
	}

}
