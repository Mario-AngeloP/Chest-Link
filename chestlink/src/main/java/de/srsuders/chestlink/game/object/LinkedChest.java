package de.srsuders.chestlink.game.object;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.mongodb.BasicDBObject;

import de.srsuders.chestlink.utils.MCUtils;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 06.04.2021 Project: chestlink
 */
public class LinkedChest {

	private Location loc;
	private LinkedChest otherChest;
	private UUID owner;
	private final long finishedAt;

	public LinkedChest(final Location loc, final UUID owner, final long finishedAt) {
		this.loc = loc;
		this.owner = owner;
		this.finishedAt = finishedAt;
	}

	public void setOtherChest(final LinkedChest linkedChest) {
		this.otherChest = linkedChest;
	}

	public long getFinishedTime() {
		return this.finishedAt;
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

	/**
	 * Gibt beide verbundene Kisten als BasicDBObject wieder
	 * @return
	 */
	public BasicDBObject toBasicDBObject() {
		final BasicDBObject obj = new BasicDBObject();
		final Location loc2 = otherChest.getLocation();
		obj.put("loc1", loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ());
		obj.put("loc2", loc2.getWorld().getName() + "," + loc2.getX() + "," + loc2.getY() + "," + loc2.getZ());
		return obj;
	}
	
	public UUID getOwner() {
		return this.owner;
	}
	
	/**
	 * Überprüft die Parameter "owner" und die "location" 
	 * Wenn es eine Übereinstimmung gibt, gibt er true aus
	 */
	public boolean equals(Object obj) {
		if(obj != null) {
			if(obj instanceof LinkedChest) {
				LinkedChest lc = (LinkedChest) obj;
				return lc.getOwner().equals(this.owner) && MCUtils.equalLocation(loc, lc.getLocation());
			}
		}
		return false;
	}

	public static LinkedChest fromBasicDBObject(final BasicDBObject obj, UUID owner) {
		final String[] sLoc1 = obj.getString("loc1").split(",");
		final String[] sLoc2 = obj.getString("loc2").split(",");
		final Location loc1 = new Location(Bukkit.getWorld(sLoc1[0]), Double.valueOf(sLoc1[1]),
				Double.valueOf(sLoc1[2]), Double.valueOf(sLoc1[3]));
		final Location loc2 = new Location(Bukkit.getWorld(sLoc2[0]), Double.valueOf(sLoc2[1]),
				Double.valueOf(sLoc2[2]), Double.valueOf(sLoc2[3]));
		final LinkedChest lc = new LinkedChest(loc1, owner, obj.getLong("time"));
		final LinkedChest lc2 = new LinkedChest(loc2, owner, obj.getLong("time"));
		lc.setOtherChest(lc2);
		lc2.setOtherChest(lc);
		return lc;
	}
}
