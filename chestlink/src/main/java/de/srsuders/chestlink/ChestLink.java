package de.srsuders.chestlink;

import org.bukkit.plugin.java.JavaPlugin;

import de.srsuders.chestlink.storage.Data;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 06.04.2021 Project: chestlink
 */
public class ChestLink extends JavaPlugin {

	public void onEnable() {
		Data.getInstance().setChestLinkInstance(this);
		Data.getInstance().getMongoDB().connect();
	}
}
