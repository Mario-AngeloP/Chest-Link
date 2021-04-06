package de.srsuders.chestlink.game.object;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.mongodb.BasicDBObject;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 06.04.2021 Project: chestlink
 */
public class LinkedChest {

	private Location loc;
	private LinkedChest otherChest;

	public LinkedChest(final Location loc) {
		this.loc = loc;
	}

	public void setOtherChest(final LinkedChest linkedChest) {
		this.otherChest = linkedChest;
	}

	public Location getLocation() {
		return this.loc;
	}

	public boolean isLinked() {
		return otherChest != null;
	}

	public LinkedChest getLinkedChest() {
		return this.otherChest;
	}

	public BasicDBObject toBasicDBObject() {
		final BasicDBObject obj = new BasicDBObject();
		final Location loc2 = otherChest.getLocation();
		obj.put("loc1", loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ());
		obj.put("loc2", loc2.getWorld().getName() + "," + loc2.getX() + "," + loc2.getY() + "," + loc2.getZ());
		return obj;
	}

	public static LinkedChest fromBasicDBObject(final BasicDBObject obj) {
		final String[] sLoc1 = obj.getString("loc1").split(",");
		final String[] sLoc2 = obj.getString("loc2").split(",");
		final Location loc1 = new Location(Bukkit.getWorld(sLoc1[0]), Double.valueOf(sLoc1[1]),
				Double.valueOf(sLoc1[2]), Double.valueOf(sLoc1[3]));
		final Location loc2 = new Location(Bukkit.getWorld(sLoc2[0]), Double.valueOf(sLoc2[1]),
				Double.valueOf(sLoc2[2]), Double.valueOf(sLoc2[3]));
		final LinkedChest lc = new LinkedChest(loc1);
		final LinkedChest lc2 = new LinkedChest(loc2);
		lc.setOtherChest(lc2);
		lc2.setOtherChest(lc);
		return lc;
	}
}
