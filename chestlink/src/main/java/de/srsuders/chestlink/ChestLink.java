package de.srsuders.chestlink;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import de.srsuders.chestlink.command.LinkCMD;
import de.srsuders.chestlink.command.LinkfinishCMD;
import de.srsuders.chestlink.game.object.CLHandler;
import de.srsuders.chestlink.game.object.LinkedChest;
import de.srsuders.chestlink.listener.PlayerListener;
import de.srsuders.chestlink.storage.Data;
import de.srsuders.chestlink.utils.MCUtils;

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
		readChests();
	}
	
	public void onDisable() {
		CLHandler.saveLinkedChests();
	}

	private void readChests() {
		final Document doc = Data.getInstance().getMongoDB().getSavedChests().find().first();
		if(doc != null) {
			final List<LinkedChest> linkedChests = new ArrayList<>();
			for(Object obj : doc.values()) {
				String chestString = (String) obj;
				String[] stringArray = chestString.split(",");
				final UUID owner = UUID.fromString(stringArray[4]);
				final Location loc = MCUtils.stringArrayToLocation(stringArray);
				linkedChests.add(CLHandler.getCLPlayer(owner).getLinkedChestByLocation(loc));
			}
			CLHandler.updateList(linkedChests);
		}
	}
}
