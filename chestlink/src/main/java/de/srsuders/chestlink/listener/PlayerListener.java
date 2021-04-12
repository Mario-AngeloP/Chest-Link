package de.srsuders.chestlink.listener;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.srsuders.chestlink.game.CLPlayer;
import de.srsuders.chestlink.game.inventory.LinkFinishInventory;
import de.srsuders.chestlink.game.object.CLHandler;
import de.srsuders.chestlink.game.object.LinkedChest;
import de.srsuders.chestlink.game.object.LinkedChestBuilder;
import de.srsuders.chestlink.game.object.inventory.LinkedChestInventory;
import de.srsuders.chestlink.storage.Const;
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
	public void onBlockBreak(final BlockBreakEvent e) {
		final Block b = e.getBlock();
		if(b != null) {
			if(b.getType() == Material.CHEST) {
				final LinkedChest lc = CLHandler.getLinkedChestByLocation(b.getLocation());
				if(lc != null) {
					e.setCancelled(true);
					final Player p = e.getPlayer();
					if(lc.getOwnerUUID().equals(p.getUniqueId())) {
						p.sendMessage(prefix + "§cUm deine LinkedChest zu zerstören, muss du vorher die Verlinkung aufheben.");
						return;
					}
					p.sendMessage(prefix + "§cDu kannst keine LinkedChest zerstören!");
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(final PlayerInteractEvent e) {
		final Player p = e.getPlayer();
		final ItemStack is = p.getItemInHand();
		final Block block = e.getClickedBlock();
		if (block == null)
			return;
		if (checkIfLinkedItem(is)) {
			if (block.getType() == Material.CHEST) {
				final BlockState state = block.getState();
				Chest chest = (Chest) state;
				final Inventory chestInv = chest.getInventory();
				if (chestInv instanceof DoubleChestInventory) {
					p.sendMessage(prefix + "§cDu kannst keine DoubleChest verlinken.");
					return;
				}
				final CLPlayer clp = CLHandler.getCLPlayer(p.getUniqueId());
				final LinkedChestBuilder lcb = clp.getLinkedChestBuilder();
				final Location loc = block.getLocation();
				if (lcb.getLoc1() != null | lcb.getLoc2() != null)
					if (checkLocation(lcb.getLoc1(), lcb.getLoc2(), block, p)) {
						e.setCancelled(true);
						return;
					}
				final LinkedChest lc = CLHandler.getLinkedChestByLocation(loc);
				if (lc != null) {
					e.setCancelled(true);
					if (lc.getOwnerUUID().equals(p.getUniqueId()))
						p.sendMessage(prefix + "§cDu hast bereits diese Kiste gelinked.");
					else
						p.sendMessage(prefix + "§cDiese Kiste wurde bereits von jemand anderem gelinkt.");
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
		}
		if (block.getType() != Material.CHEST)
			return;
		if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		final Location loc = block.getLocation();
		final LinkedChest lc = CLHandler.getLinkedChestByLocation(loc);
		if (lc == null)
			return;
		if (is != null) {
			if (is.getItemMeta() != null)
				if (Items.statusItem.getItemMeta().getDisplayName().equals(is.getItemMeta().getDisplayName())
						&& is.getType() == Items.statusItem.getType()) {
					e.setCancelled(true);
					if (lc.getOwnerUUID().equals(p.getUniqueId())) {
						p.sendMessage(prefix + "\n" + MCUtils.linkedChestToInfoStringFromSelf(lc));
					} else {
						p.sendMessage(prefix + "\n" + MCUtils.linkedChestToInfoStringFromOther(lc));
					}
					return;
				}
		}
		e.setCancelled(true);
		if (!lc.getOwnerUUID().equals(p.getUniqueId())) {
			p.sendMessage(prefix + "§cDu kannst nicht die LinkedChest eines anderen Spielers öffnen.");
			return;
		}
		final LinkedChestInventory lcInv = lc.getLinkedChestInventory();
		p.openInventory(lcInv.getInventory());
	}

	private boolean checkIfLinkedItem(final ItemStack is) {
		if (is == null)
			return false;
		if (is.getItemMeta() == null)
			return false;
		if (is.getType() != Items.markAxe.getType())
			return false;
		return is.getItemMeta().getDisplayName().equals(Items.markAxe.getItemMeta().getDisplayName());
	}

	@EventHandler
	public void onItemDrop(final PlayerDropItemEvent e) {
		if (e.getItemDrop() != null) {
			if (e.getItemDrop().getItemStack().equals(Items.markAxe))
				e.setCancelled(true);
		}
	}

	@EventHandler
	public void onInvClick(final InventoryClickEvent e) {
		final LinkFinishInventory lfInv = Data.getInstance().getLinkFinishInventory();
		final Player p = (Player) e.getWhoClicked();
		if (e.getClickedInventory() == null)
			return;
		if (e.getAction() == null || e.getCurrentItem() == null)
			return;
		final ItemStack is = e.getCurrentItem();
		if (e.getClickedInventory().getName().equals(lfInv.getInventory().getName())) {
			e.setCancelled(true);
			if (e.getClick() == ClickType.LEFT) {
				if (is.equals(Items.checkField)) {
					final CLPlayer clp = CLHandler.getCLPlayer(p.getUniqueId());
					clp.saveLinkedChest();
					if (p.getInventory().contains(Items.markAxe))
						p.getInventory().remove(Items.markAxe);
					p.sendMessage(prefix + "Du hast die Kisten erfolgreich gelinkt.");
					p.closeInventory();
				} else if (is.equals(Items.denyField)) {
					p.sendMessage(prefix + "§cDu hast die Bestätigung abgebrochen.");
					p.closeInventory();
				}
			}
		} else if (e.getClickedInventory().getName().startsWith(Const.OVERVIEW_INVENTORY_NAME)) {
			e.setCancelled(true);
			if (e.getClick() == ClickType.LEFT) {
				if (is.equals(Items.nextPage) | is.equals(Items.lastPage)) {
					final String nr = e.getClickedInventory().getName()
							.substring(e.getClickedInventory().getName().length() - 1);
					final int i = Integer.valueOf(nr);
					p.openInventory(Data.getInstance().getOverviewInventoryPlayer()
							.createInventoryForPlayer(CLHandler.getCLPlayer(p.getUniqueId()), is.equals(Items.nextPage) ? i+1 : i-1));
				} else if(is.getType() == Material.CHEST) {
					final String name = is.getItemMeta().getDisplayName().substring(2);
					final Integer nr = Integer.valueOf(name.split(" ")[0].replace(".", ""));
					final CLPlayer clp = CLHandler.getCLPlayer(p.getUniqueId());
					final LinkedChest lc = clp.getSingleLinkedChests().get(nr-1);
					p.openInventory(Data.getInstance().getOverviewInventoryPlayer().createLinkedChestInventory(lc, is, nr));
				}
			}
		} else if(e.getClickedInventory().getName().startsWith(Const.LC_OVERVIEW_NAME)) {
			e.setCancelled(true);
			if(e.getClick() == ClickType.LEFT) {
				if(is.equals(Items.back) | is.equals(Items.delete)) {
					final CLPlayer clp = CLHandler.getCLPlayer(p.getUniqueId());
					if(is.equals(Items.back)) {
						p.openInventory(Data.getInstance().getOverviewInventoryPlayer().createInventoryForPlayer(clp, 1));
						return;
					}
					final Integer nr = Integer.valueOf(e.getClickedInventory().getName().split(" ")[3]);
					final LinkedChest lc = clp.getSingleLinkedChests().get(nr-1);
					final ItemStack chest = e.getClickedInventory().getItem(13);
					p.openInventory(Data.getInstance().getOverviewInventoryPlayer().createConfirmInventory(lc, chest, nr));
				}
			}
		} else if(e.getClickedInventory().getName().startsWith(Const.CONFIRM_NAME)) {
			e.setCancelled(true);
			if(e.getClick() == ClickType.LEFT) {
				if(is.equals(Items.checkField) | is.equals(Items.denyField)) {
					p.closeInventory();
					if(is.equals(Items.denyField)) {
						return;
					}
					final CLPlayer clp = CLHandler.getCLPlayer(p.getUniqueId());
					final ItemStack chest = e.getClickedInventory().getItem(13);
					final List<String> lore = chest.getItemMeta().getLore();
					final String[] strArray = lore.get(lore.size()-1).split(":");
					final String id = strArray[strArray.length-1].replaceAll(" §a", "");
					final LinkedChest lc = CLHandler.getLinkedChestByID(id);
					if(lc.getLinkedChestInventory().hasContent()) {
						p.sendMessage(prefix + "§cDu kannst die Verlinkung nicht aufheben, solange sich noch Items in der LinkedChest befinden.");
						return;
					}
					CLHandler.removeLinkedChest(clp, lc);
					p.sendMessage(prefix + "Du hast erfolgreich die Verlinkung aufgehoben.");
				}
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
