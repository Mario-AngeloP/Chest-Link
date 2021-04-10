package de.srsuders.chestlink.storage;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.srsuders.chestlink.utils.ItemBuilder;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 07.04.2021 Project: chestlink
 */
public class Items {

	public static ItemStack markAxe = new ItemBuilder(Material.TRIPWIRE_HOOK).setDisplayName("§5Linker").build();
	@SuppressWarnings("deprecation")
	public static ItemStack placeHolder = new ItemBuilder(
			new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0, DyeColor.GRAY.getDyeData())).setDisplayName("§7")
					.build();
	@SuppressWarnings("deprecation")
	public static ItemStack checkField = new ItemBuilder(
			new ItemStack(Material.WOOL, 1, (short) 0, DyeColor.GREEN.getDyeData())).setDisplayName("§aBestätigen")
					.build();
	@SuppressWarnings("deprecation")
	public static ItemStack denyField = new ItemBuilder(
			new ItemStack(Material.WOOL, 1, (short) 0, DyeColor.RED.getDyeData())).setDisplayName("§cAbbrechen")
					.build();
}
