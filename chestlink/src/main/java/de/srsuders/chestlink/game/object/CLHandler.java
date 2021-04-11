package de.srsuders.chestlink.game.object;

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
import de.srsuders.chestlink.storage.Data;
import de.srsuders.chestlink.utils.MCUtils;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 06.04.2021 Project: chestlink
 */
public class CLHandler {

	private static final Map<CLPlayer, List<LinkedChest>> linkedChests = new HashMap<>();

	public static void updateList(final Map<CLPlayer, List<LinkedChest>> updateList) {
		linkedChests.putAll(updateList);
	}

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
			for(LinkedChest linkedChest : entry.getValue()) {
				if(!usedChests.containsKey(linkedChest.getID())) {
					usedChests.put(linkedChest.getID(), linkedChest);
				} else {
					final LinkedChest lc2 = usedChests.get(linkedChest.getID());
					lc2.getLinkedChestInventory().saveInventory();
					targetDoc.add(MCUtils.locationToString(lc2.getLocation()) + "," + lc2.getOwnerUUID().toString() + ","
							+ lc2.getFinishedTime() + "," + lc2.getID());
					targetDoc.add(MCUtils.locationToString(linkedChest.getLocation()) + "," + linkedChest.getOwnerUUID().toString() + ","
							+ linkedChest.getFinishedTime() + "," + linkedChest.getID());
				}
			}
		}
		doc.put("chests", targetDoc);
		Data.getInstance().getMongoDB().getSavedChests().findOneAndReplace(query, doc);
	}
	
	public static List<LinkedChest> getLinkedChestsOfPlayer(final CLPlayer clPlayer) {
		for(Entry<CLPlayer, List<LinkedChest>> map : linkedChests.entrySet()) {
			if(map.getKey().equals(clPlayer)) {
				return map.getValue();
			}
		}
		return null;
	}

	/**
	 * Hier kann man eine LinkedChest und deren Owner durch eine location erhalten
	 * sowie überprüfen ob es überhaupt eine LinkedChest an besagter location ist.
	 * Returnt null, wenn es keine linkedChest ist.
	 * Nicht performant
	 * @param loc
	 * @return
	 */
	public static LinkedChest getLinkedChestByLocation(final Location loc) {
		if (loc.getWorld() == null) {
			new NullPointerException("Die World von der angegeben Location darf nicht null sein!");
			return null;
		}
		for (Entry<CLPlayer, List<LinkedChest>> map : linkedChests.entrySet()) {
			for(LinkedChest lc : map.getValue()) {
				if(lc.getLocation().equals(loc)) return lc;
			}
		}
		return null;
	}

	public static Map<CLPlayer, List<LinkedChest>> getLinkedchests() {
		return linkedChests;
	}

	public static void addLinkedChest(final LinkedChest lc) {
		final List<LinkedChest> chests = linkedChests.get(lc.getCLPlayer());
		chests.add(lc);
		chests.add(lc.getLinkedChest());
		linkedChests.put(lc.getCLPlayer(), chests);
	}
	
	public static CLPlayer getCLPlayer(final UUID uuid) {
		for(Entry<CLPlayer, List<LinkedChest>> map : linkedChests.entrySet()) {
			if(map.getKey().getUUID().equals(uuid)) {
				return map.getKey();
			}
		}
		final ArrayList<LinkedChest> list = new ArrayList<>();
		final CLPlayer clp = new CLPlayer(uuid);
		linkedChests.put(clp, list);
		return clp;
	}
}
