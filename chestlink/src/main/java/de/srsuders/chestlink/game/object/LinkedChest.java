package de.srsuders.chestlink.game.object;

import java.util.UUID;

import org.bukkit.Location;

import de.srsuders.chestlink.api.ChestLinkAPI;
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

	/**
	 * Dieser Constructor setzt alle relevanten Parameter fest. 
	 * @param loc
	 * @param owner
	 * @param id
	 * @param finishedAt
	 */
	public LinkedChest(final Location loc, final UUID owner, final String id, final long finishedAt) {
		this.loc = loc;
		this.owner = owner;
		this.id = id;
		this.finishedAt = finishedAt;
	}

	/**
	 * Setzt das Inventar der LinkedChest fest, für eine korrekte Anwendung, 
	 * müsste man die verbundene Kiste ebenfalls setzen
	 * @param lcInv
	 */
	public void setLinkedChestInventory(final LinkedChestInventory lcInv) {
		if (this.inv == null)
			this.inv = lcInv;
	}

	/**
	 * Gibt das Inventar der LinkedChest wieder
	 * @return
	 */
	public LinkedChestInventory getLinkedChestInventory() {
		return this.inv;
	}

	/**
	 * Setzt die zweite LinkedChest fest, wird nicht zur Verwendung in der API empfohlen
	 * @param linkedChest
	 */
	public void setOtherChest(final LinkedChest linkedChest) {
		this.otherChest = linkedChest;
	}

	public String getID() {
		return this.id;
	}

	/**
	 * Gibt die Chest mit der hier verbundende Chest wieder
	 * @return
	 */
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
		return ChestLinkAPI.getCLPlayer(owner);
	}

	/**
	 * Überprüft die Parameter "owner" und die "location" Wenn es eine
	 * Übereinstimmung gibt, gibt er true aus
	 */
	public boolean equals(Object obj) {
		if (obj != null) {
			if (obj instanceof LinkedChest) {
				LinkedChest lc = (LinkedChest) obj;
				return lc.getOwnerUUID().equals(this.owner) && MCUtils.equalLocation(loc, lc.getLocation()) && lc.getID().equals(id);
			}
		}
		return false;
	}

	/**
	 * Gibt die Position der ersten und der zweiten Kiste als String wieder
	 * Die Owner UUID und die ID wird hinten dran gehängt
	 */
	public String toString() {
		return "loc1: " + getLocation().toString() + " loc2: " + getLinkedChest().getLocation().toString() + " owner: " + getOwnerUUID() + " id: " + getID();
	}
}
