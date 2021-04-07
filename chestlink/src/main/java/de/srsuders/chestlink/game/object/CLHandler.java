package de.srsuders.chestlink.game.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bson.Document;
import org.bukkit.Location;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

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
		final BasicDBObject obj = new BasicDBObject("_id", "007");
		for(LinkedChest linkedChest : linkedChests) {
			final BasicDBList list = (BasicDBList) obj.get(linkedChest.getOwner().toString());
			list.add(new BasicDBObject(MCUtils.locationToString(linkedChest.getLocation()), linkedChest.getFinishedTime()));
			obj.put(linkedChest.getOwner().toString(), list);
		}
		Data.getInstance().getMongoDB().getSavedChests().findOneAndReplace(new BasicDBObject("_id", "007"), Document.parse(obj.toJson()));
	}
	
	/**
	 * Ist diese Kiste bereits verlinkt? Ob es mit einem 
	 * @return
	 */
	public static boolean checkAlreadyLinked(final LinkedChest lc) {
		if(lc == null) {
			new NullPointerException("LinkedChest darf nicht null sein.");
			return true;
		}
		boolean linked = false;
		if(lc.getLocation() != null) 
			linked = linkedChests.contains(lc);
		
		if(linked != true) 
			if(lc.getLinkedChest() != null) 
				if(lc.getLinkedChest().getLocation() != null) {
					linked = linkedChests.contains(lc.getLinkedChest());
				}
		return linked;
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
