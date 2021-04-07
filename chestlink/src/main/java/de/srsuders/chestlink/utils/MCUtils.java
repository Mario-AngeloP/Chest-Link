package de.srsuders.chestlink.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 07.04.2021 Project: chestlink
 */
public class MCUtils {

	public static boolean equalLocation(final Location loc1, final Location loc2) {
		if(loc1 == null | loc2 == null) return false;
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
	
}
