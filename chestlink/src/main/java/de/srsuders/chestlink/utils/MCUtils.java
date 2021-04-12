package de.srsuders.chestlink.utils;

import java.text.SimpleDateFormat;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import de.srsuders.chestlink.game.object.LinkedChest;
import de.srsuders.chestlink.storage.Messages;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 07.04.2021 Project: chestlink
 */
public class MCUtils implements Messages {

	public static boolean equalLocation(final Location loc1, final Location loc2) {
		if (loc1 == null | loc2 == null)
			return false;
		return loc1.getX() == loc2.getX() & loc1.getY() == loc2.getY() & loc1.getZ() == loc2.getZ()
				& loc1.getWorld().equals(loc2.getWorld());
	}

	public static String locationToString(final Location loc) {
		return loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ();
	}

	public static Location stringToLocation(final String string) {
		final String[] a = string.split(",");
		return new Location(Bukkit.getWorld(a[0]), Double.valueOf(a[1]), Double.valueOf(a[2]), Double.valueOf(a[3]));
	}

	public static Location stringArrayToLocation(final String[] stringArray) {
		return stringToLocation(stringArray[0] + "," + stringArray[1] + "," + stringArray[2] + "," + stringArray[3]);
	}

	public static String linkedChestToInfoStringFromSelf(final LinkedChest lc) {
		final Location loc1 = lc.getLocation();
		final Location loc2 = lc.getLinkedChest().getLocation();
		String str = "§ePosition 1: ";
		str += "§a" + loc1.getWorld().getName() + " §eX: §a" + loc1.getBlockX() + " §eY: §a" + loc1.getBlockY()
				+ " §eZ: §a" + loc1.getBlockZ() + "\n§ePosition 2: §a";
		str += loc2.getWorld().getName() + " §eX: §a" + loc2.getBlockX() + " §eY: §a" + loc2.getBlockY() + " §eZ:§a "
				+ loc2.getBlockZ() + "\n§eErstellt: §a";
		final SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		str += date.format(lc.getFinishedTime()) + " \n§eVerlinker: §a" + UUIDUtils.getName(lc.getOwnerUUID()) + " \n§eID: §a" + lc.getID();
		return str;
	}

	public static String linkedChestToInfoStringFromOther(final LinkedChest lc) {
		final SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		String str = "§eErstellt:§a " + date.format(lc.getFinishedTime()) + "§e\nVerlinker: §a\n";
		str += UUIDUtils.getName(lc.getOwnerUUID()) + " \n§eID: §a" + lc.getID();
		return str;
	}
}
