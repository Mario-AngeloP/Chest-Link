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

	@SuppressWarnings("deprecation")
	public void onEnable() {
		Data.getInstance().setChestLinkInstance(this);
		Data.getInstance().getMongoDB().connect();
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
		this.getCommand("link").setExecutor(new LinkCMD());
		this.getCommand("linkfinish").setExecutor(new LinkfinishCMD());
		Bukkit.getScheduler().scheduleAsyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				readChests();
			}
		}, 10L);
	}
	
	public void onDisable() {
		CLHandler.saveLinkedChests();
	}

	private void readChests() {
		final Document doc = Data.getInstance().getMongoDB().getSavedChests().find().first();
		if(doc != null) {
			final List<String> lcList = doc.getList("chests", String.class);
			final List<LinkedChest> linkedChests = new ArrayList<>();
			LinkedChest lc = null;
			for(String str : lcList) {
				String[] strArray = str.split(",");
				final Location loc = MCUtils.stringArrayToLocation(strArray);
				final UUID uuid = UUID.fromString(strArray[4]);
				final Long time = Long.valueOf(strArray[5]);
				if(lc == null) {
					lc = new LinkedChest(loc, uuid, strArray[6], time);
				} else {
					final LinkedChest lc2 = new LinkedChest(loc, uuid, strArray[6], time);
					lc.setOtherChest(lc2);
					lc2.setOtherChest(lc);
					linkedChests.add(lc);
					linkedChests.add(lc2);
					lc = null;
				}
			}
			CLHandler.updateList(linkedChests);
		}
	}
}
