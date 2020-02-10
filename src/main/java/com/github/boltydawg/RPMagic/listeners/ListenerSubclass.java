package com.github.boltydawg.RPMagic.listeners;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.block.ShulkerBox;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.entity.EntityDismountEvent;
import org.spigotmc.event.entity.EntityMountEvent;

import com.github.boltydawg.RPMagic.RPMagic;
import com.github.boltydawg.RPMagic.runnables.RunnableLastDamage;
import com.github.boltydawg.RPMagic.runnables.RunnableMarine;
import com.github.boltydawg.RPMagic.runnables.RunnableRegen;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * 1 Barbarian
 * 2 Knight
 * 3 Marine
 * 4 Tank
 * 5 Angel
 * 6 Arcane Bowman
 * 7 Assassin 
 * 8 Scout
 * 
 * @author BoltyDawg
 *
 */
public class ListenerSubclass implements Listener{
	private HashMap<Player,Boolean> cool1 = new HashMap<Player,Boolean>();
	private HashMap<Player,Boolean> cool2 = new HashMap<Player,Boolean>();
	private HashMap<Player,Location> tele = new HashMap<Player,Location>();
	
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		boolean block = event.getAction().equals(Action.RIGHT_CLICK_BLOCK);
		boolean air = event.getAction().equals(Action.RIGHT_CLICK_AIR);
		if(air || block) {
			Player player = event.getPlayer();
			ItemStack item = event.getItem();
			int sub = RPMagic.scoreboard.getObjective("subclass").getScore(player.getName()).getScore();
			if(item!=null && item.getType()!=Material.AIR) {
				//Barbarian Rage
				if(item.getType().equals(Material.BLAZE_POWDER) && item.getItemMeta().getLore()!=null && item.getItemMeta().getLore().contains(ChatColor.DARK_RED + "GRRRAAAAGHHH")) {
					if(sub==1) {
						player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,260,2));
						player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,260,0));
						player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,260,1));
						player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,260,2));
						player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING,260,7));
						RPMagic.scoreboard.getObjective("damage").getScore(player.getName()).setScore(0);
						RPMagic.rageBars.get(player.getUniqueId()).setProgress(0);
						player.getInventory().removeItem(item);
						new BukkitRunnable(){
					        @Override
					        public void run(){
					        	player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,160,1));
								player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING,160,1));
					        }
					   }.runTaskLater(RPMagic.instance, 260L);
					}
					else {
						TextComponent msg = new TextComponent();
						msg.setText("IT BURNS YOUR HAND");
						msg.setColor(ChatColor.YELLOW);
						player.spigot().sendMessage(ChatMessageType.ACTION_BAR,msg);
						player.damage(1);
					}
					event.setCancelled(true);
				}
				//Barbarians equipping bone
				else if(sub==1 && item.getType().equals(Material.BONE) && item.getItemMeta().getLore()!=null) {
					if(player.getInventory().getHelmet()!=null && player.getInventory().getHelmet().getType()!=Material.AIR){
						player.sendMessage(ChatColor.DARK_PURPLE+"YOU EAT YOUR CURRENT BONE");
						player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 3.0F, 1F);
					}
					ItemStack bone = item.clone();
					bone.setAmount(1);
					player.getInventory().setHelmet(bone);
					item.setAmount(item.getAmount()-1);
					event.setCancelled(true);
				}
				else if(sub==1 && item.getType().equals(Material.SHIELD)) {
					int stam = RPMagic.scoreboard.getObjective("Stamina").getScore(player.getName()).getScore();
					if(!player.isBlocking() && stam>=75 && player.isSprinting()) {
						Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"castp "+player.getName()+" dash");
						RPMagic.scoreboard.getObjective("Stamina").getScore(player.getName()).setScore(stam-75);
						RunnableRegen.fighterRegen(player);
					}
				}
				//Tank shield
				else if(sub==4 && event.getHand().equals(EquipmentSlot.HAND) && item.getType().equals(Material.SHIELD) && item.getItemMeta().getLore()!=null && item.getItemMeta().getLore().contains("An Unbreakable Wall")){
					org.bukkit.inventory.meta.Damageable dmgable = (org.bukkit.inventory.meta.Damageable)item.getItemMeta();
					int d = dmgable.getDamage();
					if(d<=294 && !player.isBlocking()) {
						Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"castp "+player.getName()+" pull");
						if(!cool1.getOrDefault(player,false)) {
							event.setCancelled(true);
							dmgable.setDamage(d+42);
							item.setItemMeta((ItemMeta)dmgable);
							cool1.put(player, true);
							new BukkitRunnable(){
						        @Override
						        public void run(){
						        	cool1.remove(player);
						        	TextComponent msg = new TextComponent();
									msg.setText("Shield recharged!");
									msg.setColor(ChatColor.LIGHT_PURPLE);
									player.spigot().sendMessage(ChatMessageType.ACTION_BAR,msg);
									return;
						        }
						   }.runTaskLater(RPMagic.instance, 200L);
						}
					}
				}
				//Assassins
				else if(sub==7) {
					//Cloak
					if(item!=null && item.getType().equals(Material.CLOCK) && item.getItemMeta().getLore()!=null && item.getItemMeta().getLore().contains(ChatColor.GRAY+"Was there ever any doubt?")) { //Make the lore in dark red?
						if(event.getHand().equals(EquipmentSlot.HAND)) {
							if(player.hasPotionEffect(PotionEffectType.INVISIBILITY) && player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
								return;
							int dmg = RunnableLastDamage.timeSinceLastDamage(player);
							if(dmg<=5) {
								TextComponent msg = new TextComponent();
								msg.setText("You have recently taken damage and must wait another "+(5-dmg)+" seconds");
								msg.setColor(ChatColor.AQUA);
								player.spigot().sendMessage(ChatMessageType.ACTION_BAR,msg);
								return;
							}
							Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"castp "+player.getName()+" cloak");
							if(!cool2.getOrDefault(player, false)) {	
								player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,300,0,false,true));
								event.setCancelled(true);
								cool2.put(player, true);
								double stat = player.getAttribute(Attribute.GENERIC_ARMOR).getValue();
								ItemStack[] armors = player.getInventory().getArmorContents();
								player.getInventory().setArmorContents(new ItemStack[]{null,null,null,null});
								player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(stat);
								new BukkitRunnable() {
									@Override
									public void run() {
										player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(0);
										player.getInventory().setArmorContents(armors);
									}
								}.runTaskLater(RPMagic.instance, 300);
								new BukkitRunnable(){
							        @Override
							        public void run(){
							        	cool2.remove(player);
							        	TextComponent msg = new TextComponent();
										msg.setText("Cloak recharged!");
										msg.setColor(ChatColor.LIGHT_PURPLE);
										player.spigot().sendMessage(ChatMessageType.ACTION_BAR,msg);
										return;
							        }
							   }.runTaskLater(RPMagic.instance, 900L);
							}
						}
						//blink
						else {
							int dmg = RunnableLastDamage.timeSinceLastDamage(player);
							if(dmg<=2) {
								TextComponent msg = new TextComponent();
								msg.setText("You have recently taken damage and must wait another "+(2-dmg)+" seconds");
								msg.setColor(ChatColor.AQUA);
								player.spigot().sendMessage(ChatMessageType.ACTION_BAR,msg);
								return;
							}
							Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"castp "+player.getName()+" blink");
							if(!cool1.getOrDefault(player, false)) {
								event.setCancelled(true);
								cool1.put(player, true);
								new BukkitRunnable(){
							        @Override
							        public void run(){
							        	cool1.remove(player);
							        	TextComponent msg = new TextComponent();
										msg.setText("Blink recharged!");
										msg.setColor(ChatColor.LIGHT_PURPLE);
										player.spigot().sendMessage(ChatMessageType.ACTION_BAR,msg);
										return;
							        }
							   }.runTaskLater(RPMagic.instance, 140L);
							}
						}
						event.setCancelled(true);
					}
				}
				else if(sub==3 && event.getHand().equals(EquipmentSlot.OFF_HAND) && item.getType().equals(Material.TRIDENT) && player.isSneaking()) {
					RunnableMarine.callToArms(player);
				}
