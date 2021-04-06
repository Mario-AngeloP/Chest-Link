package de.srsuders.chestlink.game;

import java.util.Map.Entry;
import java.util.UUID;

import org.bson.Document;
import org.bukkit.Location;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;

import de.srsuders.chestlink.game.object.LinkedChestBuilder;
import de.srsuders.chestlink.storage.Data;
import de.srsuders.chestlink.utils.MCUtils;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 06.04.2021 Project: chestlink
 */
public class CLPlayer {

	private final UUID uuid;
	private LinkedChestBuilder linkedChestBuilder;
	private BasicDBObject linkedChests = new BasicDBObject();
	private MongoCollection<Document> dbCollection;

	public CLPlayer(final UUID uuid) {
		this.uuid = uuid;
		this.linkedChestBuilder = new LinkedChestBuilder();
		this.dbCollection = Data.getInstance().getMongoDB().getPlayertableCollection();

		final BasicDBObject query = new BasicDBObject();
		query.put("puuid", uuid.toString());

		linkedChests.put("puuid", uuid.toString());

		// Unpraktische Methode, soll aber letztlich dem Spieler linkedChests verteilen
		// falls vorhanden
		dbCollection.find(query).iterator().forEachRemaining(doc -> {
			linkedChests.putAll(doc);
		});
	}

	@SuppressWarnings("unchecked")
	public void savePlayer() {
		final BasicDBObject obj = new BasicDBObject();
		obj.append("puuid", uuid.toString());
		dbCollection.find(obj).iterator().forEachRemaining(doc -> {
			dbCollection.findOneAndReplace(obj, new Document(linkedChests.toMap()));
			return;
		});
		dbCollection.insertOne(new Document(linkedChests.toMap()));
	}

	public void saveLinkedChest() {
		this.linkedChestBuilder.saveLinkedChestToBasicDBObject(linkedChests);
		this.linkedChestBuilder.setLoc1(null);
		this.linkedChestBuilder.setLoc2(null);
	}

	public void removeLinkedChest(final Location loc) {
		final BasicDBObject linkedChestsCopy = (BasicDBObject) linkedChests.copy();
		for (Entry<String, Object> map : linkedChestsCopy.entrySet()) {
			final String uuid = map.getKey();
			final BasicDBObject obj = (BasicDBObject) map.getValue();
			if(MCUtils.equalLocation(loc, MCUtils.stringToLocation(obj.getString("loc1"))) | MCUtils.equalLocation(loc, MCUtils.stringToLocation(obj.getString("loc2")))) { 
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
