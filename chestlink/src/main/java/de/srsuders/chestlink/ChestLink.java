package de.srsuders.chestlink;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import de.srsuders.chestlink.command.LinkCMD;
import de.srsuders.chestlink.command.LinkfinishCMD;
import de.srsuders.chestlink.game.CLPlayer;
import de.srsuders.chestlink.game.object.CLHandler;
import de.srsuders.chestlink.game.object.LinkedChest;
import de.srsuders.chestlink.game.object.inventory.LinkedChestInventory;
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
		if (doc != null) {
			@SuppressWarnings("unchecked")
			final ArrayList<Object> list = (ArrayList<Object>) doc.get("chests");
			final ArrayList<LinkedChest> linkedChests = new ArrayList<>();
			final Map<String, LinkedChest> chestId = new HashMap<>();
			for (Object obj : list.toArray()) {
				String value = (String) obj;
				System.out.println("String: " + value);
				String[] strArray = value.split(",");
				final Location loc = MCUtils.stringArrayToLocation(strArray);
				final UUID uuid = UUID.fromString(strArray[4]);
				final Long time = Long.valueOf(strArray[5]);
				final String id = strArray[6];
				final LinkedChest lc = new LinkedChest(loc, uuid, id, time);
				if(chestId.containsKey(id)) {
					final LinkedChestInventory lcInv = new LinkedChestInventory(lc);
					final LinkedChest lc2 = chestId.get(id);
					lc.setLinkedChestInventory(lcInv);
					lc2.setLinkedChestInventory(lcInv);
					chestId.remove(id);
					lc.setOtherChest(lc2);
					lc2.setOtherChest(lc);
					linkedChests.add(lc);
					linkedChests.add(lc2);
				}
			}
			final HashMap<UUID, CLPlayer> playerMap = new HashMap<>();
			for(LinkedChest lcs : linkedChests) {
				if(!playerMap.containsKey(lcs.getOwnerUUID())) {
					playerMap.put(lcs.getOwnerUUID(), new CLPlayer(lcs.getOwnerUUID()));
				}
			}
			final HashMap<CLPlayer, List<LinkedChest>> map = new HashMap<>();
			for(Entry<UUID, CLPlayer> players : playerMap.entrySet()) {
				map.put(players.getValue(), players.getValue().getLinkedChests());
			}
			CLHandler.updateList(map);;
		}
	}
}
