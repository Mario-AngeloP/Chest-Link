package de.srsuders.chestlink.game.object;

import java.util.UUID;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import de.srsuders.chestlink.utils.MCUtils;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 06.04.2021 Project: chestlink
 */
public class LinkedChest {

	private Location loc;
	private LinkedChest otherChest;
	private UUID owner;
	private final long finishedAt;
	private boolean state = false;

	public LinkedChest(final Location loc, final UUID owner, final long finishedAt) {
		this.loc = loc;
		this.owner = owner;
		this.finishedAt = finishedAt;
		this.state = true;
	}
	
	public LinkedChest(final Location loc, final UUID owner, final long finishedAt, final boolean state) {
		this.loc = loc;
		this.owner = owner;
		this.finishedAt = finishedAt;
		this.state = state;
	}
	
	public boolean linked() {
		if(this.otherChest == null) return false;
		return this.state;
	}
	
	public void setState(final boolean state) {
		this.state = state;
	}

	public void setOtherChest(final LinkedChest linkedChest) {
		this.otherChest = linkedChest;
	}
	
	public LinkedChest getLinkedChest() {
		return this.otherChest;
	}

	public long getFinishedTime() {
		return this.finishedAt;
	}
	
	public Location getLocation() {
		return this.loc;
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
	
	public static LinkedChest fromDocument(final Document doc, UUID owner) {
		final String[] sLoc1 = doc.getString("loc1").split(",");
		final String[] sLoc2 = doc.getString("loc2").split(",");
		final Location loc1 = new Location(Bukkit.getWorld(sLoc1[0]), Double.valueOf(sLoc1[1]),
				Double.valueOf(sLoc1[2]), Double.valueOf(sLoc1[3]));
		final Location loc2 = new Location(Bukkit.getWorld(sLoc2[0]), Double.valueOf(sLoc2[1]),
				Double.valueOf(sLoc2[2]), Double.valueOf(sLoc2[3]));
		final LinkedChest lc = new LinkedChest(loc1, owner, doc.getLong("time"));
		final LinkedChest lc2 = new LinkedChest(loc2, owner, doc.getLong("time"));
		lc.setOtherChest(lc2);
		lc2.setOtherChest(lc);
		return lc;
	}
}
