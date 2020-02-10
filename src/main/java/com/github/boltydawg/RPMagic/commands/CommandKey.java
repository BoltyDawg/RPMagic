package com.github.boltydawg.RPMagic.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.boltydawg.RPMagic.RPMagic;

import net.md_5.bungee.api.ChatColor;

public class CommandKey implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) return false;
		Player player = ((Player) sender);
		ItemStack key = new ItemStack(Material.TRIPWIRE_HOOK);
		ItemMeta met = key.getItemMeta();
		met.setDisplayName(ChatColor.GREEN+(RPMagic.getName(player)+"'s key"));
		met.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 0, true);
		met.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		key.setItemMeta(met);
		
		if(player.getInventory().contains(key)) {
			player.sendMessage(ChatColor.GREEN+"You already have a key on you!");
		}
		else {
			player.getInventory().addItem(key);
		}
		return true;
	}
}
