package de.srsuders.chestlink.game;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bson.Document;
import org.bukkit.Location;

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
	private ArrayList<LinkedChest> linkedChests;
	private MongoCollection<Document> dbCollection;

	public CLPlayer(final UUID uuid) {
		this.uuid = uuid;
		this.linkedChestBuilder = new LinkedChestBuilder();
		this.dbCollection = Data.getInstance().getMongoDB().getPlayertableCollection();
		
		final BasicDBObject query = new BasicDBObject("_id", uuid.toString());

		this.linkedChests = new ArrayList<>();
		MongoCursor<Document> cursor = dbCollection.find(query).cursor();
		if(!cursor.hasNext()) {
			dbCollection.insertOne(new Document("_id", uuid.toString()));
		}
		if(cursor.hasNext()) {
			Document doc = cursor.tryNext();
			for(final String key : doc.keySet()) {
				if(!key.equals("_id")) {
					final Document lcDoc = (Document) doc.get(key);
					final LinkedChest lc = LinkedChest.fromDocument(lcDoc, uuid);
					this.linkedChests.add(lc);
					this.linkedChests.add(lc.getLinkedChest());
				}
			}
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
		for(LinkedChest chest : linkedChests) {
			final Location loc1 = chest.getLocation();
			final Location loc2 = chest.getLinkedChest().getLocation();
			if(MCUtils.equalLocation(loc1, loc) || MCUtils.equalLocation(loc2, loc)) 
				return chest;
		}
		return null;
	}
	
	public Document linkedChestsToDocument() {
		final Document doc = new Document("_id", this.uuid.toString());
		final List<LinkedChest> usedChests = new ArrayList<>();
		for(LinkedChest linkedChest : linkedChests) {
			if(!usedChests.contains(linkedChest)) {
				final Document lcDoc = new Document();
				lcDoc.append("loc1", MCUtils.locationToString(linkedChest.getLocation()));
				lcDoc.append("loc2", MCUtils.locationToString(linkedChest.getLinkedChest().getLocation()));
				lcDoc.append("time", linkedChest.getFinishedTime());
				lcDoc.append("state", linkedChest.linked());
				lcDoc.append("chest_id", linkedChest.getID());
				doc.append(linkedChest.getID(), lcDoc);
				usedChests.add(linkedChest.getLinkedChest());
			}
		}
		return doc;
	}

	/**
	 * Trägt den Spieler in die MongoDB ein
	 */
	public void savePlayer() {
		final Document obj = new Document("_id", uuid.toString());
		dbCollection.findOneAndReplace(obj, linkedChestsToDocument());
	}

	/**
	 * Speichert die gelinkten Kisten local ein, aber nicht in Datenbank
	 */
	public void saveLinkedChest() {
		final LinkedChest lc = this.linkedChestBuilder.toLinkedChest(uuid);
		this.linkedChests.add(lc);
		CLHandler.addLinkedChest(lc);
		this.linkedChestBuilder.setLoc1(null);
		this.linkedChestBuilder.setLoc2(null);
		lc.getLinkedChestInventory().saveInventory();
	}
	
	public void removeLinkedChest(final Location loc) {
		@SuppressWarnings("unchecked")
		final ArrayList<LinkedChest> linkedChestsCopy = (ArrayList<LinkedChest>) linkedChests.clone();
		for (LinkedChest lc : linkedChestsCopy) {
			if(MCUtils.equalLocation(loc, lc.getLocation()) || MCUtils.equalLocation(loc, lc.getLinkedChest().getLocation()) ) {
				CLHandler.removeLinkedChest(lc);
				this.linkedChests.remove(lc);
				return;
			}
		}
	}

	public LinkedChestBuilder getLinkedChestBuilder() {
		return this.linkedChestBuilder;
	}

	public ArrayList<LinkedChest> getLinkedChests() {
		return this.linkedChests;
	}

	public UUID getUUID() {
		return this.uuid;
	}
}
