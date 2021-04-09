package de.srsuders.chestlink.game.object;

import java.util.UUID;

import org.bukkit.Location;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 06.04.2021 Project: chestlink
 */
public class LinkedChestBuilder {

	private Location loc1;
	private Location loc2;

	public void setLoc1(Location loc1) {
		this.loc1 = loc1;
	}

	/**
	 * Gibt die Position der zweiten markierten Kiste wieder
	 * 
	 * @return
	 */
	public Location getLoc2() {
		return loc2;
	}

	public void setLoc2(Location loc2) {
		this.loc2 = loc2;
	}

	public Location getLoc1() {
		return loc1;
	}
	
	public boolean finished() {
		return loc1 != null && loc2 != null;
	}
	
	public LinkedChest toLinkedChest(final UUID owner) {
		final long time = System.currentTimeMillis();
		final String id = UUID.randomUUID().toString().substring(0, 3);
		final LinkedChest lc1 = new LinkedChest(loc1, owner, id, time);
		final LinkedChest lc2 = new LinkedChest(loc2, owner, id, time);
		lc1.setOtherChest(lc2);
		lc2.setOtherChest(lc1);
		return lc1;
	}
}
