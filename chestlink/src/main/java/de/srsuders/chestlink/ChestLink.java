package de.srsuders.chestlink;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import de.srsuders.chestlink.api.ChestLinkAPI;
import de.srsuders.chestlink.command.LinkCMD;
import de.srsuders.chestlink.command.LinkedChestsCMD;
import de.srsuders.chestlink.command.LinkfinishCMD;
import de.srsuders.chestlink.game.CLPlayer;
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
		this.getCommand("linkedchests").setExecutor(new LinkedChestsCMD());
		Bukkit.getScheduler().scheduleAsyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				readChests();
			}
		}, 10L);
	}

	public void onDisable() {
		ChestLinkAPI.saveLinkedChests();
	}

	private void readChests() {
		final Document doc = Data.getInstance().getMongoDB().getSavedChests().find().first();
		if (doc != null) {
			@SuppressWarnings("unchecked")
			final ArrayList<Object> list = (ArrayList<Object>) doc.get("chests");
			if (list == null)
				return;
			final ArrayList<LinkedChest> linkedChests = new ArrayList<>();
			final Map<String, LinkedChest> chestId = new HashMap<>();
			for (Object obj : list.toArray()) {
				String value = (String) obj;
				String[] strArray = value.split(",");
				final Location loc = MCUtils.stringArrayToLocation(strArray);
				final UUID uuid = UUID.fromString(strArray[4]);
				final Long time = Long.valueOf(strArray[5]);
				final String id = strArray[6];
				final LinkedChest lc = new LinkedChest(loc, uuid, id, time);
				if (chestId.containsKey(id)) {
					final LinkedChestInventory lcInv = new LinkedChestInventory(lc);
					final LinkedChest lc2 = chestId.get(id);
					lc.setLinkedChestInventory(lcInv);
					lc2.setLinkedChestInventory(lcInv);
					chestId.remove(id);
					lc.setOtherChest(lc2);
					lc2.setOtherChest(lc);
					linkedChests.add(lc);
					linkedChests.add(lc2);
				} else {
					chestId.put(id, lc);
				}
			}
			final HashMap<UUID, CLPlayer> playerMap = new HashMap<>();
			final HashMap<CLPlayer, List<LinkedChest>> lcMap = new HashMap<>();
			for (LinkedChest lcs : linkedChests) {
				CLPlayer clp = null;
				List<LinkedChest> clpLc = null;
				if (!playerMap.containsKey(lcs.getOwnerUUID())) {
					clp = new CLPlayer(lcs.getOwnerUUID());
					playerMap.put(lcs.getOwnerUUID(), clp);
					clpLc = new ArrayList<>();
					lcMap.put(clp, clpLc);
				} else {
					clp = playerMap.get(lcs.getOwnerUUID());
					clpLc = lcMap.get(clp);
				}
				clpLc.add(lcs);
				lcMap.put(clp, clpLc);
			}
			ChestLinkAPI.updateLinkedChests(lcMap);
			;
		}
	}
}
