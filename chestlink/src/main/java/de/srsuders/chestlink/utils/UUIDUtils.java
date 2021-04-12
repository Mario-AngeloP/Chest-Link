package de.srsuders.chestlink.utils;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import de.srsuders.chestlink.storage.Data;

public final class UUIDUtils {

	private static final Map<UUID, String> cache;

	static {
		cache = Data.getInstance().getCache();
	}

	/**
	 * Bestimme die UUID, mit des Namens, mit dem sich der Spieler der gesuchten
	 * UUID zuletzt verbunden hat.
	 *
	 * @param playerName letzer bekannter Name des Spielers
	 * @return Universally Unique Identifier des Spielers
	 */
	public static UUID getUUID(String playerName) {
		final Player player = Bukkit.getPlayer(playerName);
		if (player != null) {
			return player.getUniqueId();
		}
		if (cache != null) {
			for (final UUID id : cache.keySet()) {
				final String nameFromUUID = cache.get(id);
				if (nameFromUUID.equalsIgnoreCase(playerName)) {
					return id;
				}
			}
		}

		return NameFetcher.getUUID(playerName);
	}

	/**
	 * Bestimme den Namen, mithilfe der UUID, mit dem sich der Spieler des gesuchten
	 * Namen zuletzt verbunden hat.
	 *
	 * @param uuid Universally Unique Identifier des Spielers
	 * @return letzter bekannter Name des Spielers
	 */
	public static String getName(UUID uuid) {
		final Player player = Bukkit.getPlayer(uuid);
		if (player != null) {
			return player.getName();
		}
		if (cache != null) {
			if (cache.containsKey(uuid)) {
				return cache.get(uuid);
			}
		}
		return NameFetcher.getName(uuid);
	}

	/**
	 * Bestimme den Spieler, mithilfe der UUID, mit dem sich der gesuchte Spieler
	 * zuletzt verbunden hat.
	 *
	 * @param uuid Universally Unique Identifier des Spielers
	 * @return org.bukkit.entity.Player des Spielers
	 */
	public static Player getPlayer(UUID uuid) {
		Player player = Bukkit.getPlayer(uuid);
		if (player == null) {
			final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
			player = offlinePlayer.getPlayer();
		}
		return player;
	}

	/**
	 * Bestimme den Spieler, mithilfe der UUID, mit dem sich der gesuchte Spieler
	 * zuletzt verbunden hat.
	 *
	 * @param playerName letzer bekannter Name des Spielers
	 * @return org.bukkit.entity.Player des Spielers
	 */
	public static Player getPlayer(String playerName) {
		return getPlayer(getUUID(playerName));
	}

}