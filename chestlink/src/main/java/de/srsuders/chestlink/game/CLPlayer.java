package de.srsuders.chestlink.game;

import java.util.Map.Entry;
import java.util.UUID;

import org.bson.Document;
import org.bukkit.Location;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import de.srsuders.chestlink.game.object.CLHandler;
import de.srsuders.chestlink.game.object.LinkedChest;
import de.srsuders.chestlink.game.object.LinkedChestBuilder;
import de.srsuders.chestlink.storage.Data;
import de.srsuders.chestlink.utils.MCUtils;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 06.04.2021 Project: chestlink
 */
public class CLPlayer {

	private final UUID uuid;
	private LinkedChestBuilder linkedChestBuilder;
	private BasicDBObject linkedChests;
	private MongoCollection<Document> dbCollection;

	@SuppressWarnings("unchecked")
	public CLPlayer(final UUID uuid) {
		this.uuid = uuid;
		this.linkedChestBuilder = new LinkedChestBuilder();
		this.dbCollection = Data.getInstance().getMongoDB().getPlayertableCollection();
		
		final BasicDBObject query = new BasicDBObject("_id", uuid.toString());

		this.linkedChests = new BasicDBObject();
		linkedChests.put("_id", uuid.toString());
		MongoCursor<Document> cursor = dbCollection.find(query).cursor();
		if(!cursor.hasNext()) {
			dbCollection.insertOne(new Document(linkedChests.toMap()));
		}
		while(cursor.hasNext()) {
			Document doc = cursor.tryNext();
			this.linkedChests = BasicDBObject.parse(new Gson().fromJson(doc.toJson(), JsonObject.class).toString());
			return;
		}
	}
	
	/**
	 * Diese ist eine Spieler spezifische Abfrage. 
	 * Hierbei wird null returnt, selbst wenn es sogar ein Linkedchest ist,
	 * halt nur nich die vom Spieler
	 * @param loc
	 * @return
	 */
	public LinkedChest getLinkedChestByLocation(final Location loc) {
		for(String key : linkedChests.keySet()) {
			final BasicDBObject obj = (BasicDBObject) linkedChests.get(key);
			final Location loc1 = MCUtils.stringToLocation(obj.getString("loc1"));
			final Location loc2 = MCUtils.stringToLocation(obj.getString("loc2"));
			if(MCUtils.equalLocation(loc1, loc) || MCUtils.equalLocation(loc2, loc)) {
				final long time = obj.getLong("time");
				final boolean state = obj.getBoolean("state");
				final LinkedChest lc1 = new LinkedChest(loc1, uuid, time, state);
				final LinkedChest lc2 = new LinkedChest(loc2, uuid, time, state);
				lc1.setOtherChest(lc2);
				lc2.setOtherChest(lc1);
				return lc1;
			}
		}
		return null;
	}

	public void savePlayer() {
		final BasicDBObject obj = new BasicDBObject("_id", uuid.toString());
		dbCollection.findOneAndReplace(obj, Document.parse(linkedChests.toJson()));
	}

	/**
	 * Speichert die gelinkten Kisten local ein, aber nicht in Datenbank
	 */
	public void saveLinkedChest() {
		this.linkedChestBuilder.saveLinkedChestToBasicDBObject(linkedChests, this.uuid);
		this.linkedChestBuilder.setLoc1(null);
		this.linkedChestBuilder.setLoc2(null);
	}
	
	public void removeLinkedChest(final Location loc) {
		final BasicDBObject linkedChestsCopy = (BasicDBObject) linkedChests.copy();
		for (Entry<String, Object> map : linkedChestsCopy.entrySet()) {
			final String uuid = map.getKey();
			final BasicDBObject obj = (BasicDBObject) map.getValue();
			if(MCUtils.equalLocation(loc, MCUtils.stringToLocation(obj.getString("loc1"))) | MCUtils.equalLocation(loc, MCUtils.stringToLocation(obj.getString("loc2")))) { 
				CLHandler.removeLinkedChest(CLHandler.getLinkedChestByLocation(loc));
				linkedChests.remove(uuid);
				return;
			}
		}
	}

	public LinkedChestBuilder getLinkedChestBuilder() {
		return this.linkedChestBuilder;
	}

	public BasicDBObject getLinkedChests() {
		return this.linkedChests;
	}

	public UUID getUUID() {
		return this.uuid;
	}
}