//				//Angel Rocket
//				else if(sub==5) {
//					if(item.getType().equals(Material.FIREBALL) && item.getItemMeta().getLore()!=null && item.getItemMeta().getLore().contains("Take flight!")) {
//						event.setCancelled(true);
//						if(p.isGliding()) {
//							Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"castp "+p.getName()+" fling|3");
//							item.setAmount(1);
//							p.getInventory().removeItem(item);
//					}
//						else 
//							p.sendMessage("You must be flying to use this item!");
//					}
//					else {
//						ItemStack elytra = p.getInventory().getItemInOffHand();
//						if(elytra!=null && elytra.getType().equals(Material.ELYTRA)) {
//							event.setCancelled(true);
//							short dur = (short)(elytra.getDurability()+10);
//							if(dur>=432)
//								elytra.setDurability((short)432);
//							else {
//								elytra.setDurability(dur);
//								p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,120,2));
//							}
//						}
//					}
//				}
			}
		}
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		if(event.getDamager() instanceof Player) {
			Player player = ((Player)event.getDamager());
			int sub = RPMagic.scoreboard.getObjective("subclass").getScore(player.getName()).getScore();
			if(sub==1 || sub==2) {
				int dmg;
				if(player.getInventory().getItemInMainHand()==null || player.getInventory().getItemInMainHand().getType().equals(Material.AIR))
					dmg = (int)Math.round(event.getFinalDamage()*1.8 + RPMagic.scoreboard.getObjective("damage").getScore(player.getName()).getScore());
				else
					dmg= (int)Math.round(event.getFinalDamage() + RPMagic.scoreboard.getObjective("damage").getScore(player.getName()).getScore());
				RPMagic.scoreboard.getObjective("damage").getScore(player.getName()).setScore(dmg);
				//Barbarian Rage
				if(sub==1) {
					if(dmg>=RPMagic.RAGE) {
						giveRageDrop(player);
						RPMagic.rageBars.get(player.getUniqueId()).setProgress(1.0);
					}
					else
						RPMagic.rageBars.get(player.getUniqueId()).setProgress((double)dmg/RPMagic.RAGE);
				}
				//Knight Saturation
				else if(sub==2) {
					int f = player.getFoodLevel();
					if(f<19 && dmg>=200) {
						player.setFoodLevel(f+1);
						RPMagic.scoreboard.getObjective("damage").getScore(player.getName()).setScore(0);
					}
				}
			}
		}
	}
	public static void giveRageDrop(Player player) {
		ItemStack drop = new ItemStack(Material.BLAZE_POWDER);
		ItemMeta met = drop.getItemMeta();
		met.setDisplayName(ChatColor.RED+"BARBARIAN, "+ChatColor.DARK_RED+"RAGE");
		ArrayList<String> lst = new ArrayList<String>();
		lst.add(ChatColor.DARK_RED + "GRRRAAAAGHHH");
		met.setLore(lst);
		met.addEnchant(Enchantment.BINDING_CURSE, 1, false);
		met.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		drop.setItemMeta(met);
		if(!player.getInventory().contains(drop))
			player.getInventory().addItem(drop);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamageEvent(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player)event.getEntity();
			
			if(event.getCause()!=DamageCause.FALL)
				RunnableLastDamage.startCounter(player);
			
			int sub = RPMagic.scoreboard.getObjective("subclass").getScore(player.getName()).getScore();
			//Angels
			if(sub==5) {
				if(event.getCause() == EntityDamageEvent.DamageCause.FLY_INTO_WALL) {
					boolean b=false;;
					for(Entity e : player.getNearbyEntities(5, 5, 5)) {
						if(e instanceof Damageable) {
							Damageable d = (Damageable) e;
							b=true;
							if(e instanceof Player) 
								d.damage(event.getDamage()/2);
							else
								d.damage(event.getDamage());
						}
					}
					if(b) {
						player.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, player.getLocation(), 20);
					}
					event.setCancelled(true);
					player.setGliding(false);
				}
				else if(event.getCause() == EntityDamageEvent.DamageCause.FALL) {
					ItemStack boots = player.getInventory().getBoots();
					if(boots.getType().equals(Material.GOLDEN_BOOTS)) {
						ItemMeta met = boots.getItemMeta();
						if(met.getLore().contains("Sky above,") && player.getFallDistance()>=10) {
							TNTPrimed tnt = ((TNTPrimed)player.getWorld().spawnEntity(player.getLocation(), EntityType.PRIMED_TNT));
							tnt.setFuseTicks(0);
							tnt.setYield(player.getFallDistance()/10);
							boots.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, 100);
							new BukkitRunnable(){
						        @Override
						        public void run(){
						        	boots.removeEnchantment(Enchantment.PROTECTION_EXPLOSIONS);
						        }
						   }.runTaskLater(RPMagic.instance, 10L);
						}
					}
					boolean b = false;
					for(Entity e : player.getNearbyEntities(5, 5, 5)) {
						if(e instanceof Damageable) {
							b = true;
							Damageable d = (Damageable) e;
							if(e instanceof Player) {
								d.damage(event.getDamage()/2);
								((Player)d).playSound(player.getLocation(), Sound.ENTITY_GENERIC_BIG_FALL, 3.0F, 1F);
							}
							else
								d.damage(event.getDamage());
						}
					}
					if(b) {
						player.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, player.getLocation(), 20);
					}
					event.setDamage(event.getDamage()*.66666);
				}
			}
			//Barbarians
			else if(sub==1) {
				int rage = (int)Math.round(event.getDamage()+RPMagic.scoreboard.getObjective("damage").getScore(player.getName()).getScore());
				if(rage>=RPMagic.RAGE) {
					RPMagic.scoreboard.getObjective("damage").getScore(player.getName()).setScore(rage);
					RPMagic.rageBars.get(player.getUniqueId()).setProgress(1.0);
					giveRageDrop(player);
				}
				else if(rage>=0){
					RPMagic.scoreboard.getObjective("damage").getScore(player.getName()).setScore(rage);
					RPMagic.rageBars.get(player.getUniqueId()).setProgress((double)rage/RPMagic.RAGE);
				}
			}
			//Tank shield regen
			else if(sub==4) {
				ItemStack shield = player.getInventory().getItemInOffHand();
				if(shield!=null && shield.getType().equals(Material.SHIELD) && shield.getItemMeta().getLore()!=null && shield.getItemMeta().getLore().contains("An Unbreakable Wall")) {
					org.bukkit.inventory.meta.Damageable dmgable = (org.bukkit.inventory.meta.Damageable)shield.getItemMeta();
					int s = (int)Math.round(dmgable.getDamage()-(2*event.getDamage()));
					if(s>=0) {
						dmgable.setDamage(s);
						shield.setItemMeta((ItemMeta)dmgable);
					}
					else {
						dmgable.setDamage(0);
						shield.setItemMeta((ItemMeta)dmgable);
					}
				}
			}
		}
	}
	//Angels--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	@EventHandler
	public void onToggleSneakEvent(PlayerToggleSneakEvent event) {
		if(event.getPlayer().isGliding()) 
			event.getPlayer().setGliding(false);
	}
	//Knights and Scouts---------------------------------------------------------------------------------------------------------------------------------------------------------------------
	private static ItemStack knightJavelin() {
		ItemStack jav = new ItemStack(Material.TRIDENT);
		ItemMeta met = jav.getItemMeta();
		met.addEnchant(Enchantment.LOYALTY, 1, true);
		met.addEnchant(Enchantment.VANISHING_CURSE,1,true);
		met.setDisplayName("Knight Javelin");
		met.setUnbreakable(true);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.DARK_RED+"Fear cuts deeper than swords");
		met.setLore(lore);
		jav.setItemMeta(met);
		return jav;
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if(event.getEntityType().equals(EntityType.HORSE) && event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.SPAWNER_EGG)) {
			new BukkitRunnable(){
		        @Override
		        public void run(){
		        	for(Entity e : event.getEntity().getNearbyEntities(6, 6, 6)) {
						if(e instanceof Player) {
							Player player = (Player)e;
							String name = RPMagic.getName(player);
							if(event.getEntity().getScoreboardTags().contains(name+" steed")) {
								player.getInventory().addItem(collectEgg("Steed"));
								return;
							}
							else if(event.getEntity().getScoreboardTags().contains(name+" elybris")) {
								player.getInventory().addItem(collectEgg("Elybris"));
								return;
							}
						}
		        	}
		        }
		   }.runTaskLater(RPMagic.instance, 1L);
			
		}
	}
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Material mat = event.getBlockPlaced().getType();
		if(mat.equals(Material.PLAYER_HEAD) || mat.equals(Material.PLAYER_WALL_HEAD)) {
			event.setCancelled(true);
			TextComponent msg = new TextComponent();
			msg.setText("If you placed a player head, it would lose it's data!");
			msg.setColor(ChatColor.YELLOW);
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR,msg);
		}
		if(RPMagic.scoreboard.getObjective("subclass").getScore(player.getName()).getScore()==8) {
			if(mat.equals(Material.SEA_LANTERN) && (player.getInventory().getItemInMainHand().containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL) || player.getInventory().getItemInOffHand().containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL))) {
				if(player.getWorld().getHighestBlockYAt(event.getBlockPlaced().getLocation())-1<=event.getBlockPlaced().getLocation().getY()) {
					if(RPMagic.beacons.containsKey(player.getUniqueId())) {
						for(Entity e : player.getWorld().getNearbyEntities(RPMagic.beacons.get(player.getUniqueId()),1,1,1)) {
							if(e.getType().equals(EntityType.ARMOR_STAND)) 
								e.remove();
						}
						RPMagic.beacons.get(player.getUniqueId()).getBlock().breakNaturally();
					}
					RPMagic.beacons.put(player.getUniqueId(), event.getBlockPlaced().getLocation());
					event.getBlockPlaced().setType(Material.END_GATEWAY);
					ArmorStand as = ((ArmorStand)player.getWorld().spawnEntity(event.getBlockPlaced().getLocation().add(.5,0,.5), EntityType.ARMOR_STAND));
					as.setCustomName(RPMagic.getName(player)+"'s Waypoint");
					as.setCustomNameVisible(true);
					as.setHelmet(new ItemStack(Material.BEACON));
					as.setSmall(true);
				}
				else {
					event.setCancelled(true);
					TextComponent msg = new TextComponent();
					msg.setText("There can't be any blocks above your beacon!");
					msg.setColor(ChatColor.YELLOW);
					player.spigot().sendMessage(ChatMessageType.ACTION_BAR,msg);
				}
			}
			else if(mat.equals(Material.BEDROCK) && (player.getInventory().getItemInMainHand().containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL) || player.getInventory().getItemInOffHand().containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL))) {
				if(RPMagic.beacons.containsKey(player.getUniqueId())) {
					if(player.getWorld().getBlockAt(RPMagic.beacons.get(player.getUniqueId())).getType().equals(Material.END_GATEWAY)) {
						if(player.getWorld().getHighestBlockYAt(event.getBlockPlaced().getLocation())-1<=event.getBlockPlaced().getLocation().getY()) {
							int dmg = RunnableLastDamage.timeSinceLastDamage(player);
							if(dmg<10) {
								TextComponent msg = new TextComponent();
								msg.setText("You have recently taken damage and must wait another "+(10-dmg)+" seconds");
								msg.setColor(ChatColor.AQUA);
								player.spigot().sendMessage(ChatMessageType.ACTION_BAR,msg);
								event.setCancelled(true);
								return;
							}
							if(tele.containsKey(player))
								tele.get(player).getBlock().breakNaturally();
							event.getBlockPlaced().setType(Material.NETHER_PORTAL);
							tele.put(player, event.getBlockPlaced().getLocation());
							new BukkitRunnable(){
						        @Override
						        public void run(){
						        	event.getBlockPlaced().breakNaturally();
						        }
						   }.runTaskLater(RPMagic.instance, 600L);
						}
						else {
							event.setCancelled(true);
							TextComponent msg = new TextComponent();
							msg.setText("There can't be any blocks above this teleporter");
							msg.setColor(ChatColor.YELLOW);
							player.spigot().sendMessage(ChatMessageType.ACTION_BAR,msg);
						}
					}
					else {
						event.setCancelled(true);
						TextComponent msg = new TextComponent();
						msg.setText("Your beacon is either missing or destroyed");
						msg.setColor(ChatColor.YELLOW);
						player.spigot().sendMessage(ChatMessageType.ACTION_BAR,msg);
					}
				}
				else {
					TextComponent msg = new TextComponent();
					msg.setText("You haven't placed a beacon yet");
					msg.setColor(ChatColor.YELLOW);
					player.spigot().sendMessage(ChatMessageType.ACTION_BAR,msg);
				}
			}
		}
		else if(mat.equals(Material.RED_SHULKER_BOX)) {
			if(RPMagic.scoreboard.getObjective("subclass").getScore(player.getName()).getScore()!=2){
				ItemStack[] is =((ShulkerBox)event.getBlockPlaced().getState()).getInventory().getStorageContents();
				for(int i=0;i<is.length;i++) {
					if(is[i]!=null)
						event.getBlockPlaced().getWorld().dropItem(event.getBlockPlaced().getLocation(), is[i]);
				}
				event.setCancelled(true);
	        	event.getPlayer().getInventory().remove(Material.RED_SHULKER_BOX);
	        	event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ITEM_SHIELD_BREAK, 3.0F, .5F);
	        	TextComponent msg = new TextComponent();
				msg.setText("The box broke without its owner");
				msg.setColor(ChatColor.YELLOW);
				event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,msg);
			}
		}
		else if(mat.equals(Material.BROWN_SHULKER_BOX)) {
			if(RPMagic.scoreboard.getObjective("subclass").getScore(player.getName()).getScore()!=8){
				ItemStack[] is =((ShulkerBox)event.getBlockPlaced().getState()).getInventory().getStorageContents();
				for(int i=0;i<is.length;i++) {
					if(is[i]!=null)
						event.getBlockPlaced().getWorld().dropItem(event.getBlockPlaced().getLocation(), is[i]);
				}
				event.setCancelled(true);
	        	event.getPlayer().getInventory().remove(Material.BROWN_SHULKER_BOX);
	        	event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ITEM_SHIELD_BREAK, 3.0F, .5F);
	        	TextComponent msg = new TextComponent();
				msg.setText("The box broke without its owner");
				msg.setColor(ChatColor.YELLOW);
				event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,msg);
			}
		}
	}
	@EventHandler
	public void onEntityPortalEnter(EntityPortalEnterEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = ((Player)event.getEntity());
			if(tele.containsValue(event.getLocation())) {
				if(event.getLocation().equals(tele.get(player))) {
					player.teleport(RPMagic.beacons.get(player.getUniqueId()));
					tele.get(player).getBlock().breakNaturally();
					tele.remove(player);
				}
				else {
					TextComponent msg = new TextComponent();
					msg.setText("You are denied access!");
					msg.setColor(ChatColor.YELLOW);
					player.spigot().sendMessage(ChatMessageType.ACTION_BAR,msg);
					event.getLocation().getBlock().breakNaturally();
				}
			}
		}
	}
	@EventHandler
	public void onEntityDeathEvent(EntityDeathEvent event) {
		if(event.getEntityType().equals(EntityType.ARMOR_STAND) && event.getEntity().getLocation().getBlock().getType().equals(Material.END_GATEWAY)) {
			event.getEntity().getLocation().getBlock().breakNaturally();
			event.getDrops().clear();
		}
	}
	@EventHandler
	public void clickEntity(PlayerInteractEntityEvent event) {
		if(event.getRightClicked().getType().equals(EntityType.HORSE)) {
			Player player = event.getPlayer();
			Entity horse = event.getRightClicked();
			ItemStack main = player.getInventory().getItemInMainHand();
			ItemStack off = player.getInventory().getItemInOffHand();
			if(player.isSneaking()) {
				if(main.equals(collectEgg("Steed")) || off.equals(collectEgg("Steed"))) {
					if(horse.getScoreboardTags().contains(RPMagic.getName(player)+" steed")) {
						event.setCancelled(true);
						Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"castp "+player.getName()+" capture");
						player.getInventory().remove(collectEgg("Steed"));
					}
					else {
						event.setCancelled(true);
						TextComponent msg = new TextComponent();
						msg.setText("This is not your horse!");
						msg.setColor(ChatColor.YELLOW);
						player.spigot().sendMessage(ChatMessageType.ACTION_BAR,msg);
					}
				}
				else if(main.equals(collectEgg("Elybris")) || off.equals(collectEgg("Elybris"))) {
					if(horse.getScoreboardTags().contains(RPMagic.getName(player)+" elybris")) {
						event.setCancelled(true);
						Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"castp "+player.getName()+" capture");
						player.getInventory().remove(collectEgg("Elybris"));
					}
					else {
						event.setCancelled(true);
						TextComponent msg = new TextComponent();
						msg.setText("This is not your horse!");
						msg.setColor(ChatColor.YELLOW);
						player.spigot().sendMessage(ChatMessageType.ACTION_BAR,msg);
					}
				}
			}
		}
	}
	@EventHandler
	public void onMountEvent(EntityMountEvent event) {
		if(event.getEntity() instanceof Player && event.getMount().getType().equals(EntityType.HORSE)) {
			Player player = ((Player)event.getEntity());
			for(String s : event.getMount().getScoreboardTags()) {
				if(s.contains(" steed")) {
					if(!((RPMagic.getName(player)+" steed").equals(s))) {
						event.setCancelled(true);
						player.sendMessage(ChatColor.YELLOW+"The horse thrusts you off, in recognition that you aren't its master!");
					}
				}
				else if(s.contains(" elybris")) {
					if(!((RPMagic.getName(player)+" elybris").equals(s))) {
						event.setCancelled(true);
						player.sendMessage(ChatColor.YELLOW+"The horse runs away before you can mount it!");
					}
				}
			}
			if(event.getMount().getScoreboardTags().contains(RPMagic.getName(player)+" steed") && RPMagic.scoreboard.getObjective("subclass").getScore(player.getName()).getScore()==2 && player.getInventory().firstEmpty()!=-1) {
				player.getInventory().addItem(knightJavelin());
			}
		}
	}
	@EventHandler
	public void onEntityDismountEvent(EntityDismountEvent event) {
		if(event.getEntity() instanceof Player && event.getDismounted().getType().equals(EntityType.HORSE)) {
			Player player = ((Player)event.getEntity());
			if(event.getDismounted().getScoreboardTags().contains(RPMagic.getName(player)+" steed") && RPMagic.scoreboard.getObjective("subclass").getScore(player.getName()).getScore()==2) {
				ItemStack knightJavelin = knightJavelin();
				if(player.getInventory().contains(knightJavelin))
					player.getInventory().remove(knightJavelin);
				else {
					new BukkitRunnable(){
        		        @Override
        		        public void run(){
        		        	if(player.getInventory().contains(knightJavelin)) {
        						player.getInventory().remove(knightJavelin);
        						cancel();
        						return;
        		        	}
        		        	else if(player.isInsideVehicle() || !player.isOnline()) {
        		        		cancel();
        		        		return;
        		        	}
        		        }
        		   }.runTaskTimer(RPMagic.instance, 2L, 2L);
				}
			}
		}
	}
	@EventHandler
	public void dropItem(PlayerDropItemEvent event) {
		if(event.getItemDrop().getItemStack().equals(knightJavelin())) {
			event.setCancelled(true);
		}
	}
	
	private ItemStack collectEgg(String horse) {
		ItemStack egg = new ItemStack(Material.POPPED_CHORUS_FRUIT);
		ItemMeta met = egg.getItemMeta();
		met.setDisplayName("Capture "+horse);
		met.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
		met.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("Shift+right click your horse");
		met.setLore(lore);
		egg.setItemMeta(met);
		return egg;
	}
	//Barbarians-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	@EventHandler
	public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event) {
		Player player = event.getPlayer();
		//Barbarians eat food differently than regular players. They get buffs for eating raw meat, and can't eat cooked meat.
		if(RPMagic.scoreboard.getObjective("subclass").getScore(player.getName()).getScore()==1){
			Material mat = event.getItem().getType();
			if(mat.equals(Material.BEEF) || mat.equals(Material.PORKCHOP)) {
				int h = player.getFoodLevel()+8;
				if(h>=20) {
					player.setFoodLevel(20);
					player.setSaturation(player.getSaturation()+12.8f);
				}
				else {
					player.setFoodLevel(h);
				}
				player.getInventory().removeItem(new ItemStack(event.getItem().getType(),1));
				event.setCancelled(true);
				player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING,320,2));
				player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,320,0));
			}
			else if(mat.equals(Material.CHICKEN) || mat.equals(Material.MUTTON) || mat.equals(Material.RABBIT)) {
				int h = player.getFoodLevel()+6;
				if(h>=20) {
					player.setFoodLevel(20);
					player.setSaturation(player.getSaturation()+7.2f);
				}
				else {
					player.setFoodLevel(h);
				}
				player.getInventory().removeItem(new ItemStack(event.getItem().getType(),1));
				event.setCancelled(true);
				player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING,320,2));
				player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,320,0));
			}
			else if(mat.equals(Material.COOKED_BEEF) || mat.equals(Material.COOKED_CHICKEN) || mat.equals(Material.COOKED_MUTTON) || mat.equals(Material.COOKED_RABBIT) || mat.equals(Material.COOKED_PORKCHOP)) {
				TextComponent msg = new TextComponent();
				msg.setText("THE CHARRED MEAT MAKES YOU FEEL SICK");
				msg.setColor(ChatColor.DARK_BLUE);
				player.spigot().sendMessage(ChatMessageType.ACTION_BAR,msg);
				player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,100,0));
				player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER,100,2));
				player.getInventory().removeItem(new ItemStack(event.getItem().getType(),1));
				event.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		if(RPMagic.local) {
			event.getRecipients().clear();
			for(Player e: RPMagic.instance.getServer().getOnlinePlayers()) {
				if(e.getWorld().equals(event.getPlayer().getWorld()) && e.getLocation().distance(event.getPlayer().getLocation()) <=125)
					event.getRecipients().add((Player)e);
			}
			event.getRecipients().add(event.getPlayer());
		}
		//barbarian caps lock
		if(event.getMessage().charAt(0)!='/' && RPMagic.scoreboard.getObjective("subclass").getScore(event.getPlayer().getName()).getScore()==1) {
			event.setMessage(event.getMessage().toUpperCase());
		}
	}
	//ARCANE BOWMEN------------------------------------------------------------------------------------------------------------------------------------
	@EventHandler
	public void shootBow(EntityShootBowEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player)event.getEntity();
			if(RPMagic.scoreboard.getObjective("subclass").getScore(player.getName()).getScore()==6) {
				if(event.getProjectile() instanceof Arrow) {
					Arrow arrow = ((Arrow)event.getProjectile());
					PotionType effect = arrow.getBasePotionData().getType();
					player.sendMessage("Hi if you see this please tell Jason he's an idiot for leaving this in. But also tell him that the potion type is " + effect.name());
					//These are in all caps
					if(effect.equals(PotionType.NIGHT_VISION)) {
						int dmg = RunnableLastDamage.timeSinceLastDamage(player);
						if(dmg<10) {
							TextComponent msg = new TextComponent();
							msg.setText("You have recently taken damage and must wait another "+(10-dmg)+" seconds");
							msg.setColor(ChatColor.AQUA);
							player.spigot().sendMessage(ChatMessageType.ACTION_BAR,msg);
							event.setCancelled(true);
							addArrow(arrow.getBasePotionData(),arrow.getColor(),"Teleportation Arrow", Enchantment.PROTECTION_ENVIRONMENTAL,player,true);
							return;
						}
						arrow.removeCustomEffect(PotionEffectType.NIGHT_VISION);
						new BukkitRunnable(){
	        		        @Override
	        		        public void run(){
	        		        	if(arrow.isInBlock()) {
	        		        		player.teleport(arrow);
	        		        		player.damage(2.0);
	        		        		arrow.remove();
	        		        		cancel();
	        		        		return;
	        		        	}
	        		        	else if(arrow.isDead()) {
	        		        		cancel();
	        		        		return;
	        		        	}
	        		        }
	        		   }.runTaskTimer(RPMagic.instance, 8L, 2L);
					}
					else if(effect.equals(PotionType.FIRE_RESISTANCE)) {
						int dmg = RunnableLastDamage.timeSinceLastDamage(player);
						if(dmg<=6) {
							TextComponent msg = new TextComponent();
							msg.setText("You have recently taken damage and must wait another "+(6-dmg)+" seconds");
							msg.setColor(ChatColor.AQUA);
							player.spigot().sendMessage(ChatMessageType.ACTION_BAR,msg);
							event.setCancelled(true);
							addArrow(arrow.getBasePotionData(),arrow.getColor(),"Explosive Arrow", Enchantment.PROTECTION_FIRE,player,true);
							return;
						}
						arrow.removeCustomEffect(PotionEffectType.FIRE_RESISTANCE);
						new BukkitRunnable(){
	        		        @Override
	        		        public void run(){
	        		        	if(arrow.isInBlock()) {
	        		        		TNTPrimed tnt = ((TNTPrimed)arrow.getWorld().spawnEntity(arrow.getLocation(), EntityType.PRIMED_TNT));
	        		        		tnt.setYield(2F);
	        		        		tnt.setFuseTicks(0);
	        		        		arrow.remove();
	        		        		cancel();
	        		        		return;
	        		        	}
	        		        	else if(arrow.isDead()) {
	        		        		cancel();
	        		        		return;
	        		        	}
	        		        }
	        		   }.runTaskTimer(RPMagic.instance, 8L, 2L);
					}
					else if(effect.equals(PotionType.JUMP)) {
						int dmg = RunnableLastDamage.timeSinceLastDamage(player);
						if(dmg<=4) {
							TextComponent msg = new TextComponent();
							msg.setText("You have recently taken damage and must wait another "+(4-dmg)+" seconds");
							msg.setColor(ChatColor.AQUA);
							player.spigot().sendMessage(ChatMessageType.ACTION_BAR,msg);
							event.setCancelled(true);
							addArrow(arrow.getBasePotionData(),arrow.getColor(),"Seeing Arrow", Enchantment.PROTECTION_FALL,player,true);
							return;
						}
						arrow.removeCustomEffect(PotionEffectType.JUMP);
						new BukkitRunnable(){
	        		        @Override
	        		        public void run(){
	        		        	if(arrow.isInBlock()) {
	        		        		for(Entity e:arrow.getNearbyEntities(50, 50, 50)) {
	        		        			if(e instanceof LivingEntity && e!=player) {
	        		        				((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,300,0));
	        		        			}
	        		        		}
	        		        		arrow.remove();
	        		        		cancel();
	        		        		return;
	        		        	}
	        		        	else if(arrow.isDead()) {
	        		        		cancel();
	        		        		return;
	        		        	}
	        		        }
	        		   }.runTaskTimer(RPMagic.instance, 8L, 2L);
					}
					else if(effect.equals(PotionType.LUCK)) {
						int dmg = RunnableLastDamage.timeSinceLastDamage(player);
						if(dmg<=4) {
							TextComponent msg = new TextComponent();
							msg.setText("You have recently taken damage and must wait another "+(4-dmg)+" seconds");
							msg.setColor(ChatColor.AQUA);
							player.spigot().sendMessage(ChatMessageType.ACTION_BAR,msg);
							event.setCancelled(true);
							addArrow(arrow.getBasePotionData(),arrow.getColor(),"Nausea Arrow", Enchantment.PROTECTION_EXPLOSIONS,player,true);
							return;
						}
						arrow.removeCustomEffect(PotionEffectType.LUCK);
						new BukkitRunnable(){
	        		        @Override
	        		        public void run(){
	        		        	if(arrow.isInBlock()) {
	        		        		for(Entity e:arrow.getNearbyEntities(50, 50, 50)) {
	        		        			if(e instanceof LivingEntity && e!=player) {
	        		        				((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,300,0));
	        		        			}
	        		        		}
	        		        		arrow.remove();
	        		        		cancel();
	        		        		return;
	        		        	}
	        		        	else if(arrow.isDead()) {
	        		        		cancel();
	        		        		return;
	        		        	}
	        		        }
	        		   }.runTaskTimer(RPMagic.instance, 8L, 2L);
					}
					else if(effect.equals(PotionType.SLOW_FALLING)) {
						int dmg = RunnableLastDamage.timeSinceLastDamage(player);
						if(dmg<=4) {
							TextComponent msg = new TextComponent();
							msg.setText("You have recently taken damage and must wait another "+(4-dmg)+" seconds");
							msg.setColor(ChatColor.AQUA);
							player.spigot().sendMessage(ChatMessageType.ACTION_BAR,msg);
							event.setCancelled(true);
							addArrow(arrow.getBasePotionData(),arrow.getColor(),"Loyal Arrow", Enchantment.PROTECTION_PROJECTILE,player,true);
							return;
						}
						PotionData data = arrow.getBasePotionData();
						arrow.removeCustomEffect(PotionEffectType.SLOW_FALLING);
						new BukkitRunnable(){
	        		        @Override
	        		        public void run(){
	        		        	if(arrow.isInBlock() || arrow.isDead()){
	        		        		addArrow(data,Color.WHITE,"Loyal Arrow", Enchantment.PROTECTION_PROJECTILE,player,false);
	        		        		player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 3.0F, 1F);
	        		        		arrow.remove();
	        		        		cancel();
	        		        		return;
	        		        	}
	        		        }
	        		   }.runTaskTimer(RPMagic.instance, 8L, 2L);
					}
				}
			}
		}
	}
	private void addArrow(PotionData data, Color color, String name, Enchantment ench, Player player,boolean cancel) {
		ItemStack offHand = player.getInventory().getItemInOffHand();
		if(offHand.getType().equals(Material.TIPPED_ARROW) && offHand.getItemMeta().hasEnchant(ench)) {
			if(cancel) {
				player.getInventory().setItemInOffHand(null);
				player.getInventory().setItemInOffHand(offHand);
			}
			else 
				offHand.setAmount(offHand.getAmount()+1);
		}	
		else {
			ItemStack arrow = new ItemStack(Material.TIPPED_ARROW);
			PotionMeta met = (PotionMeta)arrow.getItemMeta();
			met.setColor(color);
			met.setBasePotionData(data);
			met.setDisplayName(name);
			met.addEnchant(ench, 0, true);
			met.addItemFlags(ItemFlag.HIDE_ENCHANTS,ItemFlag.HIDE_POTION_EFFECTS);
			arrow.setItemMeta(met);
    		if(offHand==null || offHand.getType().equals(Material.AIR))
    			player.getInventory().setItemInOffHand(arrow);
    		else
    			player.getInventory().addItem(arrow);
		}
	}
}

