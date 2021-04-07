package de.srsuders.chestlink;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

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
		Data.getInstance().getMongoDB().getSavedChests().find().iterator().forEachRemaining(doc -> {
			final List<LinkedChest> linkedChests = new ArrayList<>();
			Set<Entry<String, Object>> sets = doc.entrySet();
			for(Entry<String, Object> map : sets) {
				final String locationString = map.getKey();
				final long time = Long.valueOf((String) map.getValue());
				final Location loc = MCUtils.stringToLocation(locationString);
				
				linkedChests.add(new LinkedChest(loc, UUID.fromString(map.getKey()), time));
			}
			CLHandler.updateList(linkedChests);
		});;
	}
}
