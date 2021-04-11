package de.srsuders.chestlink.game.object;

import java.util.UUID;

import org.bukkit.Location;

import de.srsuders.chestlink.game.CLPlayer;
import de.srsuders.chestlink.game.object.inventory.LinkedChestInventory;
import de.srsuders.chestlink.utils.MCUtils;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 06.04.2021 Project: chestlink
 */
public class LinkedChest {

	private Location loc;
	private LinkedChest otherChest;
	private UUID owner;
	private String id;
	private LinkedChestInventory inv;
	private final long finishedAt;
	private boolean state = false;

	public LinkedChest(final Location loc, final UUID owner, final String id, final long finishedAt) {
		this.loc = loc;
		this.owner = owner;
		this.id = id;
		this.finishedAt = finishedAt;
		this.state = true;
	}

	public LinkedChest(final Location loc, final UUID owner, final String id, final long finishedAt,
			 final boolean state) {
		this.loc = loc;
		this.owner = owner;
		this.id = id;
		this.finishedAt = finishedAt;
		this.state = state;
	}
	
	public void setLinkedChestInventory(final LinkedChestInventory lcInv) {
		if(this.inv == null)
			this.inv = lcInv;
	}

	public LinkedChestInventory getLinkedChestInventory() {
		return this.inv;
	}

	public boolean linked() {
		if (this.otherChest == null)
			return false;
		return this.state;
	}

	public void setState(final boolean state) {
		this.state = state;
	}

	public void setOtherChest(final LinkedChest linkedChest) {
		this.otherChest = linkedChest;
	}

	public String getID() {
		return this.id;
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

	public UUID getOwnerUUID() {
		return this.owner;
	}
	
	public CLPlayer getCLPlayer() {
		return CLHandler.getCLPlayer(owner);
	}

	/**
	 * Überprüft die Parameter "owner" und die "location" Wenn es eine
	 * Übereinstimmung gibt, gibt er true aus
	 */
	public boolean equals(Object obj) {
		if (obj != null) {
			if (obj instanceof LinkedChest) {
				LinkedChest lc = (LinkedChest) obj;
				return lc.getOwnerUUID().equals(this.owner) && MCUtils.equalLocation(loc, lc.getLocation());
			}
		}
		return false;
	}
	
	public String toString() {
		return "loc1: " + getLocation().toString() + " loc2: " + getLinkedChest().getLocation().toString();
	}
}
