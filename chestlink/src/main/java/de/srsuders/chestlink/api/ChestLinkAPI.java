package de.srsuders.chestlink.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bson.Document;
import org.bukkit.Location;

import com.mongodb.BasicDBList;

import de.srsuders.chestlink.game.CLPlayer;
import de.srsuders.chestlink.game.object.LinkedChest;
import de.srsuders.chestlink.storage.Data;
import de.srsuders.chestlink.utils.MCUtils;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 06.04.2021 Project: chestlink
 */
public class ChestLinkAPI {

	private static final Map<CLPlayer, List<LinkedChest>> linkedChests = new HashMap<>();

	/**
	 * @param updateList
	 */
	public static void updateLinkedChests(final Map<CLPlayer, List<LinkedChest>> updateList) {
		linkedChests.putAll(updateList);
	}

	/**
	 * Diese Methode sucht nach der LinkedChest mit der angegeben ID
	 * Es wird null returnt, wenn es keine LinkedChest mit der ID gibt
	 * @param id
	 * @return
	 */
	public static LinkedChest getLinkedChestByID(final String id) {
		for(Entry<CLPlayer, List<LinkedChest>> entry : linkedChests.entrySet()) {
			final List<LinkedChest> lcs = entry.getValue();
			for(LinkedChest lc : lcs) {
				if(lc.getID().equals(id)) return lc;
			}
		}
		return null;
	}
	
	
	/**
	 * Diese Methode speichert alle LinkedChests in die MongoDB ab
	 */
	public static void saveLinkedChests() {
		final Document query = new Document("_id", "chests");
		Document doc = Data.getInstance().getMongoDB().getSavedChests().find(query).first();
		if (doc == null) {
			doc = new Document("_id", "chests");
			Data.getInstance().getMongoDB().getSavedChests().insertOne(query);
		}
		final BasicDBList targetDoc = new BasicDBList();
		for (Entry<CLPlayer, List<LinkedChest>> entry : linkedChests.entrySet()) {
			final Map<String, LinkedChest> usedChests = new HashMap<>();
			for (LinkedChest linkedChest : entry.getValue()) {
				if (!usedChests.containsKey(linkedChest.getID())) {
					usedChests.put(linkedChest.getID(), linkedChest);
				} else {
					final LinkedChest lc2 = usedChests.get(linkedChest.getID());
					lc2.getLinkedChestInventory().saveInventory();
					targetDoc.add(MCUtils.locationToString(lc2.getLocation()) + "," + lc2.getOwnerUUID().toString()
							+ "," + lc2.getFinishedTime() + "," + lc2.getID());
					targetDoc.add(MCUtils.locationToString(linkedChest.getLocation()) + ","
							+ linkedChest.getOwnerUUID().toString() + "," + linkedChest.getFinishedTime() + ","
							+ linkedChest.getID());
				}
			}
		}
		doc.put("chests", targetDoc);
		Data.getInstance().getMongoDB().getSavedChests().findOneAndReplace(query, doc);
	}

	/**
	 * Diese Methode gibt alle LinkedChests des angegebenen Spielers zurück
	 * @param clPlayer
	 * @return
	 */
	public static List<LinkedChest> getLinkedChestsOfPlayer(final CLPlayer clPlayer) {
		for (Entry<CLPlayer, List<LinkedChest>> map : linkedChests.entrySet()) {
			if (map.getKey().equals(clPlayer)) {
				return map.getValue();
			}
		}
		return null;
	}

	/**
	 * Hier kann man eine LinkedChest und deren Owner durch eine location erhalten
	 * sowie überprüfen ob es überhaupt eine LinkedChest an besagter location ist.
	 * Returnt null, wenn es keine linkedChest ist. Nicht performant
	 * 
	 * @param loc
	 * @return
	 */
	public static LinkedChest getLinkedChestByLocation(final Location loc) {
		if (loc.getWorld() == null) {
			new NullPointerException("Die World von der angegeben Location darf nicht null sein!");
			return null;
		}
		for (Entry<CLPlayer, List<LinkedChest>> map : linkedChests.entrySet()) {
			for (LinkedChest lc : map.getValue()) {
				if (MCUtils.equalLocation(lc.getLocation(), loc))
					return lc;
			}
		}
		return null;
	}

	/**
	 * Es werden alle {@link} mit den zugehörigen Spieler in einer Map wiedergegeben.
	 * @return
	 */
	public static Map<CLPlayer, List<LinkedChest>> getLinkedchests() {
		return linkedChests;
	}
	
	/**
	 * Entfernt die LinkedChest aus dem System
	 * @param clp
	 * @param lc
	 */
	public static void removeLinkedChest(final LinkedChest lc) {
		System.out.println(lc.toString());
		final List<LinkedChest> chests = linkedChests.get(lc.getCLPlayer());
		chests.remove(lc);
		chests.remove(lc.getLinkedChest());
		linkedChests.put(lc.getCLPlayer(), chests);
	}

	/**
	 * Fügt eine LinkedChest ins System ein
	 * @param lc
	 */
	public static void addLinkedChest(final LinkedChest lc) {
		final List<LinkedChest> chests = linkedChests.get(lc.getCLPlayer());
		chests.add(lc);
		chests.add(lc.getLinkedChest());
		linkedChests.put(lc.getCLPlayer(), chests);
	}

	/**
	 * Gibt den CLPlayer mit der jeweiligen UUID wieder
	 * @param uuid
	 * @return
	 */
	public static CLPlayer getCLPlayer(final UUID uuid) {
		for (Entry<CLPlayer, List<LinkedChest>> map : linkedChests.entrySet()) {
			if (map.getKey().getUUID().equals(uuid)) {
				return map.getKey();
			}
		}
		final ArrayList<LinkedChest> list = new ArrayList<>();
		final CLPlayer clp = new CLPlayer(uuid);
		linkedChests.put(clp, list);
		return clp;
	}
}
