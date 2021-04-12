package de.srsuders.chestlink.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.srsuders.chestlink.api.ChestLinkAPI;
import de.srsuders.chestlink.game.CLPlayer;
import de.srsuders.chestlink.storage.Data;
import de.srsuders.chestlink.storage.Messages;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 11.04.2021 Project: chestlink
 */
public class LinkedChestsCMD implements CommandExecutor, Messages {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(noConsoleCMD);
			return false;
		}
		final Player p = (Player) sender;
		final CLPlayer clp = ChestLinkAPI.getCLPlayer(p.getUniqueId());
		p.openInventory(Data.getInstance().getOverviewInventoryPlayer().createInventoryForPlayer(clp, 1));
		return false;
	}
}
