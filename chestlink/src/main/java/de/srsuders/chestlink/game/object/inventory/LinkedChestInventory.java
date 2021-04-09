package de.srsuders.chestlink.game.object.inventory;

import java.io.IOException;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.mongodb.client.MongoCollection;

import de.srsuders.chestlink.game.object.LinkedChest;
import de.srsuders.chestlink.storage.Data;
import de.srsuders.chestlink.utils.Serialize;

/**
* Author: SrSuders aka. Mario-Angelo
* Date: 09.04.2021
* Project: chestlink
*/
public class LinkedChestInventory {

	private LinkedChest linkedChest;
	private final MongoCollection<Document> col;
	private Inventory inventory;
	
	public LinkedChestInventory(final LinkedChest linkedChest) {
		this.linkedChest = linkedChest;
		this.inventory = Bukkit.createInventory(null, 3*9, "§eLinkedChest");
		this.col = Data.getInstance().getMongoDB().getLinkedChestInventoryCollection();
		final Document query = new Document("_id", linkedChest.getID());
		final Document rs = col.find(query).first();
		if(rs == null) {
			col.insertOne(query);
		} else {
			readInventory();
		}
	}
	
	public Inventory getInventory() {
		return this.inventory;
	}
	
	private void readInventory() {
		final Document query = new Document("_id", linkedChest.getID());
		final Document rs = col.find(query).first();
		for(String key : rs.keySet()) {
			if(!key.equals("_id")) {
				int i = Integer.valueOf(key.replaceAll("slot", ""));
				final String value = rs.getString(key);
				try {
					inventory.setItem(i, ItemStack.deserialize(Serialize.deserialize(value)));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void saveInventory() {
		//TODO abspeichern
		final Document doc = new Document("_id", linkedChest.getID());
		for(int i = 0; i<27;i++) {
			final ItemStack is = inventory.getItem(i);
			if(is != null) {
				try {
					String s = Serialize.serialize(is.serialize());
					doc.append("slot" + i, s);
					System.out.println(s);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		col.findOneAndReplace(new Document("_id", linkedChest.getID()), doc);
	}
	
	public LinkedChest getLinkedChest() {
		return this.linkedChest;
	}
}
