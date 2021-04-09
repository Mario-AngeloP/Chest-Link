package de.srsuders.chestlink.game.object.inventory;

import java.io.IOException;
import java.util.Map.Entry;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.mongodb.client.MongoCollection;

import de.srsuders.chestlink.game.object.LinkedChest;
import de.srsuders.chestlink.storage.Data;
import de.srsuders.chestlink.storage.Items;
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
		}
	}
	
	public Inventory getInventory() {
		return this.inventory;
	}
	
	public void saveInventory() {
		//TODO abspeichern
		final Document doc = new Document("_id", linkedChest.getID());
		for(int i = 0; i<27;i++) {
			
		}
		
		col.findOneAndReplace(new Document("_id", linkedChest.getID()), doc);
	}
	
	public LinkedChest getLinkedChest() {
		return this.linkedChest;
	}
}
