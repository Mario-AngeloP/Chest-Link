package de.srsuders.chestlink.game.object.inventory;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.mongodb.client.MongoCollection;

import de.srsuders.chestlink.game.object.LinkedChest;
import de.srsuders.chestlink.storage.Data;
import de.srsuders.chestlink.utils.Serialize;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 09.04.2021 Project: chestlink
 */
public class LinkedChestInventory {

	private LinkedChest linkedChest;
	private final MongoCollection<Document> col;
	private final Inventory inventory;

	public LinkedChestInventory(final LinkedChest linkedChest) {
		this.linkedChest = linkedChest;
		this.inventory = Bukkit.createInventory(null, 3 * 9, "┬žeLinkedChest");
		this.col = Data.getInstance().getMongoDB().getLinkedChestInventoryCollection();
		final Document query = new Document("_id", linkedChest.getID());
		final Document rs = col.find(query).first();
		if (rs == null) {
			col.insertOne(query);
		} else {
			readInventory();
		}
	}

	public boolean hasContent() {
		for(ItemStack is : this.inventory.getContents()) {
			if(is != null) return true;
		}
		return false;
	}
	
	public Inventory getInventory() {
		return this.inventory;
	}

	private void readInventory() {
		final Document query = new Document("_id", linkedChest.getID());
		final Document rs = col.find(query).first();
		for (String key : rs.keySet()) {
			if (!key.equals("_id")) {
				final Document doc = (Document) rs.get(key);
				final Integer slot = doc.getInteger("slot");
				inventory.setItem(slot, Serialize.fromDocumentToItemStack(doc));
			}
		}
	}

	public void saveInventory() {
		final Document doc = new Document("_id", linkedChest.getID());
		int i = 0;
		for (ItemStack is : inventory.getContents()) {
			if (is != null) {
				doc.put("slot" + i, Serialize.fromItemStackToDocument(is, i));
			}
			i++;
		}
		col.findOneAndDelete(new Document("_id", linkedChest.getID()));
		col.insertOne(doc);
	}

	public LinkedChest getLinkedChest() {
		return this.linkedChest;
	}
}
