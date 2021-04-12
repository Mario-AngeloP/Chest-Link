package de.srsuders.chestlink.game.object;

import java.util.UUID;

import org.bukkit.Location;

import de.srsuders.chestlink.game.object.inventory.LinkedChestInventory;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 06.04.2021 Project: chestlink
 */
public class LinkedChestBuilder {

	private Location loc1;
	private Location loc2;

	/**
	 * Gibt die Position der ersten markierten Kiste wieder
	 * @return
	 */
	public Location getFirstLocation() {
		return loc1;
	}
	
	/**
	 * Gibt die Position der zweiten markierten Kiste wieder
	 * 
	 * @return
	 */
	public Location getSecondLocation() {
		return loc2;
	}
	
	/**
	 * Setzt die Position der ersten Kiste fest
	 * @param loc1
	 */
	public void setFirstLocation(Location loc1) {
		this.loc1 = loc1;
	}

	/**
	 * Setzt die Position der zweiten Kiste fest
	 * @param loc2
	 */
	public void setSecondLocation(Location loc2) {
		this.loc2 = loc2;
	}

	/**
	 * Gibt an, ob die erste und zweite Location besetzt wurden ist
	 * @return
	 */
	public boolean finished() {
		return loc1 != null && loc2 != null;
	}

	/**
	 * Wandelt den LinkedChestBuilder in eine LinkedChest um
	 * Wird aber nicht ins System eingetragen
	 * @param owner
	 * @return
	 */
	public LinkedChest toLinkedChest(final UUID owner) {
		final long time = System.currentTimeMillis();
		final String id = UUID.randomUUID().toString().replaceAll("-", "");
		final LinkedChest lc1 = new LinkedChest(loc1, owner, id, time);
		final LinkedChest lc2 = new LinkedChest(loc2, owner, id, time);
		lc1.setOtherChest(lc2);
		lc2.setOtherChest(lc1);
		final LinkedChestInventory lcInv = new LinkedChestInventory(lc1);
		lc1.setLinkedChestInventory(lcInv);
		lc2.setLinkedChestInventory(lcInv);
		return lc1;
	}
}
