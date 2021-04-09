package de.srsuders.chestlink.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

import de.srsuders.chestlink.game.CLPlayer;
import de.srsuders.chestlink.game.inventory.LinkFinishInventory;
import de.srsuders.chestlink.game.object.CLHandler;
import de.srsuders.chestlink.game.object.LinkedChest;
import de.srsuders.chestlink.game.object.LinkedChestBuilder;
import de.srsuders.chestlink.storage.Data;
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
		CLHandler.getCLPlayer(p.getUniqueId());
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
				final Location loc = e.getClickedBlock().getLocation();
				if(lcb.getLoc1() != null | lcb.getLoc2() != null) 
					if(checkLocation(lcb.getLoc1(), lcb.getLoc2(), e.getClickedBlock(), p)) {
						e.setCancelled(true);
						return;
					}
				final LinkedChest lc = CLHandler.getLinkedChestByLocation(loc);
				if(lc != null) {
					if(lc.getOwner().equals(p.getUniqueId())) p.sendMessage(prefix + "§cDu hast bereits diese Kiste gelinked.");
					else p.sendMessage(prefix + "§cDiese Kiste wurde bereits von jemand anderem gelinkt.");
					return;
				}
				if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
					e.setCancelled(true);
					lcb.setLoc1(loc);
					p.sendMessage(prefix + "Du hast diese Kiste als §aerste §eKiste markiert.");
				} else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
					e.setCancelled(true);
					lcb.setLoc2(loc);
					p.sendMessage(prefix + "Du hast diese Kiste als §azweite §eKiste markiert.");
				}
			}
		} else if(e.getClickedBlock() != null & e.getClickedBlock().getType() == Material.CHEST) {
			final Location loc = e.getClickedBlock().getLocation();
			final LinkedChest lc = CLHandler.getLinkedChestByLocation(loc);
			if(lc == null) return;
			e.setCancelled(true);
			if(!lc.getOwner().equals(p.getUniqueId())) {
				p.sendMessage(prefix + "§cDu kannst nicht die LinkedChest eines anderen Spielers öffnen.");
				return;
			}
			final Inventory inv = lc.getLinkedChestInventory().getInventory();
			p.openInventory(inv);
		}
	}
	
	@EventHandler
	public void onItemDrop(final PlayerDropItemEvent e) {
		if(e.getItemDrop() != null) {
			if(e.getItemDrop().getItemStack().equals(Items.markAxe)) e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInvClick(final InventoryClickEvent e) {
		final LinkFinishInventory lfInv = Data.getInstance().getLinkFinishInventory();
		final Player p = (Player) e.getWhoClicked();
		if(e.getClickedInventory().getName().equals(lfInv.getInventory().getName())) {
			e.setCancelled(true);
			if(e.getAction() == null) return;
			if(e.getCurrentItem().equals(Items.checkField)) {
				final CLPlayer clp = CLHandler.getCLPlayer(p.getUniqueId());
				clp.saveLinkedChest();
				if(p.getInventory().contains(Items.markAxe))
					p.getInventory().remove(Items.markAxe);
				p.sendMessage(prefix + "Du hast die Kisten erfolgreich gelinkt.");
				p.closeInventory();
			} else if(e.getCurrentItem().equals(Items.denyField)) {
				p.sendMessage(prefix + "§cDu hast die Bestätigung abgebrochen.");
				p.closeInventory();
			}
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
