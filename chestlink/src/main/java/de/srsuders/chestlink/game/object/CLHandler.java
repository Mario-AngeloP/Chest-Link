package de.srsuders.chestlink.game.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

	private static final Map<UUID, CLPlayer> players = new HashMap<>();
	private static final List<LinkedChest> linkedChests = new ArrayList<>();
	
	public static void updateList(final List<LinkedChest> updateList) {
		linkedChests.addAll(updateList);
	}
	
	public static void saveLinkedChests() {
		final Document query = new Document("_id", "chests");
		Document doc = Data.getInstance().getMongoDB().getSavedChests().find(query).first();
		if(doc == null) {
			doc = new Document("_id", "chests");
			Data.getInstance().getMongoDB().getSavedChests().insertOne(query);
		}
		final BasicDBList list = new BasicDBList();
		for(LinkedChest linkedChest : linkedChests) {
			list.add(MCUtils.locationToString(linkedChest.getLocation()) + "," + linkedChest.getOwner().toString() + "," + linkedChest.getFinishedTime() + "," + linkedChest.getID());
		}
		doc.put("chests", list);
		Data.getInstance().getMongoDB().getSavedChests().findOneAndReplace(query, doc);
	}
	
	/**
	 * Hier kann man eine LinkedChest und deren Owner durch eine location erhalten sowie
	 * überprüfen ob es überhaupt eine LinkedChest an besagter location ist.
	 * Returnt null, wenn es keine linkedChest ist
	 * @param loc
	 * @return
	 */
	public static LinkedChest getLinkedChestByLocation(final Location loc) {
		if(loc.getWorld() == null) {
			new NullPointerException("Die World von der angegeben Location darf nicht null sein!");
			return null;
		}
		for(LinkedChest lc : linkedChests) 
			if(MCUtils.equalLocation(loc, lc.getLocation())) return lc;
		return null;
	}
	
	public static void removeLinkedChest(final LinkedChest lc) {
		if(linkedChests.contains(lc)) linkedChests.remove(lc);
		if(lc.getLinkedChest() != null) if(linkedChests.contains(lc.getLinkedChest())) linkedChests.remove(lc.getLinkedChest());
	}
	
	public static void addLinkedChest(final LinkedChest lc) {
		linkedChests.add(lc);
		linkedChests.add(lc.getLinkedChest());
	}
	
	public static CLPlayer getCLPlayer(final UUID uuid) {
		if (!players.containsKey(uuid)) {
			final CLPlayer clp = new CLPlayer(uuid);
			players.put(uuid, clp);
		}
		return players.get(uuid);
	}
}
