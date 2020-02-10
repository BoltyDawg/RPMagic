package com.github.boltydawg.RPMagic.commands.mages;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

//import org.bukkit.enchantments.Enchantment;
//import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.KnowledgeBookMeta;

import com.github.boltydawg.RPMagic.RPMagic;
import com.github.boltydawg.RPMagic.Spells;

import org.bukkit.entity.Player;
import org.bukkit.NamespacedKey;

/**
 * This class gives players the correct
 * spell book to cast when they either type in the command /getbook
 * or click on the spell in their spell menu
 * @author BoltyDawg
 *
 */

public class CommandBook implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player) || args == null || args.length!=1) return false;
		if(Spells.isValid(args[0])) {
			ItemStack book = setMyMeta(new ItemStack(Material.KNOWLEDGE_BOOK), args);
			Player p = ((Player)sender);
			p.getInventory().remove(Material.KNOWLEDGE_BOOK);
			RPMagic.leftHands.put(p.getUniqueId(), p.getInventory().getItemInOffHand());
			p.getInventory().setItemInOffHand(book);
			return true;
		}
		else sender.sendMessage("Invalid spell!");
		return true;
	}
	
	/**
	 * sets the meta of a given book
	 * @param item
	 * @param lore
	 * @return
	 */
	private ItemStack setMyMeta(ItemStack item, String[] lore) {
		KnowledgeBookMeta meta = (KnowledgeBookMeta) item.getItemMeta();
		meta.setDisplayName("Casting Guide");
		meta.setLore(Arrays.asList(lore));
		meta.setRecipes(Arrays.asList(new NamespacedKey(RPMagic.instance,"crafting_table")));
		item.setItemMeta(meta);
		return item;
	}
	

}
