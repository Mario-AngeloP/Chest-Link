package de.srsuders.chestlink.game;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;

import de.srsuders.chestlink.game.object.CLHandler;
import de.srsuders.chestlink.game.object.LinkedChest;
import de.srsuders.chestlink.game.object.LinkedChestBuilder;
import de.srsuders.chestlink.utils.MCUtils;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 06.04.2021 Project: chestlink
 */
public class CLPlayer {

	private final UUID uuid;
	private LinkedChestBuilder linkedChestBuilder;

	public CLPlayer(final UUID uuid) {
		this.uuid = uuid;
		this.linkedChestBuilder = new LinkedChestBuilder();
	}

	/**
	 * Diese ist eine Spieler spezifische Abfrage. Hierbei wird null returnt, selbst
	 * wenn es sogar ein Linkedchest ist, halt nur nicht die vom Spieler
	 * 
	 * @param loc
	 * @return
	 */
	public LinkedChest getLinkedChestByLocation(final Location loc) {
		for (LinkedChest chest : getLinkedChests()) {
			final Location loc1 = chest.getLocation();
			final Location loc2 = chest.getLinkedChest().getLocation();
			if (MCUtils.equalLocation(loc1, loc) || MCUtils.equalLocation(loc2, loc))
				return chest;
		}
		return null;
	}

	/**
	 * Speichert die gelinkten Kisten local ein, aber nicht in Datenbank
	 */
	public void saveLinkedChest() {
		final LinkedChest lc = this.linkedChestBuilder.toLinkedChest(uuid);
		CLHandler.addLinkedChest(lc);
		this.linkedChestBuilder.setLoc1(null);
		this.linkedChestBuilder.setLoc2(null);
	}

	public LinkedChestBuilder getLinkedChestBuilder() {
		return this.linkedChestBuilder;
	}

	public List<LinkedChest> getLinkedChests() {
		return CLHandler.getLinkedChestsOfPlayer(this);
	}

	/**
	 * Diese Methode vermeidet, dass LinkedChests mit der gleichen ID zweimal vorkommen
	 * @return
	 */
	public List<LinkedChest> getSingleLinkedChests() {
		final List<LinkedChest> list = new ArrayList<>();
		final List<String> usedChests = new ArrayList<>();
		for(LinkedChest lc : getLinkedChests()) {
			if(!usedChests.contains(lc.getID())) {
				list.add(lc);
				usedChests.add(lc.getID());
			}
		}
		return list;
	}
	
	public UUID getUUID() {
		return this.uuid;
	}

	public boolean equals(final Object obj) {
		if (obj instanceof CLPlayer) {
			final CLPlayer clPlayer = (CLPlayer) obj;
			return clPlayer.getUUID().equals(getUUID());
		}
		return false;
	}
}
