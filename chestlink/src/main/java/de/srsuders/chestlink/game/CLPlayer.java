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

	public void savePlayer() {
		final BasicDBObject obj = new BasicDBObject("_id", uuid.toString());
		dbCollection.findOneAndReplace(obj, Document.parse(linkedChests.toJson()));
	}

	public void saveLinkedChest() {
		this.linkedChestBuilder.saveLinkedChestToBasicDBObject(linkedChests);
		this.linkedChestBuilder.setLoc1(null);
		this.linkedChestBuilder.setLoc2(null);
	}
	
	public boolean checkFinishable() {
		
		return false;
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
