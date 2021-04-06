package de.srsuders.chestlink;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import de.srsuders.chestlink.command.LinkCMD;
import de.srsuders.chestlink.command.LinkfinishCMD;
import de.srsuders.chestlink.listener.PlayerListener;
import de.srsuders.chestlink.storage.Data;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 06.04.2021 Project: chestlink
 */
public class ChestLink extends JavaPlugin {

	public void onEnable() {
		Data.getInstance().setChestLinkInstance(this);
		Data.getInstance().getMongoDB().connect();
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
		this.getCommand("link").setExecutor(new LinkCMD());
		this.getCommand("linkfinish").setExecutor(new LinkfinishCMD());
	}
}
