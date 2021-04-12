package de.srsuders.chestlink.storage;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.srsuders.chestlink.utils.ItemBuilder;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 07.04.2021 Project: chestlink
 */
@SuppressWarnings("deprecation")
public class Items {

	public static ItemStack markAxe = new ItemBuilder(Material.TRIPWIRE_HOOK).setDisplayName("§5Linker").build();
	public static ItemStack placeHolder = new ItemBuilder(
			new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0, DyeColor.BLACK.getDyeData())).setDisplayName("§7")
					.build();
	public static ItemStack checkField = new ItemBuilder(new ItemStack(Material.WOOL, 1, (short) 0, (byte) 5))
			.setDisplayName("§aBestätigen").build();
	public static ItemStack denyField = new ItemBuilder(new ItemStack(Material.WOOL, 1, (short) 0, (byte) 6))
			.setDisplayName("§cAbbrechen").build();
	public static ItemStack statusItem = new ItemBuilder(new ItemStack(Material.BOOK, 1))
			.setDisplayName("§5LinkedChest leser").build();
	public static ItemStack nextPage = new ItemBuilder(
			new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0, DyeColor.GREEN.getDyeData()))
					.setDisplayName("§aNächste Seite").build();
	public static ItemStack lastPage = new ItemBuilder(
			new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0, DyeColor.RED.getDyeData()))
					.setDisplayName("§cLetzte Seite").build();
	public static ItemStack chest = new ItemBuilder(Material.CHEST).setDisplayName("§7LinkedChest").build();
	public static ItemStack back = new ItemBuilder(Material.BARRIER).setDisplayName("§eZurück").build();
	public static ItemStack delete = new ItemBuilder(Material.BUCKET).setDisplayName("§cVerlinkung Aufheben").build();
	public static ItemStack hack = new ItemBuilder(Material.BLAZE_ROD).setDisplayName("§eHackkey").setLore("§7Mit diesem Item kannst du bei einer gewissen Wahrscheinlichkeit, das Inventar einer anderen LinkedChest übernhemen.").build();
	
}
