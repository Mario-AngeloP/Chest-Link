package de.srsuders.chestlink.storage;

import java.util.HashMap;
import java.util.UUID;

import de.srsuders.chestlink.ChestLink;
import de.srsuders.chestlink.game.inventory.LinkFinishInventory;
import de.srsuders.chestlink.game.object.inventory.OverviewInventoryPlayer;
import de.srsuders.chestlink.io.config.ConfigReader;
import de.srsuders.chestlink.io.mongo.MongoDB;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 06.04.2021 Project: chestlink
 */
public class Data {

	private static Data instance;
	private ChestLink chestLink;
	private final HashMap<UUID, String> cache;
	private final MongoDB mongoDB;
	private final ConfigReader configReader;
	private final LinkFinishInventory linkFinishInventory;
	private final OverviewInventoryPlayer overviewInventoryPlayer;

	private Data() {
		this.cache = new HashMap<>();
		this.configReader = new ConfigReader();
		this.mongoDB = new MongoDB();
		this.linkFinishInventory = new LinkFinishInventory();
		this.overviewInventoryPlayer = new OverviewInventoryPlayer();
	}

	public static Data getInstance() {
		if (instance == null) {
			instance = new Data();
		}
		return instance;
	}

	public HashMap<UUID, String> getCache() {
		return this.cache;
	}

	public OverviewInventoryPlayer getOverviewInventoryPlayer() {
		return this.overviewInventoryPlayer;
	}

	public void setChestLinkInstance(ChestLink chestLinkInstance) {
		this.chestLink = chestLinkInstance;
	}

	public LinkFinishInventory getLinkFinishInventory() {
		return this.linkFinishInventory;
	}

	public ConfigReader getConfigReader() {
		return this.configReader;
	}

	public ChestLink getChestLink() {
		return this.chestLink;
	}

	public MongoDB getMongoDB() {
		return this.mongoDB;
	}
}
