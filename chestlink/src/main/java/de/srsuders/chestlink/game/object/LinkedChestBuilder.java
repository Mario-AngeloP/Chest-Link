package de.srsuders.chestlink.game.object;

import java.util.UUID;

import org.bukkit.Location;

import com.mongodb.BasicDBObject;

import de.srsuders.chestlink.utils.MCUtils;

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

	public void saveLinkedChestToBasicDBObject(BasicDBObject obj) {
		final LinkedChest lc = new LinkedChest(loc1);
		final LinkedChest lc2 = new LinkedChest(loc2);
		lc.setOtherChest(lc2);
		lc2.setOtherChest(lc);
		final BasicDBObject obj2 = new BasicDBObject();
		obj2.put("loc1", MCUtils.locationToString(lc.getLocation()));
		obj2.put("loc2", MCUtils.locationToString(lc2.getLocation()));
		obj2.put("time", System.currentTimeMillis());
		obj2.put("state", true);
		obj.put(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 3), obj2);
	}
}
