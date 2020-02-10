package com.github.boltydawg.RPMagic.listeners;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.entity.EntityDismountEvent;

import com.github.boltydawg.RPMagic.RPMagic;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * 1)blacksmith
 * 2) enchanter
 * 3) merchant
 * 4) Researcher
 * 5) Sheriff
 * 6) Sheriff
 * @author Jason
 *
 */
/**
 * @author Jason
 *
 */
public class RoleListener implements Listener{
	private HashMap<Player,Player> crooks = new HashMap<Player,Player>();
	
	@EventHandler
	public void damgeEntity(EntityDamageByEntityEvent event) {
		boolean dmger = event.getDamager() instanceof Player;
		if(dmger) {
			Player player = (Player)event.getDamager();
			if(player.isInsideVehicle() && player.getVehicle() instanceof Player)
				event.setCancelled(true);
			else if(player.getPassengers()!=null && player.getPassengers().contains(event.getEntity()))
				event.setCancelled(true);
		}
	}
	@EventHandler
	public void playerInteract(PlayerInteractEvent event) {
		if((event.getAction().equals(Action.RIGHT_CLICK_AIR) || (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) && event.getHand().equals(EquipmentSlot.HAND)){
			Player player = event.getPlayer();
			ItemStack item = event.getItem();
			if(item!=null && item.getType().equals(Material.LEAD) && item.getItemMeta().getLore()!=null && item.getItemMeta().getLore().contains("Justice!")) {
				if(RPMagic.scoreboard.getObjective("role").getScore(player.getName()).getScore()==5){
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"castp "+player.getName()+" lasso");
				}
				else {
					TextComponent msg = new TextComponent();
					msg.setText(ChatColor.AQUA+"It's just a useless, floppy rope");
					player.spigot().sendMessage(ChatMessageType.ACTION_BAR,msg);
				}
				event.setCancelled(true);
			}
		}
	}
	
	
	
	
	@EventHandler
	public void onEntitySpawn(EntitySpawnEvent event) {
		if(event.getEntity() instanceof ArmorStand) {
			ArmorStand stand = (ArmorStand)event.getEntity();
			if(!stand.isVisible() && !stand.isSmall()) {
				for(Entity e : stand.getNearbyEntities(.5, .5, .5)) {
					if(e instanceof Player) {
						Player sheriff = (Player)e;
						if(RPMagic.scoreboard.getObjective("role").getScore(sheriff.getName()).getScore()==5) {
							Bat bat = (Bat)stand.getWorld().spawnEntity(stand.getLocation(), EntityType.BAT);
		        			bat.setSilent(true);
		        			bat.setInvulnerable(true);
		        			bat.setAI(false);
		        			bat.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,200,0));
		        			bat.setLeashHolder(sheriff);
		        			bat.setVelocity(sheriff.getEyeLocation().getDirection());
		        			ItemStack lasso  = sheriff.getInventory().getItemInMainHand();
		        			sheriff.getInventory().removeItem(lasso);
		        			sheriff.playSound(sheriff.getLocation(), Sound.UI_TOAST_IN, 5F, 3F);
		        			new BukkitRunnable(){
		        		        @Override
		        		        public void run(){
		        		        	if(stand.isDead()) {
		        		        		cancel();
		        		        		bat.remove();
		        		        		sheriff.getInventory().addItem(lasso);
		        		        		return;
		        		        	}
		        		        	else{
		        		        		bat.teleport(stand);
		        		        		for(Entity e : stand.getNearbyEntities(.85, .85, .85)) {
		        		        			if(e instanceof Player) {
		        		        				Player crook = (Player)e;
		        		        				if(crook.equals(sheriff))
		        		        					continue;
		        		        				else if(crook.getHealth()>=12) {
		        		        					crook.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,100,2));
		        		        					crook.sendMessage(ChatColor.GOLD+"You have enough health to untangle yourself from the lasso");
		        		        					sheriff.sendMessage(ChatColor.DARK_AQUA+"The target had enough health to resist arrest");
		        		        					stand.remove();
		        		        				}
		        		        				else {
		        		        					crook.setGameMode(GameMode.ADVENTURE);
		        		        					crooks.put(sheriff, crook);
		        									sheriff.addPassenger(crook);
		        									crook.sendMessage(ChatColor.DARK_AQUA+"The sheriff grabs you with their lasso, ties you up, and throws you over their shoulder.");
		        									sheriff.sendMessage(ChatColor.GOLD+"You captured a crook! Right click them with your bare hand to let them down.");
		        									sheriff.chat(ChatColor.ITALIC+"You're coming with me, criminal scum!");
		        									stand.remove();
		        		        				}
		        		        			}
		        		        			else if(e instanceof LivingEntity && !(e instanceof Bat)){
		        		        				((LivingEntity)e).addPotionEffect(new PotionEffect(PotionEffectType.SLOW,100,2));
		        		        				stand.remove();
		        		        			}
		        		        		}
		        		        	}
		        		        }
		        		   }.runTaskTimer(RPMagic.instance, 1L, 1L);
						}
					}
				}
			}
		}
	}
	@EventHandler
	public void clickEntity(PlayerInteractEntityEvent event) {
		ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
		if(item==null || item.getType().equals(Material.AIR)) {
			if(event.getRightClicked() instanceof Player) {
				Player crook = (Player)event.getRightClicked();
				if(event.getPlayer().getPassengers().contains(crook) && !event.getPlayer().isSneaking()) {
					crook.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,140,100));
					event.getPlayer().removePassenger(crook);
					crook.addScoreboardTag("dismounting");
					event.getPlayer().sendMessage(ChatColor.AQUA+"You put the crook down. Shift+Right click them with your bare hand to set them free");
				}
				else if(crooks.containsKey(event.getPlayer()) && crooks.get(event.getPlayer()).equals(crook) && event.getPlayer().isSneaking()) {
					crooks.remove(event.getPlayer());
					crook.setGameMode(GameMode.SURVIVAL);
					crook.sendMessage(ChatColor.GOLD+"You're free!");
					event.getPlayer().sendMessage(ChatColor.DARK_AQUA+"You let them free");
				}
			}
		}
		else if(item.getType().equals(Material.LEAD) && item.getItemMeta().getLore()!=null && item.getItemMeta().getLore().contains("Justice!")) {
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		if(crooks.containsKey(event.getPlayer())) {
			crooks.get(event.getPlayer()).setGameMode(GameMode.SURVIVAL);
			crooks.get(event.getPlayer()).sendMessage(ChatColor.GOLD+"Your jailor left, you're free!");
			crooks.remove(event.getPlayer());
		}
	}
	@EventHandler
	public void dismount(EntityDismountEvent event) {
		new BukkitRunnable(){
	        @Override
	        public void run(){
	        	if(event.getDismounted() instanceof Player && event.getEntity() instanceof Player) {
	    			Player sheriff = (Player)event.getDismounted();
	    			Player crook = (Player)event.getEntity();
	    			if(crooks.containsKey(sheriff) && crooks.get(sheriff).equals(crook) && !crook.getScoreboardTags().contains("dismounted"))
	    				sheriff.addPassenger(crook);
	    		}
	        }
	   }.runTaskLater(RPMagic.instance, 1L);
	}
}
