package de.srsuders.chestlink.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.srsuders.chestlink.game.CLPlayer;
import de.srsuders.chestlink.game.object.CLHandler;
import de.srsuders.chestlink.storage.Const;
import de.srsuders.chestlink.storage.Data;
import de.srsuders.chestlink.storage.Messages;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 07.04.2021 Project: chestlink
 */
public class LinkfinishCMD implements CommandExecutor, Messages {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(noConsoleCMD);
			return false;
		}
		final Player p = (Player) sender;
		if (!p.hasPermission(Const.PERM_LINK_CHESTS) && !p.hasPermission(Const.PERM_ADMIN)) {
			sender.sendMessage(noPermission);
			return false;
		}
		final CLPlayer clp = CLHandler.getCLPlayer(p.getUniqueId());
		if (!clp.getLinkedChestBuilder().finished()) {
			sender.sendMessage(prefix + "§cDu musst vorher noch zwei Kisten unabhängig voneinander markieren.\n"
					+ "Um welche zu markieren, benutze bitte den Befehl \"/link\", wo dir weitere Schritte erklärt werden.");
			return false;
		}
		p.openInventory(Data.getInstance().getLinkFinishInventory().getInventory());
		return false;
	}
}
