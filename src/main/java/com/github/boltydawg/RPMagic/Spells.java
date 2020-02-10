package com.github.boltydawg.RPMagic;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.boltydawg.RPMagic.runnables.RunnableRegen;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent;

import java.util.HashMap;
import java.util.List;

/**
 * This class deals with many miscellaneous tasks when it comes
 * to spells and the spell casting process.
 * I made this so that I can organize my code more precisely
 * @author BoltyDawg
 */
public class Spells{
	
	/**
	 * a list of all currently implemented spells
	 */
	public static final String[] ALL_SPELLS = new String[] {"beam","blessing","bomb","earthquake","entangle","fireball","flashbang","fury","ghast","grenade","heal","homing","lift","meteor","nuke","platform","shock","shower","singularity","skyfall","soothe","test","time","tornado","vallone","wave"};
	
	private static HashMap<Player,Boolean> cool = new HashMap<Player,Boolean>();
	
	/**
	 * Makes mages cast the specified spell
	 * @param player player that is casting this spell
	 * @param spell name of the spell that is being cast 
	 */
	public static void cast(Player player, String spell) {
		//checks if the player is a Mage
		if(RPMagic.scoreboard.getObjective("class") == null || RPMagic.scoreboard.getObjective("class").getScore(player.getName()).getScore()!=2) {player.sendMessage("You are not a mage... how did you do this? :O"); return;}
		if(spell!="beam" && !RPMagic.mages.get(player.getUniqueId()).contains(spell)){player.sendMessage(ChatColor.GRAY+"You haven't learned this spell!");return;}
		
		int mag = RPMagic.scoreboard.getObjective("Magicka").getScore(player.getName()).getScore();
		boolean enoughMagicka = true;
		switch(spell) {
			case"beam":{
				if(mag>=5) {
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"castp "+player.getName()+" beam");
					if(!cool.getOrDefault(player, false)) {
						RPMagic.scoreboard.getObjective("Magicka").getScore(player.getName()).setScore(mag-5);
						cool.put(player, true);
						new BukkitRunnable(){
					        @Override
					        public void run(){
					        	cool.remove(player);
					        }
					   }.runTaskLater(RPMagic.instance, 30);
					}
					else
						return;
				}
				else enoughMagicka = false;
				break;
			}
			case"blessing":{
				if(mag>=90) {
					player.chat(ChatColor.GRAY+ChatColor.ITALIC.toString()+"MEDICRA!");
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"castp "+player.getName()+" blessing");
					RPMagic.scoreboard.getObjective("Magicka").getScore(player.getName()).setScore(mag-90);
				}
				else enoughMagicka = false;
				break;
			}
			case"bomb":{
				if(mag>=60) {
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"castp "+player.getName()+" bomb");
					RPMagic.scoreboard.getObjective("Magicka").getScore(player.getName()).setScore(mag-60);
				}
				else enoughMagicka = false;
				break;
			}
			case "time":{
				if(mag>=100) {
					if(player.getWorld().getTime()<13000) {
						player.chat(ChatColor.GRAY+ChatColor.ITALIC.toString()+"Nivonla!");
						Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"castp "+player.getName()+" night");
					}
					else {
						player.chat(ChatColor.GRAY+ChatColor.ITALIC.toString()+"Davonla!");
						Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"castp "+player.getName()+" day");
					}
					RPMagic.scoreboard.getObjective("Magicka").getScore(player.getName()).setScore(mag-100);
				}
				else enoughMagicka = false;
				break;
			}
			case"earthquake":{
				if(mag>=33) {
					player.chat(ChatColor.GRAY+ChatColor.ITALIC.toString()+"Gratoris!");
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"castp "+player.getName()+" earthquake");
					RPMagic.scoreboard.getObjective("Magicka").getScore(player.getName()).setScore(mag-33);
				}
				else enoughMagicka = false;
				break;
			}
			case"entangle":{
				if(mag>=33) {
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"castp "+player.getName()+" entangle");
					RPMagic.scoreboard.getObjective("Magicka").getScore(player.getName()).setScore(mag-33);
				}
				else enoughMagicka = false;
				break;
			}
			case"fireball":{
				if(mag>=15) {
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"castp "+player.getName()+" fireball");
					RPMagic.scoreboard.getObjective("Magicka").getScore(player.getName()).setScore(mag-15);
				}
				else enoughMagicka = false;
				break;
			}
			case"flashbang":{
				if(mag>=15) {
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"castp "+player.getName()+" flashbang");
					RPMagic.scoreboard.getObjective("Magicka").getScore(player.getName()).setScore(mag-15);
				}
				else enoughMagicka = false;
				break;
			}
			case"fury":{
				if(mag>=20) {
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"castp "+player.getName()+" fury");
					RPMagic.scoreboard.getObjective("Magicka").getScore(player.getName()).setScore(mag-20);
				}
				else enoughMagicka = false;
				break;
			}
			case"ghast":{
				if(mag>=20) {
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"castp "+player.getName()+" ghast");
					RPMagic.scoreboard.getObjective("Magicka").getScore(player.getName()).setScore(mag-20);
				}
				else enoughMagicka = false;
				break;
			}
			case"grenade":{
				if(mag>=25) {
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"castp "+player.getName()+" grenade");
					RPMagic.scoreboard.getObjective("Magicka").getScore(player.getName()).setScore(mag-25);
				}
				else enoughMagicka = false;
				break;
			}
			case"heal":{
				if(mag>=40) {
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"castp "+player.getName()+" heal");
					RPMagic.scoreboard.getObjective("Magicka").getScore(player.getName()).setScore(mag-40);
				}
				else enoughMagicka = false;
				break;
			}
			case"homing":{
				if(mag>=15) {
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"castp "+player.getName()+" homing");
					RPMagic.scoreboard.getObjective("Magicka").getScore(player.getName()).setScore(mag-15);
				}
				else enoughMagicka = false;
				break;
			}
			case"lift":{
				if(mag>=15) {
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"castp "+player.getName()+" lift");
					RPMagic.scoreboard.getObjective("Magicka").getScore(player.getName()).setScore(mag-15);
				}
				else enoughMagicka = false;
				break;
			}
			case"meteor":{
				if(mag>=40) {
					player.chat(ChatColor.GRAY+ChatColor.ITALIC.toString()+"Gramador!");
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"castp "+player.getName()+" meteor");
					RPMagic.scoreboard.getObjective("Magicka").getScore(player.getName()).setScore(mag-40);
				}
				else enoughMagicka = false;
				break;
			}
			case"nuke":{
				if(mag>=100) {
					player.chat(ChatColor.GRAY+ChatColor.ITALIC.toString()+"DESTUNCION!");
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"castp "+player.getName()+" nuke");
					RPMagic.scoreboard.getObjective("Magicka").getScore(player.getName()).setScore(mag-100);
				}
				else enoughMagicka = false;
				break;
			}
			case "platform":{
				if(mag>=5) {
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"castp "+player.getName()+" platform");
					RPMagic.scoreboard.getObjective("Magicka").getScore(player.getName()).setScore(mag-5);
				}
				else enoughMagicka = false;
				break;
			}
			case"shock":{
				if(mag>=20) {
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"castp "+player.getName()+" shock");
					RPMagic.scoreboard.getObjective("Magicka").getScore(player.getName()).setScore(mag-20);
				}
				else enoughMagicka = false;
				break;
			}
			case"shower":{
				if(mag>=75) {
					player.chat(ChatColor.GRAY+ChatColor.ITALIC.toString()+"Metroscis!");
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"castp "+player.getName()+" shower");
					RPMagic.scoreboard.getObjective("Magicka").getScore(player.getName()).setScore(mag-75);
				}
				else enoughMagicka = false;
				break;
			}
			case"singularity":{
				if(mag>=75) {
					player.chat(ChatColor.GRAY+ChatColor.ITALIC.toString()+"SAVRAS!");
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"castp "+player.getName()+" singularity");
					RPMagic.scoreboard.getObjective("Magicka").getScore(player.getName()).setScore(mag-75);
				}
				else enoughMagicka = false;
				break;
			}
			case"skyfall":{
				if(mag>=120) {
					player.chat(ChatColor.GRAY+ChatColor.ITALIC.toString()+"SKYFALL!");
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"castp "+player.getName()+" skyfall");
					RPMagic.scoreboard.getObjective("Magicka").getScore(player.getName()).setScore(mag-120);
				}
				else enoughMagicka = false;
				break;
			}
			case"soothe":{
				if(mag>=33) {
					player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,200,1));
					RPMagic.scoreboard.getObjective("Magicka").getScore(player.getName()).setScore(mag-33);
				}
				else enoughMagicka = false;
				break;
			}
			case"tornado":{
				if(mag>=33) {
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"castp "+player.getName()+" tornado");
					RPMagic.scoreboard.getObjective("Magicka").getScore(player.getName()).setScore(mag-33);
				}
				else enoughMagicka = false;
				break;
			}
			case"wave":{
				if(mag>=25) {
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"castp "+player.getName()+" wave");
					RPMagic.scoreboard.getObjective("Magicka").getScore(player.getName()).setScore(mag-25);
				}
				else enoughMagicka = false;
				break;
			}
			case"vallone":{
				player.sendMessage(ChatColor.GRAY+"This spell is still unimplemented");
				break;
			}
			case"test":{
				player.chat(ChatColor.GRAY+ChatColor.ITALIC.toString()+"Testistimus!");
				player.sendMessage(ChatColor.DARK_PURPLE+"Good work, you can cast spells!");
				break;
			}
			default: player.sendMessage("You can't cast this spell!");
		}
		if(spell!="beam") {
			player.getInventory().setItemInOffHand(RPMagic.leftHands.get(player.getUniqueId()));
			RPMagic.leftHands.remove(player.getUniqueId());
		}
		if(!enoughMagicka){
			TextComponent msg = new TextComponent();
			msg.setText("You don't have enough Magicka!");
			msg.setColor(ChatColor.DARK_BLUE);
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR,msg);
		}
		//This else statement makes it so that the regen process isn't cancelled if you didn't have enough Magicka to cast the spell
		else 
			RunnableRegen.mageRegen(player);
	}
	/**
	 * @param spell is the string that's being checked
	 * @return true if the string is a valid spell, false otherwise
	 */
	public static boolean isValid(String spell) {
		for(String s: ALL_SPELLS) {
			if(s.equals(spell)) return true;
		}
		return false;
	}
	
	/**
	 * This method takes a List<String> of spells and turns it into a text component
	 * that will have click events to cast the respective spells and also
	 * makes them c o l o r f u l. 
	 * @param spells
	 * @return
	 */
	public static TextComponent toText(List<String> spells) {
		if(spells.size()==0) return new TextComponent();
		//establishes the base text component. Can't be done in the for loop because of how the addExtra method works
		TextComponent text = new TextComponent(spells.get(0)+"\n\n");
		text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/getbook "+spells.get(0)));
		text.setColor(Spells.getColor(spells.get(0)));
		for(int i=1;i<spells.size();i++) {
			TextComponent temp = new TextComponent(spells.get(i)+"\n\n");
			temp.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/getbook "+spells.get(i)));
			temp.setColor(getColor(spells.get(i)));
			text.addExtra(temp);
		}
		return text;
	}
	
	/**
	 * This method is what determines the color of each spell.
	 * Cuz we can't have a bland looking spell lists!
	 * @param spell
	 * @return
	 */
	public static ChatColor getColor(String spell) {
		switch(spell) {
			//Common tier
			case "fireball": return ChatColor.GRAY;
			case "flashbang": return ChatColor.GRAY;
			case "ghast": return ChatColor.GRAY;
			case "soothe": return ChatColor.GRAY;
			case "beam": return ChatColor.GRAY;
			case "platform": return ChatColor.GRAY;
			case "homing": return ChatColor.GRAY;
			//Mid tier
			case "shock": return ChatColor.GREEN;
			case "fury": return ChatColor.GREEN;
			case "grenade": return ChatColor.GREEN;
			case "lift": return ChatColor.GREEN;
			case "wave": return ChatColor.GREEN;
			case "tornado": return ChatColor.GREEN;
			case "bomb": return ChatColor.GREEN;
			//High tier
			case "earthquake": return ChatColor.DARK_AQUA;
			case "entangle": return ChatColor.DARK_AQUA;
			case "heal": return ChatColor.DARK_AQUA;
			case "meteor": return ChatColor.DARK_AQUA;
			case "time": return ChatColor.DARK_AQUA;
			case"shower": return ChatColor.DARK_AQUA;
			//Exotic
			case "blessing": return ChatColor.LIGHT_PURPLE;
			case "nuke": return ChatColor.LIGHT_PURPLE;
			case "singularity": return ChatColor.LIGHT_PURPLE;
			case "skyfall": return ChatColor.LIGHT_PURPLE;
			//Mega rare
			case"vallone": return ChatColor.GOLD;
			case "test": return ChatColor.GOLD;
			default: return ChatColor.MAGIC;
		}
	}
}
