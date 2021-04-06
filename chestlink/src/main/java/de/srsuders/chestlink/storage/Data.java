package de.srsuders.chestlink.storage;

import de.srsuders.chestlink.ChestLink;
import de.srsuders.chestlink.io.config.ConfigReader;
import de.srsuders.chestlink.io.mongo.MongoDB;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 06.04.2021 Project: chestlink
 */
public class Data {

	private static Data instance;
	private ChestLink chestLink;
	private final MongoDB mongoDB;
	private final ConfigReader configReader;

	private Data() {
		this.configReader = new ConfigReader();
		this.mongoDB = new MongoDB();
	}

	public static Data getInstance() {
		if(instance == null) {
			instance = new Data();
		}
		return instance;
	}

	public void setChestLinkInstance(ChestLink chestLinkInstance) {
		this.chestLink = chestLinkInstance;
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