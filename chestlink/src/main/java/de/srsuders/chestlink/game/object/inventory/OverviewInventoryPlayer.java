package de.srsuders.chestlink.game.object.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.srsuders.chestlink.game.CLPlayer;
import de.srsuders.chestlink.game.object.LinkedChest;
import de.srsuders.chestlink.storage.Const;
import de.srsuders.chestlink.storage.Items;
import de.srsuders.chestlink.utils.ItemBuilder;
import de.srsuders.chestlink.utils.MCUtils;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 11.04.2021 Project: chestlink
 */
public class OverviewInventoryPlayer {

	private final Inventory overviewInv;
	private final Inventory chestInv;
	private final Inventory confirmInv;
	
	public OverviewInventoryPlayer() {
		this.overviewInv = Bukkit.createInventory(null, 4*9);
		this.chestInv = Bukkit.createInventory(null, 3*9);
		this.confirmInv = Bukkit.createInventory(null, 3*9);
		for(int i = 0; i<chestInv.getSize();i++) {
			this.overviewInv.setItem(i, Items.placeHolder);
			this.chestInv.setItem(i, Items.placeHolder);
			this.confirmInv.setItem(i, Items.placeHolder);
		}
		for (int i = 0; i <9; i++) {
			this.overviewInv.setItem(27+i, Items.placeHolder);
		}
	}
	
	public Inventory createConfirmInventory(final LinkedChest lc, final ItemStack chest, int nr) {
		final Inventory inv = Bukkit.createInventory(null, 3*9, Const.CONFIRM_NAME + nr);
		inv.setContents(chestInv.getContents());
		inv.setItem(11, Items.checkField);
		inv.setItem(13, chest);
		inv.setItem(15, Items.denyField);
		return inv;
	}
	
	public Inventory createLinkedChestInventory(final LinkedChest lc, final ItemStack chest, int nr) {
		final Inventory inv = Bukkit.createInventory(null, 3*9, Const.LC_OVERVIEW_NAME + nr);
		inv.setContents(chestInv.getContents());
		inv.setItem(11, Items.back);
		inv.setItem(13, chest);
		inv.setItem(15, Items.delete);
		return inv;
	}

	public Inventory createInventoryForPlayer(CLPlayer clp, int page) {
		final Inventory inv = Bukkit.createInventory(null, 4 * 9, Const.OVERVIEW_INVENTORY_NAME + page);
		inv.setContents(this.overviewInv.getContents());
		final List<LinkedChest> list = new ArrayList<>();
		for(LinkedChest lc : clp.getSingleLinkedChests()) {
			list.add(lc);
		}
		if (page != 1) {
			inv.setItem(inv.getSize() - 9, Items.lastPage);
		}
		for(int i = 0; i<(page-1)*27;i++) {
			list.remove(0);
		}
		int i = 0;
		for (LinkedChest lc : list) {
			if (i == 27) {
				inv.setItem(inv.getSize() - 1, Items.nextPage);
			} else {
				final String[] strArray = MCUtils.linkedChestToInfoStringFromSelf(lc).split("\n");
				List<String> lore = Arrays.asList(strArray);
				final ItemStack is = new ItemBuilder(Items.chest.clone())
						.setDisplayName("§7" + ((page-1)*27 + (i + 1)) + ". LinkedChest").setLore(lore).build();
				inv.setItem(i, is);
				i++;
			}
		}
		return inv;
	}
}
