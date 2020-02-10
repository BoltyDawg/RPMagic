package com.github.boltydawg.RPMagic.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.boltydawg.RPMagic.RPMagic;
import com.github.boltydawg.RPMagic.runnables.RunnableAngel;

import io.loyloy.nicky.Nick;

public class CommandSubclass implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args == null || args.length!=2) return false;
		Player player = Bukkit.getPlayer(args[0]);
		if (player == null) {sender.sendMessage("Invalid player name");return true;}
		if(args[1].equalsIgnoreCase("reset")) {
			if(RPMagic.scoreboard.getObjective("subclass").getScore(player.getName()).getScore()==2)
				return true;
			Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"lp user "+player.getName()+" parent remove subclass");
			RPMagic.scoreboard.getObjective("subclass").getScore(player.getName()).setScore(0);
			Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"skillreset "+player.getName()+" all");
			player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
			player.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).setBaseValue(0);
			for(ItemStack i: player.getInventory().getContents()) {
				if(i.getType()!=Material.WRITABLE_BOOK && i.getType()!=Material.WRITTEN_BOOK && i.getType()!=Material.COMPASS)
					i.setAmount(0);
			}
			player.sendMessage(ChatColor.DARK_AQUA+"Your subclass has been reset!");
		}
		if(RPMagic.scoreboard.getObjective("subclass").getScore(player.getName()).getScore()!=0) {player.sendMessage(ChatColor.LIGHT_PURPLE+"You already chose your subclass!"); return true;}
		player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
		player.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).setBaseValue(0);
		if(RPMagic.beacons.containsKey(player.getUniqueId())) {
			for(Entity e : player.getWorld().getNearbyEntities(RPMagic.beacons.get(player.getUniqueId()),1,1,1)) {
				if(e.getType().equals(EntityType.ARMOR_STAND)) {
					e.remove();
				}	
			}
			RPMagic.beacons.get(player.getUniqueId()).getBlock().breakNaturally();
			RPMagic.beacons.remove(player.getUniqueId());
		}
		
		switch(args[1]) {
			case "barbarian":{
				if(RPMagic.scoreboard.getObjective("class").getScore(player.getName()).getScore()!=1) {player.sendMessage(ChatColor.DARK_GRAY+"Only a Fighter can choose to be a BARBARIAN"); return true;}
				ItemStack[] conts = player.getInventory().getArmorContents();
				boolean t = false;
				if(conts!=null && conts.length>0) {
					for(ItemStack is: conts) {
						if(is==null) continue;
						else if(!is.getType().equals(Material.AIR)){
							t = true;
							player.getInventory().addItem(is);
						}
					}
					if(t)
						player.sendMessage(ChatColor.DARK_PURPLE+"YOUR MUSCLES BECOME SO LARGE THAT YOUR ARMOR POPS OFF");
				}
				if(RPMagic.nick) {
					Nick vallone = new Nick(player);
					vallone.set(RPMagic.getName(player).toUpperCase());
				}
				ItemStack bone = new ItemStack(Material.BONE);
				ItemMeta met = bone.getItemMeta();
				met.setDisplayName(RPMagic.getName(player)+" FIRST BONE");
				ArrayList<String> lore = new ArrayList<String>();
				lore.add("IT TASTE LIKE MOTHER");
				met.setLore(lore);
				bone.setItemMeta(met);
				player.getInventory().setArmorContents(new ItemStack[] {null,null,null,bone});
				player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);
				player.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).setBaseValue(8);
				player.setHealth(40);
				
				RPMagic.rageBars.put(player.getUniqueId(), RPMagic.rageBar(player));
				
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"mmoedit "+player.getName()+" Unarmed 100");
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"lp user "+player.getName()+" parent add barbarian");
				RPMagic.scoreboard.getObjective("subclass").getScore(player.getName()).setScore(1);
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "give "+player.getName()+" written_book{pages:[\"{\\\"text\\\":\\\"YOU BARBARIAN NOW,\\\\nGOOD.\\\\nYOU A BROTHER/SISTER NOW,\\\\nBLOOD RUNS DEEP.\\\\n\\\\nWE STEM FROM ANCIENT GROUP OF SAVAGES THAT HELD THE WORLD IN THEIR HANDS. THEY ATE WHAT THEY WANTED, THEY ATE WHO THEY WANTED.\\\"}\",\"{\\\"text\\\":\\\"BUT ENOUGH WITH OUR PEOPLE'S HISTORY, ONTO CURRENT TIME.\\\\n\\\\nYOU A BARBARIAN, YOU GOT BIG HEALTH POOL. NO NEED FOR ARMOR, YOU HAVE BIG MUSCLES INSTEAD. PUNY MAN ARMOR NOT FIT YOU. KEEP BONE IN YOUR MOUTH TO PROTECT YOU.\\\"}\",\"{\\\"text\\\":\\\"UPGRADE BONE BY SURROUNDING IT WITH IRON OR DIAMONDS. RIGHT CLICK WITH BONE IN HAND TO PUT IT IN YOUR MOUTH.\\\\n\\\\nYOU HAVE BLOOD OF ANCESTORS, ANCESTOR RAGE RUNS INSIDE YOU. AFTER DEALING/TAKING A CERTAIN AMOUNT OF DAMAGE,\\\"}\",\"{\\\"text\\\":\\\"YOU WILL GET THE RAGE FLAME. RIGHT CLICK THE FLAME TO ACTIVATE RAGE MODE, TO GET VARIOUS BUFFS. BUT IT TIRE YOU, USE WISELY.\\\\n\\\\nBARBARIANS QUICK, USE SHIELD WHILE SPRINTING TO DASH FORWARD AND SMASH THINGS IN PATH.\\\"}\",\"{\\\"text\\\":\\\"LAST THING YOU NEED TO KNOW: DON'T EAT THE PUNY MAN'S COOKED MEAT. ANCESTORS DID NOT HAVE FIRE TO COOK\\\\nTHEIR FOOD WITH. YOU EAT FOOD RAW AND GET REGENERATION BONUS.\\\\nEAT IT COOKED.... AND GET SICK!\\\\n\\\\nNOW GO SMASH SKULLS!!!!\\\"}\"],title:\"BARBARIAN BOOK\",author:\"BOBRA D'UBRA\"}");
				player.sendMessage(ChatColor.DARK_AQUA+"YOU ARE NOW A BARBARIAN");
				break;
			}
			case "knight":{
				if(RPMagic.scoreboard.getObjective("class").getScore(player.getName()).getScore()!=1) {player.sendMessage(ChatColor.DARK_GRAY+"Only a Fighter can choose to be a Knight"); return true;}
				
				//give player their knight steed spawn egg, works in 1.13 only
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"give "+player.getName()+" minecraft:horse_spawn_egg{display:{Name:\"{\\\"text\\\":\\\"Spawn Knight Steed\\\"}\"},EntityTag:{CustomNameVisible:0b,Health:60f,Age:0,Tame:1b,OwnerUUID:\""+player.getUniqueId().toString()+"\",Variant:262,Tags:[\""+RPMagic.getName(player)+" steed\"],CustomName:\"{\\\"text\\\":\\\""+RPMagic.getName(player)+"'s Steed\\\",\\\"color\\\":\\\"red\\\",\\\"bold\\\":true}\",Attributes:[{Name:generic.maxHealth,Base:60},{Name:generic.movementSpeed,Base:.280},{Name:horse.jumpStrength,Base:.75}],SaddleItem:{id:\"minecraft:saddle\",Count:1b}}} 1");
				ItemStack is = new ItemStack(Material.RED_SHULKER_BOX);
				ItemMeta met = is.getItemMeta();
				met.setDisplayName("Red Backpack");
				is.setItemMeta(met);
				player.getInventory().addItem(is);
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"mmoedit "+player.getName()+" Taming 75");
				
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"lp user "+player.getName()+" parent add knight");
				RPMagic.scoreboard.getObjective("subclass").getScore(player.getName()).setScore(2);
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "give "+player.getName()+" written_book{pages:[\"{\\\"text\\\":\\\"Congratulations on choosing to be a Knight! When you find yourself in the wilderness with no shelter available, this decision may just be the deciding factor between life and death. \\\"}\",\"{\\\"text\\\":\\\"Knights are the heartiest subclass and perhaps the best survivalists. As you can see, you have a spawn egg to spawn in your Steed. This horse has an immense amount of health so that it can survive even the toughest of fights. \\\"}\",\"{\\\"text\\\":\\\"After you spawn in your horse, you have the option to capture it and turn it back into its spawn egg, allowing you to keep it safe or cross oceans without leaving it behind!\\\\nWhile riding your horse, you'll be given a Javelin that comes back to you after you throw it!\\\"}\",\"{\\\"text\\\":\\\"Another survival tool Knights have is that you will gain saturation from dealing damage. This means you use up less food and can make your resources last longer over long journeys. \\\"}\",\"{\\\"text\\\":\\\"Thirdly, the brown box that you've been given can be used for portable storage! But don't let anyone else break it, because it'll self-destruct and spill its contents out. \\\"}\",\"[\\\"\\\",{\\\"text\\\":\\\"Lastly, you can craft a shelter than wards off hostile mobs and provides you with a bed by using the following recipe:\\\\n\\\\n \\\"},{\\\"text\\\":\\\"W\\\",\\\"color\\\":\\\"white\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":\\\"White Wool\\\"}},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"white\\\"},{\\\"text\\\":\\\"W\\\",\\\"color\\\":\\\"white\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":\\\"White Wool\\\"}},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"white\\\"},{\\\"text\\\":\\\"W\\\",\\\"color\\\":\\\"white\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":\\\"White Wool\\\"}},{\\\"text\\\":\\\"\\\\n \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"W\\\",\\\"color\\\":\\\"white\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":\\\"White Wool\\\"}},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"D\\\",\\\"color\\\":\\\"aqua\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":\\\"Diamond\\\"}},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"W\\\",\\\"color\\\":\\\"white\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":\\\"White Wool\\\"}},{\\\"text\\\":\\\"\\\\n \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"W\\\",\\\"color\\\":\\\"white\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":\\\"White Wool\\\"}},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"white\\\"},{\\\"text\\\":\\\"W\\\",\\\"color\\\":\\\"white\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":\\\"White Wool\\\"}},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"white\\\"},{\\\"text\\\":\\\"W\\\",\\\"color\\\":\\\"white\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":\\\"White Wool\\\"}}]\"],title:Knights,author:\"Edard Molquin\"}");
				player.sendMessage(ChatColor.DARK_AQUA+"You are now a Knight!");
				break;
			}
			case "marine":{
				if(RPMagic.scoreboard.getObjective("class").getScore(player.getName()).getScore()!=1) {player.sendMessage(ChatColor.DARK_GRAY+"Only a Fighter can choose to be a Marine"); return true;}
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"mmoedit "+player.getName()+" Swords 75");
				
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"lp user "+player.getName()+" parent add marine");
				RPMagic.scoreboard.getObjective("subclass").getScore(player.getName()).setScore(3);
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "give "+player.getName()+" written_book{pages:[\"[\\\"\\\",{\\\"text\\\":\\\"Congratulations on choosing your subclass! As a Marine, you'll have access to Tridents and Spears, and you have an ability called \\\"},{\\\"text\\\":\\\"Call to Arms\\\",\\\"italic\\\":true},{\\\"text\\\":\\\" that heals and increases the attack speed of yourself and nearby players. This ability is on a 45 second cooldown. \\\",\\\"color\\\":\\\"reset\\\"}]\",\"{\\\"text\\\":\\\"This means that Marines are the best support subclass for fighters, because they can stay behind the frontline and support teammates by healing them and throwing tridents!\\\"}\",\"[\\\"\\\",{\\\"text\\\":\\\"In order to activate \\\"},{\\\"text\\\":\\\"Call to Arms\\\",\\\"italic\\\":true},{\\\"text\\\":\\\", all you have to do is put your trident in your off hand, hold shift, then right click! It's easy once you get the hang of it, and make sure you press F to quickly switch the trident to your off hand.\\\",\\\"color\\\":\\\"reset\\\"}]\",\"{\\\"text\\\":\\\"As far as crafting goes, these are the recipes for the tridents/spears you can craft:\\\\n \\\"}\",\"[\\\"\\\",{\\\"text\\\":\\\"Spears: throwable tridents, and the diamond level will come back to you after it strikes.\\\\n\\\\n\\\"},{\\\"text\\\":\\\"x \\\",\\\"color\\\":\\\"gray\\\"},{\\\"text\\\":\\\"M \\\",\\\"color\\\":\\\"dark_aqua\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":\\\"Material - Iron or diamond\\\"}},{\\\"text\\\":\\\"x\\\",\\\"color\\\":\\\"gray\\\"},{\\\"text\\\":\\\"\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"M\\\",\\\"color\\\":\\\"dark_aqua\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":\\\"Material - Iron or diamond\\\"}},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"S\\\",\\\"color\\\":\\\"dark_gray\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":\\\"Stick\\\"}},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"M\\\",\\\"color\\\":\\\"dark_aqua\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":\\\"Material - Iron or diamond\\\"}},{\\\"text\\\":\\\"\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"x\\\",\\\"color\\\":\\\"gray\\\"},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"S\\\",\\\"color\\\":\\\"dark_gray\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":\\\"Stick\\\"}},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"x\\\",\\\"color\\\":\\\"gray\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\"Tridents: These weapons are only usable underwater or in the rain. They lunge you forward as you do a spin attack.\\\\n\\\\n\\\"},{\\\"text\\\":\\\"x \\\",\\\"color\\\":\\\"gray\\\"},{\\\"text\\\":\\\"M \\\",\\\"color\\\":\\\"dark_aqua\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":\\\"Material - Iron or diamond\\\"}},{\\\"text\\\":\\\"x\\\",\\\"color\\\":\\\"gray\\\"},{\\\"text\\\":\\\"\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"M\\\",\\\"color\\\":\\\"dark_aqua\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":\\\"Material - Iron or diamond\\\"}},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"P\\\",\\\"color\\\":\\\"blue\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":\\\"Prismarine Shard\\\"}},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"M\\\",\\\"color\\\":\\\"dark_aqua\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":\\\"Material - Iron or diamond\\\"}},{\\\"text\\\":\\\"\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"x\\\",\\\"color\\\":\\\"gray\\\"},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"P\\\",\\\"color\\\":\\\"blue\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":\\\"Prismarine Shard\\\"}},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"x\\\",\\\"color\\\":\\\"gray\\\"}]\"],title:Marines,author:\"Olfran Waymar\"}");
				player.sendMessage(ChatColor.DARK_AQUA+"You are now a Marine!");
				break;
			}
			case"tank":{
				if(RPMagic.scoreboard.getObjective("class").getScore(player.getName()).getScore()!=1) {player.sendMessage(ChatColor.DARK_GRAY+"Only a Fighter can choose to be a Tank"); return true;}
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"mmoedit "+player.getName()+" Axes 75");
				
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"lp user "+player.getName()+" parent add tank");
				RPMagic.scoreboard.getObjective("subclass").getScore(player.getName()).setScore(4);
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "give "+player.getName()+" written_book{pages:[\"{\\\"text\\\":\\\"Tanks have a deep tie to the history of the Overworld, as they represent how the spirit of man-kind can overcome all odds. For instance, the tale of Strongr Heavy-Foot, who once single-handedly repelled a swarm of barbarians from attacking a village.\\\"}\",\"{\\\"text\\\":\\\"As a tank, you can craft 2 different sets of armor that will provide you with lots of protection and knockback resistance. What I mean by knockback resistance is that the higher the value is, the more likely it is that you completely negate any knockback upon being hit.\\\"}\",\"{\\\"text\\\":\\\"If wearing the full Tank set with the Tank shield, you'll have full knockback resistance and will never be pushed back when hit!\\\\n\\\\nThe two sets of armor that you can craft are Brick and Tank armors.\\\"}\",\"{\\\"text\\\":\\\"Brick armor is crafted just like vanilla armor except with bricks, but Tank armor is a little different; it's made from diamonds, but also has emerald in it. Each recipe follows the vanilla format but has an emerald in the center of it, in an empty slot. Examples on the next page.\\\"}\",\"[\\\"\\\",{\\\"text\\\":\\\"D\\\",\\\"color\\\":\\\"aqua\\\"},{\\\"text\\\":\\\"=Diamond, \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"E\\\",\\\"color\\\":\\\"green\\\"},{\\\"text\\\":\\\"=Emerald\\\\n\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"D D D\\\",\\\"color\\\":\\\"aqua\\\"},{\\\"text\\\":\\\"\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"D\\\",\\\"color\\\":\\\"aqua\\\"},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"E\\\",\\\"color\\\":\\\"green\\\"},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"D\\\",\\\"color\\\":\\\"aqua\\\"},{\\\"text\\\":\\\" helmet\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"x x x\\\",\\\"color\\\":\\\"gray\\\"},{\\\"text\\\":\\\"\\\\n\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"D\\\",\\\"color\\\":\\\"aqua\\\"},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"E\\\",\\\"color\\\":\\\"green\\\"},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"D\\\",\\\"color\\\":\\\"aqua\\\"},{\\\"text\\\":\\\"\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"D D D\\\",\\\"color\\\":\\\"aqua\\\"},{\\\"text\\\":\\\" chestplate\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"D D D\\\",\\\"color\\\":\\\"aqua\\\"},{\\\"text\\\":\\\"\\\\n\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"D D D\\\",\\\"color\\\":\\\"aqua\\\"},{\\\"text\\\":\\\"\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"D\\\",\\\"color\\\":\\\"aqua\\\"},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"E\\\",\\\"color\\\":\\\"green\\\"},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"D\\\",\\\"color\\\":\\\"aqua\\\"},{\\\"text\\\":\\\" leggings\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"D\\\",\\\"color\\\":\\\"aqua\\\"},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"x\\\",\\\"color\\\":\\\"gray\\\"},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"D\\\",\\\"color\\\":\\\"aqua\\\"},{\\\"text\\\":\\\"\\\\n \\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\"D\\\",\\\"color\\\":\\\"aqua\\\"},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"x\\\",\\\"color\\\":\\\"gray\\\"},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"D\\\",\\\"color\\\":\\\"aqua\\\"},{\\\"text\\\":\\\"\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"D \\\",\\\"color\\\":\\\"aqua\\\"},{\\\"text\\\":\\\"E\\\",\\\"color\\\":\\\"green\\\"},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"D\\\",\\\"color\\\":\\\"aqua\\\"},{\\\"text\\\":\\\" boots\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"x x x\\\",\\\"color\\\":\\\"gray\\\"},{\\\"text\\\":\\\"\\\\n\\\\nAs you can tell, the Tank armor recipes are very, very expensive! Luckily, the brick recipe is very easy to make so it's recommended to stick to that during the early game.\\\\n \\\",\\\"color\\\":\\\"reset\\\"}]\",\"{\\\"text\\\":\\\"And now, for the most essential part of a tank's kit: the shield. When you put your shield in your main hand and right click, it will cast a spell that pulls your target towards you. This is extremely important considering how slow you'll be. \\\"}\",\"{\\\"text\\\":\\\"Using this ability will damage your shield, however. But fear not, because everytime you take damage it will slightly repair the shield!\\\\nYou can craft your tank shield using the vanilla recipe, except instead of wood use brick blocks.\\\"}\"],title:Tanks,author:\"Stongr Heavy-Foot\"}");
				player.sendMessage(ChatColor.DARK_AQUA+"You are now a Tank!");
				break;
			}
			case"angel":{
				if(RPMagic.scoreboard.getObjective("class").getScore(player.getName()).getScore()!=3) {player.sendMessage(ChatColor.DARK_GRAY+"Only a Ranger can choose to be an Angel"); return true;}
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"mmoedit "+player.getName()+" Acrobatics 75");
				
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "give "+player.getName()+" minecraft:elytra{display:{Name:\"{\\\"text\\\":\\\"Angel Glider\\\",\\\"color\\\":\\\"light_purple\\\",\\\"italic\\\":true}\",Lore:[\"Your wings are your brush,\",\"the sky is your canvas\"]},RepairCost:200,Enchantments:[{id:\"minecraft:vanishing_curse\",lvl:1}]} 1");
				RunnableAngel.activate(player);
				
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"lp user "+player.getName()+" parent add angel");
				RPMagic.scoreboard.getObjective("subclass").getScore(player.getName()).setScore(5);
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "give "+player.getName()+" written_book{pages:[\"{\\\"text\\\":\\\"Congratulations on choosing your subclass! As an Angel, there's quite a few tweaks that have been made to your general Minecraft experience. Firstly, you don't take kinetic energy damage anymore. That means, that if you fly into a wall with your elytra, you won't take damage!\\\"}\",\"{\\\"text\\\":\\\"Rather, it will eject you from flight, so be ready to deploy your glider again if you hit something!\\\\nSecondly, while you're flying, you can press shift to drop out of flight and fall. This is extremely helpful when it comes to using your next ability...\\\"}\",\"{\\\"text\\\":\\\"When you take fall damage or fly into a wall near an entity, you will damage that entity proportionally to the amount of damage you would have taken. \\\\nAs an Angel, you only recieve 2/3 the normal amount of fall damage, making maneuvers like mentioned above feasible!\\\"}\",\"{\\\"text\\\":\\\"And now, for the most important part of this subclass, the glider.\\\\nYou can craft a new glider by putting 7 phantom membranes in the shape that would normally craft pants.\\\\nBut for now, let's talk about the one you have right now. \\\"}\",\"{\\\"text\\\":\\\"Incase you don't know, this is how you use your glider:\\\\nwhile in the air, press the jump key (spacebar) to deploy your wings.\\\\nIf you hold your elytra in your main hand, it will make you float up in the air at a slow pace. \\\"}\",\"{\\\"text\\\":\\\"Once you reach a good enough height, you can simply right click to equip the elytra, then press spacebar to deploy the wings, and boom you're flying!\\\\nIf you want to stay in the air for longer, you can craft a firework rocket that will launch\\\"}\",\"[\\\"\\\",{\\\"text\\\":\\\"you in the direction you're facing.\\\\n\\\\n \\\"},{\\\"text\\\":\\\"x\\\",\\\"color\\\":\\\"gray\\\"},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"T\\\",\\\"color\\\":\\\"dark_red\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":\\\"TnT\\\"}},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"x\\\",\\\"color\\\":\\\"gray\\\"},{\\\"text\\\":\\\"\\\\n \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"x\\\",\\\"color\\\":\\\"gray\\\"},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"S\\\",\\\"color\\\":\\\"dark_gray\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":\\\"String\\\"}},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"x\\\",\\\"color\\\":\\\"gray\\\"},{\\\"text\\\":\\\"\\\\n \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"x\\\",\\\"color\\\":\\\"gray\\\"},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"S\\\",\\\"color\\\":\\\"dark_gray\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":\\\"String\\\"}},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"x\\\",\\\"color\\\":\\\"gray\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\"Last but not least, you can craft Angel Boots with 4 diamonds and an Emerald:\\\\n\\\"},{\\\"text\\\":\\\" D\\\",\\\"color\\\":\\\"aqua\\\"},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"x\\\",\\\"color\\\":\\\"gray\\\"},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"D\\\",\\\"color\\\":\\\"aqua\\\"},{\\\"text\\\":\\\"\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\" D \\\",\\\"color\\\":\\\"aqua\\\"},{\\\"text\\\":\\\"E\\\",\\\"color\\\":\\\"green\\\"},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"D\\\",\\\"color\\\":\\\"aqua\\\"},{\\\"text\\\":\\\" \\\\n \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"x x x\\\",\\\"color\\\":\\\"gray\\\"},{\\\"text\\\":\\\"\\\\nThese, are not ordinary boots.\\\",\\\"color\\\":\\\"reset\\\"}]\",\"{\\\"text\\\":\\\"Angel boots make it so that you take far less fall damage, so that dropping on your foes is much easier.\\\\nAlso, instead of simply dealing damage to entity near you when you hit the ground, these boots will trigger an explosion proportional to the distance that you were falling!\\\"}\"],title:Angels,author:Tidas,generation:2}");
				player.sendMessage(ChatColor.DARK_AQUA+"You are now an Angel!");
				break;
			}
			case"bowman":{
				if(RPMagic.scoreboard.getObjective("class").getScore(player.getName()).getScore()!=3) {player.sendMessage(ChatColor.DARK_GRAY+"Only a Ranger can choose to be an Arcane Bowman"); return true;}
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"mmoedit "+player.getName()+" Archery 75");
				
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"lp user "+player.getName()+" parent add bowman");
				RPMagic.scoreboard.getObjective("subclass").getScore(player.getName()).setScore(6);
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "give "+player.getName()+" written_book{pages:[\"{\\\"text\\\":\\\"Congrats on choosing your subclass! As an Arcane Bowman, you'll quickly learn how to master the art of marksmanship. While you can't tap into Magicka and cast spells, you're still magically inclined in the sense that you are able to fuse Magic into the arrows that you craft.\\\"}\",\"[\\\"\\\",{\\\"text\\\":\\\" INDEX\\\\n\\\"},{\\\"text\\\":\\\"Magic Arrows\\\",\\\"italic\\\":true,\\\"color\\\":\\\"blue\\\"},{\\\"text\\\":\\\"...........\\\",\\\"color\\\":\\\"white\\\"},{\\\"text\\\":\\\"pg 3\\\",\\\"bold\\\":true,\\\"underlined\\\":true,\\\"color\\\":\\\"blue\\\",\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":3}},{\\\"text\\\":\\\"\\\\n\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Tipped Arrows\\\",\\\"italic\\\":true,\\\"color\\\":\\\"red\\\"},{\\\"text\\\":\\\"........\\\",\\\"color\\\":\\\"white\\\"},{\\\"text\\\":\\\"pg 9\\\",\\\"bold\\\":true,\\\"underlined\\\":true,\\\"color\\\":\\\"red\\\",\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":9}},{\\\"text\\\":\\\"\\\\n\\\\n\\\\nAnd remember, your Magic arrows only work if they hit a block!\\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\" \\\"},{\\\"text\\\":\\\"MAGIC ARROWS\\\",\\\"color\\\":\\\"blue\\\"},{\\\"text\\\":\\\"\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Explosive\\\",\\\"italic\\\":true,\\\"color\\\":\\\"gold\\\"},{\\\"text\\\":\\\"...................\\\",\\\"color\\\":\\\"white\\\"},{\\\"text\\\":\\\"pg 4\\\",\\\"bold\\\":true,\\\"underlined\\\":true,\\\"color\\\":\\\"gold\\\",\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":4}},{\\\"text\\\":\\\"\\\\n\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Loyal\\\",\\\"italic\\\":true,\\\"color\\\":\\\"gray\\\"},{\\\"text\\\":\\\".............................\\\",\\\"color\\\":\\\"white\\\"},{\\\"text\\\":\\\"pg 5\\\",\\\"bold\\\":true,\\\"underlined\\\":true,\\\"color\\\":\\\"gray\\\",\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":5}},{\\\"text\\\":\\\"\\\\n\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Nausea\\\",\\\"italic\\\":true,\\\"color\\\":\\\"dark_green\\\"},{\\\"text\\\":\\\".........................\\\",\\\"color\\\":\\\"white\\\"},{\\\"text\\\":\\\"pg 6\\\",\\\"bold\\\":true,\\\"underlined\\\":true,\\\"color\\\":\\\"dark_green\\\",\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":6}},{\\\"text\\\":\\\"\\\\n\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Seeing\\\",\\\"italic\\\":true,\\\"color\\\":\\\"green\\\"},{\\\"text\\\":\\\"...........................\\\",\\\"color\\\":\\\"white\\\"},{\\\"text\\\":\\\"pg 7\\\",\\\"bold\\\":true,\\\"underlined\\\":true,\\\"color\\\":\\\"green\\\",\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":7}},{\\\"text\\\":\\\"\\\\n\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Teleportation\\\",\\\"italic\\\":true,\\\"color\\\":\\\"dark_blue\\\"},{\\\"text\\\":\\\".........\\\",\\\"color\\\":\\\"white\\\"},{\\\"text\\\":\\\"pg 8\\\",\\\"bold\\\":true,\\\"underlined\\\":true,\\\"color\\\":\\\"dark_blue\\\",\\\"clickEvent\\\":{\\\"action\\\":\\\"change_page\\\",\\\"value\\\":8}},{\\\"text\\\":\\\"\\\\n\\\\n\\\\n\\\\n \\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\"Explosive Arrows:\\\",\\\"underlined\\\":true,\\\"color\\\":\\\"gold\\\"},{\\\"text\\\":\\\"\\\\nThese arrows will trigger an explosion where struck. Great for tearing through structures.\\\\n\\\\nCrafted by surrounding an arrow with gunpowder.\\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\"Loyal Arrow:\\\",\\\"underlined\\\":true,\\\"color\\\":\\\"gray\\\"},{\\\"text\\\":\\\"\\\\nAn arrow that comes back to you after striking its target! A great replacement for infinity ;)\\\\n\\\\nCrafted by surrounding an arrow with diamonds.\\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\"Nausea Arrow:\\\",\\\"underlined\\\":true,\\\"color\\\":\\\"dark_green\\\"},{\\\"text\\\":\\\"\\\\nThis arrow will give all entities around it dizzying nausea.\\\\n\\\\nCrafted by surrounding an arrow with mushrooms (4 red on top, 4 brown on bottom).\\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\"Seeing Arrows:\\\",\\\"color\\\":\\\"green\\\"},{\\\"text\\\":\\\"\\\\nWhen one of these strikes the ground, it will make all players and creatures within a certain radius of it glow through walls.\\\\n\\\\nCrafted by combining 9 spectral arrows.\\\\n(4 glowstone around an arrow to make it spectral)\\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\"Teleportation Arrows:\\\",\\\"color\\\":\\\"dark_blue\\\"},{\\\"text\\\":\\\"\\\\nThese arrows will teleport you to wherever it lands. However, you have to wait 10 seconds after taking damage to do this.\\\\n\\\\nCrafted by surrounding an arrow with enderpearls.\\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\"\\\\n\\\\n\\\\n\\\\n\\\\n \\\"},{\\\"text\\\":\\\"Tipped Arrows\\\",\\\"color\\\":\\\"red\\\"}]\",\"{\\\"text\\\":\\\"Arrow of Healing:\\\\nEffects the target with instant health 2.\\\\nCrafted by surrounding a glistening melon with arrows. \\\"}\",\"{\\\"text\\\":\\\"Arrow of Swiftness:\\\\nGives the target speed for 1 minute.\\\\nCrafted by surrounding a sugar with arrows. \\\"}\",\"{\\\"text\\\":\\\"Arrow of Slowness:\\\\nGives the target slowness for 30 seconds.\\\\nCrafted by surrounding a slimeball with arrows. \\\"}\",\"{\\\"text\\\":\\\"Arrow of Poison:\\\\nGives the target poison 2 for 2 seconds.\\\\nCrafted by surrounding a fermented spider-eye with arrows. \\\"}\",\"{\\\"text\\\":\\\"Arrow of Harming:\\\\nGives the target instant damage 1.\\\\nCrafted by surrounding a fermented spider-eye and arrow of healing with arrows. Fermented spider-eye goes in middle, healing arrow beneath that.\\\"}\",\"{\\\"text\\\":\\\"Arrow of Weakness:\\\\nCauses the target to deal less damage for 30 seconds.\\\\nCrafted by surrounding a spider-eye with arrows.\\\"}\"],title:\"Arcane Bowman Guide\",author:\"Folwin Ulaven\",generation:2}");
				player.sendMessage(ChatColor.DARK_AQUA+"You are now an Arcane Bowman!");
				break;
			}
			case"assassin":{
				if(RPMagic.scoreboard.getObjective("class").getScore(player.getName()).getScore()!=3) {player.sendMessage(ChatColor.DARK_GRAY+"Only a Ranger can choose to be an Assassin"); return true;}
				player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(16);
				
				ItemStack clock = new ItemStack(Material.CLOCK);
				ItemMeta met = clock.getItemMeta();
				met.setDisplayName(ChatColor.AQUA+"Pocket Watch");
				ArrayList<String> lore = new ArrayList<String>();
				lore.add(ChatColor.GRAY+"Was there ever any doubt?");
				met.setLore(lore);
				clock.setItemMeta(met);
				player.getInventory().addItem(clock);
				
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"lp user "+player.getName()+" parent add assassin");
				RPMagic.scoreboard.getObjective("subclass").getScore(player.getName()).setScore(7);
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "give "+player.getName()+" written_book{pages:[\"{\\\"text\\\":\\\"As an assassin, you are a master of stealth and striking quickly but fiercely.\\\\n\\\\nThe pocket watch you have in your inventory is your greatest asset, don't lose it!\\\"}\",\"[\\\"\\\",{\\\"text\\\":\\\"When you right click while the pocket watch is in your main hand, it will cast \\\"},{\\\"text\\\":\\\"cloak\\\",\\\"italic\\\":true},{\\\"text\\\":\\\". and when you right click with it in your off-hand, it'll cast \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"blink.\\\",\\\"italic\\\":true},{\\\"text\\\":\\\"\\\\n\\\\n\\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"Blink\\\",\\\"italic\\\":true},{\\\"text\\\":\\\" causes you to teleport forward in the direction your facing.\\\",\\\"color\\\":\\\"reset\\\"}]\",\"[\\\"\\\",{\\\"text\\\":\\\"Cloak\\\",\\\"italic\\\":true},{\\\"text\\\":\\\" allows you to turn invisible for 15 seconds and gives you a damage boost. Also, it will unequip any armor you're wearing so that you turn completely invisible. During cloak you'll still keep your armor stats, but your armor's enchantments won't have any effect.\\\",\\\"color\\\":\\\"reset\\\"}]\",\"{\\\"text\\\":\\\"The other tool you have is the Assassin's \\\\\\\"Dagger\\\\\\\". This weapon deals massive damage damage to your foes, but takes very long to charge up and only has 10 uses.\\\\nYou can craft one of these by using the vanilla gold sword recipe.\\\"}\"],title:Assassins,author:\"Cerdic Tal\"}");
				player.sendMessage(ChatColor.DARK_AQUA+"You are now an Assassin!");
				break;
			}
			case"scout":{
				if(RPMagic.scoreboard.getObjective("class").getScore(player.getName()).getScore()!=3) {player.sendMessage(ChatColor.DARK_GRAY+"Only a Ranger can choose to be a Scout"); return true;}
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"mmoedit "+player.getName()+" Taming 75");
				
				//give player their elybris spawn egg, works in 1.13 only
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"give "+player.getName()+" minecraft:horse_spawn_egg{display:{Name:\"{\\\"text\\\":\\\"Spawn Elybris\\\"}\"},EntityTag:{CustomNameVisible:0b,Health:24f,Age:0,Tame:1b,OwnerUUID:"+player.getUniqueId().toString()+",Variant:0,Tags:[\""+RPMagic.getName(player)+" elybris\"],CustomName:\"{\\\"text\\\":\\\""+RPMagic.getName(player)+"'s Elybris\\\",\\\"color\\\":\\\"green\\\",\\\"bold\\\":true}\",Attributes:[{Name:generic.maxHealth,Base:24},{Name:generic.movementSpeed,Base:.33295},{Name:horse.jumpStrength,Base:.75}],SaddleItem:{id:\"minecraft:saddle\",Count:1b}}} 1");
				ItemStack is = new ItemStack(Material.BROWN_SHULKER_BOX);
				ItemMeta met = is.getItemMeta();
				met.setDisplayName("Brown Backpack");
				is.setItemMeta(met);
				player.getInventory().addItem(is);
				
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),"lp user "+player.getName()+" parent add scout");
				RPMagic.scoreboard.getObjective("subclass").getScore(player.getName()).setScore(8);
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "give "+player.getName()+" written_book{pages:[\"{\\\"text\\\":\\\"As a Scout, you are the quickest travellers in the land, and are excellent at scouting places out and exploring.\\\\n \\\"}\",\"{\\\"text\\\":\\\"You have a special horse, Elybris, named after the mount that Lo'kir the Patron Saint of Humanity once rode.\\\\nYour Elybris is the fastest horse in the realm, and can be captured like a Pokemon and carried around, so you don't have to worry about losing it!\\\"}\",\"{\\\"text\\\":\\\"Another staple of Scouts is your abililty to craft and use beacons. There are 2 different beacons you can craft: Home Beacons and Waypoint Beacons.\\\\nHome Beacons can be placed anywhere on the surface and are where you teleport back to\\\"}\",\"{\\\"text\\\":\\\"upon using a Waypoint Beacon.\\\\nYour home beacon cannot be destroyed by normal tools, it can only be destroyed by placing a new home beacon, dying, or by using TnT.\\\\nThe following page has recipes for Home Beacons and Waypoint Beacons:\\\"}\",\"[\\\"\\\",{\\\"text\\\":\\\"Home Beacon:\\\\n \\\"},{\\\"text\\\":\\\"Q\\\",\\\"color\\\":\\\"dark_gray\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":\\\"Quartz Block\\\"}},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"S\\\",\\\"color\\\":\\\"white\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":\\\"Snow Block\\\"}},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"x\\\",\\\"color\\\":\\\"gray\\\"},{\\\"text\\\":\\\"\\\\n \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"R\\\",\\\"color\\\":\\\"red\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":\\\"Red Sand\\\"}},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"D\\\",\\\"color\\\":\\\"dark_aqua\\\",\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":\\\"Dark Prismarine Block\\\"}},{\\\"text\\\":\\\" \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"x\\\",\\\"color\\\":\\\"gray\\\"},{\\\"text\\\":\\\"\\\\n \\\",\\\"color\\\":\\\"reset\\\"},{\\\"text\\\":\\\"x x x\\\",\\\"color\\\":\\\"gray\\\"},{\\\"text\\\":\\\"\\\\nWaypoint Beacon:\\\\nSurround a gold ingot with quartz.\\\\n\\\\nAnother recipe to note is that your map recipe yields 2 maps instead of 1. (compass surrounded by paper)\\\",\\\"color\\\":\\\"reset\\\"}]\",\"{\\\"text\\\":\\\"Lastly, the red box that you've been given can be used for portable storage! But don't let anyone else break it, because it'll self-destruct and spill its contents out!\\\"}\"],title:Scouts,author:\"Artaud Bilberry\",generation:2}");
				player.sendMessage(ChatColor.DARK_AQUA+"You are now a Scout!");
				break;
			}
			default:{
				player.sendMessage(ChatColor.GRAY+"Invalid subclass name! (everything is in lower case)");
			}
		}
		return true;
	}

}
