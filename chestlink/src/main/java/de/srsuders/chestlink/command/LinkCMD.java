package de.srsuders.chestlink.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.srsuders.chestlink.storage.Const;
import de.srsuders.chestlink.storage.Items;
import de.srsuders.chestlink.storage.Messages;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 07.04.2021 Project: chestlink
 */
public class LinkCMD implements CommandExecutor, Messages {

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
		if (p.getInventory().contains(Items.markAxe)) {
			sender.sendMessage(prefix + "§cDu hast bereits den §5Linker §cerhalten.");
			return false;
		}
		p.getInventory().addItem(Items.markAxe);
		sender.sendMessage(prefix + "Du hast den §5Linker §eerhalten.\n"
				+ "Um das Item zu bedienen, musst du jeweils mit der linken oder rechten Maustaste auf einer Kiste, um somit beide zu verbinden. Um das Vorgehen zu bestätigen, gib bitte: \"/linkfinish\" ein.");
		return false;
	}
}
