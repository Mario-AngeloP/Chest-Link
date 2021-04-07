package de.srsuders.chestlink.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.srsuders.chestlink.game.CLPlayer;
import de.srsuders.chestlink.game.object.CLHandler;
import de.srsuders.chestlink.game.object.LinkedChestBuilder;
import de.srsuders.chestlink.storage.Items;
import de.srsuders.chestlink.storage.Messages;
import de.srsuders.chestlink.utils.MCUtils;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 07.04.2021 Project: chestlink
 */
public class PlayerListener implements Listener, Messages {

	@EventHandler
	public void onJoin(final PlayerJoinEvent e) {
		final Player p = e.getPlayer();
		final CLPlayer clp = CLHandler.getCLPlayer(p.getUniqueId());

	}
	
	@EventHandler
	public void onQuit(final PlayerQuitEvent e) {
		final Player p = e.getPlayer();
		CLHandler.getCLPlayer(p.getUniqueId()).savePlayer();
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(final PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		if (p.getItemInHand() != null & p.getItemInHand().equals(Items.markAxe)) {
			if (e.getClickedBlock() != null & e.getClickedBlock().getType() == Material.CHEST) {
				final CLPlayer clp = CLHandler.getCLPlayer(p.getUniqueId());
				final LinkedChestBuilder lcb = clp.getLinkedChestBuilder();
				if(lcb.getLoc1() != null | lcb.getLoc2() != null) 
					if(checkLocation(lcb.getLoc1(), lcb.getLoc2(), e.getClickedBlock(), p)) return;
				if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
					e.setCancelled(true);
					lcb.setLoc1(e.getClickedBlock().getLocation());
					p.sendMessage(prefix + "Du hast diese Kiste als §aerste §eKiste markiert.");
				} else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
					e.setCancelled(true);
					lcb.setLoc2(e.getClickedBlock().getLocation());
					p.sendMessage(prefix + "Du hast diese Kiste als §azweite §eKiste markiert.");
				}
			}
		} else if(e.getClickedBlock() != null & e.getClickedBlock().getType() == Material.CHEST) {
			//TODO: Link Herrstellung
		}
	}

	private boolean checkLocation(final Location loc1, final Location loc2, final Block block, final Player player) {
		if (MCUtils.equalLocation(loc1, block.getLocation()) | MCUtils.equalLocation(loc2, block.getLocation())) {
			player.sendMessage(prefix + "§cDu hast den Block bereits markiert.");
			return true;
		}
		return false;
	}
}
