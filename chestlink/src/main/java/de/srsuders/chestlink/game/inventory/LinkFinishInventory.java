package de.srsuders.chestlink.game.inventory;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import de.srsuders.chestlink.storage.Items;
import de.srsuders.chestlink.storage.Messages;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 07.04.2021 Project: chestlink
 */
public class LinkFinishInventory implements Messages {

	private final Inventory inv = Bukkit.createInventory(null, 3 * 9, prefix + "LinkFinish");

	public LinkFinishInventory() {
		for (int i = 0; i < inv.getSize(); i++)
			inv.setItem(i, Items.placeHolder);
		inv.setItem(11, Items.checkField);
		inv.setItem(15, Items.denyField);
	}

	public Inventory getInventory() {
		return this.inv;
	}
}
