package de.srsuders.chestlink.io.mongo;

import org.bson.Document;

import com.google.gson.JsonObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import de.srsuders.chestlink.storage.Data;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 06.04.2021 Project: chestlink
 */
public class MongoDB {

	private MongoClient client;
	private MongoDatabase mongoDB;
	private MongoCollection<Document> savedChestCollection;
	private MongoCollection<Document> linkedChestInventoryCollection;

	@SuppressWarnings("deprecation")
	public void connect() {
		if (isConnected()) {
			System.out.println("MongoDB ist bereits verbunden.");
			return;
		}
		final JsonObject cfg = Data.getInstance().getConfigReader().getMongoDB();

		this.client = new MongoClient(new MongoClientURI(
				"mongodb://" + cfg.get("user").getAsString() + ":" + cfg.get("password").getAsString() + "@"
						+ cfg.get("host").getAsString() + ":" + cfg.get("port").getAsString()
						+ "/?authSource=admin&readPreference=primary&appname=MongoDB%20Compass&ssl=false"));
		this.mongoDB = client.getDatabase(cfg.get("database").getAsString());
		try {
			client.getAddress();
			System.out.println("Es konnte erfolgreich eine Verbindung zur MongoDB hergestellt werden!");
			this.savedChestCollection = mongoDB.getCollection("savedChests");
			if (this.savedChestCollection == null) {
				this.mongoDB.createCollection("savedChests");
				this.savedChestCollection = mongoDB.getCollection("savedChests");
				savedChestCollection.insertOne(new Document("_id", "chests"));
			}
			this.linkedChestInventoryCollection = mongoDB.getCollection("linkedChestInventory");
			if (this.linkedChestInventoryCollection == null) {
				this.mongoDB.createCollection("linkedChestInventory");
				this.linkedChestInventoryCollection = mongoDB.getCollection("linkedChestInventory");
				linkedChestInventoryCollection.insertOne(new Document("_id", "chests"));
			}
		} catch (Exception exc) {
			exc.printStackTrace();
			System.out.println("Es konnte keine Verbindung zur MongoDB hergestellt werden!");
			client.close();
		}
	}

	public MongoCollection<Document> getLinkedChestInventoryCollection() {
		return this.linkedChestInventoryCollection;
	}

	public MongoCollection<Document> getSavedChests() {
		return this.savedChestCollection;
	}

	public MongoDatabase getDB() {
		return this.mongoDB;
	}

	public void disconnect() {
		if (isConnected()) {
			this.client.close();
			this.client = null;
		}
	}

	public boolean isConnected() {
		return client != null;
	}
}
